package kth.ag2411.project;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.SwingConstants; 

@SuppressWarnings("serial")
public class GUI extends JFrame {
	
	public MapPanel mapPanel, previewP1, previewP2;
	private JPanel contentPane;
	private JTextField pretextField;
    private static GUI frame;
    private JTextField Field2;
    private JTextField Field1;
    public JPanel panel, l1_Mp, l2_Mp;
    public Layer currentLayer;
    public BufferedImage currentImage;
    private JButton  saveButton;
    private JLabel lblNewLabel_4;
    private Rectangle r;
    private JFrame operation;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new GUI();
					frame.setTitle("GridGIS Software");
					Color c = new Color(193, 214, 227);
					frame.getContentPane().setBackground(c);
					frame.panel.setBackground(c);
					frame.setVisible(true);
					
					frame.addComponentListener(new ComponentAdapter() 
					{  
				        public void componentResized(ComponentEvent evt) {
				            Component c = (Component)evt.getSource();
				            System.out.println();
				            frame.resizePanel();
				            
				        }
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	

	/**
	 * Create the frame.
	 */
	int scale = 1;
	String path;
	public GUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 600);
		
		currentLayer = null;
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		
		MouseWheelListenerPanel m = new MouseWheelListenerPanel();
		
		JButton btnLocal = new JButton("Local");
		btnLocal.setBounds(10, 33, 93, 23);
		btnLocal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
					openLocal();
			}
		});
		contentPane.setLayout(null);
		contentPane.add(btnLocal);
		
		JLabel lblNewLabel = new JLabel("Operations");
		lblNewLabel.setBounds(125, 11, 101, 25);
		lblNewLabel.setFont(new Font("Calibri", Font.BOLD, 20));
		contentPane.add(lblNewLabel);
		
		JButton Focal = new JButton("Focal");
		Focal.setBounds(125, 33, 93, 23);
		Focal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openFocal();
			}
		});
		contentPane.add(Focal);
		
		JButton ZonalButton = new JButton("Zonal");
		ZonalButton.setBounds(242, 33, 93, 23);
		ZonalButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openZonal();
			}
		});
		contentPane.add(ZonalButton);
		
		pretextField = new JTextField();
		pretextField.setBounds(10, 187, 93, 23);
		pretextField.setText("Layer Name");
		pretextField.setEditable(false);
		contentPane.add(pretextField);
		pretextField.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Input Specifications");
		lblNewLabel_1.setBounds(10, 76, 149, 15);
		lblNewLabel_1.setFont(new Font("Century", Font.BOLD, 15));
		contentPane.add(lblNewLabel_1);
		
		Field2 = new JTextField();
		Field2.setBounds(10, 358, 93, 23);
		Field2.setText("Layer Name");
		Field2.setEditable(false);
		contentPane.add(Field2);
		Field2.setColumns(10);
		
		Field1 = new JTextField();
		Field1.setBounds(10, 129, 107, 21);
		Field1.setEditable(false);
		contentPane.add(Field1);
		Field1.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("Operation");
		lblNewLabel_2.setBounds(10, 111, 78, 15);
		lblNewLabel_2.setFont(new Font("Century", Font.PLAIN, 13));
		contentPane.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("Layer1");
		lblNewLabel_3.setBounds(10, 161, 54, 15);
		lblNewLabel_3.setFont(new Font("Century", Font.PLAIN, 13));
		contentPane.add(lblNewLabel_3);
		
		lblNewLabel_4 = new JLabel("Layer2");
		lblNewLabel_4.setBounds(10, 332, 54, 15);
		lblNewLabel_4.setFont(new Font("Century", Font.PLAIN, 13));
		contentPane.add(lblNewLabel_4);	
		
		panel = new JPanel();
		panel.setBounds(242, 76, 277, 277);
		
		BufferedImage image = null;
		previewP1 = new MapPanel(image, 2.0);
		previewP2 = new MapPanel(image, 2.0); 
		contentPane.add(panel);
		
		l1_Mp = new JPanel();
		l1_Mp.setBounds(10, 221, 100, 100);
		contentPane.add(l1_Mp);
		
		l2_Mp = new JPanel();
		l2_Mp.setBounds(10, 392, 100, 100);
		contentPane.add(l2_Mp);
		
		//save button stuff :)
		final JFileChooser fileChooser = new JFileChooser(); 
    	fileChooser.setCurrentDirectory(new File(System.getProperty("user.home"))); 
    	fileChooser.setMultiSelectionEnabled(false); 
		
		saveButton = new JButton("Save Ascii");
		saveButton.setBounds(10, 518, 107, 23);
		saveButton.setEnabled(false);
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				int result = fileChooser.showSaveDialog(GUI.this); 
			    if (result == JFileChooser.APPROVE_OPTION) {
			    	File file = fileChooser.getSelectedFile();						
					System.out.println(file.getAbsolutePath());
				    String f = file.getAbsolutePath()+".txt";
				    System.out.println("save: "+f);
			    	currentLayer.save(f);
		    	}
			    
			}
		});
		contentPane.add(saveButton);
		saveButton.setEnabled(false);
		
		btnSaveAsPng = new JButton("Save PNG");
		btnSaveAsPng.setBounds(125, 518, 107, 23);
		btnSaveAsPng.setEnabled(false);
		btnSaveAsPng.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			
				int result = fileChooser.showSaveDialog(GUI.this); 
			    if (result == JFileChooser.APPROVE_OPTION) {
			    	File file = fileChooser.getSelectedFile();						
			    	System.out.println(file.getAbsolutePath());
			    	String f = file.getAbsolutePath()+".png";
			    	System.out.println("save: "+f);
			    	currentLayer.saveImage(f);
			       
		    	}
		    }
			
		});
		contentPane.add(btnSaveAsPng);
		btnSaveAsPng.setEnabled(false);
		
		//Copyright text
		Copyright = new JLabel("GridGIS © - 2020.");
		Copyright.setBounds(10, 545, 419, 14);
		Copyright.setHorizontalAlignment(SwingConstants.LEFT);
		r = this.getBounds();
		contentPane.add(Copyright);
		
		//Zoom buttons
		btnZin = new JButton("+");
		btnZin.setBounds(368, 28, 61, 30);
		btnZin.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnZin.setEnabled(false);
		btnZin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				zoomIn();
			}
		});
		btnZin.setEnabled(false);
		contentPane.add(btnZin);
		
		btnZout = new JButton("-");
		btnZout.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnZout.setBounds(458, 29, 61, 30);
		btnZout.setEnabled(false);
		btnZout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				zoomOut();
			}
		});
		btnZout.setEnabled(false);
		contentPane.add(btnZout);
		
		
	}//GUIfunction

	public static void infoBox(String infoMessage) {
	    JOptionPane.showMessageDialog(null, infoMessage, "", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public JTextField outputField1() {
		return Field1;
	}
	
	public JTextField outputField2() {
		return pretextField;
	}
	
	public JTextField outputField3() {
		return Field2;
	}

	//Operation-window opening methods
	private void openLocal() {
		
		if(operation != null) {
			operation.dispose();
		}

		resetInput();
		operation = new LocalOperation(frame);
		operation.setVisible(true);

		lblNewLabel_4.show();
		Field2.show();
		l2_Mp.show();
		
	}
	
	private void openFocal() {

		if(operation != null) {
			operation.dispose();
		}
		
		resetInput();
		operation = new FocalOperation(frame);
		operation.setVisible(true);
		
		lblNewLabel_4.hide();
		Field2.hide();
		l2_Mp.hide();
		
	}
	
	private void openZonal() {

		if(operation != null) {
			operation.dispose();
		}

		resetInput();
		operation = new ZonalOperation(frame);
		operation.setVisible(true);

		lblNewLabel_4.show();
		Field2.show();
		l2_Mp.show();
		
	}
	
	public void resetInput() {
		
		Field1.setText("");
		
		pretextField.setText("");
		Field2.setText("");
		
		l1_Mp.removeAll();
		l1_Mp.revalidate();
		l1_Mp.repaint();
		
		l2_Mp.removeAll();
		l2_Mp.revalidate();
		l2_Mp.repaint();
		
	}
	
	//Random window management. Jonas made these, and he is very proud of it :)
	public void resizePanel() {

        panel.setBounds(242, 76, this.getWidth() , this.getHeight() );
        panel.setLayout(null);
        panel.repaint();

		r = this.getBounds();
		if( (r.height - 55) >= 545 ) {
	        contentPane.remove(Copyright);
			Copyright.setBounds(10, r.height-55, 158, 14);
			contentPane.add(Copyright);
		}
//		System.out.println("panel: " + 10 + " " + (r.height-55) + " " + 158 + " " + 14);
	}
	
	public void setMapScale(double _scale) {
		mapScale = _scale;
	}
	
	public void updatePreview (Layer layer, Boolean window) {
		
		double ratio = (double) layer.nCols / (double) layer.nRows;
		System.out.println(ratio);
		int hRes = 50;
		int wRes = 10000;
		// for(int i = 0; i < 5; i++)
		while( (wRes*2) > 210 ) {
			
			hRes--;
			wRes = (int) (hRes * (ratio)) ;
//			System.out.println("wRes: " + wRes + " hRes: " + hRes);
			
		}
		
		BufferedImage image = layer.toImage();
		
		Image nImage =  image.getScaledInstance(wRes, hRes, Image.SCALE_SMOOTH);
		image = new BufferedImage(nImage.getWidth(null), nImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = image.createGraphics();
        graphics2D.drawImage(nImage, 0, 0, null);
        graphics2D.dispose();
        
        Dimension maxSize = new Dimension(210, 100);
        
		if( window ) { //Is layer 1
			
			l1_Mp.remove(previewP1);
			previewP1 = new MapPanel(image, 2.0); 
			previewP1.setBounds(0, 0, hRes, wRes);
			previewP1.revalidate();
			previewP1.repaint();
			
			l1_Mp.add(previewP1);
			l1_Mp.setBounds(l1_Mp.getX(), l1_Mp.getY(), previewP1.getHeight()*2, previewP1.getWidth()*2);
			l1_Mp.setLayout(null);
			l1_Mp.setVisible(true);
			l1_Mp.revalidate();
			l1_Mp.repaint();
			
		} else { //is layer 2

			l2_Mp.remove(previewP2);
			previewP2 = new MapPanel(image, 2.0); 
			previewP2.setBounds(0, 0, hRes, wRes);
			previewP2.revalidate();
			previewP2.repaint();
			
			l2_Mp.add(previewP2);
			l2_Mp.setBounds(l2_Mp.getX(), l2_Mp.getY(), previewP2.getHeight()*2, previewP2.getWidth()*2);
			l2_Mp.setLayout(null);
			l2_Mp.setVisible(true);
			l2_Mp.revalidate();
			l2_Mp.repaint();
			
		}
	}
	
	private double mapScale = 1.0;
	private JButton btnSaveAsPng;
	private JLabel Copyright;
	private JButton btnZin;
	private JButton btnZout;

	private void zoomIn() {
		mapScale = mapScale * 1.3;
		updateMapPanel(currentLayer);
	}
	
	private void zoomOut() {
		mapScale = mapScale * 0.7;
		updateMapPanel(currentLayer);
	}
	
	public void fitMapPanel(Layer layer) {
        mapScale = 1.0;
        BufferedImage layerImage; 
        layerImage = layer.toImage(); 
        
        while( ( layerImage.getWidth() * mapScale ) < 300) {
        	mapScale += 0.1;
        }
        
        while( ( layerImage.getWidth() * mapScale ) > 800 ) {
        	
        	mapScale -= 0.1;
        }
	}
	
	public void updateMapPanel(Layer outputlayer) {

        currentImage = outputlayer.toImage(); 
        
//    	System.out.println("Final " + mapScale);
        
        if(mapPanel!=null) {
        	panel.remove(mapPanel);
        }
        
        mapPanel = new MapPanel(currentImage, 2.0); 
//        System.out.println( currentImage.getHeight() * mapScale );
//        System.out.println( currentImage.getWidth() * mapScale );
        
        
        mapPanel.scale = mapScale;
        mapPanel.setBounds(0, 0, (int) (currentImage.getHeight() * mapScale) , (int) (currentImage.getWidth() * mapScale));
        mapPanel.revalidate();
        mapPanel.repaint();
        
        //add map to panel
        panel.add(mapPanel);
        panel.setBounds(242, 76, (int) ((currentImage.getWidth() * mapScale)), (int) ((currentImage.getHeight() * mapScale)));
        panel.setVisible(true);
        panel.revalidate();
        panel.repaint();
		currentLayer = outputlayer;
		
		//show mapUI
		saveButton.setEnabled(true);
		btnSaveAsPng.setEnabled(true);
		btnZin.setEnabled(true);
		btnZout.setEnabled(true);
		
	}

}//GUI class




// MOUSE WHEEL LISTENER PANEL
class MouseWheelListenerPanel extends JPanel implements MouseWheelListener
{
    MouseWheelListenerPanel()
    {
        addMouseWheelListener(this);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e)
    {
        if (e.isControlDown())
        {
            if (e.getWheelRotation() < 0)
            {
                System.out.println("mouse wheel Up");
            }
            else
            {
                System.out.println("mouse wheel Down");
            }
        }
        else
        {
            getParent().dispatchEvent(e);
        }

    }
}