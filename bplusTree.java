import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class bplusTree {

	public static void main(String[] args) {
		TreeOperations obj=new TreeOperations();
		try {
			// This readInputFile method is used to read all the lines present in input text file,
			// and making them to the string list
			List<String> fileRead=readInputFile(args[0]);
			// First string of fileRead containing Initialize key work, making a call to Initialize function
			// with the integer read from the string
			if(fileRead.get(0).contains("Initialize")) {
				// File pointer for output file
				File outputFile=new File("output_file.txt");
				// Creating the output file
				outputFile.createNewFile();
				// writeToFile being the bufferedwriter to the output file
				BufferedWriter writeToFile=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
				String read=fileRead.get(0);
				int degree=Integer.parseInt(read.substring(read.indexOf("(")+1, read.indexOf(")")));
				obj.Initialize(degree);
				// Looping through the fileRead list and making calls to the respective operations
				for(int i=1; i<fileRead.size(); i++) {
					read=fileRead.get(i);
					// If read contains Insert subString, making call to Insert operation parameterized with
					// key and value taken from string
					if(read.indexOf("Insert")!=-1) {
						obj.Insert(Integer.parseInt(read.substring(read.indexOf("(")+1, read.indexOf(",")).trim()), Double.parseDouble(read.substring(read.indexOf(",")+1, read.indexOf(")")).trim()));
					}
					// If read contains Search and "," as subString, making call to Search for values in a range of keys
					// operation parameterized with key1 and key2 fetched from string
					else if(read.indexOf("Search")!=-1&&read.indexOf(",")!=-1) {
						String toAdd=obj.Search(Integer.parseInt(read.substring(read.indexOf("(")+1, read.indexOf(",")).trim()), Integer.parseInt(read.substring(read.indexOf(",")+1, read.indexOf(")")).trim()));
						if(toAdd.equals("")) writeToFile.write("Null");
						else writeToFile.write(toAdd.substring(0, toAdd.length()-1));
						writeToFile.newLine();
					}
					// Else If read contains Search as subString, making a call to search operation parameterized 
					// with key read from the string
					else if(read.indexOf("Search")!=-1) {
						String toAdd=obj.Search(Integer.parseInt(read.substring(read.indexOf("(")+1, read.indexOf(")")).trim()));
						if(toAdd.equals("")) writeToFile.write("Null");
						else writeToFile.write(toAdd);
						writeToFile.newLine();
					}
					// If read contains Delete as subString, making a call to Delete operation parameterized with
					// key read form the string
					else if(read.indexOf("Delete")!=-1) {
						obj.Delete(Integer.parseInt(read.substring(read.indexOf("(")+1, read.indexOf(")")).trim()));
					}
					// Invalid operation case
					else System.out.println("Invalid Operatin");
				}
				
				// Closing the writeToFile buffer object
				writeToFile.close();
			}
			else {
				System.out.println("Improper Initialization");
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	// Method to read input text file and returning the string lines present in it as list of strings
	public static List<String> readInputFile(String ipFile) {
		// List of strings need to be returned
		List<String> result=new ArrayList<>();
		try {
			// FileInputStream object for input text file
			FileInputStream inputStream=new FileInputStream(new File(ipFile));
			// BufferedReader object for inputStreamReader
			BufferedReader inputBuffer=new BufferedReader(new InputStreamReader(inputStream));
			// Start reading the lines from inputBuffer
			String read=inputBuffer.readLine();
			while(read!=null) {
				result.add(read);
				read=inputBuffer.readLine();
			}
			// On successful reading the whole file close the buffer read object
			inputBuffer.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
