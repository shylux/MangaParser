package mangaparser;

//TODO check spelling
/**
 * Not sure about the correct spelling of Encodable (or Encodeable?)
 * @author shylux
 *
 */
public interface Encodable {
	public static final String XML = "XML";
	public static final String JSON = "JSON";
	
	/**
	 * Encodes the object to an external format.
	 * Encodes the object to the external format specified in type. XML and JSON are mandatory.
	 * Limit describes how deep the encoding reaches. If limit is the current class then instances of Encodable in object wont be included.
	 * @param type Type of encoding. XML and JSON are mandatory.
	 * @param limit Limit of how deep the encoding should reach.
	 * @return
	 */
	public String encode(String type, Class<?> limit);
}
