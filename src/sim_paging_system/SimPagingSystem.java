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
	/** the processes currently in memory */
	private LinkedList<Process> processList;
	
	/**
	 * Constructor: Create a SimPagingSystem object. This object can
	 * be used to simulate a paging system. 
	 */
	public SimPagingSystem() {
		int i = 0;
		freeFrames = new LinkedList<Integer>();
		processList = new LinkedList<Process>();
		frameTable = new String[totalFrames];
		for(i = 0; i < totalFrames; ++i) {
			frameTable[i] = " ";
			freeFrames.add(new Integer(i));
		}
//		for(Integer f : freeFrames) {
//			System.out.println(f);
//		}
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
	 * This method prints the page tables for the text and data 
	 * segments.
	 * @param p process that has its page tables printed
	 * @return none
	 */
	public void printProcessPageTables(Process p) {
		// Retrieve the text and data page tables from the process' PCB.
		int[] pageT = p.getPcb().getPageTableT();
		int[] pageD = p.getPcb().getPageTableD();
		
		// Print the two page tables of the process.
		System.out.println("Process " + p.getPcb().getPid() + ":");
		System.out.println("\tPage\tFrame");
		for(int i = 0; i < pageT.length; ++i) {
			if(i == 0)
				System.out.println("Text\t  " + i + "\t  " + pageT[i]);
			else
				System.out.println("\t  " + i + "\t  " + pageT[i]);
		}
		for(int i = 0; i < pageD.length; ++i) {
			if(i == 0)
				System.out.println("Data\t  " + i + "\t  " + pageD[i]);
			else
				System.out.println("\t  " + i + "\t  " + pageD[i]);
		}
	}
	
	/**
	 * The main method hosts the top level of the operating system
	 * where the loader and memory management systems are called from.
	 * @param args
	 */
	public static void main(String[] args) { 
		System.out.println("Simulated Paging System\n");
		// Create a simulation object to run the simulation.
		SimPagingSystem sim = new SimPagingSystem();
		
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
		// A process being loaded into memory.
		Process p;
		
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
				// memory and mappings from the loader.
				System.out.println("Loading process " + pid + ":" +
						" TextSize = " + TextSize + ", DataSize = " +
						DataSize + ".");
				p = new Process(pid, SimPagingSystem.pageSize, 
								TextSize, DataSize, sim.freeFrames);
				// Add the new process to the list of processes.
				sim.processList.add(p);
				
				// Print all the page tables for all processes.
				System.out.println("Page table(s)");
				for(Process pr : sim.processList)
					sim.printProcessPageTables(pr);
				
				// Print the physical memory frame page table.
				
				
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
