package application;

import java.time.DayOfWeek;
import java.time.Duration;
import java.util.function.UnaryOperator;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Adresse;
import model.Ecole;
import model.Utilisateur;
import xml.FileSaver;

public class NewSchoolWindow {

	private static Label nom, direction, numero, rue, zip, ville, horaire, lundi, mardi, mercredi, jeudi, vendredi,
			info;
	private static TextField nomTf, directionTf, numeroTf, rueTf, zipTf, villeTf, lundiTf, mardiTf, mercrediTf, jeudiTf,
			vendrediTf;

	public static void display(Utilisateur user) {

		Stage window = new Stage();
		Scene scene;
		Button saveSchoolBut = new Button("Enregistrer");
		Label label = new Label("Ajouter une école");

		UnaryOperator<Change> txtFilter = getFilter();

		GridPane gp = new GridPane();
		gp.setAlignment(Pos.CENTER);
		gp.setHalignment(saveSchoolBut, HPos.RIGHT);
		gp.setHgap(10);
		gp.setVgap(10);
		gp.setPadding(new Insets(25, 25, 25, 25));

		label.setFont(new Font(18));
		nom = new Label("Nom");
		direction = new Label("Direction");
		numero = new Label("N°");
		rue = new Label("Rue");
		zip = new Label("Code Postal");
		ville = new Label("Ville");
		horaire = new Label("Horaires:");
		lundi = new Label("Lundi");
		mardi = new Label("Mardi");
		mercredi = new Label("Mercredi");
		jeudi = new Label("Jeudi");
		vendredi = new Label("Vendredi");
		info = new Label("");

		nomTf = new TextField();
		nomTf.setPromptText("ex. \"Garibaldi\"");
		nomTf.setTextFormatter(getTextFormatter());
		directionTf = new TextField();
		directionTf.setPromptText("ex. \"Patrick Dupond\"");
		directionTf.setTextFormatter(getTextFormatter());
		numeroTf = new TextField();
		numeroTf.setPromptText("ex. \"42\"");
		numeroTf.setTextFormatter(new TextFormatter<String>(getNumFilter()));
		zipTf = new TextField();
		zipTf.setPromptText("ex. \"26100\"");
		zipTf.setTextFormatter(new TextFormatter<String>(getNumFilter()));
		villeTf = new TextField();
		villeTf.setPromptText("ex. \"Bourg lès Valence\"");
		villeTf.setTextFormatter(getTextFormatter());
		rueTf = new TextField();
		rueTf.setPromptText("ex. \"rue des Hirondelles\"");
		rueTf.setTextFormatter(getTextFormatter());
		lundiTf = new TextField();
		lundiTf.setPromptText("ex. \"8\"");
		lundiTf.setMaxWidth(55);
		lundiTf.setTextFormatter(new TextFormatter<String>(getNumFilter()));
		mardiTf = new TextField();
		mardiTf.setPromptText("ex. \"8\"");
		mardiTf.setMaxWidth(55);
		mardiTf.setTextFormatter(new TextFormatter<String>(getNumFilter()));
		mercrediTf = new TextField();
		mercrediTf.setPromptText("ex. \"8\"");
		mercrediTf.setMaxWidth(55);
		mercrediTf.setTextFormatter(new TextFormatter<String>(getNumFilter()));
		jeudiTf = new TextField();
		jeudiTf.setPromptText("ex. \"8\"");
		jeudiTf.setMaxWidth(55);
		jeudiTf.setTextFormatter(new TextFormatter<String>(getNumFilter()));
		vendrediTf = new TextField();
		vendrediTf.setPromptText("ex. \"8\"");
		vendrediTf.setMaxWidth(55);
		vendrediTf.setTextFormatter(new TextFormatter<String>(getNumFilter()));

		gp.add(nom, 0, 0);
		gp.add(nomTf, 1, 0);
		gp.add(direction, 0, 1);
		gp.add(directionTf, 1, 1);
		gp.add(numero, 0, 2);
		gp.add(numeroTf, 1, 2);
		gp.add(rue, 0, 3);
		gp.add(rueTf, 1, 3);
		gp.add(zip, 0, 4);
		gp.add(zipTf, 1, 4);
		gp.add(ville, 0, 5);
		gp.add(villeTf, 1, 5);
		gp.add(lundi, 0, 6);
		gp.add(lundiTf, 1, 6);
		gp.add(mardi, 0, 7);
		gp.add(mardiTf, 1, 7);
		gp.add(mercredi, 0, 8);
		gp.add(mercrediTf, 1, 8);
		gp.add(jeudi, 0, 9);
		gp.add(jeudiTf, 1, 9);
		gp.add(vendredi, 0, 10);
		gp.add(vendrediTf, 1, 10);
		gp.add(saveSchoolBut, 1, 11);
		gp.add(info, 1, 12);

		saveSchoolBut.setOnAction(e -> {

			if (nomTf.getText().length() != 0 && directionTf.getText().length() != 0 && numeroTf.getText().length() != 0
					&& rueTf.getText().length() != 0 && zipTf.getText().length() != 0 && villeTf.getText().length() != 0
					&& lundiTf.getText().length() != 0 && mardiTf.getText().length() != 0
					&& mercrediTf.getText().length() != 0 && jeudiTf.getText().length() != 0
					&& vendrediTf.getText().length() != 0) {

				addSchool(user);
				window.close();
			} else {
				info.setStyle("-fx-text-fill: red");
				info.setText("Informations manquantes (Si pas d'heures remplir '0')");

			}

		});

		window.setTitle("Hours4Pe - nouvelle école");
		window.initModality(Modality.APPLICATION_MODAL);

		scene = new Scene(gp);
		window.setScene(scene);
		window.showAndWait();

	}

	private static void addSchool(Utilisateur user) {

		Ecole newSchool = new Ecole();
		Adresse newSchoolAdress = new Adresse(Integer.parseInt(numeroTf.getText()), rueTf.getText(),
				Integer.parseInt(zipTf.getText()), villeTf.getText());

		newSchool.setNom(nomTf.getText());
		newSchool.setDirection(directionTf.getText());
		newSchool.setAdresse(newSchoolAdress);
		newSchool.getHoraires().put(DayOfWeek.MONDAY, Duration.ofMinutes(Long.parseLong(lundiTf.getText()) * 60));
		newSchool.getHoraires().put(DayOfWeek.TUESDAY, Duration.ofMinutes(Long.parseLong(mardiTf.getText()) * 60));
		newSchool.getHoraires().put(DayOfWeek.WEDNESDAY, Duration.ofMinutes(Long.parseLong(mercrediTf.getText()) * 60));
		newSchool.getHoraires().put(DayOfWeek.THURSDAY, Duration.ofMinutes(Long.parseLong(jeudiTf.getText()) * 60));
		newSchool.getHoraires().put(DayOfWeek.FRIDAY, Duration.ofMinutes(Long.parseLong(vendrediTf.getText()) * 60));

		user.getEcoles().add(newSchool);
		FileSaver fs = new FileSaver(user);
		fs.writeXml();
		
	}

	private static TextFormatter<String> getTextFormatter() {
		return new TextFormatter<String>(getFilter());
	}

	private static UnaryOperator<Change> getFilter() {
		return change -> {
			String text = change.getText();

			if (!change.isContentChange()) {
				return change;
			}

			if (text.matches("[a-zA-Z\\-.\\s]*") || text.isEmpty()) {
				return change;
			}
			return null;
		};
	}

	private static UnaryOperator<Change> getNumFilter() {
		return change -> {
			String text = change.getText();

			if (!change.isContentChange()) {
				return change;
			}

			if (text.matches("[0-9.]*") || text.isEmpty()) {
				return change;
			}
			return null;
		};
	}

}
