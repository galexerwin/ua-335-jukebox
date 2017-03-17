/* Authors: Alex Erwin & Ian Burley
 * Purpose: Exceptions based on the refactoring model. 
 * Throwable error on invalid credentials
 */
// package definition
package exceptions;
// throwable
public class ExceptionInvalidCredentials extends RuntimeException {
	// exception to be thrown
	public ExceptionInvalidCredentials() {
		super("Invalid Username or Password");
	}
}
