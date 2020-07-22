package ch.blandolt.turboTranscriber.lsp.documents;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.eclipse.lsp4j.DidChangeTextDocumentParams;
import org.eclipse.lsp4j.DidCloseTextDocumentParams;
import org.eclipse.lsp4j.DidOpenTextDocumentParams;
import org.eclipse.lsp4j.TextDocumentIdentifier;
import org.eclipse.lsp4j.TextDocumentItem;

import ch.blandolt.turboTranscriber.util.Log;

public class TTRDocuments <T extends TTRDocument>{

	private final Logger log;
    private final Map<String, T> documents;

    public TTRDocuments() {
		documents = new HashMap<>();
		log = Log.getJulLogger();
    }

    public T get(String uri) {
		synchronized (documents) {
			log.info("fetching document: "+uri);
			T res = documents.get(uri);
			log.info("found document: "+res);
			return res;
		}
    }
    public T onDidChangeTextDocument(DidChangeTextDocumentParams params) {
		log.info("Processing document change...");
		synchronized (documents) {
			T document = getDocument(params.getTextDocument());
			if (document != null) {
				document.setVersion(params.getTextDocument().getVersion());
				document.update(params.getContentChanges());
				return document;
			}
		}
		return null;
	}

	public T onDidOpenTextDocument(DidOpenTextDocumentParams params) {
		log.info("Processing document opening...");
		TextDocumentItem item = params.getTextDocument();
		synchronized (documents) {
			T document = createDocument(item);
			documents.put(document.getUri(), document);
			log.info("Documents in Map: "+documents);
			return document;
		}
	}

	public T createDocument(TextDocumentItem document) {
		TTRDocument doc = new TTRDocument(document);
		log.info("Created Document.");
		return (T) doc;
	}

    public T onDidCloseTextDocument(DidCloseTextDocumentParams params) {
		log.info("Processing document closing...");
		synchronized (documents) {
			T document = getDocument(params.getTextDocument());
			if (document != null) {
				documents.remove(params.getTextDocument().getUri());
			}
			return document;
		}
	}

	private T getDocument(TextDocumentIdentifier identifier) {
		return documents.get(identifier.getUri());
    }
        
    /**
	 * Returns the all opened documents.
	 * 
	 * @return the all opened documents.
	 */
	public Collection<T> all() {
		synchronized (documents) {
			return documents.values();
		}
	}
}