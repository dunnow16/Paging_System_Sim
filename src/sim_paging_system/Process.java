package sim_paging_system;

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
	/**  */
	
	public Process() {
		pcb = new Pcb();
	}
	
}
