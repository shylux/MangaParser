package mangaparser.data;

import java.util.ArrayList;
import java.util.List;

import com.google.code.morphia.annotations.Entity;

/**
 * Hoster test class. Generates a fictional Hoster. No network connection required.
 * @author shylux
 *
 */
@Entity("Hosters")
public class TestHoster extends Hoster {
	public TestHoster() {
		this.name = "TestHoster";
		this.address = "http://testhost.er/";
		this.icon = "http://testhost.er/icon.jpg";
	}
	public TestHoster(String name) {
		super();
		this.name = name;
	}

	@Override
	public List<Manga> loadMangas() {
		ArrayList<Manga> list = new ArrayList<Manga>();
		list.add(new TestManga("MangaA"));
		list.add(new TestManga("MangaB"));
		this.mangas = list;
		save();
		return list;
	}

}
