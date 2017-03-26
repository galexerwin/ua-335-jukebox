/*Author: Alex Erwin & Ian Burley
 *Purpose: The Jukebox Queue keeps a list of JukeboxSongs to played in first in first out fashion, iterates through list and plays
 */
// package definition
package model;
// import classes
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ListModel;
import javax.swing.SwingWorker;
import javax.swing.event.ListDataListener;
import songplayer.EndOfSongEvent;
import songplayer.EndOfSongListener;
import songplayer.SongPlayer;
// jukebox queue class
@SuppressWarnings("serial")
public class JukeboxQueue implements ListModel<JukeboxSong>, Serializable {
	// instance variables
	private final static JukeboxQueue instance = new JukeboxQueue(); // for the singleton
	private static boolean initialized = false; // for the singleton
	private List<JukeboxSong> queue;
	private PlaySongOnNewThread psnt;
	private Jukebox juke;
	// constructor changed for singleton
	private JukeboxQueue() {
		super();
	}
	// initialize the singleton
	private void initializeSingleton(Jukebox juke) {
		// setup the song queue
		this.queue = new ArrayList<>();
		// save back reference to Jukebox
		this.juke = juke;
		// create only one of the private class
		this.psnt = new PlaySongOnNewThread(juke);
	}
	// method which returns a reference to the singleton
	public static synchronized JukeboxQueue getInstance(Jukebox juke) {
		// check if there is a JukeboxLibrary to return
	    if (initialized) return instance;
	    // create one if it doesn't exist
	    instance.initializeSingleton(juke);
	    // set the flag
	    initialized = true;
	    // return true
	    return instance;		
	}
	// add a song to the queue
	public void addSongToQueue(JukeboxSong song) {
		// add the song
		this.queue.add(song);
	}
	// get the next song to be played
	public void getNextSong() {
		// create a new worker thread if the old one is dead
		if (this.psnt.hasThreadDied())
			this.psnt = new PlaySongOnNewThread(this.juke);
		// execute on a swing worker
		this.psnt.execute();
	}
	// if user wants to use previous data, create new PlaySongOnNewThread and then play songs in queue
	public void forceUpdate(){
		// refresh the object
		this.psnt = new PlaySongOnNewThread(juke);
		// get the next song
		getNextSong();
	}	
	// inner class that plays the song queue in a different thread to prevent unresponsiveness
	private class PlaySongOnNewThread extends SwingWorker<Void, Void> implements EndOfSongListener, Serializable {
		// instance variables
		private boolean songIsFinished = true;
		private boolean threadDied = false;
		private Jukebox juke;
		// constructor
		public PlaySongOnNewThread (Jukebox juke) {
			// store a reference to the caller
			this.juke = juke;
		}
		// create a background thread and execute it
		@Override
		protected Void doInBackground() throws Exception {
			// variables
			JukeboxSong nextSong = null;
			// make sure the current song has finished playing
			if (hasSongFinishedPlaying()) {
				// check if the queue is empty
				if (!queue.isEmpty()) {
					// get the next song in the queue
					nextSong = queue.get(0);
					// reset the song finished state
					resetSongState();
					// tell the system to play the file
					SongPlayer.playFile(this, nextSong.getFilePath()); 
				} else {
					this.threadDied = true;
				}
			}				
			// return
			return null;
		}
		// reset after each use
		public void resetSongState() {
			this.songIsFinished = false;
		}
		// is the song over
		public boolean hasSongFinishedPlaying() {
			return this.songIsFinished;
		}
		public boolean hasThreadDied() {
			return this.threadDied;
		}
		// interface method
		public void songFinishedPlaying(EndOfSongEvent eosEvent) {
			// tell us that the song is complete
			this.songIsFinished = true;
			// remove the song from the queue 
			queue.remove(0);
			// force an update for each song
			this.juke.forceStateChange();
			// get the next one
			try { doInBackground(); } 
			catch (Exception e) {}
		}		
	}
	// return the queue as an array to refresh the jlist control
	public JukeboxSong[] getQueueAsArray() {
		// dimension the array
		JukeboxSong[] theQueue = new JukeboxSong[getSize()];
		// the return the queue as an array
		return this.queue.toArray(theQueue);
	}	
	// return the item at the queue position
	@Override
	public JukeboxSong getElementAt(int rowIndex) {
		return this.queue.get(rowIndex);
	}
	// JList method for getting the size of the queue
	@Override
	public int getSize() {
		return this.queue.size();
	}
	@Override
	public void addListDataListener(ListDataListener arg0) {}	
	@Override
	public void removeListDataListener(ListDataListener arg0) {}	
}
