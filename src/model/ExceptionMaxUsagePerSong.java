/* Authors: Alex Erwin & Ian Burley
 * Purpose: Exceptions based on the refactoring model. 
 * Throwable error on reaching Max Usage Per Song
 */
// package definition
package model;
// throwable
public class ExceptionMaxUsagePerSong extends RuntimeException {
	// exception to be thrown
	public ExceptionMaxUsagePerSong(String title) {
		super("The requested song: " + title + ", has reached the allowable plays for today!");
	}
}
