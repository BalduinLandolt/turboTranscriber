package ch.blandolt.turboTranscriber.lsp.documents;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.lsp4j.DidChangeTextDocumentParams;
import org.eclipse.lsp4j.DidCloseTextDocumentParams;
import org.eclipse.lsp4j.DidOpenTextDocumentParams;
import org.eclipse.lsp4j.TextDocumentIdentifier;
import org.eclipse.lsp4j.TextDocumentItem;

public class TTRDocuments <T extends TTRDocument>{
    private final Map<String, T> documents;

    public TTRDocuments() {
        documents = new HashMap<>();
    }

    public T get(String uri) {
		synchronized (documents) {
			return documents.get(uri);
		}
    }
    public T onDidChangeTextDocument(DidChangeTextDocumentParams params) {
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
		TextDocumentItem item = params.getTextDocument();
		synchronized (documents) {
			T document = createDocument(item);
			documents.put(document.getUri(), document);
			return document;
		}
	}

	public T createDocument(TextDocumentItem document) {
		TTRDocument doc = new TTRDocument(document);
		return (T) doc;
	}

    public T onDidCloseTextDocument(DidCloseTextDocumentParams params) {
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