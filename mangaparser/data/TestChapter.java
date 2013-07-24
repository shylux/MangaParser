package mangaparser.data;

import java.util.ArrayList;
import java.util.List;

public class TestChapter extends Chapter {
	
	@SuppressWarnings("unused") // for mongodb
	private TestChapter() {}
	public TestChapter(String title) {
		this.address = title;
		this.title = getAddress().getFile();
	}

	@Override
	public List<String> loadPages() {
		List<String> p = new ArrayList<String>();
		p.add(address + "/1.jpg");
		p.add(address + "/2.jpg");
		p.add(address + "/3.jpg");
		p.add(address + "/4.jpg");
		this.pages = p;
		//TODO check if saved
		return pages;
	}

}
