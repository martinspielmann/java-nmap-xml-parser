package de.martinspielmnann.nmapxmlparser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import de.martinspielmnann.nmapxmlparser.elements.NmapRun;
import de.martinspielmnann.nmapxmlparser.internal.XmlParser;

public class NmapXmlParser {

	public NmapRun parse(Path pathToXml) throws IOException, NmapParserException {
		return parse(Files.readString(pathToXml));
	}

	public NmapRun parse(String xmlAsString) throws NmapParserException {
		try {
			return XmlParser.parse(xmlAsString);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			throw new NmapParserException(String.format("Error parsing [%s]", xmlAsString), e);
		}
	}

}
