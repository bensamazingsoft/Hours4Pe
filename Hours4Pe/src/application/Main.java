package application;

import java.io.File;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import model.Utilisateur;
import xml.FileSaver;
import xml.UserParser;

public class Main extends Application {

	public static Utilisateur user = new Utilisateur();
	private static File userFile = new File("user.xml");
	public static FileSaver fileSaver;
	private static Label userLbl = new Label();
	private Button addSchoolBut = new Button("Ajouter une école");
	private LocalDate currDate = LocalDate.now();
	private TemporalAdjuster lastMonday = TemporalAdjusters.previous(DayOfWeek.MONDAY);
	private BorderPane calendarPane = new BorderPane();
	private ContextMenu contextMn = new ContextMenu();
	private Map<StackPane, LocalDate> stackDate = new HashMap<StackPane, LocalDate>();

	@SuppressWarnings("static-access")
	public void start(Stage primaryStage) {
		userLbl.setStyle("-fx-font-weight: bold");
		userLbl.setStyle("-fx-font-size: 24");
		try {
			//initialize user data
			user = (userFile.exists()) ? loaduser() : createUser();

			//build context menu
initContextMenu();

			userLbl.setText(user.getNom() + " " + user.getPrenom());
			addSchoolBut.setOnAction(e -> openSchools());

			BorderPane rootBp = new BorderPane();
			AnchorPane topAp = new AnchorPane(userLbl, addSchoolBut);
			AnchorPane.setLeftAnchor(userLbl, 8.0);
			AnchorPane.setRightAnchor(addSchoolBut, 8.0);

			rootBp.setPadding(new Insets(25, 25, 25, 25));
			rootBp.setTop(topAp);
			rootBp.setAlignment(topAp, Pos.CENTER);

			calendarPane.setStyle("-fx-border-color: black");
			calendarPane.setAlignment(topAp, Pos.TOP_CENTER);
			rootBp.setCenter(calendarPane);
			Label month = new Label((Month.of(currDate.getMonthValue()).getDisplayName(TextStyle.FULL, Locale.FRANCE)));
			month.setStyle("-fx-font-size: 18");
			HBox calendarTopHb = new HBox(month);
			calendarTopHb.setPadding(new Insets(15, 15, 15, 15));
			;
			calendarTopHb.setAlignment(Pos.CENTER);
			calendarPane.setTop(calendarTopHb);

			TilePane calendarTp = new TilePane();
			calendarPane.setAlignment(calendarTp, Pos.TOP_CENTER);
			calendarTp.setPadding(new Insets(5, 5, 5, 5));
			calendarTp.setVgap(2);
			calendarTp.setHgap(2);

			calendarTp.setStyle("-fx-background-color: white");

			LocalDate startOfCalendar = (currDate.with(TemporalAdjusters.firstDayOfMonth())
					.getDayOfWeek() == DayOfWeek.MONDAY) ? currDate
							: (currDate.with(TemporalAdjusters.firstDayOfMonth()))
									.with(TemporalAdjusters.previous(DayOfWeek.MONDAY));

			for (int i = 0; i < 40; i++) {

				calendarTp.getChildren().add(calTile(startOfCalendar, i));
			}

			calendarPane.setCenter(calendarTp);

			Scene scene = new Scene(rootBp, 600, 600);
			primaryStage.setTitle("Ben'sAmazingSoft - Hours4Pe");
			primaryStage.setOnCloseRequest(e -> {
				FileSaver fs = new FileSaver(user);
				fs.writeXml();
			});
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("static-access")
	private void initContextMenu() {
		user.getEcoles().stream().forEach(ecole -> {
			MenuItem mit = new MenuItem(ecole.getNom());
			mit.setOnAction(e -> {
				//add new school tag to calendar
				ContextMenu parent = (ContextMenu) ((MenuItem) (e.getSource())).getParentPopup();
				StackPane stack = (StackPane) (parent.getOwnerNode());
				Label lbl = new Label(ecole.getNom().substring(0, 3).toUpperCase());
				stack.getChildren().add(lbl);
				stack.setAlignment(lbl, Pos.CENTER);
				//update user data
				user.getPlanning().put(stackDate.get(stack), ecole);
			});
			contextMn.getItems().add(mit);
		});
		
	}

	@SuppressWarnings("static-access")
	private StackPane calTile(LocalDate startOfCalendar, int i) {
		StackPane calTile = new StackPane();
		calTile.setStyle("-fx-border-color : black");
		calTile.setPrefSize(60, 60);
		
		int dayNb = startOfCalendar.plusDays(i).getDayOfMonth();
		Label dayNbLbl = new Label(String.valueOf(dayNb));
		calTile.setAlignment(dayNbLbl, Pos.TOP_RIGHT);
		
		if (startOfCalendar.plusDays(i).getDayOfWeek() == DayOfWeek.SATURDAY
				|| startOfCalendar.plusDays(i).getDayOfWeek() == DayOfWeek.SUNDAY) {
			calTile.setStyle("-fx-background-color: grey");
		}

		if (startOfCalendar.plusDays(i).getMonth() != currDate.getMonth()) {
			dayNbLbl.setStyle("-fx-text-fill: grey");
			calTile.setStyle("-fx-background-color: lightgrey");
		}
		
		calTile.getChildren().add(dayNbLbl);

		if (user.getPlanning().containsKey(startOfCalendar.plusDays(i))) {

			Label schoolLbl = new Label(
					user.getPlanning().get(startOfCalendar.plusDays(i)).getNom().substring(0, 3).toUpperCase());
			calTile.getChildren().add(schoolLbl);
			calTile.setAlignment(schoolLbl, Pos.CENTER);
		}

		calTile.setOnContextMenuRequested(evt -> contextMn.show(calTile, evt.getScreenX(), evt.getScreenY()));

		stackDate.put(calTile, startOfCalendar.plusDays(i));

		return calTile;
	}

	public static void main(String[] args) {

		launch(args);
	}

	private static Utilisateur createUser() {
		NewUserWindow.display(user);
		saveFile();
		return user;
	}

	private static Utilisateur loaduser() {

		UserParser parser = new UserParser();
		user = parser.getUser();

		return user;
	}

	public static void saveFile() {

		fileSaver = new FileSaver(user);
		fileSaver.writeXml();

	}

	public void openSchools() {
		NewSchoolWindow.display(user);
		contextMn.getItems().clear();
		initContextMenu();
		
	}
}
