import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;
public class Serverthread implements Runnable {
	Thread t;
	Socket df;
	ArrayList<String> requests;
	DataOutputStream out;

	Serverthread(Socket abc) {
		df = abc;
		t = new Thread(this);
		t.start();
		DateFormat d1f = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date today = Calendar.getInstance().getTime();      
		String reportDate = d1f.format(today);
		WebServer.logger.println(reportDate + ": Connection from: " + df.getRemoteSocketAddress().toString());
	}

	public void send(String tobesent) {
	try {
		out.writeBytes(tobesent + "\n");
		} catch (Exception e) {
			WebServer.logger.println(e.getMessage());
			System.out.println("Some error sending");
		}
	}



	public ArrayList<String> readReq(BufferedReader in) {
		String h = "";
		ArrayList<String> requests;
		requests = new ArrayList<String>();
		while (true) {
			try{
			  	if ((h = in.readLine()) != null) {
			  		System.out.println("Print:" +h);
	      			if (h.equals(""))
	      				break;
	      			requests.add(h);
	      		}
			} catch (Exception e) {
				System.out.println("Request Timed out. 10 sec."); // check here if request has genuinely timed out or what.
				return null;
			}
		}
		return requests;
	}
	public void run() {
		try {
			System.out.println("A connection has been made.");

      	BufferedReader in = new BufferedReader(new InputStreamReader(df.getInputStream()));             
      	out = new DataOutputStream(df.getOutputStream());
      	ArrayList<String> requests = readReq(in);
      	RequestParse req = new RequestParse(requests);
      	System.out.println(req.r_path);
      	System.out.println("muchWOW");
      	WebServer.logger.println("Request processed. replying...");
      	send("HTTP/1.1 200 OK");
		send("Date: Fri, 06 Nov 2014 00:35:42 GMT");
		send("Server: BILAL's Server.");
		send("Content-Length: " + req.length);
		// send("Keep-Alive: timeout=15, max=100");
		send("Connection: close");
		if (req.text)
			send("Content-Type: html"); 
		else if (req.css)
			send("Content-Type: text/css");
		else
			send("Content-Type: text/plain");
		send("");
		out.write(req.file, 0, req.length);
		WebServer.logger.println("sent");
		for (int i = 0; i < WebServer.h.length; i++)
      			if (WebServer.h[i] == this) 
					WebServer.h[i] = null;
		} catch (Exception e) {
			WebServer.logger.println(e.getMessage());
			WebServer.logger.println("Removing from thread pool");
			for (int i = 0; i < WebServer.h.length; i++)
      			if (WebServer.h[i] == this) 
					WebServer.h[i] = null;
			return;


			// out.writeBytes("badjob\n");
		}
   }
}