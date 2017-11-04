package application;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import model.Utilisateur;
import xml.FileSaver;
import xml.UserParser;

public class MainController implements Initializable {

	Utilisateur user = new Utilisateur();
	File userFile = new File("user.xml");
	FileSaver fileSaver;

	@FXML
	Label userLbl = new Label();
	@FXML
	Button addSchoolBut = new Button();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		user = (userFile.exists()) ? loaduser() : createUser();
		
		userLbl.setText(user.getNom() + " " + user.getPrenom());
		
	}

	private Utilisateur createUser() {
		NewUserWindow.display(user);
		saveFile();
		return user;
	}

	private Utilisateur loaduser() {

		UserParser parser = new UserParser();
		user = parser.getUser();

		return user;
	}

	public void saveFile() {

		fileSaver = new FileSaver(user);
		fileSaver.writeXml();

	}
	
	public void openSchools(){
		NewSchoolWindow.display(user);
	}
}
