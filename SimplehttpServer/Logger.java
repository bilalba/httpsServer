import java.io.*;
public class Logger {
	FileWriter out;
	BufferedWriter bufferedWriter;
	public Logger(String file) {
		File log = new File(file);
		try{
			if(!log.exists()){
				log.createNewFile();
			}
			out = new FileWriter(log, true);
			bufferedWriter = new BufferedWriter(out);
		} catch (Exception e) {e.printStackTrace();}
	}
	public void println(String text) {
		try{
			out.write(text + "\n");
			out.flush();
		} catch (Exception e) {System.out.println("ERROR logging!");}
	}
	public void print(String text) {
			try {
				out.write(text);
				out.flush();
			} catch (Exception e) {System.out.println("ERROR logging!");}
	}
}