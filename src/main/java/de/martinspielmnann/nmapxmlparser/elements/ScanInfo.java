package de.martinspielmnann.nmapxmlparser.elements;

public record ScanInfo(String type, String protocol, Long numServices, String services) {
}
