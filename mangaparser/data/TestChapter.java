package mangaparser.data;

import java.util.ArrayList;
import java.util.List;

public class TestChapter extends Chapter {
	private String prefix;
	
	@SuppressWarnings("unused") // for mongodb
	private TestChapter() {}
	public TestChapter(String prefix) {
		this.prefix = prefix;
	}

	@Override
	public List<String> loadPages() {
		List<String> p = new ArrayList<String>();
		p.add(prefix + "1.jpg");
		p.add(prefix + "2.jpg");
		p.add(prefix + "3.jpg");
		p.add(prefix + "4.jpg");
		this.pages = p;
		//TODO check if saved
		return pages;
	}

}
