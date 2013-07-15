package mangaparser.data;

import java.util.ArrayList;
import java.util.List;

import com.google.code.morphia.annotations.Entity;

@Entity("Mangas")
public class TestManga extends Manga {
	public TestManga() {
		title = "Tesuto";
		description = "Tesuto desu!";
		author = "Tete Suto";
		releaseYear = 2013;
		cover = "http://fro.nt/page.jpg";
		address = "http://testhost.er/";
		mature = false;
	}
	
	public TestManga(String title) {
		this();
		this.title = title;
		this.address = this.address + title;
	}
	
	@Override
	public List<Chapter> loadChapters() {
		List<Chapter> c = new ArrayList<Chapter>();
		c.add(new TestChapter(address + "/chp1"));
		c.add(new TestChapter(address + "/chp2"));
		c.add(new TestChapter(address + "/chp3"));
		this.chapters = c;
		return chapters;
	}

}
