package mangaparser.data;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import mangaparser.Encodable;
import mangaparser.MangaParser;
import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

/**
 * Hoster of Mangas. Like Mangapanda or Mangafox.
 * @author shylux
 *
 */
@Entity("Hosters")
public abstract class Hoster implements Encodable {
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
	
	/**
	 * Search for manga in hoster.
	 * @param title Name of manga.
	 * @return Manga or null if not found.
	 */
	public Manga findMangaByTitle(String title) {
		for (Manga m: mangas) {
			if (m.title.equals(title)) return m;
		}
		return null;
	}
	
	/**
	 * Returns amount of mangas on hoster.
	 * @return amount of mangas
	 */
	public int getMangaCount() {
		return mangas.size();
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
	
	public String encode(String type, Class<?> limit) {
		// templates
		String templateXML = "<Hoster><Name>%s</Name><Address>%s</Address><Icon>%s</Icon><Mangas>%s</Mangas></Hoster>";
		String templateJSON = "{'name': '%s', 'address': '%s', 'icon': '%s', 'mangas': [%s]}";
		String templateDATATABLES = "['%s', %d]";
		
		// check for limit
		StringBuilder sbmangas = new StringBuilder();
		if (limit != Hoster.class) {
			for (Manga m: mangas) {
				sbmangas.append(String.format("'%s', ", m.encode(type, limit)));
			}
		}
		
		// select format (default XML)
		String format = templateXML;
		if (type.equals(Encodable.XML)) {
			format = templateXML;
		} else if (type.equals(Encodable.JSON)) {
			format = templateJSON;
		} else if (type.equals(Encodable.DATATABLES)) {
			format = templateDATATABLES;
		}

		// build!
		if (type.equals(Encodable.DATATABLES)) {
			return String.format(format, getName(), getMangaCount());
		} else {
			return String.format(format, getName(), getAddress(), getIconURL(), sbmangas);
		}
	}
}
