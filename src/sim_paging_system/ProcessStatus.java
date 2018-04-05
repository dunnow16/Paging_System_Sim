package sim_paging_system;

/**
 * Enum type: defines all the status types for a process.
 * @author Owen Dunn
 */
public enum ProcessStatus {
	WAIT,  /* Not enough memory available for process. */
	RUN    /* Enough memory available for process.	   */
}
