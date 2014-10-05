package com.clashsoft.ts;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class TextureSeperator
{
	private JFrame				frame;
	public JLabel				labelTexture;
	public JLabel				labelOffset;
	public JLabel				labelSubtexture;
	
	public JTextField			textFieldFile;
	public JButton				buttonSelectFile;
	
	public JLabel				labelOffsetX;
	public JLabel				labelOffsetY;
	public JLabel				labelWidth;
	public JLabel				labelHeight;
	
	public JSpinner				spinnerOffsetX;
	public JSpinner				spinnerOffsetY;
	public JSpinner				spinnerWidth;
	public JSpinner				spinnerHeight;
	
	public JButton				buttonSeperate;
	
	public JButton				buttonCancel;
	
	public JProgressBar			progressBar;
	private final JFileChooser	fileChooser	= new JFileChooser();
	
	public boolean				cancel;
	
	public static void main(String[] args)
	{
		TextureSeperator window = new TextureSeperator();
		window.frame.setVisible(true);
	}
	
	public TextureSeperator()
	{
		this.initialize();
	}
	
	private void initialize()
	{
		this.frame = new JFrame();
		this.frame.setTitle("Texture Seperator");
		this.frame.setBounds(100, 100, 420, 160);
		this.frame.setMinimumSize(new Dimension(420, 160));
		this.frame.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.getContentPane().setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("50dlu:grow"), FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("50dlu:grow"), FormFactory.RELATED_GAP_COLSPEC, FormFactory.PREF_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, }, new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, }));
		
		this.labelTexture = new JLabel("Texture:");
		this.frame.getContentPane().add(this.labelTexture, "2, 2, 3, 1, left, default");
		
		this.labelOffset = new JLabel("Offset:");
		this.frame.getContentPane().add(this.labelOffset, "2, 4, left, default");
		
		this.buttonSeperate = new JButton("Seperate");
		this.buttonSeperate.addActionListener(e -> {
			File file = new File(this.textFieldFile.getText());
			int offsetX = (int) this.spinnerOffsetX.getValue();
			int offsetY = (int) this.spinnerOffsetY.getValue();
			int width = (int) this.spinnerWidth.getValue();
			int height = (int) this.spinnerHeight.getValue();
			this.seperate(file, offsetX, offsetY, width, height);
		});
		this.frame.getContentPane().add(this.buttonSeperate, "12, 4, 1, 3, fill, fill");
		
		this.labelSubtexture = new JLabel("Size:");
		this.frame.getContentPane().add(this.labelSubtexture, "2, 6, left, default");
		
		this.textFieldFile = new JTextField();
		this.textFieldFile.setColumns(10);
		this.frame.getContentPane().add(this.textFieldFile, "6, 2, 5, 1, fill, default");
		
		this.buttonSelectFile = new JButton("Select");
		this.buttonSelectFile.addActionListener(e -> {
			int ret = this.fileChooser.showOpenDialog(this.frame);
			if (ret == JFileChooser.APPROVE_OPTION)
			{
				File file = this.fileChooser.getSelectedFile();
				this.textFieldFile.setText(file.getAbsolutePath());
			}
		});
		this.frame.getContentPane().add(this.buttonSelectFile, "12, 2");
		
		this.labelOffsetX = new JLabel("x:");
		this.frame.getContentPane().add(this.labelOffsetX, "4, 4");
		
		this.labelOffsetY = new JLabel("y:");
		this.frame.getContentPane().add(this.labelOffsetY, "8, 4");
		
		this.labelWidth = new JLabel("w:");
		this.frame.getContentPane().add(this.labelWidth, "4, 6");
		
		this.labelHeight = new JLabel("h:");
		this.frame.getContentPane().add(this.labelHeight, "8, 6");
		
		this.spinnerOffsetX = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
		this.frame.getContentPane().add(this.spinnerOffsetX, "6, 4");
		
		this.spinnerOffsetY = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
		this.frame.getContentPane().add(this.spinnerOffsetY, "10, 4");
		
		this.spinnerWidth = new JSpinner(new SpinnerNumberModel(16, 0, Integer.MAX_VALUE, 1));
		this.frame.getContentPane().add(this.spinnerWidth, "6, 6");
		
		this.spinnerHeight = new JSpinner(new SpinnerNumberModel(16, 0, Integer.MAX_VALUE, 1));
		this.frame.getContentPane().add(this.spinnerHeight, "10, 6");
		
		this.progressBar = new JProgressBar();
		this.progressBar.setStringPainted(true);
		this.progressBar.setString("0 / 0");
		this.frame.getContentPane().add(this.progressBar, "2, 8, 9, 1");
		
		this.buttonCancel = new JButton("Cancel");
		this.buttonCancel.addActionListener(e -> {
			this.cancel = true;
		});
		this.frame.getContentPane().add(this.buttonCancel, "12, 8");
	}
	
	public void seperate(File file, int offsetX, int offsetY, int width, int height)
	{
		new Thread(() -> {
			try
			{
				this.seperate_(file, offsetX, offsetY, width, height);
			}
			catch (Exception ex)
			{
				JOptionPane.showMessageDialog(this.frame, "An error occured while generating Sub-Textures: " + ex.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				ex.printStackTrace();
			}
		}).start();
	}
	
	public void seperate_(File file, int offsetX, int offsetY, int width, int height) throws Exception
	{
		long time = System.currentTimeMillis();
		
		BufferedImage image = ImageIO.read(file);
		
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();
		int countX = (imageWidth - offsetX) / width;
		int countY = (imageHeight - offsetY) / height;
		
		int count = 0;
		int totalCount = countX * countY;
		
		if (totalCount > 4096)
		{
			int i = JOptionPane.showConfirmDialog(this.frame, "Are you sure you want to generate " + totalCount + " Sub-Texture files?", "Confirm large Operation", JOptionPane.YES_NO_OPTION);
			if (i != JOptionPane.YES_OPTION)
			{
				// Abort
				return;
			}
		}
		
		this.progressBar.setMaximum(totalCount);
		
		String path = file.getAbsolutePath();
		int index = path.lastIndexOf('.');
		if (index == -1)
		{
			throw new IllegalArgumentException("Invalid File");
		}
		
		File parent = new File(path.substring(0, index));
		parent.mkdirs();
		
		for (int x = 0; x < countX; x++)
		{
			for (int y = 0; y < countY; y++)
			{
				if (this.cancel)
				{
					break;
				}
				
				try
				{
					BufferedImage subimage = image.getSubimage(offsetX + x * width, offsetY + y * height, width, height);
					// Swapped order to go by rows
					File newFile = new File(parent, "texture_" + y + "_" + x + ".png");
					ImageIO.write(subimage, "png", newFile);
					
					count++;
					this.progressBar.setValue(count);
					this.progressBar.setString(count + " / " + totalCount);
				}
				catch (Exception ex)
				{}
			}
		}
		
		time = System.currentTimeMillis() - time;
		long timePerImage = count == 0 ? 0 : time / count;
		
		String message = String.format("Successfully generated %d / %d Sub-Textures in %d s (%d ms, %d ms per Sub-Texture).", count, totalCount, time / 1000L, time, timePerImage);
		JOptionPane.showMessageDialog(this.frame, message, "Texture Seperator", JOptionPane.INFORMATION_MESSAGE);
		
		this.cancel = false;
	}
}
