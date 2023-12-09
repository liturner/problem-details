# Problem Details (RFC 9457)

This project provides a Java implementation of RFC 9457.

The implementation:
- Has a module-info.java
- Is available on Maven Central
- Supports extension
- Has no dependencies, other than Java itself

## Why Zero Dependencies?

This module has been written with low maintainance and high compatibility in mind. Any dependencies to this module would become transient depedencies in other applications. If this module would then not keep up with e.g. the xml.bind-api, then it would eventually become a problem for the projects using this module. Consider the problems if this project would stagnate with jakarta.xml.bind-api:3.0.0, while the using project wants to update its Spring version which has a dependency on jakarta.xml.bind-api:4.0.1.

The Java SDK ships with enough XML and Stream tools to be able to handle such basic XML objects with ease. As such, the maintenance cost and forced transient dependencies are (in my oppinion) not worth the hassle.

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

Contributions are very welcome! Critical is that no dependencies are added to the project. Otherwise suggestions, bug fixes etc. are all welcome. Open a new Issue and label it as a question if you have suggestions or want more information.

- All code must compile (without warnings) using ```mvn clean verify```
- The site must build completely using ```mvn clean verify site```