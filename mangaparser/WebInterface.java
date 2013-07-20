package mangaparser;

import java.io.PrintStream;
import java.util.Iterator;

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
			
			/* data request */
			
			//check for format
			String format = Encodable.XML;
			if (request.getQuery().containsKey("format")) {
				format = request.getQuery().get("format").toUpperCase();
			}
			//set content-type according to format
			String mimeType = "text/plain"; 
			if (format.equals(Encodable.XML)) {
				mimeType = "text/xml";
			} else if (format.equals(Encodable.JSON) | format.equals(Encodable.DATATABLES)) {
				mimeType = "application/json";
			}
			response.setValue("Content-Type", mimeType);
			
			String data = "";
			if (request.getQuery().containsKey("hoster")) {
				// request specific data
				Hoster h = MangaParser.getInstance().getDatastore().find(Hoster.class, "name", request.getQuery().get("hoster")).get();
				if (request.getQuery().containsKey("manga")) {
					// request manga
					Manga m = h.findMangaByTitle(request.getQuery().get("manga"));
					data = m.encode(format, Chapter.class);
				} else {
					// request hoster
					data = h.encode(format, Manga.class);
				}
			} else {
				// default request. list hosters
				//for (Hoster h: MangaParser.getInstance().getDatastore().find(Hoster.class).asList()) {
				Iterator<Hoster> i = MangaParser.getInstance().getDatastore().find(Hoster.class).asList().iterator();
				while (i.hasNext()) {
					Hoster h = i.next();
					data += h.encode(format, Hoster.class);
					if (i.hasNext() & (format.equals(Encodable.JSON) | format.equals(Encodable.DATATABLES))) data += ",";
				}
				if (format.equals(Encodable.JSON) | format.equals(Encodable.DATATABLES))
					data = String.format("[%s]", data);
			}
			
			if (format.equals(Encodable.XML)) {
				data = String.format("<Document>%s</Document>", data);
			} else if (format.equals(Encodable.DATATABLES)) {
				data = String.format("{'aaData': %s}", data);
			}
			
			if (request.getQuery().containsKey("callback")) data = String.format("%s(%s);", request.getQuery().get("callback"), data);
			
			body.print(data);
			body.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
