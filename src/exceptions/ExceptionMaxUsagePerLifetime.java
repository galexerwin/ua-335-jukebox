/* Authors: Alex Erwin & Ian Burley
 * Purpose: Exceptions based on the refactoring model. 
 * Throwable error on reaching Max Usage Per User
 */
// package definition
package exceptions;
// throwable
public class ExceptionMaxUsagePerLifetime extends RuntimeException {
	// exception to be thrown
	public ExceptionMaxUsagePerLifetime() {
		super("You have exceeded your max lifetime usage of the jukebox!");
	}
}