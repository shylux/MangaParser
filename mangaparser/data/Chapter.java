package mangaparser.data;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import mangaparser.Encodable;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

/**
 * Chapter of a manga.
 * @author shylux
 *
 */
@Entity
public abstract class Chapter implements Encodable {
	@Id private ObjectId id;
	
	String title;
	String address;
	// list of URLs
	List<String> pages = new ArrayList<String>();

	/**
	 * Returns title of chapter
	 * @return title of chapter
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Returns address of chapter start
	 * @return address of chapter
	 */
	public URL getAddress() {
		try {
			return new URL(address);
		} catch (MalformedURLException e) {
			if (address != null) e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Returns a list with URLs (Strings) to the pages. Pages are stored as images.
	 * @return list of pages
	 */
	public abstract List<String> loadPages();
	
	public String encode(String type, Class<?> limit) {
		// templates
		String templateXML = "<Chapter><Title>%s</Title><Address>%s</Address><Pages>%s</Pages></Chapter>";
		String templateJSON = "{'title': '%s', 'address': '%s', 'pages': [%s]}";
		
		// no embedded objects. so no need for limit.
		StringBuilder sbpages = new StringBuilder();
		for (String p: pages) {
			sbpages.append(String.format("'%s', ", p));
		}
		
		// select format (default XML)
		String format = templateXML;
		if (type.equals(Encodable.XML)) {
			format = templateXML;
		} else if (type.equals(Encodable.JSON)) {
			format = templateJSON;
		}

		// build!
		return String.format(format, getTitle(), getAddress(), sbpages);
	}
}
