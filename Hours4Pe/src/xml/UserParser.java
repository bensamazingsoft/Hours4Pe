package xml;

import static utils.LogGenerator.log;

import java.io.File;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;

import model.Adresse;
import model.Ecole;
import model.Utilisateur;

public class UserParser {

	File userFile;
	Utilisateur user;
	DocumentBuilderFactory factory;
	DocumentBuilder builder;
	SchemaFactory schemaFactory;
	Schema schema;
	Map<String, Ecole> ecoleNames = new HashMap<String, Ecole>();
	Document doc;

	public UserParser() {
		try {
			userFile = new File("user.xml");
			factory = DocumentBuilderFactory.newInstance();
			schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			schema = schemaFactory.newSchema(getClass().getClassLoader().getResource("xml/Hours4PeUserSchema.xsd"));
			factory.setSchema(schema);
			builder = factory.newDocumentBuilder();
			ErrorHandler errHandler = new ParseErrorHandler();
			builder.setErrorHandler(errHandler);
			doc = builder.parse(userFile);

			Element rootElt = doc.getDocumentElement();

			user = new Utilisateur();
			user.setUserId(Integer.valueOf(rootElt.getAttribute("id_user")));
			user.setNom(rootElt.getAttribute("nom_user"));
			user.setPrenom(rootElt.getAttribute("prenom"));

			NodeList ecoles = rootElt.getElementsByTagName("ecole");
			for (int i = 0; i < ecoles.getLength(); i++) {

				Element ecoleElt = (Element) ecoles.item(i);
				Node adresseNode = ecoleElt.getElementsByTagName("adresse").item(0);
				Element adresseElt = (Element) adresseNode;
				int numero = Integer.parseInt(adresseElt.getAttribute("numero"));
				String rue =adresseElt.getAttribute("rue");
				int zip = Integer.parseInt(adresseElt.getAttribute("zip"));
				String ville = adresseElt.getAttribute("ville");

				Adresse adresse = new Adresse(numero, rue, zip, ville);

				String nom = ecoleElt.getAttribute("nom_ecole");
				String direction = ecoleElt.getAttribute("direction");
				
				int kms = Integer.parseInt(ecoleElt.getAttribute("kms"));

				Ecole ecole = new Ecole(adresse, nom, direction,kms);
				ecole.setId(Integer.parseInt(ecoleElt.getAttribute("id_ecole")));

				ecole.getHoraires().put(DayOfWeek.MONDAY,
						Duration.ofMinutes(Long.parseLong(ecoleElt.getAttribute("monday"))));
				ecole.getHoraires().put(DayOfWeek.TUESDAY,
						Duration.ofMinutes(Long.parseLong(ecoleElt.getAttribute("tuesday"))));
				ecole.getHoraires().put(DayOfWeek.WEDNESDAY,
						Duration.ofMinutes(Long.parseLong(ecoleElt.getAttribute("wednesday"))));
				ecole.getHoraires().put(DayOfWeek.THURSDAY,
						Duration.ofMinutes(Long.parseLong(ecoleElt.getAttribute("thursday"))));
				ecole.getHoraires().put(DayOfWeek.FRIDAY,
						Duration.ofMinutes(Long.parseLong(ecoleElt.getAttribute("friday"))));

				ecoleNames.put(ecole.getNom(), ecole);
				user.getEcoles().add(ecole);
			}

			NodeList planningNodeList = rootElt.getElementsByTagName("jour");
			for (int j = 0; j < planningNodeList.getLength(); j++) {

				Element jourElt = (Element) planningNodeList.item(j);

				LocalDate date = LocalDate.parse(jourElt.getAttribute("date"));
				Ecole ecole = ecoleNames.get(jourElt.getAttribute("ecole"));

				user.getPlanning().put(date, ecole);

			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Lecture du fichier échouée", "Erreur", JOptionPane.ERROR_MESSAGE);
			log("\nPARSING ERROR\n" + e.getMessage());
			e.printStackTrace();
		}
		
	}

	public Utilisateur getUser() {
		return user;
	}
}
