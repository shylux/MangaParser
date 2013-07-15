package mangaparser.data;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import mangaparser.XMLize;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

/**
 * Chapter of a manga.
 * @author shylux
 *
 */
@Entity
public abstract class Chapter implements XMLize {
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
	
	public String toXML() {return toXML(null);}
	public String toXML(Class<?> limit) {
		StringBuilder sb = new StringBuilder();
		sb.append("<Chapter>");
		sb.append(String.format("<Title>%s</Title>", getTitle()));
		sb.append("<Pages>");
		for (String page: pages) {
			sb.append(String.format("<Page>%s</Page>", page));
		}
		sb.append("</Pages>");
		sb.append("</Chapter>");
		return sb.toString();
	}
}
