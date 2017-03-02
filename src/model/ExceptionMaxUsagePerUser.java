/* Authors: Alex Erwin & Ian Burley
 * Purpose: Exceptions based on the refactoring model. 
 * Throwable error on reaching Max Usage Per User
 */
// package definition
package model;
// throwable
public class ExceptionMaxUsagePerUser extends RuntimeException {
	// exception to be thrown
	public ExceptionMaxUsagePerUser() {
		super("You have exceeded your max song requests today!");
	}
}