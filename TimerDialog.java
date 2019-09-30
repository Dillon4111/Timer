package CA2;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import net.miginfocom.swing.MigLayout;


public class TimerDialog extends JDialog {
	
	// Represents the number of seconds that the countdown will be performed for.
	private long seconds;
	boolean start = false;
	
	// Menu components.
	JTextField hourField, minField, secField;
	JLabel hourLabel, minLabel, secLabel;
	JButton startButton = new JButton("START");

	public TimerDialog(Frame owner, long seconds, boolean modality) {
		super(owner, modality);
		this.seconds = seconds;
		initComponents();
	}
	
	// Sets up display.
	private void initComponents() {
		setTitle("Initialise Timer");	
		setLayout(new BorderLayout());
		
		Font displayFont = new Font("Arial", Font.BOLD, 16);
		Font labelFont = new Font("Arial", Font.BOLD, 12);
		
		JPanel displayPanel = new JPanel(new GridLayout(1,3));
				
		JPanel hourPanel = new JPanel(new BorderLayout());
		hourField = new JTextField(5);
		hourField.setHorizontalAlignment(JTextField.CENTER);
		hourField.setFont(displayFont);
		hourField.setText("00");
		hourLabel = new JLabel("Hours");
		hourLabel.setHorizontalAlignment(JTextField.CENTER);
		hourLabel.setFont(labelFont);
		hourPanel.add(hourField, BorderLayout.CENTER);
		hourPanel.add(hourLabel, BorderLayout.SOUTH);
		
		displayPanel.add(hourPanel);
		
		JPanel minPanel = new JPanel(new BorderLayout());
		minField = new JTextField(5);
		minField.setHorizontalAlignment(JTextField.CENTER);
		minField.setFont(displayFont);
		minField.setText("00");
		minLabel = new JLabel("Minutes");
		minLabel.setHorizontalAlignment(JTextField.CENTER);
		minLabel.setFont(labelFont);
		minPanel.add(minField, BorderLayout.CENTER);
		minPanel.add(minLabel, BorderLayout.SOUTH);
		
		displayPanel.add(minPanel);
		
		JPanel secPanel = new JPanel(new BorderLayout());
		secField = new JTextField(5);
		secField.setHorizontalAlignment(JTextField.CENTER);
		secField.setFont(displayFont);
		secField.setText("00");
		secLabel = new JLabel("Seconds");
		secLabel.setHorizontalAlignment(JTextField.CENTER);
		secLabel.setFont(labelFont);
		secPanel.add(secField, BorderLayout.CENTER);
		secPanel.add(secLabel, BorderLayout.SOUTH);
		
		displayPanel.add(secPanel);
		
		add(displayPanel, BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel();
		
		buttonPanel.add(startButton);
		
		// TODO: This start action listener will be invoked when the start button is clicked.
		// It should take the values from the three text fields and try to convert them into integer values, and then check for NumberFormatExceptions 
		// and for the minute and second values between 0 and 59.
		startButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				int hours = 0, minutes = 0, secs = 0;
				boolean valid = true;
				
				try {
	               hours = Integer.parseInt(hourField.getText());
	               minutes = Integer.parseInt(minField.getText());
	               secs = Integer.parseInt(secField.getText());
				}catch(NumberFormatException c) {
					JOptionPane.showMessageDialog(TimerDialog.this, "You must enter valid numbers");
					valid = false;
				}
				
	           if(!(hours <= 59 && hours >= 0))
	        	   JOptionPane.showMessageDialog(TimerDialog.this, "Hours must be between 0 and 59");
	           else if(!(minutes <= 59 && minutes >= 0))
	        	   JOptionPane.showMessageDialog(TimerDialog.this, "Minutes must be between 0 and 59");
	           else if(!(secs <= 59 && secs >= 0))
	        	   JOptionPane.showMessageDialog(TimerDialog.this, "Seconds must be between 0 and 59");
	           else if(valid==true) {
	        	   seconds = (hours*3600) + (minutes*60) + secs;
	        	   Timer.setSeconds(seconds);
	        	   dispose();
	           }
			}
		});

		
		add(buttonPanel, BorderLayout.SOUTH);
		
		setSize(300, 150);
		setVisible(true);
		
	}
	
	public long getSeconds() {
		return (long)seconds;
	}
	

}

