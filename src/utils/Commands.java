package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Commands {
	private Commands() {
	}
	
	//#################################################
//	private static final String DEMAND = "<<"; // server demands something
//	private static final String RESPONSE = ">>"; // information from server
//	private static final String FORCE = ">>>>"; // server forces alteration of client
	
	//#######NEUE SERVERBEFEHLE########################
	//FROM SERVER TO CLIENT
	public static final String SERVER_PREFIX = "SERVER";
	//Server grüßt nach dem Verbindungsaufbau: SERVER GREETINGS <message>
	public static final String GREETINGS = "GREETINGS";
	public static final String SERVER_GREETINGS = SERVER_PREFIX + " "+GREETINGS;
	//Server sendet eine im Chatraum geschriebene Nachricht: SERVER SEND <date user message>
	public static final String SERVER_SEND = SERVER_PREFIX + "SEND";
	//Server löscht chatraum: SERVER DELETE <chatroomname>
//	public static final String SERVER_DELETE = SERVER_PREFIX + "DELETE";
	public static final String DELETE = "DELETE";
	//Server informiert den CLient, dass er die Verbindung beendet: QUIT <message>
	public static final String QUIT = "QUIT";
	
	
	//POSITIVE RÜCKMELDUNG VOM SERVER  <timecode> BEFEHL...
	//Antwort vom Server an Client bei erfolgreicher Anmeldung des CLients: <timecode> LOGIN SUCCESS <message>
	public static final String LOGIN_SUCCESS = "LOGIN SUCCESS";
	//Antwort vom Server an Client bei Anfrage,w as es für Chatraeume gibt: 
	//<timecode> LIST START "\n" LIST KAtzenSindHUnde "\n" LIST..... "\n" LIST END
	public static final String LIST_START = "LIST START";
	public static final String LIST_END = "LIST END";
	//Antwort vom Server bei erfolgreichem Chatroom-Wechsel: <timecode> JOIN SUCCESS
	public static final String JOIN_SUCCESS = "JOIN SUCCESS";
	//ANtwort des Servers auf USERS-Anfrage vom Client: <timecode> USERS START...<timecode> USERS <username>...<timecode>USERS END
	public static final String USERS_START = "USERS START";
	public static final String USERS_END = "USERS END";
	//erfolgreicheCLient Aktion
	public static final String SUCCESS = "SUCCESS";
	//erfolgreiche Neuerstellung eines Raumes
	public static final String CREATE_SUCCESS = "CREATE SUCCESS";
	//erfolgreiches Verlassen des Raumes
	public static final String LEAVE_SUCCESS = "LEAVE SUCCESS";
	
	//NEGATIVE RÜCKMELDUNG VOM SERVER: <timecode> BEFEHL...
	//Client-Login war nciht erfolgreich: <timecode> LOGIN FAIL
	public static final String LOGIN_FAIL = "LOGIN FAIL";
	public static final String CREATE_FAIL = "CREATE FAIL";
	public static final String JOIN_FAIL = "JOIN FAIL";
	public static final String LEAVE_FAIL = "LEAVE FAIL";
	
	//######FROM CLIENT USER TO SERVER (START WITH "/")######
	//Client logt sich mit Benutzernamen ein: <timecode> LOGIN <username>
	public static final String LOGIN = " LOGIN ";
	//Client fragt an, welche Chatraueme es gibt: <timecode> LIST
	public static final String LIST = "LIST";
	//Client waehlt Chatraum aus: <timecode> JOIN <chatroomname>
	public static final String JOIN = "JOIN";
	//Client schreibt eine Nachricht in den Chatraum: <timecode> SEND <message>
	public static final String SEND = "SEND";
	
	//Client will den Chatroom verlassen: <timecode> LEAVE
	public static final String LEAVE = "LEAVE";
	
	//Client fordert vom Server möglichst alle Nachrichten an, die jüngger sind als timecodeVonNachricht
	//<timecode> GET timecodeVonNachricht -> SErver antwortet mit "SERVER SEND oldestMsg.....SERVER SEND newestMsg
	public static final String GET = "GET";
	
	//QUIT USER??! <timecode> QUIT
	
	//Client fragt an, welche User sich im selben Raum befinden: <timecode> USERS
	public static final String USERS = "USERS";
	//Client versucht, CHatraum zu erstellen (nur wenn es diesen ncoh nicht gibt):
	//<timecode> CREATE <chatroomname>
	public static final String CREATE_ROOM = "CREATE";
	
	//#################################################
	
	private static final String SERVER_COMMAND_PREFIX = ">>>>";
	//#################################################

	// COMMANDS FROM SERVER TO CLIENT 
	public static final String GIVE_USERNAME = SERVER_COMMAND_PREFIX + "300";
	
	// SERVER COMMANDS THAT FORCE ALTERATION OF CLIENT 
	
	//public static final String FORCE_USERNAME = FORCE + "600_";
	public static final String FORCE_DISCONNECT = SERVER_COMMAND_PREFIX + "601_";
	
	// POSITIVE SERVER RESPONSES 
	/**
	 * username is valid and not-taken
	 */
	public static final String VALID_USERNAME = SERVER_COMMAND_PREFIX + "100_";
	/**
	 * user is logged in
	 */
	public static final String LOGGED_IN = SERVER_COMMAND_PREFIX + "101_";
	/**
	 * user is in a specific room
	 */
	public static final String IN_ROOM = SERVER_COMMAND_PREFIX + "102_";

	// NEGATIVE SERVER RESPONSES 
	/**
	 * username is invalid
	 */
	public static final String INVALID_USERNAME = SERVER_COMMAND_PREFIX + "000_";
	/**
	 * username is already taken
	 */
	public static final String USERNAME_TAKEN = SERVER_COMMAND_PREFIX + "001_";
	/**
	 * the server is already full
	 */
	public static final String SERVER_FULL = SERVER_COMMAND_PREFIX + "002_";

	// COMMANDS FROM CLIENT USER TO SERVER (START WITH "/")
	/**
	 * show the conditions for the login
	 */
	
	/**
	 * show a list of every usercommand
	 */
	public static final String SHOW_COMMANDS = "/commands";
	/**
	 * give server command to change to the following room
	 */
	public static final String GOTO_ROOM = "/goto ";
	/**
	 * whisper mode with following user
	 */
	public static final String WHISPER = "/whisper ";
	
	/**
	 * talk to every user in the room
	 */
	public static final String UN_WHISPER = "/unwhisper";

	/**
	 * give a list of all active users
	 * this is also a Server Admin Command
	 */
//	public static final String USERS = "/users";
	/**
	 * give a list of all active rooms
	 */
	public static final String ROOMS = "/rooms";
	
	public static final String ADD_ROOM = "/addroom ";
	/**
	 * this is also a Server Admin Command
	 */
	public static final String LOG = "/log";
	/**
	 * quit the chat session and terminates all streams and socket connection
	 */
//	public static final String QUIT = "/quit";
	
	// ADDITIONAL SERVER ADMIN BEFEHLE
	
	public static final String STOP = "/stop";
	
	public static final String KICK_USER = "/kick ";
	
	public static final List<String> USER_COMMANDS = new ArrayList<>(Arrays.asList(
			SHOW_COMMANDS + " (shows all commands)",
			GOTO_ROOM + "<designated room> (go to designated room)",
			WHISPER + "<user> (whisper to user)",
			UN_WHISPER + " (talk to everyone in the room)",
			USERS + " (shows all users in this room)",
			ADD_ROOM + "<name> (adds the room <name>)",
			QUIT + " (terminates the chat session)",
			ROOMS + " (shows all rooms)"));
	
	public static final boolean messageAllowed(String message) {
		return !(message.startsWith(SERVER_COMMAND_PREFIX)); //|| message.startsWith(RESPONSE) || message.startsWith(FORCE));
	}	
}