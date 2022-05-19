package de.martinspielmnann.nmapxmlparser.internal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import de.martinspielmnann.nmapxmlparser.elements.NmapRun;

class XmlParserTest {

	private String getXmlOutputWithScript() throws IOException {
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		InputStream is = classloader.getResourceAsStream("nmap-output-with-script.xml");
		return new String(is.readAllBytes());
	}

	private String getXmlOutputWithOs() throws IOException {
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		InputStream is = classloader.getResourceAsStream("nmap-output-with-os.xml");
		return new String(is.readAllBytes());
	}

	@Test
	void testGetDocument() throws ParserConfigurationException, SAXException, IOException {
		Document document = XmlParser.getDocument(getXmlOutputWithScript());
		assertNotNull(document);
	}

	@Test
	void testParseHostAddress() throws ParserConfigurationException, SAXException, IOException {
		NmapRun nmapRun = XmlParser.parse(getXmlOutputWithScript());
		assertEquals("nmap -oX nmap-output.xml -sV -A 8.8.8.8 example.com", nmapRun.args());
		assertEquals("8.8.8.8", nmapRun.hosts().get(0).address().addr());
	}

	@Test
	void testParseScriptKey() throws ParserConfigurationException, SAXException, IOException {
		NmapRun nmapRun = XmlParser.parse(getXmlOutputWithScript());
		assertEquals("FourOhFourRequest",
				nmapRun.hosts().get(0).ports().ports().get(1).scripts().get(0).elems().get(0).key());
		assertEquals("dns.google",
				nmapRun.hosts().get(0).ports().ports().get(1).scripts().get(2).tables().get(0).elems().get(0).value());
	}

	@Test
	void testParseOsClassCpe() throws ParserConfigurationException, SAXException, IOException {
		NmapRun nmapRun = XmlParser.parse(getXmlOutputWithOs());
		assertEquals("cpe:/o:freebsd:freebsd:9.1",
				nmapRun.hosts().get(0).os().osMatches().get(1).osClasses().get(0).cpes().get(0));
		System.out.println(nmapRun);
	}

}
