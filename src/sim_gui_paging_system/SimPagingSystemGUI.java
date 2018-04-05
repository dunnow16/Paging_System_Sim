package sim_gui_paging_system;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.LinkedList;
import javax.swing.*;
import sim_paging_system.*; /* contains simulation logic */
import sim_paging_system.Process;

/**
 * This class uses the sim_paging_system package to display a simulated
 * paging system with a GUI.
 * @author Owen Dunn
 */
public class SimPagingSystemGUI extends JPanel {
	/** The logic of the simulation. */
	private SimPagingSystem sim;
	/** top-level frame object */
	private JFrame frame;
	/** panels for frame table, page table(s), and button */
	private JPanel frameTablePanel, pageTablePanel, pagePanel, 
		buttonPanel;
	/** Grid of JLabels for frame contents */
	private JLabel[][] frameLabel;
	/** Grid of JLabels for page contents */
	private JLabel[][] pageLabel;
	/** JButtons for some game actions: next */
	private JButton nextButton;
	/** listener for JButton events */
	private Listener listener;
	/** file input stream object */
	private FileInputStream fstream;
	/** data input stream object */
	private DataInputStream in;
	/** buffered reader object */
	private BufferedReader br;
	
	public SimPagingSystemGUI() {
		// Create the simulation object.
		sim = new SimPagingSystem();
		
		// create listener for keyboard and button events
        listener = new Listener();
		
		// Set up file input.
		try {
		fstream = new FileInputStream("input3b.data");
		in = new DataInputStream(fstream);
		br = new BufferedReader(new InputStreamReader(in));
		} 
		catch (Exception e) {
		System.err.println("Error: " + e.getMessage());
		}
		
		// Construct the GUI:
		renderFrame();
		renderFrameTable();
		renderButtons();
		//renderPageTable();
		
	}
	
	/*****************************************************************
     * Uses JFrame and BorderLayout to create the top level for the 
     * GUI. The North, East, South, West, and Center frames are left
     * to be used for different panels.
     *
     * @param none
     * @return none
     ****************************************************************/
	private void renderFrame() {
		// create top-level frame object
		frame = new JFrame("Simulated Paging System");
		frame.setLayout(new BorderLayout());
		//frame.setBackground(Color.lightGray); 
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(800,800));
		frame.pack();
		frame.setVisible(true);
	}
	
	private void renderFrameTable() {
		int rows = 9, cols = 2;
		
		if(frameTablePanel == null) {
			// Create panel to hold frames.
			frameTablePanel = new JPanel();
			frameTablePanel.setOpaque(true);
			frameTablePanel.setLayout(new GridLayout(rows, cols));
			// Create a table of frames.
			frameLabel = new JLabel[9][2];
			
			// initialize all JLabels (update values later)
			for(int i = 0; i < rows; ++i) {
				for(int j = 0; j < cols; ++j) {
					frameLabel[i][j] = 
							new JLabel(" ", SwingConstants.CENTER);
					frameLabel[i][j].setFont
						(frameLabel[i][j].getFont().deriveFont
							(Font.PLAIN, 30));
					if(j == 1 && i != 0) {
						frameLabel[i][j].setBorder
							(BorderFactory.createRaisedBevelBorder());
					}
					else if(j == 0 && i != 0) {
						frameLabel[i][j].setText("Frame " + (i-1));
					}
					else if(i == 0 && j == 1) {
						frameLabel[i][j].setText("Physical M");
						frameLabel[i][j].setFont
						(frameLabel[i][j].getFont().deriveFont
								(Font.BOLD, 40));
					}
					frameTablePanel.add(frameLabel[i][j]);
					frameTablePanel.setVisible(true);
				}
			}
			// add to center panel of BorderLayout
			frame.add(frameTablePanel, BorderLayout.CENTER);
		}
		else { // Update frame table.
			// done within actionPerformed()
		}
		
		frame.pack(); // display doesn't update well without
	}
	
	private void renderPageTable() {
		int rows, cols = 3, panels = sim.getProcessList().size();
		Process p;
		
		if(pageTablePanel == null) {
			pageTablePanel = new JPanel();
			pageTablePanel.setLayout(new GridLayout(panels, 1));
			// Create a panel for each page table.
			for(int i = 0; i < panels; ++i) {
				p = sim.getProcessList().get(i);
				rows = p.getPcb().getNumTextFrames() + 
						p.getPcb().getNumDataFrames() + 1;
				pagePanel = new JPanel();
				pagePanel.setLayout(new GridLayout(rows, cols));
				pageLabel = new JLabel[rows][cols];
				
				for(int j = 0; j < rows; ++j) {
					for(int k = 0; k < cols; ++k) {
						pageLabel[j][k] = new JLabel();
						if(j == 0 && k == 0) {
							pageLabel[j][k].setText("Process " + 
												  p.getPcb().getPid());
							pagePanel.add(pageLabel[j][k]);
						}
						else if(j == 0 && k == 1) {
							pageLabel[j][k].setText("Page");
							pagePanel.add(pageLabel[j][k]);
						}
						else if(j == 0 && k == 2) {
							pageLabel[j][k].setText("Frame");
							pagePanel.add(pageLabel[j][k]);
						}
						else if(j == 1 && k == 0) {
							pageLabel[j][k].setText("Text");
							pagePanel.add(pageLabel[j][k]);
						}
						else if(j == 1 + p.getPcb().getNumTextFrames()
								&& k == 0) {
							pageLabel[j][k].setText("Data");
							pagePanel.add(pageLabel[j][k]);
						}
						else if(j < 1 + p.getPcb().getNumTextFrames()
								&& k == 2) {
							pageLabel[j][k].setText
								("" + p.getPcb().getPageTableT()[j-1]);
							pagePanel.add(pageLabel[j][k]);
						}
						else if(j < 1 + p.getPcb().getNumTextFrames()
								&& k == 1) {
							pageLabel[j][k].setText
								("" + (j-1));
							pagePanel.add(pageLabel[j][k]);
						}
						else if(j == 1 + p.getPcb().getNumTextFrames()
								&& k == 1) {
							pageLabel[j][k].setText
								("" + (j - p.getPcb().getNumTextFrames() - 1));
							pagePanel.add(pageLabel[j][k]);
							
							// Now add the rest of the data entries.
							for(int n = j+1; n < rows ; ++n) {
								pageLabel[n][2] = new JLabel();
								pageLabel[n][2].setText
									("" + p.getPcb().getPageTableD()[n - 
	                                 p.getPcb().getNumTextFrames()-1]);
								pagePanel.add(pageLabel[n][2]);
							}
						}					
					}
				}
				
				pageTablePanel.add(pagePanel);
			}
		}
		else { // create a new one and update it
			frame.remove(pageTablePanel);
			// TODO add all process page tables to frame
			
		}
		
		// add to center panel of BorderLayout
		frame.add(pageTablePanel, BorderLayout.WEST);
		frame.pack();
	}
	
	private void renderButtons() {
		// create panel for all buttons
		buttonPanel = new JPanel();
		
		// button for next event.
		nextButton = new JButton("Next");
		nextButton.addActionListener(listener);
		buttonPanel.add(nextButton);
		
		frame.add(buttonPanel, BorderLayout.SOUTH);
		frame.pack();
	}
	
	/*****************************************************************
	 * This represents a listener for all GUI button pressing events.
	 ****************************************************************/
	private class Listener implements ActionListener {

		/*************************************************************
		 * This method activates whenever a button on the GUI is 
		 * pressed.
		 ************************************************************/
		public void actionPerformed(ActionEvent ev) {
			// Process an event.
			if(ev.getSource() == nextButton) {
				String strLine = "";  // holds event string
				String[] tokens = {""}; // holds string line tokens
				// Values of process ID, text size, and data size.
				int pid = 0, TextSize = 0, DataSize = 0;
				// A process being loaded into memory.
				Process p;
				
				// Read an event line.
				try {
				if ((strLine = br.readLine()) != null) {
					frameLabel[0][0].setText(strLine);
					frameLabel[0][0].repaint();
					
					// Process the event string into an array of strings.
					tokens = strLine.split(" ");
					// The first token is the process ID.
					pid = Integer.parseInt(tokens[0]);
					
					// If the token is "Halt", then end process
					// and free all of its memory.
					if (tokens[1].compareTo("Halt") == 0) {
						System.out.println("Ending process " + pid + ".");
						// Find if the id is within the process list. Remove
						// that process from the list if it is found after 
						// deallocating all of its physical memory.
						for(Process pr : sim.getProcessList()) {
							if( pr.getPcb().getPid() == pid ) {
								// free frames used
								pr.free(sim.getFreeFrames(), 
										sim.getFrameTable());
								// remove process
								sim.getProcessList().remove(pr); 
								break;
							}
						}
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
										TextSize, DataSize, 
										sim.getFreeFrames(),
										sim.getFrameTable());
						// Add the new process to the list of processes.
						sim.getProcessList().add(p);
					}
					// Print all the page tables for all processes.
					System.out.println("Page table(s)");
					for(Process pr : sim.getProcessList())
						sim.printProcessPageTables(pr);
					// Print within GUI.
					renderPageTable();
					
					// Print the physical memory frame page table.
					sim.printMemory(sim.getProcessList());
					// Print within GUI.
					String[] frameTable = sim.getFrameTable();
					for(int i = 1; i < 9; ++i) {
						frameLabel[i][1].setText(frameTable[i-1]);
					}
				}
				else { // EOF
					frameLabel[0][0].setText("End of data");
				}
				frame.pack();
				}
				catch (Exception e2) {
				System.err.println("Error: " + e2.getMessage());
				}
				
				
			}
			
		}
		
	}
	
	public static void main(String[] arg) {
		// Create the GUI object.
		SimPagingSystemGUI g = new SimPagingSystemGUI();
	}
}
