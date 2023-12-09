package de.turnertech.problemdetails;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidParameterException;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * A POJO representation of the urn:ietf:rfc:7807 problem type.
 */
public class Problem {
    
    /** urn:ietf:rfc:7807 */
    public static final String NAMESPACE = "urn:ietf:rfc:7807";
    
    /** application/problem+xml */
    public static final String MEDIA_TYPE_XML = "application/problem+xml";
    
    /** application/problem+json */
    public static final String MEDIA_TYPE_JSON = "application/problem+json";
    
    private static final ResourceBundle i18n = ResourceBundle.getBundle("de.turnertech.problemdetails.i18n");

    private static final String PROBLEM_STRING = "problem";
    
    // Mandatory with default
    private URI type;
    
    // Optional, must be same as HTTP response if present
    private Integer status;
    
    // Advisory
    private String title;
    
    // Optional
    private String detail;
    
    // Optional
    private URI instance;

    /**
     * Constructs an empty Problem with the type "about:blank" (RFC 9457 - 4.2.1.).
     */
    public Problem() {
        this(URI.create("about:blank"));
    }

    /**
     * Constructs a Problem with the provided parameters. Note that type may not be null.
     * @param type RFC 9457 - 3.1.1.
     * @throws NullPointerException if type is null.
     */
    public Problem(URI type) {
        this(type, null);
    }

    /**
     * Constructs a Problem with the provided parameters. Note that type may not be null.
     * @param type RFC 9457 - 3.1.1.
     * @param instance RFC 9457 - 3.1.5.
     * @throws NullPointerException if type is null.
     */
    public Problem(URI type, URI instance) throws NullPointerException {
        this.type = Objects.requireNonNull(type, i18n.getString("error.type.nonnull"));
        this.instance = instance;
    }
    
    /**
     * Constructs a deep copy of the provided problem. If you extend this class, make sure
     * that your implementation of the copy constructor also returns deep copies.
     * @param other problem to copy.
     */
    public Problem(Problem other) {
        this.type = other.type;
        this.status = other.status;
        this.title = other.title;
        this.detail = other.detail;
        this.instance = other.instance;
    }

    /**
     * Gets the problem type.
     * @return the problem type.
     */
    public URI getType() {
        return type;
    }

    /**
     * Sets the problem type.
     * @param type the problem type.
     * @throws NullPointerException if type is null
     */
    public void setType(URI type) throws NullPointerException {
        this.type = Objects.requireNonNull(type, i18n.getString("error.type.nonnull"));
    }

    /**
     * Gets the problem status code (HTTP Status Code).
     * @return the problem status code (HTTP Status Code)
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * Sets the problem status code (HTTP Status Code). If set, this must be the same as the one used in your HTTP Response.
     * @param status the problem status code (HTTP Status Code).
     * @see #findStatusPhrase(int)
     */
    public void setStatus(Integer status) {
        if(status != null && (status < 100 || status > 599)) {
            throw new InvalidParameterException(i18n.getString("error.status.inrange"));
        }
        this.status = status;
    }

    /**
     * Gets the problem title.
     * @return the problem title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the problem title.
     * @param title the short human readable title.
     * @see #findStatusPhrase(int)
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the problem detail.
     * @return the problem detail.
     */
    public String getDetail() {
        return detail;
    }

    /**
     * Sets the problem detail.
     * @param detail the problem detail.
     */
    public void setDetail(String detail) {
        this.detail = detail;
    }

    /**
     * Gets the problem instance.
     * @return the problem instance.
     */
    public URI getInstance() {
        return instance;
    }

    /**
     * Sets the problem instance.
     * @param instance the problem instance.
     */
    public void setInstance(URI instance) {
        this.instance = instance;
    }

    /**
     * Helper for retrieving the HTTP Status Phrase for a HTTP Status Code.
     * @param statusCode the HTTP Status Code.
     * @return the HTTP Status Phrase in English, may be null.
     * @see #findStatusPhrase(int, Locale)
     */
    public static String findStatusPhrase(int statusCode) {
        return findStatusPhrase(statusCode, Locale.ENGLISH);
    }

    /**
     * Helper for retrieving the localised HTTP Status Phrase for a HTTP Status Code.
     * @param statusCode the HTTP Status Code.
     * @param locale the desired language.
     * @return the HTTP Status Phrase, may be null.
     * @see #findStatusPhrase(int)
     */
    public static String findStatusPhrase(int statusCode, Locale locale) {
        ResourceBundle strings = ResourceBundle.getBundle("de.turnertech.problemdetails.i18n", locale);
        return strings.getString(Integer.toString(statusCode));
    }
    
    /**
     * Converts the contents to a String containing the JSON representation of this Problem.
     * @return the JSON String.
     * @throws IOException if there are problems with extending the JSON.
     * @see #extendJson(OutputStream, Charset)
     * @see #toJson(OutputStream)
     * @see #toJson(OutputStream, Charset)
     */
    public String toJson() throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            toJson(outputStream);
            return outputStream.toString(StandardCharsets.UTF_8);
        }
    }

    /**
     * Writes the Problem to the supplied stream in JSON format using UTF-8.
     * @param outputStream the stream to write to.
     * @throws IOException if there are problems with extending the JSON.
     * @see #extendJson(OutputStream, Charset)
     * @see #toJson()
     * @see #toJson(OutputStream, Charset)
     */
    public void toJson(OutputStream outputStream) throws IOException {
        toJson(outputStream, StandardCharsets.UTF_8);
    }

    /**
     * Writes the Problem to the supplied stream in JSON format using the supplied Charset.
     * @param outputStream the stream to write to.
     * @param charset the Charset to use.
     * @throws IOException if there are problems with extending the JSON.
     */
    public void toJson(OutputStream outputStream, Charset charset) throws IOException {
        try(PrintWriter printWriter = new PrintWriter(outputStream)) {
            printWriter.write("{\"type\":\"");
            printWriter.write(type.toString());
            printWriter.write("\"");

            if(title != null) {
                printWriter.write(",\"title\":\"");
                printWriter.write(title);
                printWriter.write("\"");
            }
            if(status != null) {
                printWriter.write(",\"status\":\"");
                printWriter.write(Integer.toString(status));
                printWriter.write("\"");
            }
            if(detail != null) {
                printWriter.write(",\"detail\":\"");
                printWriter.write(detail);
                printWriter.write("\"");
            }
            if(instance != null) {
                printWriter.write(",\"instance\":\"");
                printWriter.write(instance.toString());
                printWriter.write("\"");
            }

            printWriter.flush();

        }

        extendJson(outputStream, charset);

        outputStream.write("}".getBytes(charset));
    }

    /**
     * <p>Override this if you wish to extend the JSON response. This function is called directly before
     * closing the problem element. Pay carefull attention to the namespaces!</p>
     * @param outputStream the stream to write to
     * @param charset the charset this XML is being written in
     * @return true if an element was inserted (so that the caller can decide whether or not to write a ",")
     */
    protected boolean extendJson(OutputStream outputStream, Charset charset) {
        // Called before closing the last element.
        return false;
    }

    /**
     * Converts the contents to a String containing the XML representation of this Problem.
     * @return the XML String.
     * @throws XMLStreamException if there are problems with extending the XML.
     * @see #extendXml(XMLStreamWriter, Charset)
     * @see #toXml(OutputStream, Charset, boolean)
     * @see #toXml(XMLStreamWriter, Charset, boolean)
     */
    public String toXml() throws XMLStreamException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            toXml(outputStream, StandardCharsets.UTF_8, true);
            return outputStream.toString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            return null;
        }
    }
    
    /**
     * Writes the Problem to the supplied stream in XML format using the supplied charset.
     * @param outputStream to write to.
     * @param charset to write using.
     * @param writeStartDocument to indicate if the xml start document should also be written.
     * @throws XMLStreamException if there are problems with extending the XML.
     */
    public void toXml(OutputStream outputStream, Charset charset, boolean writeStartDocument) throws XMLStreamException {
        XMLOutputFactory outputFactory = XMLOutputFactory.newFactory();
        outputFactory.setProperty(XMLOutputFactory.IS_REPAIRING_NAMESPACES, true);
        XMLStreamWriter xmlStreamWriter = outputFactory.createXMLStreamWriter(outputStream, charset.toString());
        toXml(xmlStreamWriter, charset, writeStartDocument);
    }
    
    /**
     * Writes the Problem to the supplied writer in XML format using the supplied charset.
     * @param xmlStreamWriter to write using.
     * @param charset to write using.
     * @param writeStartDocument to indicate if the xml start document should also be written.
     * @throws XMLStreamException if there are problems with extending the XML.
     */
    public void toXml(XMLStreamWriter xmlStreamWriter, Charset charset, boolean writeStartDocument) throws XMLStreamException {
        if(writeStartDocument) {
            xmlStreamWriter.writeStartDocument(charset.toString(), "1.0");
        }
        
        String defaultNs = xmlStreamWriter.getNamespaceContext().getNamespaceURI("");
        String prefix = xmlStreamWriter.getNamespaceContext().getPrefix(NAMESPACE);
        
        if(prefix != null) {
            xmlStreamWriter.writeStartElement(NAMESPACE, PROBLEM_STRING);
        } else if(defaultNs == null) {
            xmlStreamWriter.writeStartElement(PROBLEM_STRING);
            xmlStreamWriter.writeDefaultNamespace(NAMESPACE);
        } else {
            prefix = "p";
            int i = 0;
            while(xmlStreamWriter.getNamespaceContext().getNamespaceURI(prefix) != null) {
                prefix = "p" + Integer.toString(i);
            }
            xmlStreamWriter.writeStartElement(prefix, PROBLEM_STRING, NAMESPACE);
            xmlStreamWriter.writeNamespace(prefix, NAMESPACE);
        }
        
        if(type != null) {
            xmlStreamWriter.writeStartElement(NAMESPACE, PROBLEM_STRING);
            xmlStreamWriter.writeCharacters(type.toString());
            xmlStreamWriter.writeEndElement();
        }

        if(title != null) {
            xmlStreamWriter.writeStartElement(NAMESPACE, "title");
            xmlStreamWriter.writeCharacters(title);
            xmlStreamWriter.writeEndElement();
        }

        if(status != null) {
            xmlStreamWriter.writeStartElement(NAMESPACE, "status");
            xmlStreamWriter.writeCharacters(Integer.toString(status));
            xmlStreamWriter.writeEndElement();
        }

        if(detail != null) {
            xmlStreamWriter.writeStartElement(NAMESPACE, "detail");
            xmlStreamWriter.writeCharacters(detail);
            xmlStreamWriter.writeEndElement();
        }

        if(instance != null) {
            xmlStreamWriter.writeStartElement(NAMESPACE, "instance");
            xmlStreamWriter.writeCharacters(instance.toString());
            xmlStreamWriter.writeEndElement();
        }
        
        extendXml(xmlStreamWriter, charset);
        
        // /problem
        xmlStreamWriter.writeEndElement();
        
        if(writeStartDocument) {
            xmlStreamWriter.writeEndDocument();
        }
    }
    
    /**
     * Override this if you wish to extend the XML response. This function is called directly before
     * closing the problem element. Pay carefull attention to the namespaces!
     * @param xmlStreamWriter the writer you must use to write to the stream
     * @param charset the charset this XML is being written in
     * @throws XMLStreamException if there are problems with extending the XML.
     */
    protected void extendXml(XMLStreamWriter xmlStreamWriter, Charset charset) throws XMLStreamException {
        // Called before closing the last element.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        result = prime * result + ((detail == null) ? 0 : detail.hashCode());
        result = prime * result + ((instance == null) ? 0 : instance.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Problem other = (Problem) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        if (status == null) {
            if (other.status != null)
                return false;
        } else if (!status.equals(other.status))
            return false;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        if (detail == null) {
            if (other.detail != null)
                return false;
        } else if (!detail.equals(other.detail))
            return false;
        if (instance == null) {
            if (other.instance != null)
                return false;
        } else if (!instance.equals(other.instance))
            return false;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Problem [type=" + type + ", title=" + title + "]";
    }
    
}
