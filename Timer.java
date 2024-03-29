package CA2;

import javax.swing.*;
import java.awt.*;
import java.awt.Dialog.ModalityType;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

import net.miginfocom.swing.MigLayout;

public class Timer extends JFrame {
	
	// Interface components
	
	// Fonts to be used
	Font countdownFont = new Font("Arial", Font.BOLD, 20);
	Font elapsedFont = new Font("Arial", Font.PLAIN, 14);
	
	// Labels and text fields
	JLabel countdownLabel = new JLabel("Seconds remaining:");
	JTextField countdownField = new JTextField(15);
	JLabel elapsedLabel = new JLabel("Time running:");
	JTextField elapsedField = new JTextField(15);
	JButton startButton = new JButton("START");
	JButton pauseButton = new JButton("PAUSE");
	JButton stopButton = new JButton("STOP");
	
	 // The text area and the scroll pane in which it resides
	JTextArea display;
	
	JScrollPane myPane;
	
	// These represent the menus
	JMenuItem saveData = new JMenuItem("Save data", KeyEvent.VK_S);
	JMenuItem displayData = new JMenuItem("Display data", KeyEvent.VK_D);
	
	JMenu options = new JMenu("Options");
	
	JMenuBar menuBar = new JMenuBar();
	
	// These booleans are used to indicate whether the START button has been clicked
	boolean started;
	
	// and the state of the timer (paused or running)
	boolean paused;
	
	// Number of seconds
	long totalSeconds = 0;
	static long secondsToRun = 0;
	long secondsSinceStart = 0;
	
	// This is the thread that performs the countdown and can be started, paused and stopped
	TimerThread countdownThread;
	Thread thread;
	
	JFileChooser jfc;

	// Interface constructed
	Timer() {
		
		jfc = new JFileChooser();
		jfc.setCurrentDirectory(new File(System.getProperty("user.dir")));
		
		setTitle("Timer Application");
		
    	MigLayout layout = new MigLayout("fillx");
    	JPanel panel = new JPanel(layout);
    	getContentPane().add(panel);
    	
    	options.add(saveData);
    	options.add(displayData);
    	menuBar.add(options);
    	
    	panel.add(menuBar, "spanx, north, wrap");
    	
    	MigLayout centralLayout = new MigLayout("fillx");
    	
    	JPanel centralPanel = new JPanel(centralLayout);
    	
    	GridLayout timeLayout = new GridLayout(2,2);
    	
    	JPanel timePanel = new JPanel(timeLayout);
    	
    	countdownField.setEditable(false);
    	countdownField.setHorizontalAlignment(JTextField.CENTER);
    	countdownField.setFont(countdownFont);
    	countdownField.setText("00:00:00");
    	
    	timePanel.add(countdownLabel);
    	timePanel.add(countdownField);

    	elapsedField.setEditable(false);
    	elapsedField.setHorizontalAlignment(JTextField.CENTER);
    	elapsedField.setFont(elapsedFont);
    	elapsedField.setText("00:00:00");
    	
    	timePanel.add(elapsedLabel);
    	timePanel.add(elapsedField);

    	centralPanel.add(timePanel, "wrap");
    	
    	GridLayout buttonLayout = new GridLayout(1, 3);
    	
    	JPanel buttonPanel = new JPanel(buttonLayout);
    	
    	buttonPanel.add(startButton);
    	buttonPanel.add(pauseButton, "");
    	buttonPanel.add(stopButton, "");
    	
    	centralPanel.add(buttonPanel, "spanx, growx, wrap");
    	
    	panel.add(centralPanel, "wrap");
    	
    	display = new JTextArea(100,150);
        display.setMargin(new Insets(5,5,5,5));
        display.setEditable(false);
        
        JScrollPane myPane = new JScrollPane(display, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        panel.add(myPane, "alignybottom, h 100:320, wrap");
        
        
        // Initial state of system
        paused = false;
        started = false;
        
        // Allowing interface to be displayed
    	setSize(400, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        // TODO: SAVE: This method should allow the user to specify a file name to which to save the contents of the text area using a 
        // JFileChooser. You should check to see that the file does not already exist in the system.
        saveData.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(display.getText()==null){
					JOptionPane.showMessageDialog(Timer.this, "There is no text");
				}
				else{
					int returnVal = jfc.showSaveDialog(Timer.this);
					
					if(returnVal == JFileChooser.APPROVE_OPTION){
						File fileSelected = jfc.getSelectedFile();
						
						System.out.println(fileSelected.getAbsolutePath());
						try {
							writeDataFile(fileSelected);
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
			}
		});
        // TODO: DISPLAY DATa: This method should retrieve the contents of a file representing a previous report using a JFileChooser.
        // The result should be displayed as the contents of a dialog object.
        displayData.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				jfc = new JFileChooser();
				//set working folder 
				jfc.setCurrentDirectory(new File(System.getProperty("user.dir")));
				String s = "";
				
				int returnVal = jfc.showOpenDialog(Timer.this);
				   if (returnVal == JFileChooser.APPROVE_OPTION) { 
				     File file = jfc.getSelectedFile(); 
				     //This is where a real application would open the file. 
				     System.out.println("Opening: " + file.getName() + "." + "\n"); 
				     try {
						s = readDataFile(file);
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                          ///display on text box here
				     JTextArea area = new JTextArea();
				     area.setText(s);
				          JOptionPane.showMessageDialog(Timer.this, area, file.getName(), JOptionPane.PLAIN_MESSAGE);
				   } 
				   else { 
					   System.out.println("Open command cancelled by user." + "\n"); 	  
				} 
			}
		});
        
        // TODO: START: This method should check to see if the application is already running, and if not, launch a TimerThread object.
		// If the application is running, you may wish to ask the user if the existing thread should be stopped and a new thread started.
        // It should begin by launching a TimerDialog to get the number of seconds to count down, and then pass that number of seconds along
		// with the seconds since the start (0) to the TimerThread constructor.
		// It can then display a message in the text area stating how long the countdown is for.
        startButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(countdownThread.isAlive()) {
					int reply = JOptionPane.showConfirmDialog(Timer.this, "Restart Timer?", "Restart Timer", 
							JOptionPane.YES_NO_OPTION);
			        if (reply == JOptionPane.YES_OPTION) {
			           countdownThread.stop();
			           display.setText(null);
			           TimerDialog td = new TimerDialog(Timer.this, secondsToRun, true);
						countdownThread = new TimerThread(countdownField, elapsedField, secondsToRun, secondsSinceStart);
					    thread = new Thread(countdownThread);
					    thread.start();
					    display.append("Countdown for " + secondsToRun + " seconds\n");
					    pauseButton.setText("Pause");
			        }
				}
				else {
					TimerDialog td = new TimerDialog(Timer.this, secondsToRun, true);
					countdownThread = new TimerThread(countdownField, elapsedField, secondsToRun, secondsSinceStart);
				    thread = new Thread(countdownThread);
				    thread.start();
				    display.append("Countdown for " + secondsToRun + " seconds\n");
				    pauseButton.setText("Pause");
				}
			}
		});
        
        // TODO: PAUSE: This method should call the TimerThread object's pause method and display a message in the text area
        // indicating whether this represents pausing or restarting the timer.
        pauseButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
			       countdownThread.pause();
			       if(countdownThread.isPaused() && countdownThread.isAlive()){
			    	   pauseButton.setText("Resume");
			    	   display.append("Paused at " + countdownThread.getElapsedSeconds() + " seconds\n");
			       }
			       else if(countdownThread.isAlive()==false){
			    	   pauseButton.setText("Pause");
			       }
			       else {
			    	   display.append("Resumed at " + countdownThread.getElapsedSeconds() + " seconds\n");
			    	   pauseButton.setText("Pause");
			       }
			}
		});

        
        // TODO: STOP: This method should stop the TimerThread object and use appropriate methods to display the stop time
        // and the total amount of time remaining in the countdown (if any).
        stopButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(countdownThread.isAlive()){
	              countdownThread.stop();
	              display.append("Stopped at " + countdownThread.getElapsedSeconds() + " seconds\n");
	              pauseButton.setText("Pause");
				}
				countdownField.setText("00:00:00");
				elapsedField.setText("00:00:00");
			}
		});

    	
	}
	
	// TODO: These methods can be used in the action listeners above.
	public synchronized void writeDataFile(File path) throws IOException, FileNotFoundException {
                ObjectOutputStream out = null;
                try{
                	FileOutputStream fs = new FileOutputStream(path);
                	out = new ObjectOutputStream(fs);
                	
                    out.writeObject(display.getText());
                }
                catch(FileNotFoundException e)
                {
                	e.printStackTrace();
                }
                catch(IOException e){
                	e.printStackTrace();
                }
                finally
                {
                	try
                	{
                		out.close();
                	}
                	catch(IOException e){
                		e.printStackTrace();
                	}
                }
	}
	
	// TODO: These methods can be used in the action listeners above.
	public synchronized String readDataFile(File path) throws IOException, ClassNotFoundException {
		
		String result = new String();
		ObjectInputStream in = null;
		//ArrayList<Customer> tempArray = new ArrayList<Customer>();
		try {
			
			
			FileInputStream fs = new FileInputStream(path);
			in = new ObjectInputStream(fs);
			
					try {
						//tempArray = (ArrayList<Customer>) in.readObject();
						result = (String) in.readObject();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				    //customersArrOut.clear();
				    //customersArrOut.addAll(tempArray);
			
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		finally {
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static void setSeconds(long seconds) {
	    secondsToRun = seconds;
	}

    public static void main(String[] args) {

        Timer timer = new Timer();

    }
   

}

