package de.martinspielmnann.nmapxmlparser.elements;

import java.util.List;

public record Script(String id, String output, List<Elem> elems, List<Table> tables) {
}
