# java-nmap-xml-parser
A Java parser that converts nmap xml output to a POJO without any additional dependencies.

> For whatever reason you might want to work with Java on your nmap results o.0

[![license](https://img.shields.io/github/license/martinspielmann/java-nmap-xml-parser)](LICENSE)
[![maven central](https://img.shields.io/maven-central/v/de.martinspielmann.nmapxmlparser/nmapxmlparser)](https://search.maven.org/artifact/de.martinspielmann.nmapxmlparser/nmapxmlparser)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=martinspielmann_java-nmap-xml-parser&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=martinspielmann_java-nmap-xml-parser)

## Getting Started

### Prerequisites

This library requires Java 17 as a minimum version.

The easiest way to install nmapxmlparser is importing it via Maven. It is available from Maven Central:

```xml
<dependency>
  <groupId>de.martinspielmann.nmapxmlparser</groupId>
  <artifactId>nmapxmlparser</artifactId>
  <version>1.0.0</version>
</dependency>
```

### Usage
```java
// run 'nmap -oX' to get the result in XML format.
var parser = new NmapXmlParser();
var nmapRunFromString = parser.parse(yourNmapXmlResultAsString);

var nmapRunFromPath = parser.parse(Paths.get("pathToXmlResultFile"));

```

## Contributing

Any contributions you make will benefit everybody else and are **greatly appreciated**.



## License

This project is licensed under the **Apache License v2**. Feel free to edit and distribute this template as you like.

See [LICENSE](LICENSE) for more information.

