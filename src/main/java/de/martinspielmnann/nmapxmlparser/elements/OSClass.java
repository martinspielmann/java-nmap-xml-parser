package de.martinspielmnann.nmapxmlparser.elements;

import java.util.List;

//<osclass type="general purpose" vendor="FreeBSD"
//osfamily="FreeBSD" osgen="9.X" accuracy="85">
//<cpe>cpe:/o:freebsd:freebsd:9.1</cpe>
//</osclass>
public record OSClass(String type, String vendor, String osFamily, String osGen, Long accuracy, List<String> cpes) {
}
