package ch.blandolt.turboTranscriber;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CompletionTest {
    
    // TODO: stuff to make it work... magic.

    @BeforeEach
    public void initialize() {
        // TODO: initialize
    }

    @Test
    public void testEmptyDocumentCompletion() {
        int a = 23;
        assertTrue(12 < a);
        // assertTrue(12 > a);
    }
}