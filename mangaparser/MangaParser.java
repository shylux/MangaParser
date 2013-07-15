package mangaparser;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import mangaparser.data.Hoster;
import mangaparser.data.TestHoster;
import mangaparser.data.MangapandaHoster;

import org.simpleframework.http.core.Container;
import org.simpleframework.http.core.ContainerServer;
import org.simpleframework.transport.Server;
import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;

/**
 * Main Class
 * @author shylux
 *
 */
public class MangaParser {
	Morphia morphia = null;
	Datastore ds = null;
	
	public static final String DATASTORE_NAME = "devMangaParser";
	
	public static void main(String[] list) {
		getInstance();
	}
	
	// Singleton
	private static MangaParser _inst = null;
	public static MangaParser getInstance() {
		if (_inst == null) _inst = new MangaParser();
		return _inst;
	}
	private MangaParser() {
		morphia = new Morphia();
		morphia.map(Hoster.class);
		try {
			ds = morphia.createDatastore(new Mongo(), DATASTORE_NAME);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setupHosters();
		try {
			startServer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void startServer() throws IOException {
		Container container = new WebInterface(this);
		Server server = new ContainerServer(container);
		@SuppressWarnings("resource")
		Connection connection = new SocketConnection(server);
		SocketAddress address = new InetSocketAddress(1337);
		connection.connect(address);
	}
	
	public Datastore getDatastore() {
		return ds;
	}
	
	public void setupHosters() {
		//TODO search for cleaner code
		if (getDatastore().find(Hoster.class, "name", "TestHoster").get() == null) {
			getDatastore().save(new TestHoster());
		}
		if (getDatastore().find(Hoster.class, "name", "MangaPanda").get() == null) {
			getDatastore().save(new MangapandaHoster());
		}
	}
}
