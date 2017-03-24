/*Author: Alex Erwin & Ian Burley
 *Purpose: The Jukebox Driver. Controls access via login and tests rules before playing any songs
 */
// package definition
package model;
import java.io.Serializable;
// import classes
import java.util.Observable;

import javax.swing.ListModel;
import javax.swing.table.TableModel;

import exceptions.ExceptionInvalidCredentials;
import exceptions.ExceptionMaxUsagePerLifetime;
import exceptions.ExceptionMaxUsagePerSong;
import exceptions.ExceptionMaxUsagePerUser;
import exceptions.ExceptionNotLoggedIn;
// jukebox
public class Jukebox extends Observable implements Serializable {
	// instance vars
	private JukeboxCredentials jukeboxUsers;
	private JukeboxLibrary jukeboxSongs;
	private ListModel<JukeboxSong> listModel;
	private TableModel tableModel;
	private String userLoggedIn;
	private String messageToDialog;
	// constructor
	public Jukebox() {
		// setup objects
		jukeboxUsers = JukeboxCredentials.getInstance();
		jukeboxSongs = JukeboxLibrary.getInstance(this);
		// setup models
		tableModel = jukeboxSongs;
		listModel = jukeboxSongs;
		// set defaults
		this.userLoggedIn = "";
		this.messageToDialog = "";
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
		} catch(ExceptionMaxUsagePerSong e) {
			// forward the message
			this.messageToDialog = e.getMessage();
		} catch(ExceptionMaxUsagePerUser e) {
			// forward the message
			this.messageToDialog = e.getMessage();
		}
		// tell the interface something has changed
		setChanged();
		// notify observers
		notifyObservers();		
	}
	// log the user in
	public void userLogin(String username, char[] password) throws ExceptionInvalidCredentials {
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
		// clear last logged in
		this.userLoggedIn = "";
		// tell the interface something has changed
		setChanged();
		// notify observers
		notifyObservers();				
	}
	// get the loggedInState
	public boolean isUserLoggedIn() {
		// variables
		boolean response = false;
		JukeboxUser theUser = getCurrentUserAsObject();
		// check the user
		if (!(theUser == null))
			return theUser.isLoggedIn();
		// return the response
		return response;
	}
	// get the currentUser as a String
	public String getCurrentUserAsString() {
		return this.userLoggedIn;
	}
	// get the currentUser
	public JukeboxUser getCurrentUserAsObject() {
		if (!(jukeboxUsers.getUser(this.userLoggedIn) == null))
			return jukeboxUsers.getUser(this.userLoggedIn);
		return null;
	}	
	// get message
	public String getUserMessage() {
		return this.messageToDialog;
	}
	// clear message after use. Added because of tests in the view
	public void clearUserMessage() {
		this.messageToDialog = "";
	}
	// force a state change
	public void forceStateChange() {
		// tell the interface something has changed
		setChanged();
		// notify observers
		notifyObservers();			
	}
	// return the table model object based on the JTable
	public TableModel getJTableData() {
		return tableModel;
	}
	// return the list model object based on the JList
	public ListModel<JukeboxSong> getJListData() {
		return listModel;
	}
	// return the queue as an array to refresh in the update method of view
	public JukeboxSong[] getJListRefresh() {
		return this.jukeboxSongs.getQueueAsArray();
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
	
	//return the current library for this Jukebox
	public JukeboxLibrary getLibrary(){
		return jukeboxSongs;
	}
	
	//If using previous data, get current library, then call forceUpdate, allowing for songs in saved queue to play.
	public void callNext(){
		JukeboxLibrary temp=getLibrary();
		temp.forceUpdate();
	}
}
