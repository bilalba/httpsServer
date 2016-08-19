import java.util.*;
import java.io.*;
public class RequestParse {
	String r_path;
	String version;
	byte[] file;
	String dir;
	int length;
	String total_path;
	int start;
	boolean css = false;
	boolean ranged = false;
	boolean text = false;
	boolean fileexists = true;
	int end;
	RequestParse(ArrayList<String> requests) {
		String[] paths = requests.get(0).split(" ");
		if (paths[0].equals("GET"))
			r_path = paths[1];
		for (int i = 1; i < requests.size(); i++) {
			String[] abc = requests.get(i).split(":");
			System.out.println(abc[0]);
			if (abc[0].equals("Range")) {
				System.out.println("hel");
				ranged = true;
				String[] re = abc[1].split("=");
				String[] wq = re[1].split("-");
				start = Integer.parseInt(wq[0]);
				end = Integer.parseInt(wq[1]);
			}
		}
		// look for range here. to make it resume support
		version = paths[2];
		dir = System.getProperty("user.dir");
		readfile();

		String extension = "";
		int i = total_path.lastIndexOf('.');
		if (i > 0) {
		    extension = total_path.substring(i+1);
		}
		if ((extension.equals("html") || (extension.equals("php")) || extension.equals("htm"))) {
			text = true;
		}
		if (extension.equals("css")) {
			css = true;
		}
	}

	// public void convertPath() {
	// 	if path.equals("/") {

	// 	}
	// }
	public void readfile() {
		String path = dir +r_path;
		try {
					path = java.net.URLDecoder.decode(path, "UTF-8");
				}catch(Throwable e){
							e.printStackTrace();}
		if (new File(path).isDirectory()) {
			try {
				path = directoryscene(path);
			} catch(Throwable e) {
				// file = ""; // fill this out with error of missing index
			}
		}
		// System.out.println(path);
		WebServer.logger.println("Get Request:" + path);
		if (new File(path).exists()) {
			WebServer.logger.println("File Found!");
			// Scanner fileh = new Scanner(new File(path));
			try {
				RandomAccessFile f = new RandomAccessFile(new File(path), "r");
				if (ranged) {
					WebServer.logger.println("RANGED!");
					length = end - start;
					byte[] data = new byte[length];
					f.seek(start);
					f.read(data);
					file = data;

				} else{
					long longlength = f.length();
		             length = (int) longlength;
		             if (length > 104857600) {
		            	byte[] data = new byte[104857600];
		            f.read(data);
		        	System.out.println("file reading:" + length);
		        	System.out.println(data);
		            file = data; 	
		             } else{
	     	            byte[] data = new byte[length];
	     	            f.read(data);
	     	        	System.out.println("file reading:" + length);
	     	        	System.out.println(data);
	     	            file = data;
	     	        }
		        }
	        } catch (Exception e) {
	        	fileexists = false;
	        	System.out.println("error reading file");
	        }
		} else WebServer.logger.println("File Not Found!");
		total_path = path;
	}
	public String directoryscene(String directory) throws Throwable{
		String path ="";
		if (new File(directory + "/index.html").exists()) {
			return directory + "/index.html";
		} else if (new File(directory + "/index.php").exists()) {
			return directory + "/index.php";
		} else {
			throw new Throwable();
		}
	}

	public int getLength() {
		return 0;
	}
}