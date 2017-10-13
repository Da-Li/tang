package com.migudm.tang.code_generate.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.controlsfx.dialog.Wizard;

/**
 * Created by lixiaoshenxian on 2017/9/30.
 */
public class CodeGenerate extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception {
		Wizard wizard = new Wizard();
		wizard.setTitle("Tang Code-Generate");
		// config page
		FXMLLoader fxmlLoader = new FXMLLoader();
//		Parent settingPagePanel = fxmlLoader.load(getClass().getResourceAsStream("/test.fxml"));
		Parent settingPagePanel = fxmlLoader.load(getClass().getResourceAsStream("/settingPageConfig.fxml"));
//		WizardPane settingPage = new WizardPane();
//		settingPage.setContent(settingPagePanel);
		Scene scene = new Scene(settingPagePanel, 1190, 850);
		primaryStage.setTitle("Tang Code-Generate");
		primaryStage.setScene(scene);
		primaryStage.show();
		//todo liyu 分多页面
//		wizard.setFlow(new Wizard.LinearFlow(settingPage));
//
//		// show wizard and wait for response
//
//		wizard.showAndWait().ifPresent(result -> {
//			if (result == ButtonType.FINISH) {
//				System.out.println("Wizard finished, settings: " + wizard.getSettings());
//			} else if (result == ButtonType.NEXT) {
//				System.out.println("NEXT");
//			}
//		});

	}

	public static void main(String[] args) {
		CodeGenerate.launch(args);
	}

}
