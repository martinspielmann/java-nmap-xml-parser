package de.martinspielmnann.nmapxmlparser.elements;

public record Port(String protocol, Long portId, State state, Service service) {
}
