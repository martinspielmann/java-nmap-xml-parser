package de.martinspielmnann.nmapxmlparser.elements;

import java.util.List;

public record Table(String key, List<Elem> elems, List<Table> tables) {
}
