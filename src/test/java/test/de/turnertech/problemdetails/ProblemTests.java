package test.de.turnertech.problemdetails;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.net.URI;

import javax.xml.stream.XMLStreamException;

import org.junit.jupiter.api.Test;

import de.turnertech.problemdetails.Problem;

class ProblemTests {
 
    @Test
    void equals_is_implemented() throws XMLStreamException, IOException {
        Problem a = new Problem();
        Problem b = new Problem();
        assertEquals(b, a);
    }

    @Test
    void to_json() throws XMLStreamException, IOException {
        Problem myProblem = new Problem();
        myProblem.setStatus(404);
        myProblem.setTitle(Problem.findStatusPhrase(404));
        System.out.println(myProblem.toJson());
        System.out.println(myProblem.toXml());
    }

    @Test
    void to_string_handles_null_title() throws XMLStreamException, IOException {
        Problem a = new Problem();
        assertNotNull(a.toString());
    }

    @Test
    void instance_may_not_be_null() {
        assertThrows(NullPointerException.class, () -> new Problem((URI)null));

        Problem a = new Problem(URI.create("my:problem"));
        assertThrows(NullPointerException.class, () -> a.setType(null));
    }

    @Test
    void default_type_is_about_blank() {
        Problem a = new Problem();
        assertEquals(URI.create("about:blank"), a.getType());
    }

}
