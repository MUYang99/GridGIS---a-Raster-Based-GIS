package kth.ag2411.project;
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

public class ZonalOperation extends JFrame {

	private JPanel contentPane;
	private JButton btnNewButton;
	private JButton btnChooseFile;
	private JPopupMenu popupMenu;
	private JComboBox comboBox;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel_2;
	private JLabel lblNewLabel_3;
	private JLabel binaryLabel;
	private JTextField textField_1;
	private JLabel lblNewLabel_4;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ZonalOperation frame = new ZonalOperation(null);
					frame.setTitle("GridGIS - Zonal Operation");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	MainOperation mo = new MainOperation();
	DataManagement dm = new DataManagement();
	
	String name;
	int operation, id, radius;
	boolean IsSquare;
	double search;
	Layer layer1, layer2;
	/**
	 * Create the frame.
	 */
	public ZonalOperation(GUI gui) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		gui.outputField1().setText("Sum");
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
		String string[] = {"Sum","Multiplication","Binary","Mean","Max","Min","Variety"};
		JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				operation = 2;
				textField_1.setEnabled(false);
				lblNewLabel_4.setEnabled(false);
				textField_1.setText("0");
				
				if (comboBox.getSelectedItem() == "Sum")
				{
					gui.outputField1().setText("Sum");
					id = 0;
				}else if(comboBox.getSelectedItem() == "Multiplication"){
					gui.outputField1().setText("Multiplication");
					id = 2;
				}else if(comboBox.getSelectedItem() == "Binary"){
					gui.outputField1().setText("Binary");
					id = 4;
					textField_1.setEnabled(true);
					lblNewLabel_4.setEnabled(true);
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
			}
		});
		ComboBoxModel comboBoxModel = new DefaultComboBoxModel<>(string);
		contentPane.setLayout(null);
		comboBox.setModel(comboBoxModel);
		comboBox.setBounds(204, 52, 127, 23);
		contentPane.add(comboBox);
		
	    
		//layer1
		btnNewButton = new JButton("Choose File1...");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int result = fileChooser.showOpenDialog(ZonalOperation.this); 
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
				int result = fileChooser.showOpenDialog(ZonalOperation.this); 
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
		
		lblNewLabel = new JLabel("Zonal Operation");
		lblNewLabel.setFont(new Font("Calibri", Font.BOLD, 17));
		lblNewLabel.setBounds(39, 11, 161, 15);
		contentPane.add(lblNewLabel);
		
		lblNewLabel_1 = new JLabel("Operation Type");
		lblNewLabel_1.setBounds(39, 56, 92, 15);
		contentPane.add(lblNewLabel_1);
		
		lblNewLabel_2 = new JLabel("Input Value File");
		lblNewLabel_2.setBounds(39, 89, 93, 15);
		contentPane.add(lblNewLabel_2);
		
		lblNewLabel_3 = new JLabel("Input Zone File");
		lblNewLabel_3.setBounds(39, 122, 100, 15);
		contentPane.add(lblNewLabel_3);
		
		//Neighborhood Type
		String string1[] = {"None"};
		ComboBoxModel comboBoxModel1 = new DefaultComboBoxModel<>(string1);
		
		//Radius
		radius = 0;
		
		JLabel binaryLabel = new JLabel("Search (Binary)");
		binaryLabel.setBounds(39, 152, 216, 15);
		
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
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		textField_1.setEnabled(false);
		
		//Run
		JButton btnNewButton_1 = new JButton("Run");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ( ( layer1 != null ) || ( layer2 != null )) {
					if ( ( layer1.nCols == layer2.nCols ) || ( layer1.nRows == layer2.nRows ) ) {
						if(name == null) {
							name = "Output";
						}
						
						Layer outputlayer;
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
		btnNewButton_1.setBounds(333, 207, 93, 23);
		contentPane.add(btnNewButton_1);
		
		lblNewLabel_4 = new JLabel("Search (Binary)");
		lblNewLabel_4.setBounds(39, 155, 186, 15);
		contentPane.add(lblNewLabel_4);
		lblNewLabel_4.setEnabled(false);
	}
	

	public static void infoBox(String infoMessage)
    {
        JOptionPane.showMessageDialog(null, infoMessage, "", JOptionPane.INFORMATION_MESSAGE);
    }
}