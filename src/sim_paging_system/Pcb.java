package sim_paging_system;

import java.util.LinkedList;

/**
 * This class represents the process control block (PBC) object that is
 * a part of every process. It contains information of a process such
 * as a page table, the length of the page table, and the length of
 * individual segments of text and data.
 * @author Owen Dunn
 */
public class Pcb {
	/** the process identification number */
	private int pid;
	/** the size of a page */
	private int pageSize;
	/** the total number of text frames for the process */
	private int numTextFrames;
	/** the total number of data frames for the process */
	private int numDataFrames;
	/** the page table for text (pointer to usually in practice) */
	private int[] pageTableT;
	/** the page table for data (pointer to usually in practice) */
	private int[] pageTableD;
	/** the status of the process */
	private ProcessStatus status;
	
	/**
	 * Constructor: Create a new PBC for a process.
	 * @param pid the process id
	 * @param tSize the text size in bytes
	 * @param dSize the data size in bytes
	 */
	public Pcb(int pid, int pageSize, int tSize, int dSize, 
			   LinkedList<Integer> freeFrames, String[] frameTable) {
		this.pid = pid;
		this.pageSize = pageSize;
		numTextFrames = (int)Math.ceil((float)tSize / pageSize);
		numDataFrames = (int)Math.ceil((float)dSize / pageSize);
		
		// Create a page table for the process.
		createPageTable(numTextFrames, numDataFrames, freeFrames, 
					    frameTable);
	}
	
	/**
	 * This method creates a page table for a process. The page table
	 * gives the mappings from the processes pages to frames of
	 * physical memory. The mapping is done for the text and data
	 * sections separately. The variables 'numTextFrames' and 
	 * 'numDataFrames' are used to know the size of each page table.
	 * A list of free frames kept by the operating system (Sim class)
	 * is used to find free frames to map pages. The frame table is 
	 * updated as the page tables are updated.
	 * @param numTextFrames
	 * @param numDataFrames
	 */
	private void createPageTable(int numTextFrames, int numDataFrames,
								 LinkedList<Integer> freeFrames,
								 String[] frameTable) {
		pageTableT = new int[numTextFrames];
		pageTableD = new int[numDataFrames];
		
		// Check if enough memory space is available.
		// TODO add process wait list for full memory cases
		if(freeFrames.size() < numTextFrames + numDataFrames) {
			System.out.println("Not enough frames available for process " 
							   + pid);
			status = ProcessStatus.WAIT;
			return;
		}
		else
			status = ProcessStatus.RUN;
		
		// Create the text page table.
		for(int i = 0; i < numTextFrames; ++i) {
			pageTableT[i] = freeFrames.removeFirst();
			frameTable[pageTableT[i]] = "P" + pid + " Text Page " + i;
		}
		
		// Create the data page table.
		for(int i = 0; i < numDataFrames; ++i) {
			pageTableD[i] = freeFrames.removeFirst();
			frameTable[pageTableD[i]] = "P" + pid + " Data Page " + i;
		}
	}
	
	public int getPid() {
		return pid;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getNumTextFrames() {
		return numTextFrames;
	}

	public int getNumDataFrames() {
		return numDataFrames;
	}

	public int[] getPageTableT() {
		return pageTableT;
	}
	
	public int[] getPageTableD() {
		return pageTableD;
	}
}
