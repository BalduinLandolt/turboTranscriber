/**
 * 
 */
package ch.blandolt.turboTranscriber.util;

/**
 * Interface ensuring that a class can log information.
 * <p>
 * Every class implementing this interface can be passed to {@link Log} via {@link Log#addLoggable(Loggable)}
 *
 * @author Balduin Landolt
 *
 */
public interface Loggable {
	public void log(String s);
}
