package de.martinspielmnann.nmapxmlparser.elements;

import java.util.List;

public record NmapRun(
		String scanner,
		String args,
		Long start,
		String startstr,
		String version,
		String xmlOutputVersion,
		ScanInfo scanInfo,
		Verbose verbose,
		Debugging debugging,
		List<HostHint> hostHints,
		List<Host> hosts,
		RunStats runStats
) {
}