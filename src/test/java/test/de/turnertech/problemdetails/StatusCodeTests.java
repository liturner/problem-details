package test.de.turnertech.problemdetails;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Locale;

import org.junit.jupiter.api.Test;

import de.turnertech.problemdetails.Problem;

class StatusCodeTests {
    
    @Test
    void get_status_phrase_404_default_english() {    
        assertEquals("Not Found", Problem.getStatusPhrase(404));
    }

    @Test
    void get_status_phrase_404_german() {    
        assertEquals("Nicht gefunden", Problem.getStatusPhrase(404, Locale.GERMAN));
    }

    @Test
    void get_status_phrase_404_unknown_returns_english() {    
        // ToDo: assertEquals("Not Found", Problem.getStatusPhrase(404, Locale.TRADITIONAL_CHINESE));
    }

}
