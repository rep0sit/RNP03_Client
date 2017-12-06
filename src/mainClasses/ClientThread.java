package mainClasses;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Date;

import gui.ClientGui;
import utils.Commands;
import utils.Constants;

/**
 * 
 * @author Nelli Welker, Etienne Onasch
 *
 */
public final class ClientThread extends AbstractClientServerThread {

	//private String serverIp;
	private String name;
	private InetAddress inetAddress;
	
	private ClientGui gui = null;
	

	// state
	//private boolean loggedIn = false;
	
	
	private int timeout = Constants.TIMEOUT_5SEC;
	
	public ClientThread(String name, String serverIp, int port) {
		this.name = name;
		//this.serverIp = serverIp;
		this.port = port;
		try {
			this.inetAddress = InetAddress.getByName(serverIp);
		} catch (UnknownHostException e) {
			selfMessage("serverIp couldn't be resolved into a valid internet address.");
			e.printStackTrace();
		}

	}
	
	private void init() {

		try {
			
			socket = new Socket();
			socket.connect(new InetSocketAddress(inetAddress, port), timeout);
			
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			pw = new PrintWriter(socket.getOutputStream());
			
			Date date = new Date();
			long timecode = date.getTime();
			write(timecode+" LOGIN "+name);
		} 
		catch (SocketTimeoutException e) {

			e.printStackTrace();
		}
		
		
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		selfMessage("ClientThread initialized.");
//		String[] cmdAry;
//		try {
//			cmdAry = br.readLine().split(" ");
//			String cmdAryCmd = cmdAry[0]+cmdAry[1];
//			
//			System.out.println("GREETINGS: "+cmdAryCmd);
//			
//			if(cmdAryCmd.equals(Commands.SERVER_GREETINGS)){
//				Date date = new Date();
//				long timecode = date.getTime();
//				write(timecode+" LOGIN "+name);
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	
	public void setGui(ClientGui gui) {
		this.gui = gui;
	}
	

	/**
	 * Writes a server response to the user invoking the message(String message)
	 * method.
	 * 
	 * @param command
	 *            a command from server to client defined in Commands.
	 * @param interpretation
	 *            the interpretation of the command.
	 * @param completeLine
	 *            the complete line the server has sended over the stream. (command
	 *            + optional message)
	 */
	private void selfMessageResponse(String command, String interpretation, String completeLine) {
		StringBuilder sb = new StringBuilder();
		sb.append("Server command: ").append(command);
		if (!interpretation.equals("")) {
			sb.append("(Interpretation = ").append(interpretation).append(")");
		}
		if (completeLine.length() > command.length()) {
			sb.append("[optional server message: ").append(completeLine.substring(command.length())).append("]");
		}
		selfMessage(sb.toString());
	}

	/**
	 * Writes a server response to the user invoking the message(String message)
	 * method.
	 * 
	 * @param command
	 *            a command from server to client defined in Commands.
	 * @param completeLine
	 *            the complete line the server has sended over the stream. (command
	 *            + optional message)
	 */
	private void selfMessageResponse(String command, String completeLine) {
		selfMessageResponse(command, "", completeLine);
	}

	
	@Override
	protected void selfMessage(String message) {
		if(gui != null) {
			gui.console(message);
		}
		super.selfMessage(message);
	}
	
	
	@Override
	public void run() {
		init();
		String currentLine;
		boolean invalidName = false;
		System.out.println("ClientThread is running now.");
		
		String currentTimeCode = "";
		
		try {
			while (!closed && (currentLine = br.readLine()) != null) {
				//neue server commands
				//timecode
				//long timeCode = System.currentTimeMillis();
				Date date = new Date();
				long timecode = date.getTime();
//				String stringTimeCode = String.valueOf(timecode);
//				long timecode = Long.parseLong(date.toString());
				String stringTimeCode = String.valueOf(timecode);
				
				String [] lineAry = currentLine.split(" ");
				int lineAryLen = lineAry.length;
				
				// server commands
				if(lineAry[0].equals(Commands.SERVER_PREFIX)) {

//					System.out.println("stringTimecode "+stringTimeCode);
					
//					if(lineAry[1].equals(Commands.GREETINGS)) {
//						selfMessage(buildMessage(3, lineAry));
//						write(timecode + Commands.LOGIN + name);
//						currentTimeCode = stringTimeCode;
//					}
//					else 
					if(lineAry[1].equals(Commands.SEND)) {
						selfMessage("Server: "+buildMessage(3, lineAry));
					}
					else if(lineAry[1].equals(Commands.DELETE)) {
						selfMessage("Server: Der Raum " + buildMessage(3, lineAry) + " wurde geschlossen.");
					}
				}
				else if(lineAry[0].equals(Commands.QUIT)) {
					selfMessage("Server: "+buildMessage(2, lineAry) +"(You were kicked from Server!)");
					close();
					gui.dispose();
					break;
				}	
				else if(lineAry[0].matches("[0-9]+")) {
					if(lineAry[1].equals(Commands.LOGIN)) {
						
						if(lineAry[2].equals(Commands.SUCCESS)){
							selfMessage("Server: "+buildMessage(4, lineAry) + "(Login war erfolgreich.)");
						}else if(lineAry[2].equals(Commands.LOGIN_FAIL)){
							selfMessage("Server: (Login war nicht erfolgreich.)");
						}
					}
					else if(lineAry[1].equals(Commands.USERS)) {
						selfMessage("Server: "+currentLine);
					}else if(lineAry[1].equals(Commands.LIST)){
						selfMessage("Server: Liste der Raeume-> "+currentLine);
					}else if(lineAry[1].equals(Commands.GET)){
						selfMessage("Server: "+currentLine);
					}else if((lineAry[1]+" "+lineAry[2]).equals(Commands.CREATE_SUCCESS)){
						selfMessage("Server: "+currentLine);
					}else if((lineAry[1]+" "+lineAry[2]).equals(Commands.CREATE_FAIL)){
						selfMessage("Server: "+currentLine+" Es konnte kein neuer Chatraum erstellt werden.");
					}else if((lineAry[1]+" "+lineAry[2]).equals(Commands.JOIN_SUCCESS)){
						selfMessage("Server: "+currentLine+" Betreten des neuen Chatraums war erfolgreich.");
					}else if((lineAry[1]+" "+lineAry[2]).equals(Commands.JOIN_FAIL)){
						selfMessage("Server: "+currentLine+ " Der neue Chatraum konnte nicht betreten werden.");
					}else if((lineAry[1]+" "+lineAry[2]).equals(Commands.LEAVE_SUCCESS)){
						selfMessage("Server: "+currentLine+ " Chatraum konnte erfolgreich verlassen werden.");
					}else if((lineAry[1]+" "+lineAry[2]).equals(Commands.LEAVE_FAIL)){
						selfMessage("Server: "+currentLine+ " Der neue Chatraum konnte nicht verlassen werden.");
					}
				}
				
//				// server commands
//				if (currentLine.equals(Commands.GIVE_USERNAME)) {
//					write(name);
//				}
//				else if(currentLine.startsWith(Commands.FORCE_DISCONNECT)) {
//					//selfMessage("You were kicked from server.");
//					selfMessageResponse(Commands.FORCE_DISCONNECT, "kicked from server", currentLine);
//					close();
//					gui.dispose();
//					break;
//				}
//
//				// negative server responses
//				else if (currentLine.startsWith(Commands.USERNAME_TAKEN)) {
//					selfMessageResponse(Commands.USERNAME_TAKEN, "username already taken", currentLine);
//					invalidName = true;
//				} 
//				else if (currentLine.startsWith(Commands.INVALID_USERNAME)) {
//					selfMessageResponse(Commands.INVALID_USERNAME, "invalid username", currentLine);
//					invalidName = true;
//				} 
//				else if (currentLine.startsWith(Commands.SERVER_FULL)) {
//					selfMessageResponse(Commands.SERVER_FULL, "server already full", currentLine);
//					close();
//					gui.dispose();
//					break;
//				}
//
//				// positive server responses
//				else if (currentLine.startsWith(Commands.LOGGED_IN)) {
//					//loggedIn = true;
//					selfMessageResponse(Commands.LOGGED_IN, "you are logged in now", currentLine);
//				}
//
//				else if (currentLine.startsWith(Commands.VALID_USERNAME)) {
//					selfMessageResponse(Commands.VALID_USERNAME,"username was valid", currentLine);
//					invalidName = false;
//				} 
//				else if (currentLine.startsWith(Commands.IN_ROOM)) {
//					selfMessageResponse(Commands.IN_ROOM, currentLine);
//				}
//
				else {
					selfMessage(currentLine);
				}
//
//				if (invalidName) {
//					selfMessage("--> Enter new username.");
//				}

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			
			System.out.println("Server does not respond anymore!");
		}
	}
	
	private String buildMessage(int minLen, String[] ary) {
		StringBuilder sb = new StringBuilder();
			
			
			
		if(ary.length >= minLen) {
			for(int i = minLen - 1; i < ary.length; i++) {
				sb.append(ary[i]).append(" ");
			}
		}
		return sb.toString();
	}

	public static void main(String...args) throws IOException {
		if(args.length != 3) {
			System.out.println("3 arguments needed! <username>, <server ip> and <server port>.");
		}else {
			String username = args[0];
			String ip = args[1];
			String port = args[2];
			
			ClientThread ct = new ClientThread(username, ip, Integer.parseInt(port));
			ct.start();
			
		}
		
	}

	public void msgFromClient(String message) {

		Date date = new Date();
		long timecode = date.getTime();
		
		//Vom client ankommende, im Chat eingegebene Nachricht, gesplittet nach Leerzeichen
		String[] splitMsg = message.split(" ");
		
		if(splitMsg.length>0){
			System.out.println("MESSAGE FROM CLIENT: "+message);
			if(splitMsg[0].equals(Commands.USERS) || splitMsg[0].equals(Commands.LIST) 
					|| splitMsg[0].equals(Commands.LEAVE) || splitMsg[0].equals(Commands.QUIT)){
				write(timecode+" "+message);
			}else if(splitMsg[0].equals(Commands.GET)){
				write(timecode+" "+Commands.GET+" "+buildMessage(2,splitMsg));
			}else if(splitMsg[0].equals(Commands.CREATE_ROOM)){
				write(timecode+" "+Commands.CREATE_ROOM+" "+buildMessage(2, splitMsg));
			}else if(splitMsg[0].equals(Commands.JOIN)){
				write(timecode+" "+Commands.JOIN+" "+buildMessage(2, splitMsg));
			}else{
				write(timecode + " "+Commands.SEND+" "+message);
			}
			
//			switch(splitMsg[0]){
//				case Commands.USERS: write(timecode+" "+message);
//					break;
//				case Commands.LIST : write(timecode+" "+message);
//					break;
//				case Commands.LEAVE : write(timecode+" "+message);
//					break;
//				case Commands.QUIT : write(timecode+" "+message);
//					break;
//				case Commands.GET : write(timecode+" GET "+buildMessage(2,splitMsg));
//					break;
//				case Commands.CREATE_ROOM: write(timecode+" CREATE "+buildMessage(2, splitMsg));
//					break;
//				case Commands.JOIN: write(timecode+" JOIN "+buildMessage(2, splitMsg));
//					break;
//				default: write(timecode + " SEND "+message);
//					break;
//			}
		}
	}
	public String getUserName(){
		return name;
	}
	
}
