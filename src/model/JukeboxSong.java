/*Author: Alex Erwin & Ian Burley
 *Purpose: An object representing the song
 */
// package definition
package model;
// import classes
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import exceptions.ExceptionFileNotFound;
import exceptions.ExceptionMaxUsagePerSong;

import java.io.File;
import java.io.Serializable;
// song object
public class JukeboxSong implements Serializable {
	// instance vars
	private String title;
	private int year;
	private String artist;
	private int runtime;
	private String filePath;
	private int timesPlayedToday;
	private LocalDate lastRequestDate;
	// constructor
	public JukeboxSong(String title, String artist, int runtime, String filePath) throws ExceptionFileNotFound {
		// variables
		File path = new File(filePath);
		// check if the path exists
		if (!path.exists())
			throw new ExceptionFileNotFound(title);
		// if the file exists continue
		this.title = title;
		this.year = 1998;
		this.artist = artist;
		this.runtime = runtime;
		this.filePath = filePath;
		this.timesPlayedToday = 0;
		this.lastRequestDate = LocalDate.now();
	}
	// get artist
	public String getArtist() {
		return this.artist;
	}
	// get title
	public String getTitle() {
		return this.title;
	}
	// get filePath
	public String getFilePath() {
		return this.filePath;
	}
	// get runtime
	public int getRunTime() {
		return this.runtime;
	}
	// get runtime as a minute:second string
	public String getRunTimeAsDuration() {
		// variables
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("mm:ss"); // format duration as minute:second
		LocalTime duration = LocalTime.of(0, ((runtime / 60) % 60), (runtime % 60)); // convert runtime into time format
		// return as a duration
		return formatter.format(duration);
	}
	// the to string method for displaying in a JList
	public String toString() {
		return getRunTimeAsDuration() + " " + getTitle() + " by " + getArtist();
	}
	// check if this a valid request and throw an exception if not. record the usage
	public boolean isValidRequest() throws ExceptionMaxUsagePerSong {
		// check if the max requests have been received
		if (hasMaxRequestsBeenReached())
			throw new ExceptionMaxUsagePerSong(this.title);
		// return true
		return true;
	}
	// increment the number of times the song has been requested today
	public void recordJukeboxUse() {
		// increment the number of songs requested
		this.timesPlayedToday++;
		// record the last 
		this.lastRequestDate = LocalDate.now();
	}
	// method to reset the times played
	public void resetTimesPlayed() {
		this.timesPlayedToday = 0;
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
		else if (currentDate.isAfter(futureDate) || currentDate.isEqual(futureDate))
			resetTimesPlayed();
		// return false otherwise
		return false;
	}	
}
