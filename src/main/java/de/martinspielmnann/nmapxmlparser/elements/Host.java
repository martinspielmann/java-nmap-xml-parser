package de.martinspielmnann.nmapxmlparser.elements;

public record Host(String startTime, String endTime, Status status, Address address, HostNames hostNames, Ports ports,
		OS os, Uptime uptime, TcpSequence tcpSequence, IpIdSequence ipIdSequence, TcpTsSequence tcpTsSequence,
		Times times) {
}

