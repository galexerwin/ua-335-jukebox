/*Author: Alex Erwin & Ian Burley
 *Purpose: The Jukebox Driver. Controls access via login and tests rules before playing any songs
 */
// package definition
package model;
// import classes
import java.util.Observable;
// jukebox
public class Jukebox extends Observable {
	// instance vars
	private JukeboxLibrary jl;
	private JukeboxCredentials jukeboxUsers;
	private JukeboxUser jukeboxUser;
	private JukeboxLibrary jukeboxSongs;
	private String userLoggedIn;
	private String messageToDialog;
	// main method (TO BE REMOVED AFTER GUI)
	public static void main(String[] args) {
		// construct the jukebox
		Jukebox j = new Jukebox();
	}	
	// constructor
	public Jukebox() {
		// setup objects
		jukeboxUsers = new JukeboxCredentials();
		jukeboxSongs = new JukeboxLibrary();
		// set defaults
		this.userLoggedIn = "";
		this.messageToDialog = "";
		
		// usage stub
		/*
		userLogin("Chris", 1);
		addSong("Tada");
		addSong("Flute");
		addSong("SpaceMusic");
		userLogin("Devon", 22);
		addSong("Tada");
		addSong("Tada");
		addSong("Flute");
		userLogin("Dev", 8);
		userLogout();
		addSong("Tada");
		userLogin("River", 333);
		addSong("Tada");
		*/
	}
	// add a song to the queue. Max plays per song, per user, and lifetime and/or not logged in may throw exceptions
	public void addSong(String songTitle) throws ExceptionMaxUsagePerSong, ExceptionMaxUsagePerUser, ExceptionMaxUsagePerLifetime, ExceptionNotLoggedIn {	
		// wrap in try catch because it throws max usage per song
		try {
			// check if the user and song are both valid
			if (canRequestSong(songTitle)) {
				// add the song to the queue
				this.jukeboxSongs.addSongToQueue(songTitle);			
				// update the counters on the song
				this.jukeboxSongs.updateCounters(songTitle);
				// update the counters on the user
				this.jukeboxUsers.updateCounters(userLoggedIn, this.jukeboxSongs.getRunTime(songTitle));
				// begin playing
				this.jukeboxSongs.getNextSong();
			}
		} catch(ExceptionNotLoggedIn e) {
			// forward the message
			this.messageToDialog = e.getMessage();
		} catch(ExceptionMaxUsagePerLifetime e) {
			// forward the message
			this.messageToDialog = e.getMessage();
		} catch(ExceptionMaxUsagePerUser e) {
			// forward the message
			this.messageToDialog = e.getMessage();		
		} catch(ExceptionMaxUsagePerSong e) {
			// forward the message
			this.messageToDialog = e.getMessage();			
		}
		// tell the interface something has changed
		setChanged();
		// notify observers
		notifyObservers();		
	}
	// log the user in
	public void userLogin(String username, int password) throws ExceptionInvalidCredentials {
		// wrap in try catch because it throws invalid credentials exception
		try {
			// log in the user
			this.jukeboxUsers.loginUser(username, password);
			// save the user name
			this.userLoggedIn = username;
		} catch (ExceptionInvalidCredentials e) {
			// forward the message
			this.messageToDialog = e.getMessage();
		}
		// tell the interface something has changed
		setChanged();
		// notify observers
		notifyObservers();		
	}
	// log out
	public void userLogout() {
		// logout the user
		this.jukeboxUsers.logoutUser(this.userLoggedIn);
		// tell the interface something has changed
		setChanged();
		// notify observers
		notifyObservers();				
	}
	// helper method
	private boolean canRequestSong(String songTitle) {
		// determine if the user can request this song
		if (!this.jukeboxSongs.songExists(songTitle)) // check if the song exists first
			return false;
		else if (!this.jukeboxUsers.canUserRequestSong(this.userLoggedIn, this.jukeboxSongs.getRunTime(songTitle))) // check if user can select any songs
			return false;
		else if (!this.jukeboxSongs.canSongBePlayed(songTitle)) // check if the song is playable
			return false;
		else 			
			return true;
	}
}
