package test.de.turnertech.problemdetails;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import de.turnertech.problemdetails.Problem;

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
