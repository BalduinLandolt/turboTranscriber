package ch.blandolt.turboTranscriber.lsp.completions;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.CompletionItemKind;
import org.eclipse.lsp4j.InsertTextFormat;
import org.eclipse.lsp4j.Position;

import ch.blandolt.turboTranscriber.lsp.documents.TTRDocument;
import ch.blandolt.turboTranscriber.util.Log;

public class TTRCompletions {
    private static Logger log;

    public TTRCompletions() {
        log = Log.getJulLogger();
    }

    public List<CompletionItem> getCompletions(TTRDocument doc, Position position) {
        // LATER: replace hard coding snippets with importing json resource

        log.info("getting completions...");
        List<CompletionItem> completionItems = new ArrayList<>();

        String text = doc.getText();

        try {
            if (text.trim().isEmpty()) {
                // TODO: does that catch empty lines too?

                // LATER: do fancy stuff here, like standard snippets, or so

                CompletionItem cl = new CompletionItem();
                cl.setInsertText("\n# Title: ${1:Title}\n\n[div]\n\n[p]\n[pb=${2:page-number}]\n[lb=${3:line-number}] $0\n[/p]\n[/div]");
                cl.setLabel("Empty Document");
                //cl.setInsertTextFormat(InsertTextFormat.Snippet);
                cl.setKind(CompletionItemKind.Snippet);
                cl.setDetail("Adds structure for a new Transcription.");
                cl.setInsertTextFormat(InsertTextFormat.Snippet);
                completionItems.add(cl);

                return completionItems;
            } else {
                // TODO: generate completions based on tokens in text
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed to get snippet.", e);
        }

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