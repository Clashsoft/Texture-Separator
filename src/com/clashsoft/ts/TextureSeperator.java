package com.clashsoft.ts;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.GridLayout;
import javax.swing.JLabel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

public class TextureSeperator
{
	
	private JFrame	frmTextureSeperator;
	
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
					window.frmTextureSeperator.setVisible(true);
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
		this.frmTextureSeperator = new JFrame();
		this.frmTextureSeperator.setTitle("Texture Seperator");
		this.frmTextureSeperator.setBounds(100, 100, 450, 300);
		this.frmTextureSeperator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
}
