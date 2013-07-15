package mangaparser.junit;

import static org.junit.Assert.*;

import java.util.List;

import mangaparser.MangaParser;
import mangaparser.data.Hoster;
import mangaparser.data.Manga;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class HosterTest {
	private static List<Hoster> hosters;

	@Before
	public void setUp() throws Exception {
		hosters = MangaParser.getInstance().getDatastore().find(Hoster.class).asList();
	}

	@Test
	public void testName() {
		for (Hoster h: hosters) {
			assertTrue("Hoster name empty.", h.getName().length() > 0);
		}
	}
	
	@Test
	public void testAddress() {
		for (Hoster h: hosters) {
			assertNotNull("Address not set.", h.getAddress());
		}
	}
	
	@Test
	public void testLoadMangas() {
		for (Hoster h: hosters) {
			List<Manga> l = h.loadMangas();
			assertNotNull("Load failed.", l);
			Assert.assertNotSame("No mangas found.", 0, l.size());
		}
	}
	
	@Test
	public void testQueryMangas() {
		for (Hoster h: hosters) {
			List<Manga> l = h.loadMangas();
			assertNotNull("Query failed.", l);
			Assert.assertNotSame("No mangas found.", 0, l.size());
		}
	}
	
	@Test
	public void printHosters() {
		for (Hoster h: hosters) {
			System.out.println(h);
		}
	}
}
