package de.martinspielmnann.nmapxmlparser.elements;

public record Service(String name, String product, String extraInfo, String tunnel, String method, String conf,
    String serviceFP) {
}
