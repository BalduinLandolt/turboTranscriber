/**
 * 
 */
package ch.blandolt.turboTranscriber.util;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

//import ch.unibas.landolt.balduin.XMLSpeedupV6.src.application.main.MainGUI;
//TODO get parent here

//TODO Import from older version. Check!


/**
 * Logger for TurboTranscriber.
 * <p>
 * Is used to log whatever happens in the app.
 * <p>
 * The log generally prints everything to `System.out` and to a file `log.txt` in the project root.
 * Furthermore, as many {@link Loggable} as wanted can be added via {@link Log#addLoggable(Loggable)},
 * to which the whole log stream is printed as well.
 *
 * @author Balduin Landolt
 *
 */
public class Log {

	/**
	 * System specific line separator.
	 */
	public static final String lineSep = System.getProperty("line.separator");
	
	private static Log lastLog;
	private File logFile;
	private PrintStream printStream;
	private LogStream logStream;
	private LinkedList<Loggable> loggables;

	/**
	 * Generate an instance of {@link Log}
	 *
	 * @author Balduin Landolt
	 */
	public Log() {
		createLogFile();
		Log.lastLog = this;
		logStream = new LogStream();
		loggables = new LinkedList<>();
		println("Log File created.");
	}


	/**
	 * Get last created instance of {@link Log}.
	 *
	 * @return The last instance of {@link Log} that has been created.
	 */
	public static Log getLatestLogInstance() {
		return lastLog;
	}


	/**
	 * Log a message String to the last created Log.
	 * <p>
	 * This method is thread safe.
	 *
	 * @param msg The message to be logged.
	 */
	public static synchronized void log(String msg){
		if (lastLog == null){
			new Log();
		}
		lastLog.println(msg);
	}


	/**
	 * Log an Iterable to the last created Log.
	 * <p>
	 * This method is thread safe.
	 *
	 * @param i The iterable list to be logged.
	 */
	public static synchronized void log(Iterable<Object> i){
		if (lastLog == null){
			new Log();
		}
		lastLog.println(i);
	}


	/**
	 * Log an Array of Objects to the last created Log.
	 * <p>
	 * This method is thread safe.
	 *
	 * @param oo The Array of Objects to be logged.
	 */
	public static synchronized void log(Object[] oo){
		if (lastLog == null){
			new Log();
		}
		lastLog.println(oo);
	}


	/**
	 * Log an Object to the last created Log.
	 * <p>
	 * This method is thread safe.
	 *
	 * @param o The Object to be logged.
	 */
	public static synchronized void log(Object o){
		if (lastLog == null){
			new Log();
		}
		lastLog.println(o);
	}


	/**
	 * Log an empty line to the last created Log.
	 * <p>
	 * This method is thread safe.
	 *
	 */
	public static synchronized void log(){
		if (lastLog == null){
			new Log();
		}
		lastLog.println();
	}



	/**
	 * Print an empty line to all registered logging streams of this instance of Log.
	 * <p>
	 * This method is thread safe.
	 */
	public synchronized void println (){
		println("");
	}


	/**
	 * Print an Object to all registered logging streams of this instance of Log.
	 * <p>
	 * This method is thread safe.
	 *
	 * @param o The Object to be logged.
	 */
	public synchronized void println (Object o){
		println(o.toString());
	}


	/**
	 * Print all elements of an Array to all registered logging streams of this instance of Log.
	 * <p>
	 * This method is thread safe.
	 *
	 * @param oo The Array to be logged.
	 */
	public synchronized void println (Object[] oo){
		println(Arrays.asList(oo));
	}


	/**
	 * Print all elements of an Iterable to all registered logging streams of this instance of Log.
	 * <p>
	 * This method is thread safe.
	 *
	 * @param i The Iterable to be logged.
	 */
	public synchronized void println (Iterable<Object> i){
		println(i.toString());
		for (Object o: i) {
			println("   "+o.toString());
		}
	}


	/**
	 * Print a message String to all registered logging streams of this instance of Log.
	 * <p>
	 * This method is thread safe.
	 *
	 * @param msg The message to be logged.
	 */
	public synchronized void println (String msg){
		System.out.println(msg);
		printStream.println(msg);
		for (Loggable l: loggables) {
			l.log(msg);
		}
	}
	

	private void createLogFile() {
		try {
			logFile = new File("log.txt");
			createAndFlush(logFile);
			FileOutputStream o = new FileOutputStream(logFile);
			printStream = new PrintStream(o);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	private void createAndFlush(File log) throws IOException {
		if (log.exists())
			log.delete();
		log.createNewFile();
	}


	/**
	 * Close the log properly.
	 *
	 * @param b If true, the log file is automatically shown after termination.
	 */
	public void terminate(boolean b) {
		println("Closing log...");
		println("");
		println("Shut down.");
		printStream.close();
		if (b)
			displayLogFile();
	}

	/**
	 * Display the log file.
	 */
	public void displayLogFile() {
		if (Desktop.isDesktopSupported()){
			Desktop d = Desktop.getDesktop();
			try {
				d.open(logFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Static method that initializes the log by creating a first instance.
	 */
	public static void initialize() {
		new Log();
	}

	/**
	 * Static method that terminates the log by terminating the last instance.
	 * Uses the flag {@link Settings#openLogOnClose()} as argument.
	 */
	public static void terminate() {
		lastLog.terminate(Settings.openLogOnClose());
	}
	
	public LogStream getLogStream() {
		return logStream;
	}
	
	public static LogStream getCurrentLogStream() {
		return lastLog.getLogStream();
	}
	
	class LogStream extends OutputStream {

		/* (non-Javadoc)
		 * @see java.io.OutputStream#write(int)
		 */
		@Override
		public void write(int arg0) throws IOException {
			printStream.write(arg0);
			System.out.write(arg0);
		}


		
	}

	/**
	 * @param ex
	 */
	public static void err(Exception ex) {
		log("ERROR!");
		log(ex.getMessage());
		StringWriter writer = new StringWriter();
		ex.printStackTrace(new PrintWriter(writer));
		String stackTrace = writer.toString();
		log(stackTrace);
		//ex.printStackTrace(new PrintStream(getCurrentLogStream()));
	}

	/**
	 * Add {@link Loggable} to last instance of Log.
	 *
	 * After that, everything that gets logged, will also be logged to {@link Loggable#log(String)} of the Loggable.
	 *
	 * @param l a Loggable.
	 */
	public static void addLoggable(Loggable l) {
		lastLog.addLoggableToList(l);
	}

	/**
	 * @param l
	 */
	private void addLoggableToList(Loggable l) {
		loggables.add(l);
	}
}
