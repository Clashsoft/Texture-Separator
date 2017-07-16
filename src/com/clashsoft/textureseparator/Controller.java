package com.clashsoft.textureseparator;

import javafx.application.Platform;
import javafx.beans.binding.When;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Optional;

public class Controller
{
	@FXML
	public Button separateButton;

	@FXML
	public TextField fileTextField;
	@FXML
	public Button    browseButton;

	@FXML
	public CheckBox leftoverCheckBox;

	@FXML
	public Spinner<Integer> widthSpinner;
	@FXML
	public Spinner<Integer> heightSpinner;

	@FXML
	public Spinner<Integer> offsetXSpinner;
	@FXML
	public Spinner<Integer> offsetYSpinner;

	@FXML
	public ProgressBar progressBar;

	private volatile int state = WAIT;

	private static final int WAIT   = 0;
	private static final int WORK   = 1;
	private static final int CANCEL = 2;

	@FXML
	public void initialize()
	{
		this.widthSpinner.setValueFactory(this.spinnerFactory(16));
		this.heightSpinner.setValueFactory(this.spinnerFactory(16));
		this.offsetXSpinner.setValueFactory(this.spinnerFactory(0));
		this.offsetYSpinner.setValueFactory(this.spinnerFactory(0));

		this.separateButton.textProperty().bind(
			new When(new SimpleIntegerProperty(this, "state").isEqualTo(WAIT)).then("Separate").otherwise("Cancel"));
	}

	private SpinnerValueFactory.IntegerSpinnerValueFactory spinnerFactory(int initialValue)
	{
		return new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, initialValue);
	}

	@FXML
	private void separateClicked()
	{
		if (this.state > WAIT)
		{
			this.state = CANCEL;
			return;
		}

		final File file = new File(this.fileTextField.getText());
		final int offsetX = this.offsetXSpinner.getValue();
		final int offsetY = this.offsetYSpinner.getValue();
		final int width = this.widthSpinner.getValue();
		final int height = this.heightSpinner.getValue();
		final boolean leftover = this.leftoverCheckBox.isSelected();

		new Thread(() ->
		           {
			           try
			           {
				           this.separate(file, offsetX, offsetY, width, height, leftover);
			           }
			           catch (Exception ex)
			           {
				           String message =
					           "An error occured while generating Sub-Textures: " + ex.getLocalizedMessage();
				           new Alert(Alert.AlertType.ERROR, message, ButtonType.OK).showAndWait();
				           ex.printStackTrace();
			           }
		           }).start();
	}

	private void separate(File file, int offsetX, int offsetY, int width, int height, boolean leftover) throws Exception
	{
		final BufferedImage image = ImageIO.read(file);
		final int imageWidth = image.getWidth();
		final int imageHeight = image.getHeight();

		final int countX = (imageWidth - offsetX) / width;
		final int countY = (imageHeight - offsetY) / height;

		int count = 0;
		final int totalCount = countX * countY;

		if (totalCount > 1024)
		{
			Platform.runLater(() -> this.askConfirmation(totalCount));

			while (this.state == WAIT)
			{
				Thread.sleep(30);
			}

			if (this.state == CANCEL)
			{
				this.state = WAIT;
				return;
			}
		}

		final String path = file.getAbsolutePath();
		final int index = path.lastIndexOf('.');
		if (index == -1)
		{
			this.state = WAIT;
			throw new IllegalArgumentException("Invalid File");
		}

		final File parent = new File(path.substring(0, index));
		//noinspection ResultOfMethodCallIgnored
		parent.mkdirs();

		this.state = WORK;
		long time = System.currentTimeMillis();
		long lastTime = time;

		for (int x = 0; x < countX; x++)
		{
			for (int y = 0; y < countY; y++)
			{
				if (this.state == CANCEL)
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

					final long now = System.currentTimeMillis();
					if (now - lastTime >= 20)
					{
						lastTime = now;
						final int finalCount = count;
						Platform.runLater(() -> this.updateProgress((double) finalCount / totalCount));
					}
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}

		time = System.currentTimeMillis() - time;
		final float seconds = time / 1000F;

		if (leftover && this.state != CANCEL)
		{
			final BufferedImage leftoverImage = image.getSubimage(0, 0, imageWidth, imageHeight);
			final Graphics2D graphics = leftoverImage.createGraphics();
			graphics.fillRect(offsetX, offsetY, width * countX, height * countY);

			final File leftoverFile = new File(parent, "leftover.png");
			ImageIO.write(leftoverImage, "png", leftoverFile);
		}

		final int finalCount = count;
		Platform.runLater(() -> this.onSuccess(totalCount, seconds, finalCount));

		this.state = WAIT;
	}

	private void updateProgress(double value)
	{
		this.progressBar.setProgress(value);
	}

	private void onSuccess(int totalCount, float seconds, int finalCount)
	{
		this.updateProgress(1);

		final String message = String.format(
			"Successfully generated %d / %d Sub-Textures.\nTime: %.2f s (%.1f t/s, %.3f s/t).",
			finalCount, totalCount, seconds, finalCount / seconds, seconds / finalCount);
		new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK).showAndWait();
	}

	private void askConfirmation(int totalCount)
	{
		final String message =
			"Are you sure you want to generate " + totalCount + " Sub-Texture files?";
		final Optional<ButtonType> result = new Alert(Alert.AlertType.CONFIRMATION, message,
		                                              ButtonType.YES, ButtonType.CANCEL)
			.showAndWait();
		if (!result.isPresent() || result.get() != ButtonType.YES)
		{
			// Abort
			this.state = CANCEL;
		}
		else
		{
			this.state = WORK;
		}
	}

	@FXML
	public void browseClicked()
	{
		final FileChooser fileChooser = new FileChooser();
		final File file = fileChooser.showOpenDialog(null);
		if (file != null)
		{
			this.fileTextField.setText(file.getAbsolutePath());
		}
	}
}
