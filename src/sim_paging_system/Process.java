package sim_paging_system;

import java.util.LinkedList;

/**
 * This class represents a process object. As processes are created
 * and halted by the loader and memory management simulation, this
 * class is used. A process contains process related data structures,
 * including a process control board (PCB). 
 * @author Owen Dunn
 */

public class Process {
	/** the process control block */
	private Pcb pcb; 
	
	/**
	 * Constructor: Create a process object. Processes are created from
	 * simulated executable files.
	 * @param pid
	 * @param pageSize
	 * @param tSize
	 * @param dSize
	 * @param freeFrames
	 */
	public Process(int pid, int pageSize, int tSize, int dSize,
				   LinkedList<Integer> freeFrames, String[] frameTable) {
		pcb = new Pcb(pid, pageSize, tSize, dSize, freeFrames, frameTable);
	}
	
	/**
	 * This method releases all memory allocated to a process. The
	 * frames previously used in physical memory by the process are 
	 * added to the free-frame list. 
	 * @param freeFrames
	 */
	public void free(LinkedList<Integer> freeFrames, 
			         String[] frameTable) {
		int[] pageT = pcb.getPageTableT();
		int[] pageD = pcb.getPageTableD();
		
		for(int pg : pageT) {
			freeFrames.add(new Integer(pg));
			// Clear the printable frame table entry.
			frameTable[pg] = " ";  
		}
		for(int pg : pageD) {
			freeFrames.add(new Integer(pg));
			frameTable[pg] = " ";
		}
	}
	
	public Pcb getPcb() {
		return pcb;
	}
}
