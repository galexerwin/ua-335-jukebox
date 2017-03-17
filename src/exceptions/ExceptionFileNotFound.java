/* Authors: Alex Erwin & Ian Burley
 * Purpose: Exceptions based on the refactoring model. 
 * Throwable error on file not found
 */
// package definition
package exceptions;
// throwable
public class ExceptionFileNotFound extends RuntimeException {
	// exception to be thrown
	public ExceptionFileNotFound(String song) {
		super("The song: " + song + " was not found!");
	}
}
