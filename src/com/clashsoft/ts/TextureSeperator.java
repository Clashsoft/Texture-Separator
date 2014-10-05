package com.clashsoft.ts;

import java.awt.EventQueue;

import javax.swing.*;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class TextureSeperator
{
	private JFrame		frame;
	public JLabel		labelTexture;
	public JLabel		labelOffset;
	public JLabel		labelSubtexture;
	
	public JTextField	textFieldFile;
	public JButton		buttonSelectFile;
	
	public JLabel		labelOffsetX;
	public JLabel		labelOffsetY;
	public JLabel		labelWidth;
	public JLabel		labelHeight;
	
	public JSpinner		spinnerOffsetX;
	public JSpinner		spinnerOffsetY;
	public JSpinner		spinnerWidth;
	public JSpinner		spinnerHeight;
	
	public JButton		buttonSeperate;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					TextureSeperator window = new TextureSeperator();
					window.frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Create the application.
	 */
	public TextureSeperator()
	{
		initialize();
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		this.frame = new JFrame();
		this.frame.setTitle("Texture Seperator");
		this.frame.setBounds(100, 100, 436, 183);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.getContentPane().setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("max(43dlu;default):grow"), FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("max(40dlu;default):grow"), FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("max(31dlu;default)"), FormFactory.RELATED_GAP_COLSPEC, }, new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, }));
		
		this.labelTexture = new JLabel("Texture:");
		this.frame.getContentPane().add(this.labelTexture, "2, 2, right, default");
		
		this.labelOffset = new JLabel("Offset:");
		this.frame.getContentPane().add(this.labelOffset, "2, 4, right, default");
		
		this.labelSubtexture = new JLabel("Sub-Texture:");
		this.frame.getContentPane().add(this.labelSubtexture, "2, 6, right, default");
		
		this.textFieldFile = new JTextField();
		this.textFieldFile.setColumns(10);
		this.frame.getContentPane().add(this.textFieldFile, "4, 2, 7, 1, fill, default");
		
		this.buttonSelectFile = new JButton("...");
		this.frame.getContentPane().add(this.buttonSelectFile, "12, 2");
		
		this.labelOffsetX = new JLabel("x:");
		this.frame.getContentPane().add(this.labelOffsetX, "4, 4");
		
		this.labelOffsetY = new JLabel("y:");
		this.frame.getContentPane().add(this.labelOffsetY, "8, 4");
		
		this.labelWidth = new JLabel("w:");
		this.frame.getContentPane().add(this.labelWidth, "4, 6");
		
		this.labelHeight = new JLabel("h:");
		this.frame.getContentPane().add(this.labelHeight, "8, 6");
		
		this.spinnerOffsetX = new JSpinner();
		this.frame.getContentPane().add(this.spinnerOffsetX, "6, 4");
		
		this.spinnerOffsetY = new JSpinner();
		this.frame.getContentPane().add(this.spinnerOffsetY, "10, 4, 3, 1");
		
		this.spinnerWidth = new JSpinner();
		this.frame.getContentPane().add(this.spinnerWidth, "6, 6");
		
		this.spinnerHeight = new JSpinner();
		this.frame.getContentPane().add(this.spinnerHeight, "10, 6, 3, 1");
		
		this.buttonSeperate = new JButton("Seperate");
		this.frame.getContentPane().add(this.buttonSeperate, "2, 8, 11, 3");
	}
	
}
