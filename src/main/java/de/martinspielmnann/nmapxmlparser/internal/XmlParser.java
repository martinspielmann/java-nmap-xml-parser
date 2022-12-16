package de.martinspielmnann.nmapxmlparser.internal;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import de.martinspielmnann.nmapxmlparser.elements.Address;
import de.martinspielmnann.nmapxmlparser.elements.Debugging;
import de.martinspielmnann.nmapxmlparser.elements.Elem;
import de.martinspielmnann.nmapxmlparser.elements.ExtraPorts;
import de.martinspielmnann.nmapxmlparser.elements.ExtraReasons;
import de.martinspielmnann.nmapxmlparser.elements.Finished;
import de.martinspielmnann.nmapxmlparser.elements.Host;
import de.martinspielmnann.nmapxmlparser.elements.HostHint;
import de.martinspielmnann.nmapxmlparser.elements.HostName;
import de.martinspielmnann.nmapxmlparser.elements.HostNames;
import de.martinspielmnann.nmapxmlparser.elements.Hosts;
import de.martinspielmnann.nmapxmlparser.elements.IpIdSequence;
import de.martinspielmnann.nmapxmlparser.elements.NmapRun;
import de.martinspielmnann.nmapxmlparser.elements.OS;
import de.martinspielmnann.nmapxmlparser.elements.OSClass;
import de.martinspielmnann.nmapxmlparser.elements.OSMatch;
import de.martinspielmnann.nmapxmlparser.elements.Port;
import de.martinspielmnann.nmapxmlparser.elements.PortUsed;
import de.martinspielmnann.nmapxmlparser.elements.Ports;
import de.martinspielmnann.nmapxmlparser.elements.RunStats;
import de.martinspielmnann.nmapxmlparser.elements.ScanInfo;
import de.martinspielmnann.nmapxmlparser.elements.Script;
import de.martinspielmnann.nmapxmlparser.elements.Service;
import de.martinspielmnann.nmapxmlparser.elements.State;
import de.martinspielmnann.nmapxmlparser.elements.Status;
import de.martinspielmnann.nmapxmlparser.elements.Table;
import de.martinspielmnann.nmapxmlparser.elements.TcpSequence;
import de.martinspielmnann.nmapxmlparser.elements.TcpTsSequence;
import de.martinspielmnann.nmapxmlparser.elements.Times;
import de.martinspielmnann.nmapxmlparser.elements.Uptime;
import de.martinspielmnann.nmapxmlparser.elements.Verbose;

public class XmlParser {

  private static final String ADDRESS = "address";
  private static final String STATUS = "status";
  private static final String STATE = "state";
  private static final DocumentBuilderFactory FACTORY = DocumentBuilderFactory.newInstance();

  protected static Document getDocument(String xmlAsString)
      throws ParserConfigurationException, SAXException, IOException {
    DocumentBuilder builder = FACTORY.newDocumentBuilder();
    return builder.parse(new InputSource(new StringReader(xmlAsString)));
  }

  public static NmapRun parse(String xmlAsString) throws ParserConfigurationException, SAXException, IOException {
    Document document = getDocument(xmlAsString);
    var documentElement = document.getDocumentElement();
    return parseNmapRun(documentElement);
  }

  protected static NmapRun parseNmapRun(Element nmapRunElement) {
    validateNodeName(nmapRunElement, "nmaprun");

    var hostHintElements = nmapRunElement.getElementsByTagName("hosthint");
    var hostHints = new ArrayList<HostHint>();
    for (int i = 0; i < hostHintElements.getLength(); i++) {
      hostHints.add(parseHostHint((Element) hostHintElements.item(i)));
    }

    var hostElements = nmapRunElement.getElementsByTagName("host");
    var hosts = new ArrayList<Host>();
    for (int i = 0; i < hostElements.getLength(); i++) {
      hosts.add(parseHost((Element) hostElements.item(i)));
    }

    return new NmapRun(nmapRunElement.getAttribute("scanner"), nmapRunElement.getAttribute("args"),
        Long.parseLong(nmapRunElement.getAttribute("start")), nmapRunElement.getAttribute("startstr"),
        nmapRunElement.getAttribute("version"), nmapRunElement.getAttribute("xmloutputversion"),
        parseScanInfo(getSingleChildElement(nmapRunElement, "scaninfo")),
        parseVerbose(getSingleChildElement(nmapRunElement, "verbose")),
        parseDebugging(getSingleChildElement(nmapRunElement, "debugging")), hostHints, hosts,
        parseRunStats(getSingleChildElement(nmapRunElement, "runstats")));
  }

  protected static RunStats parseRunStats(Element runStatsElement) {
    validateNodeName(runStatsElement, "runstats");
    return new RunStats(parseFinished(getSingleChildElement(runStatsElement, "finished")),
        parseHosts(getSingleChildElement(runStatsElement, "hosts")));
  }

  private static Hosts parseHosts(Element hostsElement) {
    validateNodeName(hostsElement, "hosts");
    return new Hosts(Long.parseLong(hostsElement.getAttribute("up")), Long.parseLong(hostsElement.getAttribute("down")),
        Long.parseLong(hostsElement.getAttribute("total")));
  }

  private static Finished parseFinished(Element finishedElement) {
    validateNodeName(finishedElement, "finished");
    return new Finished(Long.parseLong(finishedElement.getAttribute("time")), finishedElement.getAttribute("timestr"),
        finishedElement.getAttribute("summary"), finishedElement.getAttribute("elapsed"),
        finishedElement.getAttribute("exit"));
  }

  protected static Debugging parseDebugging(Element debuggingElement) {
    validateNodeName(debuggingElement, "debugging");
    return new Debugging(debuggingElement.getAttribute("level"));
  }

  private static Verbose parseVerbose(Element verboseElement) {
    validateNodeName(verboseElement, "verbose");
    return new Verbose(verboseElement.getAttribute("level"));
  }

  protected static ScanInfo parseScanInfo(Element scanInfoElement) {
    validateNodeName(scanInfoElement, "scaninfo");
    return new ScanInfo(scanInfoElement.getAttribute("type"), scanInfoElement.getAttribute("protocol"),
        Long.parseLong(scanInfoElement.getAttribute("numservices")), scanInfoElement.getAttribute("services"));
  }

  protected static Address parseAddress(Element addressElement) {
    validateNodeName(addressElement, ADDRESS);
    return new Address(addressElement.getAttribute("addr"), addressElement.getAttribute("addrtype"));
  }

  protected static Status parseStatus(Element statusElement) {
    validateNodeName(statusElement, STATUS);
    return new Status(statusElement.getAttribute(STATE), statusElement.getAttribute("reason"),
        statusElement.getAttribute("reason_ttl"));
  }

  protected static State parseState(Element stateElement) {
    validateNodeName(stateElement, STATE);
    return new State(stateElement.getAttribute(STATE), stateElement.getAttribute("reason"),
        stateElement.getAttribute("reason_ttl"));
  }

  private static PortUsed parsePortUsed(Element el) {
    if (el == null) {
      return null;
    }
    validateNodeName(el, "portused");
    return new PortUsed(el.getAttribute(STATE), el.getAttribute("proto"), Long.parseLong(el.getAttribute("portid")));
  }

  protected static Service parseService(Element el) {
    if (el == null) {
      return null;
    }
    validateNodeName(el, "service");
    return new Service(el.getAttribute("name"), el.getAttribute("product"), el.getAttribute("extrainfo"),
        el.getAttribute("tunnel"), el.getAttribute("method"), el.getAttribute("conf"), el.getAttribute("servicefp"));
  }

  protected static HostHint parseHostHint(Element el) {
    if (el == null) {
      return null;
    }
    validateNodeName(el, "hosthint");
    var status = parseStatus(getSingleChildElement(el, STATUS));
    var address = parseAddress(getSingleChildElement(el, ADDRESS));
    var hostNames = parseHostNames(getSingleChildElement(el, "hostnames"));
    return new HostHint(status, address, hostNames);
  }

  protected static HostName parseHostName(Element el) {
    if (el == null) {
      return null;
    }
    validateNodeName(el, "hostname");
    return new HostName(el.getAttribute("name"), el.getAttribute("type"));
  }

  protected static ExtraReasons parseExtraReasons(Element el) {
    if (el == null) {
      return null;
    }
    validateNodeName(el, "extrareasons");
    var countString = el.getAttribute("count");
    var count = Long.parseLong(countString);
    return new ExtraReasons(el.getAttribute("reason"), count, el.getAttribute("proto"), el.getAttribute("ports"));
  }

  protected static ExtraPorts parseExtraPorts(Element el) {
    if (el == null) {
      return null;
    }
    validateNodeName(el, "extraports");
    var countString = el.getAttribute("count");
    var count = Long.parseLong(countString);
    var extraReasons = new ArrayList<ExtraReasons>();
    var extraReasonsElement = el.getElementsByTagName("extrareasons");
    for (int j = 0; j < extraReasonsElement.getLength(); j++) {
      extraReasons.add(parseExtraReasons((Element) extraReasonsElement.item(j)));
    }
    return new ExtraPorts(el.getAttribute("filtered"), count, extraReasons);
  }

  protected static HostNames parseHostNames(Element el) {
    if (el == null) {
      return null;
    }
    validateNodeName(el, "hostnames");
    var hostNames = new ArrayList<HostName>();
    var hostNameElement = el.getElementsByTagName("hostname");
    for (int j = 0; j < hostNameElement.getLength(); j++) {
      hostNames.add(parseHostName((Element) hostNameElement.item(j)));
    }
    return new HostNames(hostNames);
  }

  protected static Ports parsePorts(Element el) {
    if (el == null) {
      return null;
    }
    validateNodeName(el, "ports");
    var extraPortsElement = getSingleChildElement(el, "extraports");
    var portElement = el.getElementsByTagName("port");
    var ports = new ArrayList<Port>();
    for (int j = 0; j < portElement.getLength(); j++) {
      ports.add(parsePort((Element) portElement.item(j)));
    }
    return new Ports(parseExtraPorts(extraPortsElement), ports);
  }

  private static Port parsePort(Element el) {
    if (el == null) {
      return null;
    }
    validateNodeName(el, "port");
    var portId = Long.parseLong(el.getAttribute("portid"));
    var stateElement = getSingleChildElement(el, STATE);
    var serviceElement = getSingleChildElement(el, "service");
    var scriptElement = el.getElementsByTagName("script");
    var scripts = new ArrayList<Script>();
    for (int i = 0; i < scriptElement.getLength(); i++) {
      scripts.add(parseScript((Element) scriptElement.item(i)));
    }
    return new Port(el.getAttribute("protocol"), portId, parseState(stateElement), parseService(serviceElement),
        scripts);
  }

  private static Script parseScript(Element el) {
    if (el == null) {
      return null;
    }
    validateNodeName(el, "script");
    var elems = new ArrayList<Elem>();
    var elemElement = el.getElementsByTagName("elem");
    for (int i = 0; i < elemElement.getLength(); i++) {
      elems.add(parseElem((Element) elemElement.item(i)));
    }
    var tables = new ArrayList<Table>();
    var tableElement = el.getElementsByTagName("table");
    for (int i = 0; i < tableElement.getLength(); i++) {
      tables.add(parseTable((Element) tableElement.item(i)));
    }

    return new Script(el.getAttribute("id"), el.getAttribute("output"), elems, tables);
  }

  private static Table parseTable(Element el) {
    if (el == null) {
      return null;
    }
    validateNodeName(el, "table");
    var elems = new ArrayList<Elem>();
    var elemElement = el.getElementsByTagName("elem");
    for (int i = 0; i < elemElement.getLength(); i++) {
      elems.add(parseElem((Element) elemElement.item(i)));
    }
    var tables = new ArrayList<Table>();
    var tableElement = el.getElementsByTagName("table");
    for (int i = 0; i < tableElement.getLength(); i++) {
      tables.add(parseTable((Element) tableElement.item(i)));
    }
    return new Table(el.getAttribute("key"), elems, tables);
  }

  private static Elem parseElem(Element el) {
    if (el == null) {
      return null;
    }
    validateNodeName(el, "elem");
    return new Elem(el.getAttribute("key"), el.getTextContent());
  }

  private static OS parseOS(Element el) {
    if (el == null) {
      return null;
    }
    validateNodeName(el, "os");
    var portsUsed = new ArrayList<PortUsed>();
    var osMatches = new ArrayList<OSMatch>();
    var portUsedElement = el.getElementsByTagName("portused");
    for (int j = 0; j < portUsedElement.getLength(); j++) {
      portsUsed.add(parsePortUsed((Element) portUsedElement.item(j)));
    }
    var osMatchElement = el.getElementsByTagName("osmatch");
    for (int j = 0; j < osMatchElement.getLength(); j++) {
      osMatches.add(parseOSMatch((Element) osMatchElement.item(j)));
    }
    return new OS(portsUsed, osMatches);
  }

  private static OSMatch parseOSMatch(Element el) {
    if (el == null) {
      return null;
    }
    validateNodeName(el, "osmatch");
    var osClasses = new ArrayList<OSClass>();
    var osClassElement = el.getElementsByTagName("osclass");
    for (int j = 0; j < osClassElement.getLength(); j++) {
      osClasses.add(parseOSClass((Element) osClassElement.item(j)));
    }
    return new OSMatch(el.getAttribute("name"), Long.parseLong(el.getAttribute("accuracy")),
        Long.parseLong(el.getAttribute("line")), osClasses);
  }

  private static OSClass parseOSClass(Element el) {
    if (el == null) {
      return null;
    }
    validateNodeName(el, "osclass");
    var cpeElement = el.getElementsByTagName("cpe");
    var cpes = new ArrayList<String>();
    for (int i = 0; i < cpeElement.getLength(); i++) {
      cpes.add(cpeElement.item(i).getTextContent());
    }

    return new OSClass(el.getAttribute("name"), el.getAttribute("vendor"), el.getAttribute("osfamily"),
        el.getAttribute("osgen"), Long.parseLong(el.getAttribute("accuracy")), cpes);
  }

  protected static Host parseHost(Element el) {
    if (el == null) {
      return null;
    }
    validateNodeName(el, "host");
    return new Host(el.getAttribute("starttime"), el.getAttribute("endtime"),
        parseStatus(getSingleChildElement(el, STATUS)), parseAddress(getSingleChildElement(el, ADDRESS)),
        parseHostNames(getSingleChildElement(el, "hostnames")), parsePorts(getSingleChildElement(el, "ports")),
        parseOS(getSingleChildElement(el, "os")), parseUptime(getSingleChildElement(el, "uptime")),
        parseTcpSequence(getSingleChildElement(el, "tcpsequence")),
        parseIdIpSequence(getSingleChildElement(el, "ipidsequence")),
        parseTcpTsSequence(getSingleChildElement(el, "tcptssequence")), parseTimes(getSingleChildElement(el, "times")));
  }

  private static Uptime parseUptime(Element el) {
    if (el == null) {
      return null;
    }
    validateNodeName(el, "uptime");
    return new Uptime(Long.parseLong(el.getAttribute("seconds")), el.getAttribute("lastboot"));
  }

  private static TcpSequence parseTcpSequence(Element el) {
    if (el == null) {
      return null;
    }
    validateNodeName(el, "tcpsequence");
    return new TcpSequence(Long.parseLong(el.getAttribute("index")), el.getAttribute("difficulty"),
        el.getAttribute("values"));
  }

  private static IpIdSequence parseIdIpSequence(Element el) {
    if (el == null) {
      return null;
    }
    validateNodeName(el, "ipidsequence");
    return new IpIdSequence(el.getAttribute("class"), el.getAttribute("values"));
  }

  private static TcpTsSequence parseTcpTsSequence(Element el) {
    if (el == null) {
      return null;
    }
    validateNodeName(el, "tcptssequence");
    return new TcpTsSequence(el.getAttribute("class"), el.getAttribute("values"));
  }

  private static Times parseTimes(Element el) {
    if (el == null) {
      return null;
    }
    validateNodeName(el, "times");
    return new Times(Long.parseLong(el.getAttribute("srtt")), Long.parseLong(el.getAttribute("rttvar")),
        Long.parseLong(el.getAttribute("to")));
  }

  protected static Element getSingleChildElement(Element el, String tagName) {
    if (el == null) {
      return null;
    }
    var childElements = el.getElementsByTagName(tagName);
    return switch (childElements.getLength()) {
    case 1 -> (Element) childElements.item(0);
    case 0 -> null;
    default -> throw new IllegalArgumentException(tagName + " should only appear once in " + el);
    };
  }

  protected static void validateNodeName(Element element, String nodeName) {
    if (element != null && !element.getNodeName().equals(nodeName)) {
      throw new IllegalArgumentException(element + " has wrong node name. Should be " + nodeName);
    }
  }

}
