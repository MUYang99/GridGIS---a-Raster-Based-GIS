package kth.ag2411.project;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.JTextField;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPopupMenu;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LocalOperation extends JFrame {

	private JPanel contentPane;
	private JButton btnNewButton;
	private JButton btnChooseFile;
	private JPopupMenu popupMenu;
	private JComboBox comboBox;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel_2;
	private JLabel lblNewLabel_3;
	private JLabel lblNewLabel_5;
	private JTextField textField;
	private JTextField textField_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LocalOperation frame = new LocalOperation(null);
					frame.setVisible(true);
					frame.setTitle("GridGIS - Local Operation");
					frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	String name;
	int operation, id, radius;
	boolean IsSquare;
	double search;
	Layer layer1, layer2;
	
	MainOperation mo = new MainOperation();
	DataManagement dm = new DataManagement();
	
	/**
	 * Create the frame.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public LocalOperation(GUI gui) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		gui.outputField1().setText("sum");
		id = 0;
		
		
		final JFileChooser fileChooser = new JFileChooser(); 
	    fileChooser.addChoosableFileFilter(new FileFilter() { 
	       public String getDescription() { 
	           return "ASCII (*.txt)"; 
	       } 
	    
	       public boolean accept(File f) { 
	           if (f.isDirectory()) { 
	               return true; 
	           } else { 
	               return f.getName().toLowerCase().endsWith(".txt"); 
	           } 
	       } 
	    }); 
	    fileChooser.setCurrentDirectory(new File(System.getProperty("user.home"))); 
	    fileChooser.setMultiSelectionEnabled(false); 
		
		//operation, id 
		String string[] = {"Sum","Subtraction","Multiplication","Division","Binary","Mean","Max","Min","Variety"};
		JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				operation = 0;
				textField_1.setEnabled(false);
				lblNewLabel_5.setEnabled(false);
				textField_1.setText("0");
				
				if (comboBox.getSelectedItem() == "Sum")
				{
					gui.outputField1().setText("Sum");
					id = 0;
				}else if(comboBox.getSelectedItem() == "Subtraction"){
					gui.outputField1().setText("Subtraction");
					id = 1;
				}else if(comboBox.getSelectedItem() == "Multiplication"){
					gui.outputField1().setText("Multiplication");
					id = 2;
				}else if(comboBox.getSelectedItem() == "Division"){
					gui.outputField1().setText("Division");
					id = 3;
				}else if(comboBox.getSelectedItem() == "Binary"){
					gui.outputField1().setText("Binary");
					id = 4;
					textField_1.setEnabled(true);
					lblNewLabel_5.setEnabled(true);
				}else if(comboBox.getSelectedItem() == "Mean"){
					gui.outputField1().setText("Mean");
					id = 5;
				}else if(comboBox.getSelectedItem() == "Max"){
					gui.outputField1().setText("Max");
					id = 6;
				}else if(comboBox.getSelectedItem() == "Min"){
					gui.outputField1().setText("Min");
					id = 7;
				}else if(comboBox.getSelectedItem() == "Variety"){
					gui.outputField1().setText("Variety");
					id = 8;
				}
				System.out.println("operation:" + operation);
				System.out.println("id:" + id);
				contentPane.repaint();
			}
		});
		ComboBoxModel comboBoxModel = new DefaultComboBoxModel<>(string);
		comboBox.setModel(comboBoxModel);
		comboBox.setBounds(204, 52, 127, 23);
		contentPane.add(comboBox);
		
	    
	    //layer1
		btnNewButton = new JButton("Choose File1...");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int result = fileChooser.showOpenDialog(LocalOperation.this); 
				File selectedFile = fileChooser.getSelectedFile(); 
			    if (result == JFileChooser.APPROVE_OPTION) { 
		    		
			    	System.out.println("Input file1: " + selectedFile.getAbsolutePath());
		    		gui.outputField2().setText(selectedFile.getName());
			    		
				  	dm.readFile(selectedFile.getName(), selectedFile.getAbsolutePath());
				  	layer1 = dm.getLayer(selectedFile.getName());
				  	btnNewButton.setText(selectedFile.getName());
				    
					// Load layer to preview window
			      	gui.updatePreview (layer1, true);
			    }
			}
		});
		btnNewButton.setBounds(204, 85, 127, 23);
		contentPane.add(btnNewButton);
		
		//layer2
		btnChooseFile = new JButton("Choose File2...");
		btnChooseFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int result = fileChooser.showOpenDialog(LocalOperation.this); 
				File selectedFile = fileChooser.getSelectedFile(); 
			    if (result == JFileChooser.APPROVE_OPTION) {
		    		
			    	System.out.println("Input file2: " + selectedFile.getAbsolutePath());
		    		gui.outputField3().setText(selectedFile.getName());
					    
			      	dm.readFile(selectedFile.getName(), selectedFile.getAbsolutePath());
			      	layer2 = dm.getLayer(selectedFile.getName());
			      	btnChooseFile.setText(selectedFile.getName());

					// Load layer to preview window
			      	gui.updatePreview (layer2, false);
			    }
			}
		});
		
		btnChooseFile.setBounds(204, 118, 127, 23);
		contentPane.add(btnChooseFile);
		
		lblNewLabel = new JLabel("Local Operation");
		lblNewLabel.setFont(new Font("Calibri", Font.BOLD, 17));
		lblNewLabel.setBounds(38, 11, 161, 15);
		contentPane.add(lblNewLabel);
		
		lblNewLabel_1 = new JLabel("Operation Type");
		lblNewLabel_1.setBounds(38, 56, 92, 15);
		contentPane.add(lblNewLabel_1);
		
		lblNewLabel_2 = new JLabel("Input File1");
		lblNewLabel_2.setBounds(38, 89, 93, 15);
		contentPane.add(lblNewLabel_2);
		
		lblNewLabel_3 = new JLabel("Input File2");
		lblNewLabel_3.setBounds(38, 122, 84, 15);
		contentPane.add(lblNewLabel_3);
		
		//Neighborhood Type
		IsSquare = true;
		String string1[] = {"None"};
		ComboBoxModel comboBoxModel1 = new DefaultComboBoxModel<>(string1);
		
		lblNewLabel_5 = new JLabel("Search (Binary)");
		lblNewLabel_5.setBounds(38, 155, 116, 15);
		contentPane.add(lblNewLabel_5);
		lblNewLabel_5.setEnabled(false);
		
		//Search
		textField_1 = new JTextField();
		textField_1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				String s = textField_1.getText();
				search = Double.parseDouble(s);
				System.out.println("Search:" + search);
			}
		});
		textField_1.setBounds(265, 152, 66, 21);
		textField_1.setColumns(10);
		contentPane.add(textField_1);
		textField_1.setEnabled(false);
		
		//run
		JButton btnNewButton_1 = new JButton("Run");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ( ( layer1 != null ) || ( layer2 != null ) ) {
					if ( ( layer1.nCols == layer2.nCols ) || ( layer1.nRows == layer2.nRows ) ) {
						Layer outputlayer;
						if(name == null) {
							name = "Output";
						}
						outputlayer = mo.PerformOperation (name, layer1, layer2, operation, id, IsSquare, radius, search);
				        
				        gui.fitMapPanel(outputlayer);
				        gui.updateMapPanel(outputlayer);
				        
						dm.addFileToList(outputlayer);
					} else {
						infoBox("Images must be of same size. \n" + layer1.name + " is " + layer1.nCols + " by " + layer1.nRows + "\n" + layer2.name + " is " + layer2.nCols + " by " + layer2.nRows);
					}
				} else {
					infoBox("You have to define what layers to use.");
				}
			}
		});
		btnNewButton_1.setBounds(333, 192, 93, 23);
		contentPane.add(btnNewButton_1);
	}
	
	public static void infoBox(String infoMessage)
    {
        JOptionPane.showMessageDialog(null, infoMessage, "", JOptionPane.INFORMATION_MESSAGE);
    }
}