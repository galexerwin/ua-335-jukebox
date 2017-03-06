/* Authors: Alex Erwin & Ian Burley
 * Purpose: To test the Jukebox logic
 */
// package definition
package tests;
// import classes
import static org.junit.Assert.*;
import org.junit.Test;

import model.ExceptionFileNotFound;
import model.ExceptionInvalidCredentials;
import model.ExceptionMaxUsagePerLifetime;
import model.ExceptionMaxUsagePerSong;
import model.ExceptionMaxUsagePerUser;
import model.ExceptionNotLoggedIn;
import model.Jukebox;
import model.JukeboxCredentials;
import model.JukeboxLibrary;
import model.JukeboxSong;
import model.JukeboxUser;

// logic tests
public class JukeboxLogicTest {
	// test exception raised when testing if user can request a song
	@Test(expected = ExceptionNotLoggedIn.class) 
	public void noOneLoggedIn() {
		// new jukebox credentials
		JukeboxCredentials jc = new JukeboxCredentials();
		// check if a song can be requested by empty user
		jc.canUserRequestSong("", 0);
		// check if the counters can be updated for an empty user
		jc.updateCounters("", 0);
	}
	// test exception raised when the login is incorrect
	@Test(expected = ExceptionInvalidCredentials.class)
	public void invalidUserPassword() {
		// new jukebox credentials
		JukeboxCredentials jc = new JukeboxCredentials();
		// check if we can log in as anyone
		jc.loginUser("", 1);
		// check if we can log in as a proper user with the wrong password
		jc.loginUser("Devon", 5);
	}
	// test exception raised when the file isn't found
	@Test(expected = ExceptionFileNotFound.class)
	public void invalidSongFile() {
		// new jukebox library
		JukeboxLibrary jl = new JukeboxLibrary();
		// attempt to add a file we know isn't there
		jl.addSongToLibrary("JavaIsFun", "CS335", 0, "songfiles/javaisfun.wav");
	}
	// test exception raised when the user has exceed the lifetime max
	@Test(expected = ExceptionMaxUsagePerLifetime.class)
	public void lifetimeMaxExceeded() {
		// new jukebox credentials
		JukeboxCredentials jc = new JukeboxCredentials();
		// log in a user
		jc.loginUser("Devon", 22);
		// check if a song exceeding 90000 seconds (1500 minutes) can be requested
		jc.canUserRequestSong("Devon", 95000);
	}
	// test exception raised when the user attempts to add too many songs to the queue
	@Test(expected = ExceptionMaxUsagePerUser.class)
	public void perUserMaxExceeded() {
		// new jukebox credentials
		JukeboxCredentials jc = new JukeboxCredentials();
		// log in a user
		jc.loginUser("Devon", 22);
		// check if the song can be added and then update the counters
		for (int i = 0; i < 4; i++)
			if (jc.canUserRequestSong("Devon", 1))
				jc.updateCounters("Devon", 1);
	}
	// test exception raised when the song being added to the queue has been played too many times 
	@Test(expected = ExceptionMaxUsagePerSong.class)
	public void perSongMaxExceeded() {
		// new jukebox library
		JukeboxLibrary jl = new JukeboxLibrary();
		// attempt to add songs to the queue
		for(int i = 0; i < 4; i++) {
			jl.addSongToQueue("Tada");
			jl.updateCounters("Tada");
		}
	}
	// test the login in and logout then requesting a song
	@Test
	public void testUserAuth() {
		// new jukebox credentials
		JukeboxCredentials jc = new JukeboxCredentials();
		// new jukebox user (directly)
		JukeboxUser ju = new JukeboxUser("Devon", 1);
		// test should not work if no user is associated
		try { jc.canUserRequestSong("", 0);} 
		catch (Exception e) {}
		// updating counters should not work if no user is associated
		try { jc.updateCounters("", 0);} 
		catch (Exception e) {}	
		// login a user and try again
		jc.loginUser("Devon", 22);
		jc.canUserRequestSong("Devon", 2);
		jc.updateCounters("Devon", 2);
		// logout the user
		jc.logoutUser("Devon");
		// attempt the same again
		// test should not work if no user is associated
		try { jc.canUserRequestSong("", 0);} 
		catch (Exception e) {}
		// updating counters should not work if no user is associated
		try { jc.updateCounters("", 0);} 
		catch (Exception e) {}	
		// log in Devon with an incorrect password
		try { ju.authenticateUser(5); }
		catch (Exception e) {}
		// record with Devon not logged in
		try { ju.recordJukeboxUse(2); }
		catch (Exception e) {}
		// try to request song with Devon not logged in
		try { ju.isValidRequest(2); }
		catch (Exception e) {}		
	}
	// add a song and play it
	@Test
	public void addAndPlaySong() {
		// create a jukebox library
		JukeboxLibrary jl = new JukeboxLibrary();
		// add song to queue
		jl.addSongToQueue("Tada");
		// play the song
		jl.getNextSong();
		// play an empty queue
		jl.getNextSong();
	}
	// test the main procedure
	@Test
	public void testMainProcedure() {
		// create a new jukebox
		Jukebox jbox = new Jukebox();
		// login a user
		jbox.userLogin("Chris", 1);
		// add three songs
		jbox.addSong("Tada");
		jbox.addSong("Flute");
		jbox.addSong("SpaceMusic");
		// login another user
		jbox.userLogin("Devon", 22);
		// add three more songs
		jbox.addSong("Tada");
		jbox.addSong("Tada");
		jbox.addSong("Flute");
		// add a non-existent user
		jbox.userLogin("Dev", 8);
		// log out
		jbox.userLogout();
		// add a max song
		jbox.addSong("Tada");
		// login a valid user
		jbox.userLogin("River", 333);
		// try again
		jbox.addSong("Tada");		
	}
	// test main with exceptions
	@Test
	public void testMainAddSongNotLoggedIn() {
		// variables
		String message = "";
		// create a new jukebox
		Jukebox jbox = new Jukebox();
		// add a song
		jbox.addSong("Tada");
		// get message
		message = jbox.getUserMessage();
		// test the message
		assertEquals("Please login first!", message);
	}
	// test main with exceptions
	@Test
	public void testMainMaxSongPlays() {
		// variables
		String message = "";
		// create a new jukebox
		Jukebox jbox = new Jukebox();
		// login
		jbox.userLogin("Devon", 22);
		// add two songs
		jbox.addSong("Tada");
		jbox.addSong("Tada");
		jbox.addSong("Flute");
		// login
		jbox.userLogin("Chris", 1);
		// add two songs
		jbox.addSong("Tada");
		jbox.addSong("Tada");
		jbox.addSong("Flute");		
		// get message
		message = jbox.getUserMessage();
		// test the message
		assertEquals("The requested song: Tada, has reached the allowable plays for today!", message);
	}
	// test main with exceptions
	@Test
	public void testMainMaxUserRequests() {
		// variables
		String message = "";
		// create a new jukebox
		Jukebox jbox = new Jukebox();
		// login
		jbox.userLogin("Devon", 22);
		// add a song
		jbox.addSong("Tada");
		jbox.addSong("Tada");
		jbox.addSong("Flute");
		jbox.addSong("Space Music");
		// get message
		message = jbox.getUserMessage();
		// test the message
		assertEquals("You have exceeded your max song requests today!", message);
	}	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Test
	public void unimplemented() {
		// variables 
		boolean testSuccess = false;
		// create a jukebox library
		JukeboxLibrary jl = new JukeboxLibrary();		
		// create a jukebox song
		JukeboxSong js = new JukeboxSong("Tada", "Test", 2, "songfiles/tada.wav");
		// create a jukebox user
		JukeboxUser ju = new JukeboxUser("Devon", 1);
		// get lifetimeMaxBalance
		assertEquals(90000, ju.getLifetimeMaxBalance());
		// get runtime
		assertEquals(2, js.getRunTime());
		// get title
		assertEquals("Tada", js.getTitle());
		// get file path
		assertEquals("songfiles/tada.wav", js.getFilePath());
		// record jukebox usage
		for (int i = 0; i < 4; i++)
			js.recordJukeboxUse();
		// try to validate
		try { testSuccess = !js.isValidRequest(); } // negate the truth statement because success should equal false
		catch (Exception e) { testSuccess = true; }
		// test
		assertTrue(testSuccess);
		// reset
		js.resetTimesPlayed();
		// try to validate
		try { testSuccess = !js.isValidRequest(); } // negate the truth statement because success should equal false
		catch (Exception e) { testSuccess = true; }
		// test
		assertFalse(testSuccess);
		// test
		assertEquals(2, jl.getRunTime("Tada"));
		// test
		assertTrue(jl.songExists("Tada"));
		assertFalse(jl.songExists("Darkside"));
		
		Jukebox jbox = new Jukebox();
		jbox.main(null);
	}
}
