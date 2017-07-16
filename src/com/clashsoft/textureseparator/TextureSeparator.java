package com.clashsoft.textureseparator;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;

@SuppressWarnings("unused")
public class TextureSeparator
{
	private JFrame frame;
	public  JLabel labelTexture;
	public  JLabel labelOffset;
	public  JLabel labelSubtexture;

	public JTextField textFieldFile;
	public JButton    buttonSelectFile;

	public JLabel labelOffsetX;
	public JLabel labelOffsetY;
	public JLabel labelWidth;
	public JLabel labelHeight;

	public JSpinner spinnerOffsetX;
	public JSpinner spinnerOffsetY;
	public JSpinner spinnerWidth;
	public JSpinner spinnerHeight;

	public JCheckBox checkboxLeftover;

	public JButton buttonSeparate;
	public JButton buttonCancel;

	public JProgressBar progressBar;
	private final JFileChooser fileChooser = new JFileChooser();

	public boolean cancel;

	public static void main(String[] args)
	{
		TextureSeparator window = new TextureSeparator();
		window.frame.setVisible(true);
	}

	public TextureSeparator()
	{
		this.initialize();
	}

	private void initialize()
	{
		this.frame = new JFrame();
		this.frame.setTitle("Texture Separator");
		this.frame.setBounds(100, 100, 430, 160);
		this.frame.setMinimumSize(new Dimension(430, 160));
		this.frame.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));
		this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.frame.getContentPane().setLayout(
			new FormLayout(new ColumnSpec[] { FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("50dlu:grow"), FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("50dlu:grow"), FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.PREF_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC, },
			               new RowSpec[] { FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
				               FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
				               FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
				               FormSpecs.RELATED_GAP_ROWSPEC, }));

		this.labelTexture = new JLabel("Texture:");
		this.frame.getContentPane().add(this.labelTexture, "2, 2, 3, 1, left, default");

		this.labelOffset = new JLabel("Offset:");
		this.frame.getContentPane().add(this.labelOffset, "2, 4, left, default");

		this.labelSubtexture = new JLabel("Size:");
		this.frame.getContentPane().add(this.labelSubtexture, "2, 6, left, default");

		this.textFieldFile = new JTextField();
		this.textFieldFile.setColumns(10);
		this.frame.getContentPane().add(this.textFieldFile, "6, 2, 5, 1, fill, default");

		this.buttonSelectFile = new JButton("Browse...");
		this.buttonSelectFile.addActionListener(this::onSelectClicked);
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

		this.checkboxLeftover = new JCheckBox("Leftover");
		this.checkboxLeftover.setToolTipText("Checks if any leftover pixels should be saved into a new file.");
		this.frame.getContentPane().add(this.checkboxLeftover, "12, 4");

		this.buttonSeparate = new JButton("Separate");
		this.buttonSeparate.addActionListener(this::onSeparateClicked);
		this.frame.getContentPane().add(this.buttonSeparate, "12, 6, fill, fill");

		this.buttonCancel = new JButton("Cancel");
		this.buttonCancel.setEnabled(false);
		this.buttonCancel.addActionListener(e -> this.cancel = true);
		this.frame.getContentPane().add(this.buttonCancel, "12, 8");

		this.progressBar = new JProgressBar();
		this.progressBar.setStringPainted(true);
		this.progressBar.setString("0 / 0");
		this.frame.getContentPane().add(this.progressBar, "2, 8, 9, 1");
	}

	private void onSelectClicked(ActionEvent e)
	{
		int ret = this.fileChooser.showOpenDialog(this.frame);
		if (ret == JFileChooser.APPROVE_OPTION)
		{
			File file = this.fileChooser.getSelectedFile();
			this.textFieldFile.setText(file.getAbsolutePath());
		}
	}

	private void onSeparateClicked(ActionEvent e)
	{
		final File file = new File(this.textFieldFile.getText());
		final int offsetX = (int) this.spinnerOffsetX.getValue();
		final int offsetY = (int) this.spinnerOffsetY.getValue();
		final int width = (int) this.spinnerWidth.getValue();
		final int height = (int) this.spinnerHeight.getValue();
		final boolean leftover = this.checkboxLeftover.isSelected();

		new Thread(() ->
		           {
			           try
			           {
				           this.separate_(file, offsetX, offsetY, width, height, leftover);
			           }
			           catch (Exception ex)
			           {
				           JOptionPane.showMessageDialog(this.frame, "An error occured while generating Sub-Textures: "
					                                                     + ex.getLocalizedMessage(), "Error",
				                                         JOptionPane.ERROR_MESSAGE);
				           ex.printStackTrace();
			           }
		           }).start();
	}

	public void separate_(File file, int offsetX, int offsetY, int width, int height, boolean leftover) throws Exception
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
			int i = JOptionPane.showConfirmDialog(this.frame, "Are you sure you want to generate " + totalCount
				                                                  + " Sub-Texture files?", "Confirm large Operation",
			                                      JOptionPane.YES_NO_OPTION);
			if (i != JOptionPane.YES_OPTION)
			{
				// Abort
				return;
			}
		}

		this.buttonCancel.setEnabled(true);
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
					int x1 = offsetX + x * width;
					int y1 = offsetY + y * height;
					BufferedImage subimage = image.getSubimage(x1, y1, width, height);
					// Swapped order to go by rows
					File newFile = new File(parent, "texture_" + y + "_" + x + ".png");
					ImageIO.write(subimage, "png", newFile);

					count++;
					this.progressBar.setValue(count);
					this.progressBar.setString(count + " / " + totalCount);
				}
				catch (Exception ex)
				{
				}
			}
		}

		if (leftover)
		{
			final BufferedImage leftoverImage = image.getSubimage(0, 0, imageWidth, imageHeight);
			final Graphics2D graphics = leftoverImage.createGraphics();
			graphics.fillRect(offsetX, offsetY, width * countX, height * countY);

			final File leftoverFile = new File(parent, "leftover.png");
			ImageIO.write(leftoverImage, "png", leftoverFile);
		}

		time = System.currentTimeMillis() - time;
		float seconds = time / 1000F;

		String message = String.format(
			"Successfully generated %d / %d Sub-Textures.\nTime: %.2f s (%.3f s / ST, %.1f ST / s).", count, totalCount,
			seconds, seconds / count, count / seconds);
		JOptionPane.showMessageDialog(this.frame, message, "Texture Separator", JOptionPane.INFORMATION_MESSAGE);

		this.cancel = false;
		this.buttonCancel.setEnabled(false);
	}
}