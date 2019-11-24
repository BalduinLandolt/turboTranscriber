package ch.blandolt.turboTranscriber.util.rsyntax;

import org.fife.ui.autocomplete.DefaultCompletionProvider;

public class TTCompletionProvider extends DefaultCompletionProvider {

    @Override
    protected boolean isValidChar(char ch) {
        return super.isValidChar(ch) || ch=='{' || ch== '(' || ch=='[' || ch==';';
    }
}
