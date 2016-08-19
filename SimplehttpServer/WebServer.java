import java.io.*;
import java.net.*;

public class WebServer {
	static Logger logger = new Logger("webserv.txt");
	static Serverthread[] h = new Serverthread[10]; // thread pool.
	static ServerSocket welcomeSocket;
	public static void main(String[] args) throws Exception {
		if (args.length == 0)
			welcomeSocket = new ServerSocket(80);
		else
			welcomeSocket = new ServerSocket(Integer.parseInt(args[0]));
		logger.println("");
		logger.println("");
		logger.println("Server started.");
		logger.println("");
		logger.println("");
		while (true) {
			System.out.println("t");
      		Socket socks = welcomeSocket.accept();

      		socks.setSoTimeout(10*1000); // timeout seconds.
      		for (int i = 0; i < h.length; i++) {
      			if (h[i] == null) {
      				Serverthread h = new Serverthread(socks);	
      				break;	
      			}
      		}
      		
      	}
	}
}