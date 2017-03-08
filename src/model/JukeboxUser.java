/*Author: Alex Erwin & Ian Burley
 *Purpose: The JukeboxUser class encapsulates each user. 
 *@throws ExceptionNotLoggedIn on user hasn't logged in
 *@throws ExceptionMaxUsagePerUser on hitting 3 songs played today. Resets on midnight of the next day after the last request
 */
// package definition
package model;
// import classes
import java.time.LocalDate;
import java.time.LocalDateTime;
// the user object
public class JukeboxUser {
	// instance variables
	private String studentName;
	private String studentID;
	private String username;
	private int password;
	private boolean isLoggedIn;
	private int numOfSongsRequested;
	private LocalDate lastRequestDate;
	private int limetimeBalanceRemainingInSeconds;
	// constructor
	public JukeboxUser(String username, int password) {
		// set settings
		this.username = username;
		this.password = password;
		// set defaults
		this.isLoggedIn = false;
		this.numOfSongsRequested = 0;
		this.lastRequestDate = LocalDate.now();
		this.limetimeBalanceRemainingInSeconds = 1500 * 60;
	}
	// authenticate the user
	public void authenticateUser(int password) throws ExceptionInvalidCredentials {
		// check password
		if (!(this.password == password))
			throw new ExceptionInvalidCredentials();
		// login the user
		this.isLoggedIn = true;
	}
	// check if this a valid request and throw an exception if not. record the usage
	public boolean isValidRequest(int runtime) throws ExceptionNotLoggedIn, ExceptionMaxUsagePerUser, ExceptionMaxUsagePerLifetime {
		// are they logged in
		if (!isLoggedIn)
			throw new ExceptionNotLoggedIn();
		// check if the max requests have been received
		if (hasMaxRequestsBeenReached())
			throw new ExceptionMaxUsagePerUser();
		// check if the lifetime limits have been reached
		if (this.limetimeBalanceRemainingInSeconds < runtime)
			throw new ExceptionMaxUsagePerLifetime();
		// return true
		return true;
	}
	// return the amount of seconds remaining in the account towards the lifetime max
	public int getLifetimeMaxBalance() {
		return this.limetimeBalanceRemainingInSeconds;
	}
	// save when the user has added a song to the jukebox
	public void recordJukeboxUse(int runtime) {
		// increment the number of songs requested
		this.numOfSongsRequested++;
		// subtract the usage from the lifetime
		this.limetimeBalanceRemainingInSeconds -= runtime;
		// record the last 
		this.lastRequestDate = LocalDate.now();
	}
	// discover if max request have been reached for last requested date and if date needs to be reset
	private boolean hasMaxRequestsBeenReached() {
		// variables
		//not necessarily tomorrow, but 24+ hours in the future of 12 midnight the day first used
		LocalDateTime futureDate = this.lastRequestDate.atStartOfDay().plusDays(1);
		LocalDateTime currentDate = LocalDateTime.now();
		// check if the last used time is before the future date and the user has used all the songs
		if (currentDate.isBefore(futureDate) && this.numOfSongsRequested >= 3)
			return true;
		// return false otherwise
		return false;
	}
	
	//return number of songs requested
	public int getSongRequests(){
		return numOfSongsRequested;
	}
}