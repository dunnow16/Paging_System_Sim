package sim_paging_system;

import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * @author Owen Dunn 
 * Program 3, CIS 452, 4/5/18
 * 
 * Simulated Paging System:
 * This application simulates the memory management system and loader 
 * of an operating system. A text and GUI display of the results for 
 * the page table and page frame table are made after each event. A 
 * next button allows the user to move to the next event. This program 
 * is designed to help you understand how these systems work.
 */
public class SimPagingSystem {
	/** the size of a page or frame in bytes */
	private final static int pageSize = 512;
	/** the total amount of physical memory */
	private final static int totalM = 4096; 
	/** the number of frames in the system */
	private final static int totalFrames = totalM / pageSize;
	/** frame table for the system */
	private String[] frameTable;
	/** linked list of free frames in the operating system. */
	private LinkedList<Integer> freeFrames;
	
	public SimPagingSystem() {
		frameTable = new String[totalFrames];
		for(int i = 0; i < totalFrames; ++i) {
			frameTable[i] = " ";
			freeFrames.add(i);
		}
		
	}
	
	/**
	 * This method is used to run the top level of the simulation from 
	 * the separate GUI class.
	 * @param
	 * @return
	 */
	public void sim() {
		
	}
	
	/**
	 * The main method hosts the top level of the operating system
	 * where the loader and memory management systems are called from.
	 * @param args
	 */
	public static void main(String[] args) { 
		System.out.println("Simulated Paging System\n");
		
		// Read the file line by line for events. Process each event
		// in the simulation until the user presses the 'next' button
		// or hits the space bar.
		try {
		// TODO May prompt for file name here.
		// TODO ADD GUI in addition or to replace command line input/display
		// Hard code a new file name if using a different one.
		FileInputStream fstream = new FileInputStream("input3a.data");
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = 
				new BufferedReader(new InputStreamReader(in));
		String strLine = "";  // holds event string
		String[] tokens = {""}; // holds string line tokens
		Scanner s = new Scanner(System.in);
		// Values of process ID, text size, and data size.
		int pid = 0, TextSize = 0, DataSize = 0;
		
		// Read the event file line by line.
		while ((strLine = br.readLine()) != null) {
			// Wait for enter key.
			System.out.println("Press enter to continue.");
			s.nextLine();
			// Print the current event.
			System.out.println(strLine);
			
			// Process the event string into an array of strings.
			tokens = strLine.split(" ");
			
			// The first token is the process ID.
			pid = Integer.parseInt(tokens[0]);
			
			// If the token is "Halt", then end process
			// and free all of its memory.
			if (tokens[1].compareTo("Halt") == 0) {
				System.out.println("Ending process " + pid + ".");
			}
			// If the second token is a number, then use as 
			// TextSize and then get DataSize if available.
			else { // TODO error check
				TextSize = Integer.parseInt(tokens[1]);
				DataSize = Integer.parseInt(tokens[2]);
				// Simulate the creation of a process with allocated
				// memory from the loader.
				System.out.println("Loading process " + pid + ":" +
						" TextSize = " + TextSize + ", DataSize = " +
						DataSize + ".");
			}
		}
		
		System.out.println("All memory events have been processed.");
		System.out.println("Press enter to exit.");
		s.nextLine();
		// Close the input streams.
		in.close();
		s.close();
		} 
		catch (Exception e) {
		System.err.println("Error: " + e.getMessage());
		}
	}
}
