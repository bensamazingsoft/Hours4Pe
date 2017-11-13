package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LongStringConverter;
import model.Ecole;
import model.EcoleData;
import model.Utilisateur;
import xml.FileSaver;
import xml.UserParser;

public class Main extends Application {

	// user data
	public static Utilisateur user = new Utilisateur();
	private static File userFile = new File("user.xml");
	public static FileSaver fileSaver;
	public static Properties prop = new Properties();

	// Parents
	private static BorderPane rootBp = new BorderPane();
	private static BorderPane calendarPane = new BorderPane();
	private static TilePane calendarTp = new TilePane();
	private HBox infosHb = new HBox(thisMonthLbl, totalLbl);
	private VBox tables = new VBox(infosHb, schoolTable, cumulTable);
	private static ContextMenu contextMn = new ContextMenu();
	private static TableView<Ecole> schoolTable = new TableView<>();
	private static TableView<EcoleData> cumulTable = new TableView<>();

	// Controls
	private static Label userLbl = new Label();
	private static Label thisMonthLbl = new Label();
	private static Label totalLbl = new Label();
	private static Label month;
	private static Button addSchoolBut = new Button("Ajouter une école");

	// Collections
	private static Set<LocalDate> joursFeries = new HashSet<>();
	private static ObservableList<Ecole> schoolObsList;
	private static ObservableList<EcoleData> schoolCumulHoursData;
	private static ArrayList<StackPane> selectedCalTile = new ArrayList<>();
	private static Map<StackPane, LocalDate> stackDate = new HashMap<StackPane, LocalDate>();

	// Objects
	private static LocalDate currDate = LocalDate.now();
	private static AnneeScolaire currAnneeSco;
	private static Holidays.ZONE zone;
	private static Holidays toussaint;
	private static Holidays noel;
	private static Holidays hiver;
	private static Holidays paques;
	private static Holidays ete;

	// States
	public static boolean firstUse;

	@SuppressWarnings({ "static-access" })
	public void start(Stage primaryStage) {

		// Init App data
		firstUse = true;
		loadProperties();
		currAnneeSco = new AnneeScolaire(currDate);
		String[] dateFerieTab = prop.getProperty("joursFeries").split(",");
		for (String str : dateFerieTab) {
			joursFeries.add(LocalDate.parse(str));
		}

		// initialize user data
		try {
			user = (userFile.exists()) ? loaduser() : createUser();

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (user.getEcoles().isEmpty()) {
			NewSchoolWindow.display(user);
		}

		if (!user.getEcoles().isEmpty()) {
			makeHolidays();
			schoolObsList = FXCollections.observableArrayList(user.getEcoles());
			initTable();
			initCumulTable();
		}

		// build interface
		initContextMenu();

		userLbl.setText(user.getNom() + " " + user.getPrenom() + " - " + currAnneeSco.getAnneeSco());
		userLbl.setStyle(
				"-fx-font-weight: bold; -fx-font-size: 26; -fx-effect : dropshadow(gaussian , #959595 , 5,0,3,3 ); -fx-text-fill : white");

		addSchoolBut.setOnAction(e -> openSchools());

		VBox topVp = new VBox(userLbl, addSchoolBut);
		topVp.setSpacing(10);

		rootBp.setPadding(new Insets(25, 25, 25, 25));
		rootBp.setStyle("-fx-background-color : linear-gradient(from 0% 0% to 0% 50%, #adc3ec, #ffffff)");
		rootBp.setTop(topVp);
		rootBp.setCenter(calendarPane);

		month = new Label((Month.of(currDate.getMonthValue()).getDisplayName(TextStyle.FULL, Locale.FRANCE)));
		month.setStyle("-fx-font-size: 18");

		Button nextMonth = new Button("->");
		nextMonth.setOnAction(e -> {
			currDate = currDate.plusMonths(1);
			currAnneeSco = new AnneeScolaire(currDate);
			selectedCalTile.clear();
			initcalendar();
			initInfos();
		});
		Button prevMonth = new Button("<-");
		prevMonth.setOnAction(e -> {
			currDate = currDate.plusMonths(-1);
			currAnneeSco = new AnneeScolaire(currDate);
			selectedCalTile.clear();
			initcalendar();
			initInfos();
		});
		nextMonth.setStyle(
				"-fx-border-color : lightgrey; -fx-background-color: transparent; -fx-font-weight: bold; -fx-font-size: 22");
		prevMonth.setStyle(
				"-fx-border-color : lightgrey; -fx-background-color: transparent; -fx-font-weight: bold; -fx-font-size: 22");

		HBox calendarTopHb = new HBox(prevMonth, month, nextMonth);
		// calendarTopHb.setSpacing(4);
		calendarTopHb.setPadding(new Insets(15, 15, 15, 15));
		;
		calendarTopHb.setAlignment(Pos.CENTER);
		calendarPane.setTop(calendarTopHb);

		calendarTp.setPadding(new Insets(5, 5, 5, 5));
		calendarTp.setVgap(2);
		calendarTp.setHgap(2);

		initcalendar();

		calendarPane.setCenter(calendarTp);
		calendarPane.setMinWidth(460);
		calendarPane.setMaxWidth(460);
		calendarPane.setMinHeight(470);
		calendarPane.setMaxHeight(470);
		calendarPane.setAlignment(topVp, Pos.TOP_CENTER);

		tables.setPadding(new Insets(20, 20, 20, 20));
		tables.setSpacing(20);
		tables.setAlignment(Pos.CENTER);
		infosHb.setSpacing(20);
		infosHb.setStyle("-fx-font-size : 22");
		rootBp.setRight(tables);

		initInfos();

		Scene scene = new Scene(rootBp);
		primaryStage.setTitle("Ben'sAmazingSoft - Hours4Pe");
		primaryStage.setOnCloseRequest(e -> {
			FileSaver fs = new FileSaver(user);
			fs.writeXml();
		});
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	@SuppressWarnings("unchecked")
	public static void initCumulTable() {

		ArrayList<EcoleData> cumulTableData = new ArrayList<>();
		user.getEcoles().forEach(ecole -> {
			cumulTableData.add(new EcoleData(user, ecole));
		});
		schoolCumulHoursData = FXCollections.observableArrayList(cumulTableData);

		TableColumn<EcoleData, String> nomCol = new TableColumn<>("nom");
		nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
		TableColumn<EcoleData, Long> septCol = new TableColumn<>("sept");
		septCol.setCellValueFactory(new PropertyValueFactory<>("sept"));
		TableColumn<EcoleData, Long> octCol = new TableColumn<>("oct");
		octCol.setCellValueFactory(new PropertyValueFactory<>("oct"));
		TableColumn<EcoleData, Long> novCol = new TableColumn<>("nov");
		novCol.setCellValueFactory(new PropertyValueFactory<>("nov"));
		TableColumn<EcoleData, Long> decCol = new TableColumn<>("dec");
		decCol.setCellValueFactory(new PropertyValueFactory<>("dec"));
		TableColumn<EcoleData, Long> janvCol = new TableColumn<>("janv");
		janvCol.setCellValueFactory(new PropertyValueFactory<>("janv"));
		TableColumn<EcoleData, Long> fevCol = new TableColumn<>("fev");
		fevCol.setCellValueFactory(new PropertyValueFactory<>("fev"));
		TableColumn<EcoleData, Long> marCol = new TableColumn<>("mar");
		marCol.setCellValueFactory(new PropertyValueFactory<>("mar"));
		TableColumn<EcoleData, Long> avrCol = new TableColumn<>("avr");
		avrCol.setCellValueFactory(new PropertyValueFactory<>("avr"));
		TableColumn<EcoleData, Long> maiCol = new TableColumn<>("mai");
		maiCol.setCellValueFactory(new PropertyValueFactory<>("mai"));
		TableColumn<EcoleData, Long> juinCol = new TableColumn<>("juin");
		juinCol.setCellValueFactory(new PropertyValueFactory<>("juin"));
		TableColumn<EcoleData, Long> juillCol = new TableColumn<>("juill");
		juillCol.setCellValueFactory(new PropertyValueFactory<>("juill"));

		cumulTable.setItems(schoolCumulHoursData);
		cumulTable.getColumns().clear();
		cumulTable.getColumns().addAll(nomCol, septCol, octCol, novCol, decCol, janvCol, fevCol, marCol, avrCol, maiCol,
				juinCol, juillCol);

	}

	@SuppressWarnings("unchecked")
	public static void initTable() {

		schoolTable.setEditable(true);

		schoolObsList = FXCollections.observableArrayList(user.getEcoles());

		TableColumn<Ecole, String> nameCol = new TableColumn<>("Nom");
		nameCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
		nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
		nameCol.setOnEditCommit(new EventHandler<CellEditEvent<Ecole, String>>() {
			@Override
			public void handle(CellEditEvent<Ecole, String> t) {
				((Ecole) t.getTableView().getItems().get(t.getTablePosition().getRow())).setNom((t.getNewValue()));
			}
		});
		TableColumn<Ecole, Integer> kmsCol = new TableColumn<>("Kms");
		kmsCol.setCellValueFactory(new PropertyValueFactory<>("kms"));
		kmsCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
		kmsCol.setOnEditCommit(new EventHandler<CellEditEvent<Ecole, Integer>>() {
			@Override
			public void handle(CellEditEvent<Ecole, Integer> t) {
				((Ecole) t.getTableView().getItems().get(t.getTablePosition().getRow())).setKms((t.getNewValue()));
				initInfos();
				initCumulTable();
			}
		});

		TableColumn<Ecole, Long> mardiCol = new TableColumn<Ecole, Long>("Ma");
		mardiCol.setCellValueFactory(new PropertyValueFactory<>("mardi"));
		mardiCol.setCellFactory(TextFieldTableCell.forTableColumn(new LongStringConverter()));
		mardiCol.setOnEditCommit(new EventHandler<CellEditEvent<Ecole, Long>>() {
			@Override
			public void handle(CellEditEvent<Ecole, Long> t) {
				Ecole ecole = (Ecole) t.getTableView().getItems().get(t.getTablePosition().getRow());
				ecole.getHoraires().remove(DayOfWeek.TUESDAY);
				ecole.getHoraires().put(DayOfWeek.TUESDAY, Duration.ofHours((t.getNewValue())));
				user.getEcoles().clear();
				user.getEcoles().addAll(schoolObsList);
				initInfos();
				initCumulTable();
			}
		});
		TableColumn<Ecole, Integer> horaireCol = new TableColumn<>("Horaires");
		TableColumn<Ecole, Long> lundiCol = new TableColumn<Ecole, Long>("Lu");
		lundiCol.setCellValueFactory(new PropertyValueFactory<>("lundi"));
		lundiCol.setCellFactory(TextFieldTableCell.forTableColumn(new LongStringConverter()));
		lundiCol.setOnEditCommit(new EventHandler<CellEditEvent<Ecole, Long>>() {
			@Override
			public void handle(CellEditEvent<Ecole, Long> t) {
				Ecole ecole = (Ecole) t.getTableView().getItems().get(t.getTablePosition().getRow());
				ecole.getHoraires().remove(DayOfWeek.MONDAY);
				ecole.getHoraires().put(DayOfWeek.MONDAY, Duration.ofHours((t.getNewValue())));
				user.getEcoles().clear();
				user.getEcoles().addAll(schoolObsList);
				initInfos();
				initCumulTable();
			}
		});
		TableColumn<Ecole, Long> mercrediCol = new TableColumn<Ecole, Long>("Me");
		mercrediCol.setCellValueFactory(new PropertyValueFactory<>("mercredi"));
		mercrediCol.setCellFactory(TextFieldTableCell.forTableColumn(new LongStringConverter()));
		mercrediCol.setOnEditCommit(new EventHandler<CellEditEvent<Ecole, Long>>() {
			@Override
			public void handle(CellEditEvent<Ecole, Long> t) {
				Ecole ecole = (Ecole) t.getTableView().getItems().get(t.getTablePosition().getRow());
				ecole.getHoraires().remove(DayOfWeek.WEDNESDAY);
				ecole.getHoraires().put(DayOfWeek.WEDNESDAY, Duration.ofHours((t.getNewValue())));
				user.getEcoles().clear();
				user.getEcoles().addAll(schoolObsList);
				initInfos();
				initCumulTable();
			}
		});
		TableColumn<Ecole, Long> jeudiCol = new TableColumn<Ecole, Long>("Je");
		jeudiCol.setCellValueFactory(new PropertyValueFactory<>("jeudi"));
		jeudiCol.setCellFactory(TextFieldTableCell.forTableColumn(new LongStringConverter()));
		jeudiCol.setOnEditCommit(new EventHandler<CellEditEvent<Ecole, Long>>() {
			@Override
			public void handle(CellEditEvent<Ecole, Long> t) {
				Ecole ecole = (Ecole) t.getTableView().getItems().get(t.getTablePosition().getRow());
				ecole.getHoraires().remove(DayOfWeek.THURSDAY);
				ecole.getHoraires().put(DayOfWeek.THURSDAY, Duration.ofHours((t.getNewValue())));
				user.getEcoles().clear();
				user.getEcoles().addAll(schoolObsList);
				initInfos();
				initCumulTable();
			}
		});
		TableColumn<Ecole, Long> vendrediCol = new TableColumn<Ecole, Long>("Ve");
		vendrediCol.setCellValueFactory(new PropertyValueFactory<>("vendredi"));
		vendrediCol.setCellFactory(TextFieldTableCell.forTableColumn(new LongStringConverter()));
		vendrediCol.setOnEditCommit(new EventHandler<CellEditEvent<Ecole, Long>>() {
			@Override
			public void handle(CellEditEvent<Ecole, Long> t) {
				Ecole ecole = (Ecole) t.getTableView().getItems().get(t.getTablePosition().getRow());
				ecole.getHoraires().remove(DayOfWeek.FRIDAY);
				ecole.getHoraires().put(DayOfWeek.FRIDAY, Duration.ofHours((t.getNewValue())));
				user.getEcoles().clear();
				user.getEcoles().addAll(schoolObsList);
				initInfos();
				initCumulTable();
			}
		});

		horaireCol.getColumns().addAll(lundiCol, mardiCol, mercrediCol, jeudiCol, vendrediCol);

		schoolTable.setItems(schoolObsList);
		schoolTable.getColumns().clear();
		schoolTable.getColumns().addAll(nameCol, kmsCol, horaireCol);

	}

	private void makeHolidays() {
		LocalDate toussaintastart = LocalDate.parse(prop.getProperty("toussaintAstart"));
		LocalDate toussaintaend = LocalDate.parse(prop.getProperty("toussaintAend"));
		LocalDate toussaintbstart = LocalDate.parse(prop.getProperty("toussaintBstart"));
		LocalDate toussaintbend = LocalDate.parse(prop.getProperty("toussaintBend"));
		LocalDate toussaintcstart = LocalDate.parse(prop.getProperty("toussaintCstart"));
		LocalDate toussaintcend = LocalDate.parse(prop.getProperty("toussaintCend"));
		toussaint = new Holidays(toussaintastart, toussaintaend, toussaintbstart, toussaintbend, toussaintcstart,
				toussaintcend);

		LocalDate noelastart = LocalDate.parse(prop.getProperty("noelAstart"));
		LocalDate noelaend = LocalDate.parse(prop.getProperty("noelAend"));
		LocalDate noelbstart = LocalDate.parse(prop.getProperty("noelBstart"));
		LocalDate noelbend = LocalDate.parse(prop.getProperty("noelBend"));
		LocalDate noelcstart = LocalDate.parse(prop.getProperty("noelCstart"));
		LocalDate noelcend = LocalDate.parse(prop.getProperty("noelCend"));
		noel = new Holidays(noelastart, noelaend, noelbstart, noelbend, noelcstart, noelcend);

		LocalDate hiverastart = LocalDate.parse(prop.getProperty("hiverAstart"));
		LocalDate hiveraend = LocalDate.parse(prop.getProperty("hiverAend"));
		LocalDate hiverbstart = LocalDate.parse(prop.getProperty("hiverBstart"));
		LocalDate hiverbend = LocalDate.parse(prop.getProperty("hiverBend"));
		LocalDate hivercstart = LocalDate.parse(prop.getProperty("hiverCstart"));
		LocalDate hivercend = LocalDate.parse(prop.getProperty("hiverCend"));
		hiver = new Holidays(hiverastart, hiveraend, hiverbstart, hiverbend, hivercstart, hivercend);

		LocalDate paquesastart = LocalDate.parse(prop.getProperty("paquesAstart"));
		LocalDate paquesaend = LocalDate.parse(prop.getProperty("paquesAend"));
		LocalDate paquesbstart = LocalDate.parse(prop.getProperty("paquesBstart"));
		LocalDate paquesbend = LocalDate.parse(prop.getProperty("paquesBend"));
		LocalDate paquescstart = LocalDate.parse(prop.getProperty("paquesCstart"));
		LocalDate paquescend = LocalDate.parse(prop.getProperty("paquesCend"));
		paques = new Holidays(paquesastart, paquesaend, paquesbstart, paquesbend, paquescstart, paquescend);

		LocalDate eteastart = LocalDate.parse(prop.getProperty("eteAstart"));
		LocalDate eteaend = LocalDate.parse(prop.getProperty("eteAend"));
		LocalDate etebstart = LocalDate.parse(prop.getProperty("eteBstart"));
		LocalDate etebend = LocalDate.parse(prop.getProperty("eteBend"));
		LocalDate etecstart = LocalDate.parse(prop.getProperty("eteCstart"));
		LocalDate etecend = LocalDate.parse(prop.getProperty("eteCend"));
		ete = new Holidays(eteastart, eteaend, etebstart, etebend, etecstart, etecend);
	}

	public static void initcalendar() {

		userLbl.setText(user.getNom() + " " + user.getPrenom() + " - " + currAnneeSco.getAnneeSco());
		month.setText((Month.of(currDate.getMonthValue()).getDisplayName(TextStyle.FULL, Locale.FRANCE)));
		LocalDate startOfCalendar = (currDate.with(TemporalAdjusters.firstDayOfMonth())
				.getDayOfWeek() == DayOfWeek.MONDAY) ? currDate.with(TemporalAdjusters.firstDayOfMonth())
						: (currDate.with(TemporalAdjusters.firstDayOfMonth()))
								.with(TemporalAdjusters.previous(DayOfWeek.MONDAY));

		// infer holidays zone
		if (!user.getEcoles().isEmpty()) {
			// get the '76' out of '76330' as int
			int dpt = Integer
					.parseInt((String.valueOf(user.getEcoles().first().getAdresse().getCodePostal())).substring(0, 2));
			if (Holidays.getZoneDept().get(Holidays.ZONE.A).contains(dpt)) {
				zone = Holidays.ZONE.A;
			}
			if (Holidays.getZoneDept().get(Holidays.ZONE.B).contains(dpt)) {
				zone = Holidays.ZONE.B;
			}
			if (Holidays.getZoneDept().get(Holidays.ZONE.C).contains(dpt)) {
				zone = Holidays.ZONE.C;
			}

		}

		// populate calendar with tiles
		calendarTp.getChildren().clear();
		for (int i = 0; i < 40; i++) {
			calendarTp.getChildren().add(calTile(startOfCalendar, i));
		}
	}

	@SuppressWarnings("static-access")
	private static StackPane calTile(LocalDate startOfCalendar, int i) {
		StackPane calTile = new StackPane();
		calTile.setStyle("-fx-border-color : black");
		calTile.setPrefSize(60, 60);
		calTile.setPadding(new Insets(0, 3, 0, 0));

		int dayNb = startOfCalendar.plusDays(i).getDayOfMonth();
		Label dayNbLbl = new Label(
				startOfCalendar.plusDays(i).getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.FRANCE)
						+ String.valueOf(dayNb));
		// dayNbLbl.setStyle("-fx-font-size : 10");
		calTile.setAlignment(dayNbLbl, Pos.TOP_RIGHT);

		calTile.getChildren().add(dayNbLbl);

		if (startOfCalendar.plusDays(i).getDayOfWeek() == DayOfWeek.SATURDAY
				|| startOfCalendar.plusDays(i).getDayOfWeek() == DayOfWeek.SUNDAY) {
			calTile.setStyle("-fx-background-color: pink");
		}

		if (startOfCalendar.plusDays(i).getMonth() != currDate.getMonth()) {
			dayNbLbl.setStyle("-fx-text-fill: grey");
			calTile.setStyle("-fx-background-color: lightgrey");
		}

		if (isHoliday(startOfCalendar.plusDays(i))) {
			calTile.setStyle("-fx-background-color: lightgreen");
		}

		if (user.getPlanning().containsKey(startOfCalendar.plusDays(i))) {
			calTile.setStyle("-fx-background-color : lightblue");
			Label schoolLbl = new Label(
					user.getPlanning().get(startOfCalendar.plusDays(i)).getNom().substring(0, 3).toUpperCase());
			calTile.getChildren().add(schoolLbl);
			calTile.setAlignment(schoolLbl, Pos.CENTER);
		}

		if (startOfCalendar.plusDays(i).equals(LocalDate.now())) {
			calTile.setStyle("-fx-background-color: #f7fc76");
		}

		if (!isHoliday(startOfCalendar.plusDays(i)) && startOfCalendar.plusDays(i).getDayOfWeek() != DayOfWeek.SATURDAY
				&& startOfCalendar.plusDays(i).getDayOfWeek() != DayOfWeek.SUNDAY
				&& startOfCalendar.plusDays(i).getMonth().equals(currDate.getMonth())) {
			calTile.setOnContextMenuRequested(evt -> contextMn.show(calTile, evt.getScreenX(), evt.getScreenY()));
			calTile.setOnMouseClicked(evt -> {
				if (!evt.isPopupTrigger()) {
					if (!selectedCalTile.contains(calTile)) {
						calTile.setStyle("-fx-border-color : #7479e5; -fx-border-insets : 1; -fx-border-width : 3");
						selectedCalTile.add(calTile);
					} else {
						selectedCalTile.remove(calTile);
						calTile.setStyle("-fx-border-color : black; -fx-border-width : 1");
					}
				}
			});
		}
		stackDate.put(calTile, startOfCalendar.plusDays(i));

		return calTile;
	}

	public static void period(LocalDate start, LocalDate end, Ecole ecole) {
		ArrayList<LocalDate> days = new ArrayList<>();

		while (!start.equals(end)) {
			days.add(start);
			start = start.plusDays(1);
		}
		days.add(end);

		days.stream().filter(day -> !isHoliday(day)).forEach(day -> user.getPlanning().put(day, ecole));
		initcalendar();
		initInfos();
		initCumulTable();
	}

	private static void initInfos() {

		thisMonthLbl.setText(month.getText() + " : " + totalMois() + "h  " + totalKmsMois() + "kms");
		totalLbl.setText("\tTotal : " + totalHours() + "h  " + totalKms() + "  kms");

	}

	private static int totalKmsMois() {
		ArrayList<Integer> kms = new ArrayList<>();
		user.getPlanning().forEach((date, ecole) -> {
			if (date.getMonth() == currDate.getMonth()) {
				kms.add(ecole.getKms());
			}
		});
		return kms.stream().mapToInt(i -> i).sum();
	}

	private static int totalKms() {
		return user.getPlanning().values().stream().mapToInt(ecole -> ecole.getKms()).sum();
	}

	private static String totalHours() {
		ArrayList<Long> hours = new ArrayList<>();

		user.getPlanning().forEach((date, ecole) -> {

			if (currAnneeSco.isAnneeSco(date)) {
				hours.add(user.getPlanning().get(date).getHoraires().get(date.getDayOfWeek()).toHours());
			}

		});
		return (user.getPlanning().isEmpty()) ? "0" : String.valueOf(hours.stream().mapToLong(l -> l).sum());
	}

	private static String totalMois() {
		ArrayList<Long> hours = new ArrayList<>();

		user.getPlanning().forEach((date, ecole) -> {
			if (date.getMonth() == currDate.getMonth()) {
				hours.add(user.getPlanning().get(date).getHoraires().get(date.getDayOfWeek()).toHours());
			}
		});

		return (user.getPlanning().isEmpty()) ? "0" : String.valueOf(hours.stream().mapToLong(l -> l).sum());
	}

	private void initContextMenu() {

		contextMn.getItems().clear();
		user.getEcoles().stream().forEach(ecole -> {

			MenuItem mit = new MenuItem(ecole.getNom());
			mit.setOnAction(e -> {
				// add new school tag to calendar
				if (!selectedCalTile.isEmpty()) {
					selectedCalTile.stream().forEach(calTile -> {
						user.getPlanning().put(stackDate.get(calTile), ecole);
					});
					selectedCalTile.clear();
				} else {
					ContextMenu parent = (ContextMenu) ((MenuItem) (e.getSource())).getParentPopup();
					StackPane stack = (StackPane) (parent.getOwnerNode());
					user.getPlanning().put(stackDate.get(stack), ecole);
				}
				initcalendar();
				initInfos();
				initCumulTable();
			});
			contextMn.getItems().add(mit);
		});

		contextMn.getItems().add(new SeparatorMenuItem());
		MenuItem removeDayTag = new MenuItem("Supprimer");
		contextMn.getItems().add(removeDayTag);

		removeDayTag.setOnAction(e -> {

			if (!selectedCalTile.isEmpty()) {
				selectedCalTile.stream().forEach(calTile -> {
					if (stackDate.containsKey(calTile)) {
						user.getPlanning().remove(stackDate.get(calTile));
					}
				});
				selectedCalTile.clear();
			}

			ContextMenu parent = (ContextMenu) ((MenuItem) (e.getSource())).getParentPopup();
			StackPane stack = (StackPane) (parent.getOwnerNode());
			if (stackDate.containsKey(stack)) {
				user.getPlanning().remove(stackDate.get(stack));
			}
			initcalendar();
			initInfos();
			initCumulTable();
		});

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

		firstUse = false;
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

	public void loadProperties() {
		FileInputStream input = null;

		try {
			input = new FileInputStream(new File("Hours4Pe.properties"));
		} catch (FileNotFoundException e1) {
			SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, e1.getMessage(), "error in properties",
					JOptionPane.ERROR_MESSAGE));
			e1.printStackTrace();
		}

		try {
			prop.load(input);
		} catch (final IOException e) {
			SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, e.getMessage(), "error in properties",
					JOptionPane.ERROR_MESSAGE));
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, e.getMessage(), "error",
							JOptionPane.ERROR_MESSAGE));
					e.printStackTrace();
				}
			}

		}
	}

	public static boolean isHoliday(LocalDate date) {
		boolean isit = false;
		if (toussaint.isHolyDay(zone, date)) {
			isit = true;
		}

		if (noel.isHolyDay(zone, date)) {
			isit = true;
		}

		if (hiver.isHolyDay(zone, date)) {
			isit = true;
		}

		if (paques.isHolyDay(zone, date)) {
			isit = true;
		}

		if (ete.isHolyDay(zone, date)) {
			isit = true;
		}

		if (joursFeries.contains(date)) {
			isit = true;
		}
		return isit;
	}

	/**
	 * @return the firstUse
	 */
	public static boolean isFirstUse() {
		return firstUse;
	}

	/**
	 * @param firstUse
	 *            the firstUse to set
	 */
	public static void setFirstUse(boolean firstUse) {
		Main.firstUse = firstUse;
	}

}
