# java-nmap-xml-parser
A Java parser that converts nmap xml output to a POJO without any additional dependencies.

-- For whatever reason you might want to work with Java on your nmap results o.0


### Usage

```java
// run 'nmap -oX' to get the result in XML format.
var nmapRun = XmlParser.parse(yourNmapXmlOutputAsString);

```

### Requirements
This library requires Java 17 as a minimum version.

