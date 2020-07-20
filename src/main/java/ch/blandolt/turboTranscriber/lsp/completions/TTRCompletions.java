package ch.blandolt.turboTranscriber.lsp.completions;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.CompletionItemKind;
import org.eclipse.lsp4j.Position;

import ch.blandolt.turboTranscriber.util.Log;

public class TTRCompletions {
    private static Logger log;

    public TTRCompletions() {
        log = Log.getJulLogger();
    }

    public List<CompletionItem> getCompletions(Position position) {
        List<CompletionItem> completionItems = new ArrayList<>();

        // TODO: implement actual

        // Sample Completion item for sayHello
        CompletionItem completionItem = new CompletionItem();
        // Define the text to be inserted in to the file if the completion item is
        // selected.
        completionItem.setInsertText("sayHello() {\n    print(\"hello\")\n}");
        // Set the label that shows when the completion drop down appears in the Editor.
        completionItem.setLabel("sayHello()");
        // Set the completion kind. This is a snippet.
        // That means it replace character which trigger the completion and
        // replace it with what defined in inserted text.
        completionItem.setKind(CompletionItemKind.Snippet);
        // This will set the details for the snippet code which will help user to
        // understand what this completion item is.
        completionItem.setDetail("sayHello()\n this will say hello to the people");

        // Add the sample completion item to the list.
        completionItems.add(completionItem);

        return completionItems;
    }
}