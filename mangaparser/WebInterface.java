package mangaparser;

import java.io.PrintStream;

import mangaparser.data.Chapter;
import mangaparser.data.Hoster;
import mangaparser.data.Manga;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.core.Container;

public class WebInterface implements Container {
	MangaParser parent;
	
	public WebInterface(MangaParser parent) {
		this.parent = parent;
	}

	@Override
	public void handle(Request request, Response response) {
		try {
			PrintStream body = response.getPrintStream();
			long time = System.currentTimeMillis();
			
			response.setValue("Content-Type", "text/xml");
			response.setValue("Server", "MangaParser/0.1 (Simple 5.1.4)");
			response.setDate("Date", time);
			response.setDate("Last-Modified", time);
			
			// issue refresh
			if (request.getQuery().containsKey("refresh")) {
				if (request.getQuery().containsKey("hoster")) {
					// get hoster
					String requestedHoster = request.getQuery().get("hoster");
					Hoster hoster = MangaParser.getInstance().ds.find(Hoster.class, "name", requestedHoster).get();
					if (request.getQuery().containsKey("manga")) {
						String requestedManga = request.getQuery().get("manga");
						Manga manga = hoster.findMangaByTitle(requestedManga);
						manga.loadChapters();
					} else {
						// refresh mangas
						hoster.loadMangas();
					}
				}
			}
			
			// data request
			if (request.getQuery().containsKey("hoster")) {
				// get hoster
				String requestedHoster = request.getQuery().get("hoster");
				Hoster hoster = MangaParser.getInstance().ds.find(Hoster.class, "name", requestedHoster).get();
				if (request.getQuery().containsKey("manga")) {
					String requestedManga = request.getQuery().get("manga");
					Manga m = hoster.findMangaByTitle(requestedManga);
					body.print(m.toXML(Chapter.class));
				} else {
					// request hoster
					body.print(hoster.toXML(Manga.class));
				}
			} else {
				// default request. list hoster.
				StringBuilder sb = new StringBuilder();
				for (Hoster h: MangaParser.getInstance().ds.find(Hoster.class).asList()) {
					sb.append(h.toXML(Hoster.class));
				}
				body.println(String.format("<Hosters>%s</Hosters>", sb));
			}
			body.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
