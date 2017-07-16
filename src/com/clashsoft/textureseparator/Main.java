package com.clashsoft.textureseparator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

public class Main extends Application
{
	public static void main(String[] args)
	{
		launch(args);
	}

	@Override
	public void start(Stage primaryStage)
	{
		try (final InputStream inputStream = Main.class.getResource("MainUI.fxml").openStream();)
		{
			FXMLLoader loader = new FXMLLoader();
			final Parent root = loader.load(inputStream);
			primaryStage.setTitle("Texture Separator");
			primaryStage.setScene(new Scene(root));
			primaryStage.show();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
