package de.martinspielmnann.nmapxmlparser.elements;

import java.util.List;

public record Port(String protocol, Long portId, State state, Service service, List<Script> scripts) {
}
