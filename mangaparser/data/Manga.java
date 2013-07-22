package mangaparser.data;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mangaparser.Encodable;
import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

/**
 * Manga on a hoster. Like Naruto or One Piece.
 * @author shylux
 *
 */
@Entity("Mangas")
public abstract class Manga implements Encodable {
	@Id private ObjectId id;
	
	String title, description, author;
	int releaseYear;
	// valid url's
	String cover, address;
	boolean mature;
	List<Chapter> chapters = new ArrayList<Chapter>();
	
	/**
	 * Returns morphia id.
	 * @return morphia id
	 */
	public ObjectId getId() {
		return id;
	}
	
	/**
	 * Returns title of manga
	 * @return title of manga
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Returns a description of the manga
	 * @return description of manga
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Returns name of the author
	 * @return name or nickname of author
	 */
	public String getAuthor() {
		return author;
	}
	
	/**
	 * Returns year of first release
	 * @return release year
	 */
	public int getReleaseYear() {
		return releaseYear;
	}
	
	/**
	 * Returns URL to an image source used as front page of the manga.
	 * If URL is malformed return null.
	 * @return URL to front page
	 */
	public URL getCover() {
		try {
			return new URL(cover);
		} catch (MalformedURLException e) {
			if (address != null) e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Returns whether the manga is suitable for minors or not.
	 * @return whether manga contains mature content
	 */
	public boolean isMature() {
		return mature;
	}
	
	/**
	 * Returns URL of manga overview page.
	 * If URL is malformed return null.
	 * @return URL of manga overview page.
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
	 * Loads all the chapters from hoster.
	 * @return list of chapters on hoster
	 */
	public abstract List<Chapter> loadChapters();
	
	public int getChapterCount() {
		return chapters.size();
	}
	
	public String toString() {
		return String.format("{%s by %s, %d}", getTitle(), getAuthor(), getReleaseYear());
	}

	public String encode(String type, Class<?> limit) {
		if (type.equals(Encodable.DATATABLES)) return encodeDataTables(limit);
		// templates
		String templateXML = "<Manga><Title>%s</Title><Description>%s</Description><Author>%s</Author><ReleaseYear>%d</ReleaseYear><Mature>%b</Mature><Cover>%s</Cover><Address>%s</Address><Chapters>%s</Chapters></Manga>";
		String templateJSON = "{title: '%s', description: '%s', author: '%s', releaseYear: %d, mature: %b, cover: '%s', address: '%s', chapters: [%s]}";
		
		// check for limit
		StringBuilder sbchapters = new StringBuilder();
		if (limit != Manga.class) {
			Iterator<Chapter> i = chapters.iterator();
			while (i.hasNext()) {
				Chapter c = i.next();
				if (type.equals(Encodable.XML)) sbchapters.append(String.format("<Chapter>%s</Chapter>", c.encode(type, limit)));
				if (type.equals(Encodable.JSON)) {
					sbchapters.append(c.encode(type, limit));
					if (i.hasNext()) sbchapters.append(",");
				}
			}
		}
		
		// select format (default XML)
		String format = templateXML;
		if (type.equals(Encodable.XML)) {
			format = templateXML;
		} else if (type.equals(Encodable.JSON)) {
			format = templateJSON;
		}

		// build!
		return String.format(format, getTitle(), getDescription(), getAuthor(), getReleaseYear(), isMature(), getCover(), getAddress(), sbchapters);
	}
	
	private String encodeDataTables(Class<?> limit) {
		String templateDATATABLES = "['%s', '%s', %d, %d]";
		if (limit == Manga.class) {
			return String.format(templateDATATABLES, getTitle(), getAuthor(), getReleaseYear(), getChapterCount());
		} else if (limit == Chapter.class) {
			StringBuilder sb = new StringBuilder().append("[");
			Iterator<Chapter> i = chapters.iterator();
			while (i.hasNext()) {
				Chapter m = i.next();
				sb.append(m.encode(Encodable.DATATABLES, limit));
				if (i.hasNext()) sb.append(",");
			}
			sb.append("]");
			return sb.toString();
		}
		return null;
	}
}
