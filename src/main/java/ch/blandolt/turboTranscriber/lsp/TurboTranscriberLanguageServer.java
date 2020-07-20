package ch.blandolt.turboTranscriber.lsp;

import ch.blandolt.turboTranscriber.util.Log;

import org.eclipse.lsp4j.CompletionOptions;
import org.eclipse.lsp4j.InitializeParams;
import org.eclipse.lsp4j.InitializeResult;
import org.eclipse.lsp4j.InitializedParams;
import org.eclipse.lsp4j.ServerCapabilities;
import org.eclipse.lsp4j.TextDocumentSyncKind;
import org.eclipse.lsp4j.services.LanguageClient;
import org.eclipse.lsp4j.services.LanguageClientAware;
import org.eclipse.lsp4j.services.LanguageServer;
import org.eclipse.lsp4j.services.TextDocumentService;
import org.eclipse.lsp4j.services.WorkspaceService;

import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

public class TurboTranscriberLanguageServer implements LanguageServer, LanguageClientAware {
    private static Logger log;

    private final TTRLanguageService ttrLanguageService;


    private TextDocumentService textDocumentService;
    private WorkspaceService workspaceService;
    private LanguageClient client;
    private int errorCode = 1;

    public TurboTranscriberLanguageServer() {
        this.textDocumentService = new TurboTranscriberTextDocumentService(this);
        this.workspaceService = new TurboTranscriberWorkspaceService();
        ttrLanguageService = new TTRLanguageService();
        log = Log.getJulLogger();
    }

    @Override
    public CompletableFuture<InitializeResult> initialize(InitializeParams initializeParams) {
        log.info("Initialize Server.");

        // Initialize the InitializeResult for this LS.
        final InitializeResult initializeResult = new InitializeResult(new ServerCapabilities());

        // Set the capabilities of the LS to inform the client.
        initializeResult.getCapabilities().setTextDocumentSync(TextDocumentSyncKind.Full);
        CompletionOptions completionOptions = new CompletionOptions();
        initializeResult.getCapabilities().setCompletionProvider(completionOptions);
        return CompletableFuture.supplyAsync(()->initializeResult);
    }

    @Override
    public void initialized(InitializedParams params) {
        LanguageServer.super.initialized(params);
        log.info("Initialization done.");
    }

    @Override
    public CompletableFuture<Object> shutdown() {
        log.info("Server Shutdown requested.");
        // If shutdown request comes from client, set the error code to 0.
        errorCode = 0;
        return null;

        // TODO: should I shut down here?
    }

    @Override
    public void exit() {
        log.info("Server Terminated.");
        // Kill the LS on exit request from client.
        System.exit(errorCode);

        // LATER: Never seems to have happened, so far. Do I need to worry?
    }

    @Override
    public TextDocumentService getTextDocumentService() {
        // Return the endpoint for language features.
        return this.textDocumentService;
    }

    @Override
    public WorkspaceService getWorkspaceService() {
        // Return the endpoint for workspace functionality.
        return this.workspaceService;
    }

    @Override
    public void connect(LanguageClient languageClient) {
        log.info("Client connected to Server.");
        // Get the client which started this LS.
        this.client = languageClient;
    }

	public TTRLanguageService getTTRLanguageService() {
		return ttrLanguageService;
	}
}
