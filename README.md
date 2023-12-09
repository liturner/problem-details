# Problem Details (RFC 9457)

This project provides a Java implementation of RFC 9457.

The implementation:
- Has a module-info.java
- Is available on Maven Central
- Supports extension
- Has no dependencies, other than Java itself

# Usage

Check out the Maven Site for all developer documentation [here](https://liturner.github.io/problem-details/)

The Maven Coordinates are:

```xml
<dependency>
  <groupId>de.turnertech</groupId>
  <artifactId>problem-details</artifactId>
  <version>...</version>
</dependency>
```

# Contributing

Contributions are very welcome! Critical is that no dependencies are added to the project. Otherwise suggestions, bug fixes etc. are all welcome.

- All code must compile (without warnings) using ```mvn clean verify```
- The site must build completely using ```mvn clean verify site```