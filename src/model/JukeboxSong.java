/*Author: Alex Erwin & Ian Burley
 *Purpose: An object representing the song
 */
// package definition
package model;
// import classes
import java.time.LocalDate;
import java.time.LocalDateTime;
// song object
public class JukeboxSong {
	// instance vars
	private String title;
	private int year;
	private String artist;
	private int runtime;
	private String fileName;
	private int timesPlayedToday;
	private LocalDate lastRequestDate;
	// constructor
	public JukeboxSong(String title, int year, String artist, int runtime, String fileName) {
		this.title = title;
		this.year = year;
		this.artist = artist;
		this.runtime = runtime;
		this.fileName = fileName;
		this.timesPlayedToday = 0;
		this.lastRequestDate = LocalDate.now();
	}
	// check if this a valid request and throw an exception if not. record the usage
	public boolean isValidRequest() throws ExceptionMaxUsagePerSong {
		// check if the max requests have been received
		if (hasMaxRequestsBeenReached())
			throw new ExceptionMaxUsagePerUser();
		// if valid, record the usage and return true
		recordJukeboxUse();
		// return true
		return true;
	}
	// increment the number of times the song has been requested today
	private void recordJukeboxUse() {
		// increment the number of songs requested
		this.timesPlayedToday++;
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
		if (currentDate.isBefore(futureDate) && this.timesPlayedToday >= 3)
			return true;
		// return false otherwise
		return false;
	}	
}
