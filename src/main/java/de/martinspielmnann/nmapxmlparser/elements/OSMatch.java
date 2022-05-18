package de.martinspielmnann.nmapxmlparser.elements;

import java.util.List;

public record OSMatch(String name, Long accuracy, Long line, List<OSClass> osClasses) {
}
