package de.martinspielmnann.nmapxmlparser.elements;

import java.util.List;

public record ExtraPorts(String filtered, Long count, List<ExtraReasons> extraReasons) {}
