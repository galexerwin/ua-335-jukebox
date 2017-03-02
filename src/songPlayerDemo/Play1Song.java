package songPlayerDemo;

import songplayer.EndOfSongEvent;
import songplayer.EndOfSongListener;
/**
 * Play one audio file from the songfiles directory.
 * There is no listener for the end of song event.
 * 
 * @author Rick Mercer
 */
import songplayer.SongPlayer;

public class Play1Song {

	/**
	 * Play one audio file with a listener for the end of song event. A println
	 * is included to indicate the song is playing in a separate Thread.
	 */
	public static void main(String[] args) {
	  new Play1Song();
	}
	
	public Play1Song() {
		// Construct an instance of the private inner class
		EndOfSongListener waitForSongEnd = new WaitingForSongToEnd();
		SongPlayer.playFile(waitForSongEnd, "songfiles/LopingSting.mp3");
	}

	/**
	 * This inner class allows us to have an callback function that receive a
	 * songFinishedPlaying message whenever an audio file has been played.
	 */
	private class WaitingForSongToEnd implements EndOfSongListener {

		public void songFinishedPlaying(EndOfSongEvent eosEvent) {
			System.out.println("\nFinished " + eosEvent.fileName() + ", " + eosEvent.finishedDate() + ", "
					+ eosEvent.finishedTime());
		}
	}
}