# Problem Details (RFC 9457)

This project provides a Java implementation of RFC 9457.

The implementation:
- Has a module-info.java
- Has an OSGi compatible MANIFEST.MF
- Is available on Maven Central
- Supports extension
- Has no dependencies, other than Java itself

# Usage

The full [Javadoc](apidocs) is available.

```java
Problem myProblem = new Problem();
myProblem.setStatus(404);
myProblem.setTitle(Problem.findStatusPhrase(404));
System.out.println(myProblem.toJson());
System.out.println(myProblem.toXml());

// Results in the folowing output:
//
// {"type":"about:blank","title":"Not Found","status":"404"}
// <?xml version="1.0" encoding="UTF-8"?><problem xmlns="urn:ietf:rfc:7807"><problem>about:blank</problem><title>Not Found</title><status>404</status></problem>
```

If you are using this in a HTTP Servlet or similar, you will probably be using something like this:

```java
@Override
protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
    String requestedContentType = httpServletRequest.getHeader("Accept");

    Problem myProblem = new Problem();
    myProblem.setStatus(404);
    myProblem.setTitle(Problem.findStatusPhrase(myProblem.getStatus()));
    
    httpServletResponse.setStatus(myProblem.getStatus());

    if(Problem.MEDIA_TYPE_XML.equals(requestedContentType)) {
        httpServletResponse.setContentType(Problem.MEDIA_TYPE_XML);
        myProblem.toXml(httpServletResponse.getOutputStream(), StandardCharsets.UTF_8, true);
    } else if(Problem.MEDIA_TYPE_JSON.equals(requestedContentType)) {
        httpServletResponse.setContentType(Problem.MEDIA_TYPE_JSON);
        myProblem.toJson(httpServletResponse.getOutputStream(), StandardCharsets.UTF_8);    
    }
}
```

# Extensions

```java
public class BiggerProblem extends Problem {

    private static final String NAMESPACE = "http://my.namespace";

    private String solution = "Moar Hugs";

    @Override
    protected boolean extendJson(OutputStream outputStream, Charset charset) {
        try(PrintWriter printWriter = new PrintWriter(outputStream)) {
            printWriter.write(",\"solution\":\"");
            printWriter.write(solution);
            printWriter.write("\"");
            printWriter.flush();
        }
        return true;
    }

    @Override
    protected void extendXml(XMLStreamWriter xmlStreamWriter, Charset charset) throws XMLStreamException {
        xmlStreamWriter.writeStartElement("bp", solution, NAMESPACE);
        xmlStreamWriter.writeCharacters(solution);
        xmlStreamWriter.writeEndElement();
    }

}
```

```java
Problem myProblem = new BiggerProblem();
myProblem.setStatus(404);
myProblem.setTitle(Problem.findStatusPhrase(404));
System.out.println(myProblem.toJson());
System.out.println(myProblem.toXml());

// Results in the folowing output:
//
// {"type":"about:blank","title":"Not Found","status":"404","solution":"Moar Hugs"}
// <?xml version="1.0" encoding="UTF-8"?><problem xmlns="urn:ietf:rfc:7807"><problem>about:blank</problem><title>Not Found</title><status>404</status><bp:Moar Hugs>Moar Hugs</bp:Moar Hugs></problem>
```
