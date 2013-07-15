package mangaparser.data;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import mangaparser.MangaParser;
import mangaparser.XMLize;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

/**
 * Hoster of Mangas. Like Mangapanda or Mangafox.
 * @author shylux
 *
 */
@Entity("Hosters")
public abstract class Hoster implements XMLize {
	@Id private ObjectId id;
	
	String name, address;
	// icon url
	String icon;
	List<Manga> mangas = new ArrayList<Manga>();
	
	/**
	 * Returns morphia id.
	 * @return morphia id
	 */
	public ObjectId getId() {
		return id;
	}
	
	/**
	 * Returns the name of the hoster.
	 * @return Name of hoster
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the URL to the main page of the hoster.
	 * @return URL to hoster main page
	 */
	public URL getAddress() {
		try {
			return new URL(address);
		} catch (MalformedURLException e) {
			if (address != null) e.printStackTrace();
			return null;
		}
	}
	
	public URL getIconURL() {
		try {
			return new URL(icon);
		} catch (MalformedURLException e) {
			if (address != null) e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Loads mangas from the hoster.
	 * May take some time.
	 * @return list of mangas on hoster
	 */
	public abstract List<Manga> loadMangas();
	
	/**
	 * Loads mangas from local db.
	 * Fast and no network connection required. But of course not updated. 
	 * @return list of mangas in db
	 */
	public List<Manga> queryMangas() {
		//TODO query mangas from db
		return new ArrayList<Manga>();
	}
	
	public Manga findMangaByTitle(String title) {
		for (Manga m: mangas) {
			if (m.title.equals(title)) return m;
		}
		return null;
	}
	
	/**
	 * Saves the instance to db.
	 */
	public void save() {
		MangaParser.getInstance().getDatastore().save(this);
	}
	
	public String toString() {
		return String.format("{%s, %s}", getName(), getAddress());
	}
	
	public String toXML() {return toXML(null);}
	public String toXML(Class<?> limit) {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("<Name>%s</Name>", getName()));
		sb.append(String.format("<Address>%s</Address>", getAddress()));
		sb.append(String.format("<Icon>%s</Icon>", getIconURL()));
		if (limit == null | limit != Hoster.class) {
			sb.append("<Mangas>");
			for (Manga m: mangas) {
				sb.append(m.toXML(limit));
			}
			sb.append("</Mangas>");
		}
		return String.format("<Hoster>%s</Hoster>", sb);
	}
}
