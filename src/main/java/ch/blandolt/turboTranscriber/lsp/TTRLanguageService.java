package ch.blandolt.turboTranscriber.lsp;

import java.util.List;

import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.Position;

import ch.blandolt.turboTranscriber.lsp.completions.TTRCompletions;

public class TTRLanguageService {
    
    private final TTRCompletions completions;

    public TTRLanguageService() {
        completions = new TTRCompletions();
    }

    public List<CompletionItem> getCompletions(Position position) {
        return completions.getCompletions(position);
    }
}