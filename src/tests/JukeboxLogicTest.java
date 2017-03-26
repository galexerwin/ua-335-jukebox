/* Authors: Alex Erwin & Ian Burley
 * Purpose: To test the Jukebox logic
 */
// package definition
package tests;
// import classes
import static org.junit.Assert.*;
import javax.swing.JPasswordField;
import org.junit.Test;
import exceptions.ExceptionFileNotFound;
import exceptions.ExceptionInvalidCredentials;
import exceptions.ExceptionMaxUsagePerLifetime;
import exceptions.ExceptionMaxUsagePerSong;
import exceptions.ExceptionMaxUsagePerUser;
import exceptions.ExceptionNotLoggedIn;
import model.Jukebox;
import model.JukeboxCredentials;
import model.JukeboxLibrary;
import model.JukeboxQueue;
import model.JukeboxSong;
import model.JukeboxUser;
// logic tests
public class JukeboxLogicTest {
	// tester for passwords
	JPasswordField password = new JPasswordField(); 
	// test exception raised when testing if user can request a song
	@Test(expected = ExceptionNotLoggedIn.class) 
	public void noOneLoggedIn() {
		// new jukebox credentials
		JukeboxCredentials jc = JukeboxCredentials.getInstance();
		// check if a song can be requested by empty user
		jc.canUserRequestSong("", 0);
		// check if the counters can be updated for an empty user
		jc.updateCounters("", 0);
	}
	// test exception raised when the login is incorrect
	@Test(expected = ExceptionInvalidCredentials.class)
	public void invalidUserPassword() {
		// new jukebox credentials
		JukeboxCredentials jc = JukeboxCredentials.getInstance();
		// password
		password.setText("1");
		// check if we can log in as anyone
		jc.loginUser("", password.getPassword());
		// password
		password.setText("5");	
		// check if we can log in as a proper user with the wrong password
		jc.loginUser("Devon", password.getPassword());
	}
	// test exception raised when the file isn't found
	@Test(expected = ExceptionFileNotFound.class)
	public void invalidSongFile() {
		// new jukebox
		Jukebox jbox = new Jukebox();
		// new jukebox library
		JukeboxLibrary jl = JukeboxLibrary.getInstance(jbox);
		// attempt to add a file we know isn't there
		jl.addSongToLibrary("JavaIsFun", "CS335", 0, "songfiles/javaisfun.wav");
	}
	// test exception raised when the user has exceed the lifetime max
	@Test(expected = ExceptionMaxUsagePerLifetime.class)
	public void lifetimeMaxExceeded() {	
		// new jukebox
		Jukebox jbox = new Jukebox();
		// new jukebox credentials
		JukeboxCredentials jc = jbox.getUsers();
		// try
		try {
			// password
			password.setText("22");
			// log in a user
			jc.loginUser("Devon", password.getPassword());
			// check if a song exceeding 90000 seconds (1500 minutes) can be requested
			jc.canUserRequestSong("Devon", 95000);
		} catch (Exception e) {}
	}
	// test exception raised when the user attempts to add too many songs to the queue
	@Test(expected = ExceptionMaxUsagePerUser.class)
	public void perUserMaxExceeded() {
		// new jukebox
		Jukebox jbox = new Jukebox();
		// new jukebox credentials
		JukeboxCredentials jc = jbox.getUsers();
		// password
		password.setText("22");
		// log in a user
		jc.loginUser("Devon", password.getPassword());
		// check if the song can be added and then update the counters
		for (int i = 0; i < 4; i++)
			if (jc.canUserRequestSong("Devon", 1))
				jc.updateCounters("Devon", 1);
	}
	// test exception raised when the song being added to the queue has been played too many times 
	@Test(expected = ExceptionMaxUsagePerSong.class)
	public void perSongMaxExceeded() {
		// new jukebox
		Jukebox jbox = new Jukebox();
		// new jukebox library
		JukeboxLibrary jl = JukeboxLibrary.getInstance(jbox);
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
		JukeboxCredentials jc = JukeboxCredentials.getInstance();
		// new jukebox user (directly)
		JukeboxUser ju = new JukeboxUser("Devon", "1");
		// test should not work if no user is associated
		try { jc.canUserRequestSong("", 0);} 
		catch (Exception e) {}
		// updating counters should not work if no user is associated
		try { jc.updateCounters("", 0);} 
		catch (Exception e) {}	
		// the singleton is catching this
		try {
			// password
			password.setText("22");
			// log in a user
			jc.loginUser("Devon", password.getPassword());
			jc.canUserRequestSong("Devon", 2);
			jc.updateCounters("Devon", 2);
			// logout the user
			jc.logoutUser("Devon");			
		} catch (Exception e) {}
		// attempt the same again
		// test should not work if no user is associated
		try { jc.canUserRequestSong("", 0);} 
		catch (Exception e) {}
		// updating counters should not work if no user is associated
		try { jc.updateCounters("", 0);} 
		catch (Exception e) {}	
		// password
		password.setText("5");
		// log in Devon with an incorrect password
		try { ju.loginUser(password.getPassword()); }
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
		// new jukebox
		Jukebox jbox = new Jukebox();
		// new jukebox library
		JukeboxLibrary jl = jbox.getLibrary();
		// new jukebox queue
		JukeboxQueue jq = jbox.getQueue();
		// try
		try {
			// add song to queue
			jl.addSongToQueue("Tada");
			// play the song
			jq.getNextSong();
			// play an empty queue
			jq.getNextSong();					
		} catch (Exception e) {}
	}
	// test the main procedure
	@Test
	public void testMainProcedure() {		
		// create a new jukebox
		Jukebox jbox = new Jukebox();
		// password
		password.setText("1");	
		// login a user
		jbox.userLogin("Chris", password.getPassword());
		// add three songs
		jbox.addSong("Tada");
		jbox.addSong("Flute");
		jbox.addSong("SpaceMusic");
		// password
		password.setText("22");
		// login another user
		jbox.userLogin("Devon", password.getPassword());
		// add three more songs
		jbox.addSong("Tada");
		jbox.addSong("Tada");
		jbox.addSong("Flute");
		// password
		password.setText("1");		
		// add a non-existent user
		jbox.userLogin("Dev", password.getPassword());
		// log out
		jbox.userLogout();
		// add a max song
		jbox.addSong("Tada");
		// password
		password.setText("333");
		// login a valid user
		jbox.userLogin("River", password.getPassword());
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
		// password
		password.setText("22");
		// login
		jbox.userLogin("Devon", password.getPassword());
		// add two songs
		jbox.addSong("Tada");
		jbox.addSong("Tada");
		jbox.addSong("Flute");
		// password
		password.setText("1");		
		// login
		jbox.userLogin("Chris", password.getPassword());
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
		String message = "", user = "";		
		// create a new jukebox
		Jukebox jbox = new Jukebox();
		// password
		password.setText("22");
		// login
		jbox.userLogin("Devon", password.getPassword());
		// get current user
		JukeboxUser userObj = jbox.getCurrentUserAsObject();
		// test the user
		assertEquals("Devon", userObj.whoAmI());
		// test the user as string
		assertEquals("Devon", jbox.getCurrentUserAsString());
		// add a song
		jbox.addSong("Tada");
		jbox.addSong("Tada");
		jbox.addSong("Flute");
		jbox.addSong("Space Music");
		// test the song requests
		assertEquals(3, userObj.getSongRequests());
		// test the login state
		assertTrue(userObj.isLoggedIn());
		// get message
		message = jbox.getUserMessage();
		// test the message
		assertEquals("You have exceeded your max song requests today!", message);
		// clear message
		jbox.clearUserMessage();
		// get message
		message = jbox.getUserMessage();		
		// test the message
		assertEquals("", message);
	}
	@Test
	public void testModels() {
		// intialize
		Jukebox jbox = new Jukebox();
		JukeboxLibrary jl = jbox.getLibrary();
		JukeboxQueue jq = jbox.getQueue();
		// library table model tests
		assertEquals("Artist", jl.getColumnName(0));
		assertEquals("Title", jl.getColumnName(1));
		assertEquals("Seconds", jl.getColumnName(2));
		assertEquals(3, jl.getColumnCount());
		assertFalse(0 == jl.getColumnCount());
		assertFalse(0 == jl.getRowCount());
		assertFalse(jl.isCellEditable(0, 0));
		jl.getValueAt(0, 0);
		jl.getValueAt(0, 1);
		jl.getValueAt(0, 2);
		jl.getColumnClass(1).toString();
		jl.setValueAt(null, 0, 0);
		jl.addTableModelListener(null);
		jl.removeTableModelListener(null);
		// queue list model tests
		jq.getElementAt(0);
		jq.removeListDataListener(null);
		jq.addListDataListener(null);
	}
	
	
	
	@Test
	public void unimplemented() {
		// variables 
		boolean testSuccess = false;		
		// new jukebox
		Jukebox jbox = new Jukebox();
		// new jukebox library
		JukeboxLibrary jl = JukeboxLibrary.getInstance(jbox);
		// create a jukebox song
		JukeboxSong js = new JukeboxSong("Tada", "Test", 2, "songfiles/tada.wav");
		// create a jukebox user
		JukeboxUser ju = new JukeboxUser("Devon", "1");
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
		//
		jbox.forceStateChange();
		jbox.getJListRefresh();
		jbox.callNext();
		jbox.getJListData();
		jbox.getJTableData();
		jbox.isUserLoggedIn();
		jbox.getQueue();
		ju.getLifetimeMaxAsDuration();
		js.getRunTimeAsDuration();
		js.toString();
	}
}
