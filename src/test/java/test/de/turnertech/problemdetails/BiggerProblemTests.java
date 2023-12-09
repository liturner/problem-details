package test.de.turnertech.problemdetails;

import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import org.junit.jupiter.api.Test;

import de.turnertech.problemdetails.Problem;

class BiggerProblemTests {

    @Test
    void sanity_check() throws XMLStreamException, IOException {
        Problem myProblem = new BiggerProblem();
        myProblem.setStatus(404);
        myProblem.setTitle(Problem.findStatusPhrase(404));
        System.out.println(myProblem.toJson());
        System.out.println(myProblem.toXml());
    }

}
