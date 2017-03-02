/*Author: Alex Erwin & Ian Burley
 *Purpose: The Jukebox Library stores a collection of songs 
 *Phase 1: Hardwired; Phase 2: Persisted
 */
// package definition
package model;
//import classes
import java.util.Map;
import java.util.TreeMap;
// library object containing the songs
public class JukeboxLibrary {
	// instance variables
	Map<String, JukeboxSong> songs;
	// constructor
	public JukeboxLibrary() {
		// load the songs that we have
		loadSongsTable();
	}
	// can song be played. may throw ExceptionMaxUsagePerSong
	public boolean canSongBePlayed(String songTitle) {
		return this.songs.get((String)songTitle).isValidRequest();
	}
	// private load credentials
	private void loadSongsTable() {
		// setup the songs table
		this.songs = new TreeMap<String, JukeboxSong>();		
	}	
}
