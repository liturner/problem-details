package de.turnertech.problemdetails;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.InvalidParameterException;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class Problem {
    
    public static final String NAMESPACE = "urn:ietf:rfc:7807";
    
    public static final String MEDIA_TYPE_XML = "application/problem+xml";
    
    public static final String MEDIA_TYPE_JSON = "application/problem+json";
    
    private static final ResourceBundle i18n = ResourceBundle.getBundle("de.turnertech.problemdetails.i18n");

    private static final String PROBLEM_STRING = "PROBLEM";
    
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

    public Problem() {
        this(URI.create("about:blank"));
    }

    public Problem(URI type) throws NullPointerException {
        this(type, null);
    }

    public Problem(URI type, URI instance) throws NullPointerException {
        this.type = Objects.requireNonNull(type, i18n.getString("error.type.nonnull"));
        this.instance = instance;
    }
    
    public Problem(Problem other) {
        this.type = other.type;
        this.status = other.status;
        this.title = other.title;
        this.detail = other.detail;
        this.instance = other.instance;
    }

    public URI getType() {
        return type;
    }

    public void setType(URI type) throws NullPointerException {
        this.type = Objects.requireNonNull(type, i18n.getString("error.type.nonnull"));
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        if(status != null && (status < 100 || status > 599)) {
            throw new InvalidParameterException(i18n.getString("error.status.inrange"));
        }
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public URI getInstance() {
        return instance;
    }

    public void setInstance(URI instance) {
        this.instance = instance;
    }

    public static String getStatusPhrase(int statusCode) {
        return getStatusPhrase(statusCode, Locale.ENGLISH);
    }

    public static String getStatusPhrase(int statusCode, Locale locale) {
        ResourceBundle strings = ResourceBundle.getBundle("de.turnertech.problemdetails.i18n", locale);
        return strings.getString(Integer.toString(statusCode));
    }
    
    public String toJson() throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            toJson(outputStream);
            return outputStream.toString(StandardCharsets.UTF_8);
        }
    }

    public void toJson(OutputStream outputStream) throws IOException {
        boolean previousElemet = false;
        try(PrintWriter printWriter = new PrintWriter(outputStream)) {
            printWriter.write('{');
            if(type != null) {
                printWriter.write("\"type\":\"");
                printWriter.write(type.toString());
                printWriter.write("\"");
                previousElemet = true;
            }
            if(title != null) {
                if(previousElemet) {
                    printWriter.write(",");
                }
                printWriter.write("\"title\":\"");
                printWriter.write(title);
                printWriter.write("\"");
                previousElemet = true;
            }
            if(detail != null) {
                if(previousElemet) {
                    printWriter.write(",");
                }
                printWriter.write("\"detail\":\"");
                printWriter.write(detail);
                printWriter.write("\"");
                previousElemet = true;

            }
            if(instance != null) {
                if(previousElemet) {
                    printWriter.write(",");
                }
                printWriter.write("\"instance\":\"");
                printWriter.write(instance.toString());
                printWriter.write("\"");
                previousElemet = true;
            }

            printWriter.flush();

        }

        extendJson(outputStream);

        outputStream.write("}".getBytes(StandardCharsets.UTF_8));
        
    }

    protected void extendJson(OutputStream outputStream) {
        // Called before closing the last element.
    }

    public String toXml() throws XMLStreamException, IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            toXml(outputStream, true);
            return outputStream.toString(StandardCharsets.UTF_8);
        }
    }
    
    public void toXml(OutputStream outputStream, boolean writeStartDocument) throws XMLStreamException {
        XMLOutputFactory outputFactory = XMLOutputFactory.newFactory();
        XMLStreamWriter xmlStreamWriter = outputFactory.createXMLStreamWriter(outputStream);
        toXml(xmlStreamWriter, writeStartDocument);
    }
    
    public void toXml(XMLStreamWriter xmlStreamWriter, boolean writeStartDocument) throws XMLStreamException {
        if(writeStartDocument) {
            xmlStreamWriter.writeStartDocument(StandardCharsets.UTF_8.toString(), "1.0");
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
        
        extendXml(xmlStreamWriter);
        
        // /problem
        xmlStreamWriter.writeEndElement();
        
        if(writeStartDocument) {
            xmlStreamWriter.writeEndDocument();
        }
    }
    
    protected void extendXml(XMLStreamWriter xmlStreamWriter) {
        // Called before closing the last element.
    }

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

    @Override
    public String toString() {
        return "Problem [type=" + type + ", title=" + title + "]";
    }
    
}
