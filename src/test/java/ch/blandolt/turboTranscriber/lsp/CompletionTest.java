package ch.blandolt.turboTranscriber.lsp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.TextDocumentItem;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.blandolt.turboTranscriber.lsp.completions.TTRCompletions;
import ch.blandolt.turboTranscriber.lsp.documents.TTRDocument;

public class CompletionTest {

    public static final int BOILERPLATE_SNIPPETS = 1;
    public static final String BOILERPLATE_LABEL = "Empty Document";

    private TTRLanguageService service;
    
    // TODO: stuff to make it work... magic.


    /**
     * Initialization before each test.
     */
    @BeforeEach
    public void initializeEach() {
        // TODO: initialize
        service = new TTRLanguageService();
    }

    /**
     * Test completion for empty document.
     */
    @Test
    public void testEmptyDocumentCompletion() {
        
        // empty text document
        TTRDocument doc = new TTRDocument(new TextDocumentItem("uri", "ttr", 0, ""));
        Position pos = new Position(0, 0);
        List<CompletionItem> completions = service.getCompletions(doc, pos);
        CompletionItem completion = completions.get(0);

        assertEquals(CompletionTest.BOILERPLATE_SNIPPETS, completions.size());
        assertTrue(completion.getLabel().equals(CompletionTest.BOILERPLATE_LABEL));

        // whitespace text document
        doc = new TTRDocument(new TextDocumentItem("uri", "ttr", 0, " \t "));
        pos = new Position(0, 0);
        completions = service.getCompletions(doc, pos);
        completion = completions.get(0);

        assertEquals(CompletionTest.BOILERPLATE_SNIPPETS, completions.size());
        assertTrue(completion.getLabel().equals(CompletionTest.BOILERPLATE_LABEL));
        
        // whitespace text document, multi line
        doc = new TTRDocument(new TextDocumentItem("uri", "ttr", 0, " \t \n  \n"));
        pos = new Position(0, 0);
        completions = service.getCompletions(doc, pos);
        completion = completions.get(0);

        assertEquals(CompletionTest.BOILERPLATE_SNIPPETS, completions.size());
        assertTrue(completion.getLabel().equals(CompletionTest.BOILERPLATE_LABEL));
    }
}