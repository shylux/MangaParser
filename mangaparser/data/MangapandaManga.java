package mangaparser.data;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class MangapandaManga extends Manga {
	private static final String DESCRIPTION_SELECTOR = "#readmangasum p";
	
	@SuppressWarnings("unused")
	private MangapandaManga() {} // for morphia/mongodb
	public MangapandaManga(URL manga_overview) {
		// address
		this.address = manga_overview.toExternalForm();
		try {
			Document overview = Jsoup.connect(this.address).get();
			// description
			this.description = overview.select(DESCRIPTION_SELECTOR).text();
			for (Element e: overview.select(".propertytitle")) {
				// title
				if (e.text().startsWith("Name")) {
					this.title = e.parent().select("td:last-child").text();
				// author
				} else if (e.text().startsWith("Author")) {
					this.author = e.parent().select("td:last-child").text();
				// release year
				} else if (e.text().startsWith("Year of Release")) {
					this.releaseYear = Integer.valueOf(e.parent().select("td:last-child").text());
				}
			}
			// cover
			this.cover = overview.select("#mangaimg img").get(0).attr("src");
			// mature
			this.mature = false;
			
			// already load chapters
			for (Element e: overview.select("#listing a")) {
				try {
					this.chapters.add(new MangapandaChapter(e.text().trim(), new URL(e.attr("abs:href"))));
				} catch (MalformedURLException e1) {
					e1.printStackTrace();
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public List<Chapter> loadChapters() {
		return this.chapters; // already done
	}

}
