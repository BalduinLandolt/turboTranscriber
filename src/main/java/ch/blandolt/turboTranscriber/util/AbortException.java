/**
 * 
 */
package ch.blandolt.turboTranscriber.util;

/**
 * Exception that is thrown, when a process is aborted by the user.
 *
 * @author Balduin Landolt
 *
 */
public class AbortException extends Exception {

	/**
	 * Creates a new AbortException.
	 *
	 * @param msg The exception message.
	 */
	public AbortException(String msg) {
		super(msg);
	}
	
}
