package de.martinspielmnann.nmapxmlparser.elements;

import java.util.List;

public record Ports(ExtraPorts extraPorts, List<Port> ports) {
}
