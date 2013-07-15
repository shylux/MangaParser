package mangaparser;

import java.io.PrintStream;
import mangaparser.data.Hoster;
import mangaparser.data.Manga;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.core.Container;

import com.google.code.morphia.query.Query;

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
					String requestedHoster = request.getQuery().get("hoster");
					if (request.getQuery().containsKey("manga")) {
						// refresh chapters
						//TODO code!
					} else {
						// refresh mangas
						Hoster res = MangaParser.getInstance().ds.find(Hoster.class, "name", requestedHoster).get();
						res.loadMangas();
					}
				}
			}
			
			// data request
			if (request.getQuery().containsKey("hoster")) {
				// get hoster
				String requestedHoster = request.getQuery().get("hoster");
				if (request.getQuery().containsKey("manga")) {
					String requestedManga = request.getQuery().get("manga");
					body.println(requestedManga);
					Manga res = MangaParser.getInstance().ds.createQuery(Manga.class).get();
					//Manga res = MangaParser.getInstance().getDatastore().find(Manga.class, "title", requestedManga).get();
					if (res != null) body.print(res.toXML(Manga.class));
				} else {
					// request hoster
					Hoster res = MangaParser.getInstance().ds.find(Hoster.class, "name", requestedHoster).get();
					body.println(res.toXML(Manga.class));
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
