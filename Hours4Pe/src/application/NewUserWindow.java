package application;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Utilisateur;

public class NewUserWindow {

	public static void display(Utilisateur user) {

		Stage window = new Stage();
		Scene scene;
		Label label = new Label("Création de l'utilisateur");
		TextField nameTf = new TextField();
		nameTf.setPromptText("Nom");
		TextField prenomTf = new TextField();
		prenomTf.setPromptText("Prénom");

		nameTf.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldValue, Boolean newValue) {
				if (!newValue) {
					String str = (nameTf.getLength() != 0) ? nameTf.getText() : "";
					if (nameTf.getLength() != 0 && nameTf != null) {
						str = str.substring(0, 1).toUpperCase() + str.substring(1);
						nameTf.setText(str);
					}
				}
			}

		});

		prenomTf.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldValue, Boolean newValue) {
				if (!newValue) {
					String str = (prenomTf.getLength() != 0) ? prenomTf.getText() : "";
					if (prenomTf.getLength() != 0 && prenomTf != null) {
						str = str.substring(0, 1).toUpperCase() + str.substring(1);
						prenomTf.setText(str);
					}
				}
			}

		});

		Button okBut = new Button("OK");
		okBut.setOnAction(e -> {
			user.setNom(nameTf.getText());
			user.setPrenom(prenomTf.getText());
			window.close();

		});

		window.setTitle("Hours4Pe - nouvel utilisateur");
		window.initModality(Modality.APPLICATION_MODAL);

		VBox vb = new VBox();
		vb.setPadding(new Insets(10));
		vb.setSpacing(5);
		HBox hb = new HBox();
		hb.setSpacing(10);

		hb.getChildren().addAll(nameTf, prenomTf);
		vb.getChildren().addAll(label, hb, okBut);

		scene = new Scene(vb, 320, 110);
		window.setScene(scene);
		window.showAndWait();

	}

}
