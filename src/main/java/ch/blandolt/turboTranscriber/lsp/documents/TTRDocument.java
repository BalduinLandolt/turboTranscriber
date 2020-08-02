package ch.blandolt.turboTranscriber.lsp.documents;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.AbstractMap.SimpleEntry;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

import org.eclipse.lsp4j.TextDocumentContentChangeEvent;
import org.eclipse.lsp4j.TextDocumentItem;

import ch.blandolt.turboTranscriber.util.Log;
import ch.blandolt.turboTranscriber.util.SuggestionCounter;
import ch.blandolt.turboTranscriber.util.datastructure.tokenization.TokenTypeAbbreviation;
import ch.blandolt.turboTranscriber.util.datastructure.tokenization.TokenTypeClosingTag;
import ch.blandolt.turboTranscriber.util.datastructure.tokenization.TokenTypeGlyph;
import ch.blandolt.turboTranscriber.util.datastructure.tokenization.TokenTypeLegitWord;
import ch.blandolt.turboTranscriber.util.datastructure.tokenization.TokenTypeOpeningTag;
import ch.blandolt.turboTranscriber.util.datastructure.tokenization.Tokenizer;
import ch.blandolt.turboTranscriber.util.datastructure.tokenization.TranscriptionToken;

// LATER: make Core TTR use this class aswell

public class TTRDocument extends TextDocumentItem {

	private static String DEFAULT_DELIMTER = System.lineSeparator();
	// TODO: make this a setting insted of hardcoding it
	private static long LOCK_DURATION = 2500;

	private final Object lock = new Object();
	private final Logger log;

	private boolean isTokenized = false;
	private boolean hasSuggestions = false;
	private List<TranscriptionToken> tokens;
	private SuggestionCounter<String> completionSuggestions;
	private TokenizationLock tokenizationLock;

	public TTRDocument(TextDocumentItem document) {
		this(document.getText(), document.getUri());
		super.setVersion(document.getVersion());
		super.setLanguageId(document.getLanguageId());
	}

	public TTRDocument(String text, String uri) {
		super.setUri(uri);
		super.setText(text);
		log = Log.getJulLogger();
		tokens = new ArrayList<>();
		completionSuggestions = new SuggestionCounter<String>();
		tokenizationLock = new TokenizationLock();
		tokenizeContents();
	}

	public String getUri() {
		return super.getUri();
	}

	/**
	 * tokenizes the text contents asynchronously.
	 * 
	 * Will set <code>isTokenized</code> to true when finished.
	 */
	private void tokenizeContents() {
		if (isLocked()) {
			tokenizationLock.requestRetokenization();
			return;
		}

		log.info("Tokenizing Document.");
		isTokenized = false; // TODO: do I really want this, might cost me a lot of tokenized stat while it
								// is working on minor changes.
		CompletableFuture<List<TranscriptionToken>> future = CompletableFuture.supplyAsync(() -> {
			tokenizationLock.lock(LOCK_DURATION);
			return Tokenizer.tokenize(this.getText());
		});
		future.thenAccept((tokens) -> {
			this.tokens = tokens;
			isTokenized = true;
			updateCompletionSuggestions();  // LATER: use actual Data, not transcriptiontokens
		});
	}

	private void updateCompletionSuggestions() {
		CompletableFuture<SuggestionCounter<String>> future = CompletableFuture.supplyAsync(() -> {
			SuggestionCounter<String> res = new SuggestionCounter<String>();
			for (TranscriptionToken token : getTokenFlatList()) {
				if (token instanceof TokenTypeLegitWord){ // FIXME: some words turn out wrong
					if (!token.getText().isEmpty()){
						res.add(token.getText());
					} else {
						StringBuilder sb = new StringBuilder();
						for (TranscriptionToken t : ((TokenTypeLegitWord) token).getContents()) {
							if (token instanceof TokenTypeOpeningTag || token instanceof TokenTypeClosingTag){
								sb.append("["+t.getText()+"]");
							} else if (token instanceof TokenTypeAbbreviation){
								sb.append("("+t.getText()+")");
							} else if (token instanceof TokenTypeGlyph){
								sb.append("{"+t.getText()+"}");
							} else {
								sb.append(t.getText());
							}
						}
						res.add(sb.toString());
					}
				}
				if (token instanceof TokenTypeOpeningTag || token instanceof TokenTypeClosingTag) {
					res.add("["+token.getText()+"]");
				}
				if (token instanceof TokenTypeAbbreviation) {
					res.add("(" + token.getText() + ")");
				}
				if (token instanceof TokenTypeGlyph) {
					res.add("{" + token.getText() + "}");
				}

			}

			// res.addAll(getWordSuggestions());
			// res.addAll(gettTagSuggestions());
			// res.addAll(gettAbbreviationSuggestions());
			// res.addAll(gettGlyphSuggestions());
			// TODO: more?
			return res;
		});
		future.thenAccept((suggestions) -> {
			completionSuggestions = suggestions;
			hasSuggestions = true;
		});
	}

	private List<TranscriptionToken> getTokenFlatList() {
		List<TranscriptionToken> res = new ArrayList<>();
		for (TranscriptionToken rootToken : tokens) {
			res.addAll(rootToken.getFlatList());
		}
		return res;
	}

	// private SuggestionCounter<String> getWordSuggestions() {
	// SuggestionCounter<String> res = new SuggestionCounter<String>();
	// // log.info("looking at tokens: "+tokens.size());
	// for (TranscriptionToken token : tokens) {
	// if (token instanceof TokenTypeLegitWord) {
	// res.add(token.getText());
	// }
	// }
	// return res;
	// }

	// private SuggestionCounter<String> gettTagSuggestions() { // LATER: these
	// things should be actual smart snippets, not just string
	// SuggestionCounter<String> res = new SuggestionCounter<String>();
	// for (TranscriptionToken token : tokens) {
	// if (token instanceof TokenTypeOpeningTag || token instanceof
	// TokenTypeClosingTag) {
	// res.add("["+token.getText()+"]");
	// }
	// }
	// return res;
	// }

	// private SuggestionCounter<String> gettAbbreviationSuggestions() {
	// 	SuggestionCounter<String> res = new SuggestionCounter<String>();
	// 	for (TranscriptionToken token : tokens) {
	// 		if (token instanceof TokenTypeClosingTag) {
	// 			res.add("(" + token.getText() + ")");
	// 		}
	// 	}
	// 	return res;
	// }

	// private SuggestionCounter<String> gettGlyphSuggestions() {
	// 	SuggestionCounter<String> res = new SuggestionCounter<String>();
	// 	for (TranscriptionToken token : tokens) {
	// 		if (token instanceof TokenTypeGlyph) {
	// 			res.add("{" + token.getText() + "}");
	// 		}
	// 	}
	// 	return res;
	// }

	public Map<String, Integer> getSuggestionCount() {
		return completionSuggestions.asMap();
	}

	private boolean isLocked() {
		return tokenizationLock.isLocked();
	}

	public List<TranscriptionToken> getTokens() {
		return tokens;
	}

	public boolean isTokenized() {
		return isTokenized;
	}

	public boolean hasSuggestions() {
		return hasSuggestions;
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
		// try {
		// long start = System.currentTimeMillis();
		// synchronized (lock) {
		// // Initialize buffer and line tracker from the current text document
		// StringBuilder buffer = new StringBuilder(getText());

		// // Loop for each changes and update the buffer
		// for (int i = 0; i < changes.size(); i++) {

		// TextDocumentContentChangeEvent changeEvent = changes.get(i);
		// Range range = changeEvent.getRange();
		// int length = 0;

		// if (range != null) {
		// length = changeEvent.getRangeLength().intValue();
		// } else {
		// // range is optional and if not given, the whole file content is replaced
		// length = buffer.length();
		// range = new Range(positionAt(0), positionAt(length));
		// }
		// String text = changeEvent.getText();
		// int startOffset = offsetAt(range.getStart());
		// buffer.replace(startOffset, startOffset + length, text);
		// lineTracker.replace(startOffset, length, text);
		// }
		// // Update the new text content from the updated buffer
		// setText(buffer.toString());
		// }
		// LOGGER.fine("Text document content updated in " + (System.currentTimeMillis()
		// - start) + "ms");
		// } catch (BadLocationException e) {
		// // Should never occur.
		// }
		// } else {
		// like vscode does, get the last changes
		// see
		// https://github.com/Microsoft/vscode-languageserver-node/blob/master/server/src/main.ts
		TextDocumentContentChangeEvent last = changes.size() > 0 ? changes.get(changes.size() - 1) : null;
		if (last != null) {
			setText(last.getText());
			// lineTracker.set(last.getText());
		}
		tokenizeContents();
	}

	private class TokenizationLock {
		private boolean isLocked = false;
		private boolean willRetokenize = false;

		public boolean isLocked() {
			return isLocked;
		}

		public void lock(long duration) {
			isLocked = true;
			willRetokenize = false;
			TimerTask task = new TimerTask() {
				public void run() {
					unlock();
				}
			};
			Timer timer = new Timer();
			timer.schedule(task, duration);
		}

		protected void unlock() {
			isLocked = false;
			if (willRetokenize) {
				TTRDocument.this.tokenizeContents();
			}
		}

		public void requestRetokenization() {
			willRetokenize = true;
		}

	}

}