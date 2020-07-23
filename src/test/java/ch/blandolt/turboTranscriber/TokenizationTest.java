package ch.blandolt.turboTranscriber;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.logging.Logger;

import org.eclipse.lsp4j.TextDocumentItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.blandolt.turboTranscriber.lsp.TTRLanguageService;
import ch.blandolt.turboTranscriber.lsp.documents.TTRDocument;
import ch.blandolt.turboTranscriber.util.datastructure.tokenization.TranscriptionToken;

public class TokenizationTest {
    private final Logger log = Logger.getLogger(TokenizationTest.class.getName());

    private TTRLanguageService service;

    /**
     * Initialization before each test.
     */
    @BeforeEach
    public void initializeEach() {
        // TODO: initialize
        service = new TTRLanguageService();
    }

    /**
     * Test tokenization for minimal document.
     */
    @Test
    public void testTokenizationBasics() {

        // empty text document
        String text = "bla bla";
        TTRDocument doc = new TTRDocument(new TextDocumentItem("uri", "ttr", 0, text));
        long start = System.currentTimeMillis();
        while (!doc.isTokenized()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        log.info("Finished waiting for tokens after (" + Long.valueOf(System.currentTimeMillis()-start) + ")ms");
        assertTrue(doc.isTokenized());
        List<TranscriptionToken> tokens = doc.getTokens();
        assertNotNull(tokens);
        assertEquals(2, tokens.size());
    }
}