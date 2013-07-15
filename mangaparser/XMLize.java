package mangaparser;

public interface XMLize {
	/**
	 * Get a deep XML representation of the class. 
	 * @return XML of class and all subclasses
	 */
	public String toXML();
	/**
	 * Get a XML representation of the class. Stop descending to subclass at 'limit'.
	 * @param limit stop descending at this class
	 * @return XML of class and subclasses until 'limit'
	 */
	public String toXML(Class<?> limit);
}
