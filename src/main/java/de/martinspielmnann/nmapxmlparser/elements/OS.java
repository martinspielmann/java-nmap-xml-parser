package de.martinspielmnann.nmapxmlparser.elements;

import java.util.List;

public record OS(List<PortUsed> portsUsed, List<OSMatch> osMatches) {
}
