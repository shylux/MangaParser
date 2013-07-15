package mangaparser.data;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MangapandaHoster extends Hoster {
	private static final String OVERVIEW_URL = "http://www.mangapanda.com/alphabetical";
	private static final String MANGA_SELECTOR = ".series_col li a";
	
	public MangapandaHoster() {
		name = "MangaPanda";
		address = "http://www.mangapanda.com/";
		icon = "http://s4.mangapanda.com/favicon.ico";
	}

	@Override
	public List<Manga> loadMangas() {
		ArrayList<Manga> mangalist = new ArrayList<Manga>();
		try {
			Document overview = Jsoup.connect(OVERVIEW_URL).get();
			Elements mangaRaw = overview.select(MANGA_SELECTOR);
			for (Element e: mangaRaw) {
				// TODO remove dev
				if (mangalist.size() >= 3) break;
				try {
					mangalist.add(new MangapandaManga(new URL(e.attr("abs:href"))));
				} catch (MalformedURLException ex) {}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		//TODO save!
		this.mangas = mangalist;
		save();
		return mangalist;
	}

}
