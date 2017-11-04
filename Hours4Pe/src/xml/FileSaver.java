package xml;

import static utils.LogGenerator.log;

import java.io.File;
import java.time.DayOfWeek;

import javax.swing.JOptionPane;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

import model.Utilisateur;

public class FileSaver {

	File userFile;
	DocumentBuilderFactory factory;
	DocumentBuilder builder;
	SchemaFactory schemaFactory;
	Schema schema;
	Document doc;

	public FileSaver(Utilisateur user) {

		userFile = new File("user.xml");

		// create Document
		try {

			factory = DocumentBuilderFactory.newInstance();
			schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			schema = schemaFactory.newSchema(getClass().getClassLoader().getResource("xml/Hours4PeUserSchema.xsd"));
			factory.setSchema(schema);
			builder = factory.newDocumentBuilder();
			ErrorHandler errHandler = new ParseErrorHandler();
			builder.setErrorHandler(errHandler);
			doc = builder.newDocument();

			Element rootElt = doc.createElement("user");

			Attr nom_userAttr = doc.createAttribute("nom_user");
			nom_userAttr.setValue(user.getNom());
			rootElt.setAttributeNode(nom_userAttr);

			Attr prenomAttr = doc.createAttribute("prenom");
			prenomAttr.setValue(user.getPrenom());
			rootElt.setAttributeNode(prenomAttr);

			Attr idAttr = doc.createAttribute("id_user");
			idAttr.setValue(String.valueOf(user.getUserId()));
			rootElt.setAttributeNode(idAttr);

			Element ecolesElt = doc.createElement("ecoles");

			if (!user.getEcoles().isEmpty()) {
				user.getEcoles().stream().forEach(ecole -> {

					Element ecoleElt = doc.createElement("ecole");

					Attr nom_ecoleAttr = doc.createAttribute("nom_ecole");
					nom_ecoleAttr.setValue(ecole.getNom());
					ecoleElt.setAttributeNode(nom_ecoleAttr);

					Element adresseElt = doc.createElement("adresse");
					Attr numeroAttr = doc.createAttribute("numero");
					numeroAttr.setValue(String.valueOf(ecole.getAdresse().getNumero()));
					adresseElt.setAttributeNode(numeroAttr);
					Attr zipAttr = doc.createAttribute("zip");
					zipAttr.setValue(String.valueOf(ecole.getAdresse().getCodePostal()));
					adresseElt.setAttributeNode(zipAttr);
					Attr rueAttr = doc.createAttribute("rue");
					rueAttr.setValue(ecole.getAdresse().getRue());
					adresseElt.setAttributeNode(rueAttr);
					Attr villeAttr = doc.createAttribute("ville");
					villeAttr.setValue(ecole.getAdresse().getVille());
					adresseElt.setAttributeNode(villeAttr);
					ecoleElt.appendChild(adresseElt);

					Attr directionAttr = doc.createAttribute("direction");
					directionAttr.setValue(ecole.getDirection());
					ecoleElt.setAttributeNode(directionAttr);

					Attr id_ecoleAttr = doc.createAttribute("id_ecole");
					id_ecoleAttr.setValue(String.valueOf(ecole.getId()));
					ecoleElt.setAttributeNode(id_ecoleAttr);

					Attr mondayAttr = doc.createAttribute("monday");
					mondayAttr.setValue(String.valueOf((ecole.getHoraires().get(DayOfWeek.MONDAY)).toMinutes()));
					ecoleElt.setAttributeNode(mondayAttr);
					Attr tuesdayAttr = doc.createAttribute("tuesday");
					tuesdayAttr.setValue(String.valueOf((ecole.getHoraires().get(DayOfWeek.TUESDAY)).toMinutes()));
					ecoleElt.setAttributeNode(tuesdayAttr);
					Attr wednesdayAttr = doc.createAttribute("wednesday");
					wednesdayAttr.setValue(String.valueOf((ecole.getHoraires().get(DayOfWeek.WEDNESDAY)).toMinutes()));
					ecoleElt.setAttributeNode(wednesdayAttr);
					Attr thursdayAttr = doc.createAttribute("thursday");
					thursdayAttr.setValue(String.valueOf((ecole.getHoraires().get(DayOfWeek.THURSDAY)).toMinutes()));
					ecoleElt.setAttributeNode(thursdayAttr);
					Attr fridayAttr = doc.createAttribute("friday");
					fridayAttr.setValue(String.valueOf((ecole.getHoraires().get(DayOfWeek.FRIDAY)).toMinutes()));
					ecoleElt.setAttributeNode(fridayAttr);

					ecolesElt.appendChild(ecoleElt);
				});
			}
			rootElt.appendChild(ecolesElt);

			Element planningElt = doc.createElement("planning");

			if (!user.getPlanning().isEmpty()) {
				user.getPlanning().forEach((date, ecole) -> {

					Element jourElt = doc.createElement("jour");
					Attr dateAttr = doc.createAttribute("date");
					dateAttr.setValue(String.valueOf(date));
					jourElt.setAttributeNode(dateAttr);
					Attr ecoleAttr = doc.createAttribute("ecole");
					ecoleAttr.setValue(ecole.getNom());
					jourElt.setAttributeNode(ecoleAttr);

					planningElt.appendChild(jourElt);

				});
			}
			rootElt.appendChild(planningElt);

			doc.appendChild(rootElt);

		} catch (SAXException | ParserConfigurationException e) {
			JOptionPane.showMessageDialog(null, "Sauvegarde du fichier échouée", "Erreur", JOptionPane.ERROR_MESSAGE);
			log("\nPARSING ERROR\n" + e.getMessage());
			e.printStackTrace();
		}

	}

	public void writeXml() {

		try {
			TransformerFactory transFactory = TransformerFactory.newInstance();
			Transformer trans = transFactory.newTransformer();
			DOMSource domSrc = new DOMSource(doc);
			StreamResult result = new StreamResult(userFile);
			trans.setOutputProperty(OutputKeys.INDENT, "yes");
			trans.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			trans.transform(domSrc, result);
		} catch (IllegalArgumentException | TransformerFactoryConfigurationError | TransformerException e) {
			JOptionPane.showMessageDialog(null, "Sauvegarde du fichier échouée", "Erreur", JOptionPane.ERROR_MESSAGE);
			log("\nXML WRITE ERROR\n" + e.getMessage());
			e.printStackTrace();
		}
	}

}
