package info.thezero.eclipse.pdt.restapi.uri;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class UriFileParser extends DefaultHandler {
	private StringBuilder content;
	private UriMapCollection mapCollection;
	private UriMap currentMap;

	public UriFileParser(UriMapCollection mapCollection) {
		this.content = new StringBuilder();
		this.mapCollection = mapCollection;
	}

	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {
		this.content = new StringBuilder();

		if (qName.equalsIgnoreCase("collection")) {
			this.currentMap = this.mapCollection.addMap();
		}
	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		if (qName.equalsIgnoreCase("uri")) {
			this.currentMap.addUri(this.content.toString());
		} else if (qName.equalsIgnoreCase("collection")) {
			this.currentMap.finishInit();
		} else if (qName.equalsIgnoreCase("trigger")) {
			this.currentMap.addTrigger(this.content.toString());
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
