package mainClasses;

import java.io.IOException;
import java.util.Date;

import utils.Commands;

abstract class AbstractClientServerThread extends AbstractWriteThread {
	
	protected boolean closed = false;
	
	protected int port;
	
	protected void selfMessage(String message) {
		
		if(!message.equals("")) {
			System.out.println(message);
		}
		
	}

	protected void selfMessage(String... messages) {
		StringBuilder sb = new StringBuilder();
		for (String m : messages) {
			sb.append(m);
		}
		selfMessage(sb.toString());
	}
	
	
	public void close() {
		closed = true;
		
		try {
			Date date = new Date();
			long timecode = date.getTime();
			write(timecode + " "+Commands.QUIT);
			br.close();
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(getClass().getSimpleName() + " closed.");
		
	}
	
	
	
}
