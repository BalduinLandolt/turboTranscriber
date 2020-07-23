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
        String text;
        TTRDocument doc;
        long start;
        List<TranscriptionToken> tokens;

        // empty text document
        text = "";
        doc = new TTRDocument(new TextDocumentItem("uri", "ttr", 0, text));
        start = System.currentTimeMillis();
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
        tokens = doc.getTokens();
        assertNotNull(tokens);
        assertEquals(0, tokens.size());

        // simple words
        text = "bla bla";
        doc = new TTRDocument(new TextDocumentItem("uri", "ttr", 0, text));
        start = System.currentTimeMillis();
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
        tokens = doc.getTokens();
        assertNotNull(tokens);
        assertEquals(2, tokens.size());

        // glyph
        text = "{slong}";
        doc = new TTRDocument(new TextDocumentItem("uri", "ttr", 0, text));
        start = System.currentTimeMillis();
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
        tokens = doc.getTokens();
        assertNotNull(tokens);
        assertEquals(1, tokens.size());

        // word with glyph
        text = "{slong}em";
        doc = new TTRDocument(new TextDocumentItem("uri", "ttr", 0, text));
        start = System.currentTimeMillis();
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
        tokens = doc.getTokens();
        assertNotNull(tokens);
        assertEquals(1, tokens.size());

        // abbreviation
        text = "(m;bar)";
        doc = new TTRDocument(new TextDocumentItem("uri", "ttr", 0, text));
        start = System.currentTimeMillis();
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
        tokens = doc.getTokens();
        assertNotNull(tokens);
        assertEquals(1, tokens.size());

        // word with glyph and abbreviation
        text = "{slong}e(m;bar)";
        doc = new TTRDocument(new TextDocumentItem("uri", "ttr", 0, text));
        start = System.currentTimeMillis();
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
        tokens = doc.getTokens();
        assertNotNull(tokens);
        assertEquals(1, tokens.size());

        // tags
        text = "[div]\n[lb=2] sem \n[/div]";  // FIXME: if no space after "sem", test will fail
        doc = new TTRDocument(new TextDocumentItem("uri", "ttr", 0, text));
        start = System.currentTimeMillis();
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
        tokens = doc.getTokens();
        assertNotNull(tokens);
        assertEquals(4, tokens.size());
    }
}