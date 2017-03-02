/* Authors: Alex Erwin & Ian Burley
 * Purpose: Exceptions based on the refactoring model. 
 * Throwable error on logged in
 */
// package definition
package model;
// throwable
public class ExceptionNotLoggedIn extends RuntimeException {
	// exception to be thrown
	public ExceptionNotLoggedIn() {
		super("Please login first!");
	}
}