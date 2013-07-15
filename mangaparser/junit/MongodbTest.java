package mangaparser.junit;

import static org.junit.Assert.*;

import java.util.List;

import mangaparser.MangaParser;
import mangaparser.data.Hoster;
import mangaparser.data.Manga;
import mangaparser.data.TestHoster;
import mangaparser.data.TestManga;

import org.junit.Before;
import org.junit.Test;

import com.google.code.morphia.Datastore;

public class MongodbTest {
	static Datastore ds;

	@Before
	public void setUp() {
		ds = MangaParser.getInstance().getDatastore();
		assertNotNull(ds);
	}
	
	@Test
	public void testHosterSave() {
		TestHoster h = new TestHoster();
		assertNull(h.getId());
		ds.save(h);
		assertNotNull(h.getId());
	}
	
	@Test
	public void testHosterLoad() {
		List<Hoster> lh = ds.find(Hoster.class).asList();
		assertNotSame("Cant find any hoster.", 0, lh.size());
		System.out.print(lh);
	}
	
	@Test
	public void testMangaSave() {
		Manga m = new TestManga();
		assertNull(m.getId());
		ds.save(m);
		assertNotNull("Cant save manga.", m.getId());
	}

	@Test
	public void testMangaLoad() {
		List<Manga> lm = ds.find(Manga.class).asList();
		assertNotSame("Cant find any Mangas", 0, lm.size());
		System.out.println(lm);
	}
}
