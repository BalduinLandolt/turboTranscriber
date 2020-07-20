package ch.blandolt.turboTranscriber.lsp.documents;

import java.util.List;

import org.eclipse.lsp4j.TextDocumentContentChangeEvent;
import org.eclipse.lsp4j.TextDocumentItem;

// LATER: make Core TTR use this class aswell

public class TTRDocument extends TextDocumentItem {

    private final Object lock = new Object();
    
	private static String DEFAULT_DELIMTER = System.lineSeparator();

	public TTRDocument(TextDocumentItem document) {
		this(document.getText(), document.getUri());
		super.setVersion(document.getVersion());
		super.setLanguageId(document.getLanguageId());
    }

    public TTRDocument(String text, String uri) {
        super.setUri(uri);
        super.setText(text);
    }
    
    public String getUri() {
        return null;
    }


    // TODO: handle Position stuff here?

	/**
	 * Update text of the document by using the changes
	 * 
	 * @param changes the text document changes.
	 */
	public void update(List<TextDocumentContentChangeEvent> changes) {
		if (changes.size() < 1) {
			// no changes, ignore it.
			return;
		}

		// LATER: for performance, implement incremental too
		
		// if (isIncremental()) {
		// 	try {
		// 		long start = System.currentTimeMillis();
		// 		synchronized (lock) {
		// 			// Initialize buffer and line tracker from the current text document
		// 			StringBuilder buffer = new StringBuilder(getText());

		// 			// Loop for each changes and update the buffer
		// 			for (int i = 0; i < changes.size(); i++) {

		// 				TextDocumentContentChangeEvent changeEvent = changes.get(i);
		// 				Range range = changeEvent.getRange();
		// 				int length = 0;

		// 				if (range != null) {
		// 					length = changeEvent.getRangeLength().intValue();
		// 				} else {
		// 					// range is optional and if not given, the whole file content is replaced
		// 					length = buffer.length();
		// 					range = new Range(positionAt(0), positionAt(length));
		// 				}
		// 				String text = changeEvent.getText();
		// 				int startOffset = offsetAt(range.getStart());
		// 				buffer.replace(startOffset, startOffset + length, text);
		// 				lineTracker.replace(startOffset, length, text);
		// 			}
		// 			// Update the new text content from the updated buffer
		// 			setText(buffer.toString());
		// 		}
		// 		LOGGER.fine("Text document content updated in " + (System.currentTimeMillis() - start) + "ms");
		// 	} catch (BadLocationException e) {
		// 		// Should never occur.
		// 	}
		// } else {
			// like vscode does, get the last changes
			// see
			// https://github.com/Microsoft/vscode-languageserver-node/blob/master/server/src/main.ts
			TextDocumentContentChangeEvent last = changes.size() > 0 ? changes.get(changes.size() - 1) : null;
			if (last != null) {
				setText(last.getText());
				// lineTracker.set(last.getText());
            }
            // TODO: does that already do something? -> yes... but keep an eye on it
		// }
	}
    
}