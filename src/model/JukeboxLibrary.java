/*Author: Alex Erwin & Ian Burley
 *Purpose: The Jukebox Library stores a collection of songs, and keeps the queue of songs to be played 
 *Phase 1: Hardwired; Phase 2: Persisted
 */
// package definition
package model;
//import classes
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import songplayer.EndOfSongEvent;
import songplayer.EndOfSongListener;
import songplayer.SongPlayer;
// library object containing the songs
public class JukeboxLibrary {
	// instance variables
	Map<String, JukeboxSong> songs;
	List<JukeboxSong> queue;
	WaitingForSongToEnd eos;
	// constructor
	public JukeboxLibrary() {
		// setup the songs table
		this.songs = new TreeMap<String, JukeboxSong>();
		// setup the song queue
		this.queue = new ArrayList<>();
		// load the songs that we have
		loadSongsTable();
		// create only one of the private class
		this.eos = new WaitingForSongToEnd();		
	}
	// add song to queue
	public void addSongToQueue(String songTitle) throws ExceptionMaxUsagePerSong {
		// check if the song can be played
		if (canSongBePlayed(songTitle))
			this.queue.add(this.songs.get((String)songTitle));
	}
	// get the next song to be played
	public void getNextSong() {
		// note: data check for exists
		// variables
		JukeboxSong nextSong = null;
		// make sure the current song has finished playing
		if (this.eos.hasSongFinishedPlaying()) {
			// get the next song in the queue
			nextSong = popNextSong();
			// check the next
			if (!(nextSong == null)) {
				// reset the song finished state
				this.eos.resetSongState();
				// tell the system to play the file
				SongPlayer.playFile(this.eos, nextSong.getFilePath()); 
				// sleep for runtime before getting the next song
				sleepUntilNextSong(nextSong.getRunTime());
			}			
		}

	}
	// can song be played. may throw ExceptionMaxUsagePerSong
	public boolean canSongBePlayed(String songTitle) throws ExceptionMaxUsagePerSong {
		return this.songs.get((String)songTitle).isValidRequest();
	}
	// get the runtime for user check
	public int getRunTime(String songTitle) {
		return this.songs.get((String)songTitle).getRunTime();
	}
	// update on success after checks the counter variables
	public void updateCounters(String songTitle) {
		this.songs.get((String)songTitle).recordJukeboxUse();
	}
	// public test for song exists in catalog
	public boolean songExists(String songTitle) {
		return this.songs.containsKey((String)songTitle);
	}
	// public load songs method
	public void addSongToLibrary(String title, String artist, int runtime, String filePath) throws ExceptionFileNotFound {
		this.songs.put(title, new JukeboxSong(title, artist, runtime, filePath));
	}
	// private load songs helper for testing
	private void loadSongsTable() {
		addSongToLibrary("Tada","Microsoft", 2, "songfiles/tada.wav");
		addSongToLibrary("Flute", "Sun Microsystems", 6, "songfiles/flute.aif");
		addSongToLibrary("Space Music", "Unknown", 6, "songfiles/spacemusic.au");
	}
	// private pop array method 
	private JukeboxSong popNextSong() {
		// variables
		JukeboxSong element = null;
		// check if empty
		if (!this.queue.isEmpty()) {
			// get the element at the head
			element = this.queue.get(0);
			// remove it from the queue
			this.queue.remove(0);
		}
		// return it
		return element;
	}
	// private sleep method to account for the pause between tracks
	private void sleepUntilNextSong(int wait) {
		// variables
		int paddedWait = (int)((double)(wait) * 1000);
		// cause the thread to sleep
		try { Thread.sleep(paddedWait); } 
		catch (InterruptedException e) {}		
	}
	// inner class to see if the song is over.. credit to Rick Mercer
	private class WaitingForSongToEnd implements EndOfSongListener {
		// instance variables
		private boolean songIsFinished;
		// constructor
		public WaitingForSongToEnd() {
			this.songIsFinished = true;
		}
		// reset after each use
		public void resetSongState() {
			this.songIsFinished = false;
		}
		
		public boolean getState() {
			return this.songIsFinished;
		}
		// is the song over
		public boolean hasSongFinishedPlaying() {
			return this.songIsFinished;
		}
		// interface method
		public void songFinishedPlaying(EndOfSongEvent eosEvent) {
			// tell us that the song is complete
			this.songIsFinished = true;
			// get the next one
			getNextSong();
		}
	}		
}
