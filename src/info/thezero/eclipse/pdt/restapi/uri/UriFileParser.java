package info.thezero.eclipse.pdt.restapi.uri;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class UriFileParser extends DefaultHandler {
	private StringBuilder content;
	private UriMap map;

	public UriFileParser(UriMap map) {
		this.content = new StringBuilder();
		this.map = map;
	}

	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {
		this.content = new StringBuilder();
	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		if (qName.equalsIgnoreCase("uri")) {
			this.map.addUri(this.content.toString());
		}
	}

	public void characters(char[] ch, int start, int length)
			throws SAXException {
		content.append(ch, start, length);
	}

	public void endDocument() throws SAXException {
		// you can do something here for example send
		// the Channel object somewhere or whatever.
	}
}
