/*Author: Alex Erwin & Ian Burley
 *Purpose: The Jukebox Library stores a collection of songs, and keeps the queue of songs to be played 
 *Phase 1: Hardwired; Phase 2: Persisted
 *Phase 2: Changes to implement the list and table models
 */
// package definition
package model;
import java.io.Serializable;
//import classes
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import exceptions.ExceptionFileNotFound;
import exceptions.ExceptionMaxUsagePerSong;
// library object containing the songs
@SuppressWarnings("serial")
public class JukeboxLibrary implements TableModel, Serializable {
	// instance variables
	private final static JukeboxLibrary instance = new JukeboxLibrary(); // for the singleton
	private static boolean initialized = false; // for the singleton
	private Map<String, JukeboxSong> songs;
	private List<String> songKeys;
	private Jukebox juke;
	// constructor changed for singleton
	private JukeboxLibrary() {
		super();
	}
	// initialize the singleton
	private void initializeSingleton(Jukebox juke) {
		// setup the songs table
		this.songs = new TreeMap<String, JukeboxSong>();
		// setup the song keys
		this.songKeys = new ArrayList<>();
		// load the songs that we have
		loadSongsTable();
		// save back reference to Jukebox
		this.juke = juke;
	}
	// method which returns a reference to the singleton
	public static synchronized JukeboxLibrary getInstance(Jukebox juke) {
		// check if there is a JukeboxLibrary to return
	    if (initialized) return instance;
	    // create one if it doesn't exist
	    instance.initializeSingleton(juke);
	    // set the flag
	    initialized = true;
	    // return true
	    return instance;		
	}
	// add song to queue
	public void addSongToQueue(String songTitle) throws ExceptionMaxUsagePerSong {
		// check if the song can be played
		if (canSongBePlayed(songTitle)) {
			// get a handle to the queue
			JukeboxQueue jq = juke.getQueue();
			// add the song to the queue
			jq.addSongToQueue(this.songs.get((String)songTitle));
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
		// add song to map
		this.songs.put(title, new JukeboxSong(title, artist, runtime, filePath));
		// save the key for the JTable to find later
		this.songKeys.add(title);
	}
	// private load songs helper for testing
	private void loadSongsTable() {
		addSongToLibrary("Danse Macabre", "Kevin MacLeod", 34, "songfiles/DanseMacabreViolinHook.mp3");
		addSongToLibrary("Determined Tumbao", "FreePlay Music", 20, "songfiles/DeterminedTumbao.mp3");
		addSongToLibrary("Flute", "Sun Microsystems", 6, "songfiles/flute.aif");
		addSongToLibrary("Loping Sting", "Kevin MacLeod", 5, "songfiles/LopingSting.mp3");
		addSongToLibrary("Space Music", "Unknown", 6, "songfiles/spacemusic.au");
		addSongToLibrary("Swing Cheese", "FreePlay Music", 15, "songfiles/SwingCheese.mp3");
		addSongToLibrary("Tada","Microsoft", 2, "songfiles/tada.wav");
		addSongToLibrary("The Curtain Rises", "Kevin MacLeod", 28, "songfiles/TheCurtainRises.mp3");
		addSongToLibrary("Untameable Fire", "Pierre Langer", 282, "songfiles/UntameableFire.mp3");
	}
	// get song key at
	public String getSongKeyAt(int rowIndex) {
		return this.songKeys.get(rowIndex);
	}
	// return the class type of the data contained within the cell
	@Override
	public Class<?> getColumnClass(int colIndex) {
		return getValueAt(0, colIndex).getClass();
	}
	// JTable method for getting the column count of our list
	@Override
	public int getColumnCount() {
		return 3; // three is hard coded because of the required specification
	}
	// JTable method for getting the column names by ID
	@Override
	public String getColumnName(int columnID) {
		// return column name by column ID
		switch (columnID) {
		case 0:	return "Artist";
		case 1: return "Title";
		}
		// return last column for anything else
		return "Seconds";
	}
	// JTable method for returning the number of rows
	@Override
	public int getRowCount() {
		return songs.size();
	}
	// JTable method for retrieving data at a specified cell
	@Override
	public Object getValueAt(int rowIndex, int colIndex) {
		// get the title we are after
		String title = this.songKeys.get(rowIndex);
		// return cell at rowIndex x colIndex
		switch (colIndex) {
		case 0: return this.songs.get((String)title).getArtist();
		case 1: return this.songs.get((String)title).getTitle();
		}
		// default
		return this.songs.get((String)title).getRunTime();
	}
	// unused method stubs
	@Override
	public boolean isCellEditable(int arg0, int arg1) { return false; }
	@Override
	public void setValueAt(Object arg0, int arg1, int arg2) {}	
	@Override
	public void addTableModelListener(TableModelListener arg0) {}
	@Override
	public void removeTableModelListener(TableModelListener arg0) {}	
}
