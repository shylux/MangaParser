package mangaparser.junit;

import static org.junit.Assert.*;

import java.util.List;

import mangaparser.MangaParser;
import mangaparser.data.Hoster;
import mangaparser.data.Manga;
import mangaparser.data.TestHoster;
import mangaparser.data.TestManga;

import org.junit.Test;

public class MangaParserTest {

	@Test
	public void testSetup() {
		MangaParser mp = MangaParser.getInstance();
		assertNotNull(mp);
	}
	
	public void testCleanup() {
		//TODO clean db
	}
	
	@Test
	public void testDeepSave() {
		//TODO add chapter level
		TestHoster h = new TestHoster();
		h.loadMangas();
		MangaParser.getInstance().getDatastore().save(h);
	}

	@Test
	public void testHosterSave() {
		TestHoster h = new TestHoster();
		assertNull(h.getId());
		//MangaParser.getInstance().getDatastore().save(h);
		assertNotNull(h.getId());
	}
	
	@Test
	public void testHosterLoad() {
		List<Hoster> lh = MangaParser.getInstance().getDatastore().find(Hoster.class).asList();
		assertNotSame("Cant find any hoster.", 0, lh.size());
		System.out.print(lh);
	}
	
	@Test
	public void testMangaSave() {
		Manga m = new TestManga();
		assertNull(m.getId());
		//MangaParser.getInstance().getDatastore().save(m);
		assertNotNull("Cant save manga.", m.getId());
	}

	@Test
	public void testMangaLoad() {
		List<Manga> lm = MangaParser.getInstance().getDatastore().find(Manga.class).asList();
		assertNotSame("Cant find any Mangas", 0, lm.size());
		System.out.println(lm);
	}
}
