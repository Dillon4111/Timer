package CA2;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.*;

public class OpenFile extends JFrame{
        JTextArea area;
	public OpenFile(String s) {
		area = new JTextArea(40,50);
		
		area.setText(s);
		
		add(area);
		setSize(50, 60);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
	}

}
