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
 * Manga on a hoster. Like Naruto or One Piece.
 * @author shylux
 *
 */
@Entity("Mangas")
public abstract class Manga implements XMLize {
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
	
	public String toString() {
		return String.format("{%s by %s, %d}", getTitle(), getAuthor(), getReleaseYear());
	}

	public String toXML() {return toXML(null);}
	public String toXML(Class<?> limit) {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("<Title>%s</Title>", getTitle()));
		sb.append(String.format("<Description>%s</Description>", getDescription()));
		sb.append(String.format("<Author>%s</Author>", getAuthor()));
		sb.append(String.format("<ReleaseYear>%d</ReleaseYear>", getReleaseYear()));
		sb.append(String.format("<Cover>%s</Cover>", getCover()));
		sb.append(String.format("<Address>%s</Address>", getAddress()));
		sb.append(String.format("<Mature>%b</Mature>", isMature()));
		if (limit == null | limit == Chapter.class) {
			sb.append("<Chapters>");
			for (Chapter c: chapters) {
				sb.append(c.toXML());
			}
			sb.append("</Chapters>");
		}
		return String.format("<Manga>%s</Manga>", sb);
	}
}
