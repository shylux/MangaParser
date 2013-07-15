package mangaparser.data;

import java.net.URL;
import java.util.List;

public class MangapandaChapter extends Chapter {
	@SuppressWarnings("unused")
	private MangapandaChapter() {} // for mongodb
	
	public MangapandaChapter(String title, URL address) {
		this.title = title;
		this.address = address.toExternalForm();
	}

	@Override
	public List<String> loadPages() {
		// TODO Auto-generated method stub
		return pages;
	}

}
