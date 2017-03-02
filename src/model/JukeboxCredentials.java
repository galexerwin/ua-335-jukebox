/*Author: Alex Erwin & Ian Burley
 *Purpose: The Jukebox Credentials stores a collection of users, authenticates users, verifies a request 
 *Phase 1: Hardwired; Phase 2: Persisted
 */
// package definition
package model;
// import classes
import java.util.Map;
import java.util.TreeMap;
// credentials object
public class JukeboxCredentials {
	// instance variables
	private Map<String, JukeboxUser> users; 
	// constructor
	public JukeboxCredentials() {
		// load the users
		loadUserTable();
	}
	// login user
	public void loginUser(String username, int password) throws ExceptionInvalidCredentials {
		// check and see if the user is valid
		if (!(users.containsKey((String)username)))
			throw new ExceptionInvalidCredentials();
		// check the password for the user
		this.users.get((String)username).authenticateUser(password);
	}
	// check if user can request a song
	public boolean canUserRequestSong(String username) {
		// return if the request is valid. may throw ExceptionMaxUsagePerUser
		return this.users.get((String)username).isValidRequest();
	}
	// private load credentials
	private void loadUserTable() {
		// setup the users table
		this.users = new TreeMap<String, JukeboxUser>();		
		// add the default users
		users.put("Chris", new JukeboxUser("Chris", 1));
		users.put("Devon", new JukeboxUser("Devon", 22));
		users.put("River", new JukeboxUser("River", 333));
		users.put("Ryan", new JukeboxUser("Ryan", 4444));
	}
}
