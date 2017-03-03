package model;

import java.time.LocalDateTime;

/*Author: Alex Erwin
 *Purpose: The main Jukebox Object
 */
public class Jukebox {
	// instance vars
	JukeboxLibrary jl;
	// main method
	public static void main(String[] args) {
		// construct the jukebox
		Jukebox j = new Jukebox();
	}	
	// constructor
	public Jukebox() {
		// setup the library
		jl = new JukeboxLibrary();
		
		
		System.out.println(LocalDateTime.now().toString());
		
		addSong("Tada");
		jl.getNextSong();		
		addSong("Tada");
		jl.getNextSong();
		addSong("Tada");
		jl.getNextSong();		
		addSong("Tada");
		jl.getNextSong();		
		try {
			Thread.sleep(60000);
		} catch (InterruptedException e) {}
		
		addSong("Tada");
		jl.getNextSong();		
		addSong("Tada");
		jl.getNextSong();
		addSong("Tada");
		jl.getNextSong();
		addSong("Tada");
		jl.getNextSong();		
		/*
		playSong("Flute");
		playSong("Space Music");
		playSong("Flute");
		playSong("Tada");
		playSong("Flute");
		playSong("Space Music");
		playSong("Flute");*/

	}
	
	public void addSong(String songTitle) {	
		try {
			jl.addSongToQueue(songTitle);
		} catch(ExceptionMaxUsagePerSong e) {
			System.out.println(e.getMessage());
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
}
