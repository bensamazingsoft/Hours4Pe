package model;

import java.time.DayOfWeek;
import java.time.Duration;
import java.util.Map;

public class Ecole {

	private static int idEcole;
	private int id;
	private Adresse adresse;
	private String nom;
	private String direction;
	private Map<DayOfWeek, Duration> horaires;

	public Ecole() {

		this.id = idEcole;
		this.adresse = null;
		this.nom = "";
		this.direction = "";

		this.horaires.put(DayOfWeek.MONDAY, null);
		this.horaires.put(DayOfWeek.TUESDAY, null);
		this.horaires.put(DayOfWeek.WEDNESDAY, null);
		this.horaires.put(DayOfWeek.THURSDAY, null);
		this.horaires.put(DayOfWeek.FRIDAY, null);

		idEcole++;
	}

	public Ecole(Adresse adresse, String nom, String direction) {

		this.id = idEcole;
		this.adresse = adresse;
		this.nom = nom;
		this.direction = direction;

		this.horaires.put(DayOfWeek.MONDAY, null);
		this.horaires.put(DayOfWeek.TUESDAY, null);
		this.horaires.put(DayOfWeek.WEDNESDAY, null);
		this.horaires.put(DayOfWeek.THURSDAY, null);
		this.horaires.put(DayOfWeek.FRIDAY, null);

		idEcole++;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String str = "";

		str += "Nom " + nom + "\n" + "adresse" + adresse.toString() + "Direction " + direction + " Heures : Lundi "
				+ horaires.get(DayOfWeek.MONDAY) + " \n" + " Mardi " + horaires.get(DayOfWeek.TUESDAY) + "\n"
				+ " Mercredi " + horaires.get(DayOfWeek.WEDNESDAY) + " \n" + " Jeudi "
				+ horaires.get(DayOfWeek.THURSDAY) + " \n" + " Vendredi " + horaires.get(DayOfWeek.FRIDAY) + " \n";

		return str;
	}

	// {{{ fold start

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the adresse
	 */
	public Adresse getAdresse() {
		return adresse;
	}

	/**
	 * @param adresse
	 *            the adresse to set
	 */
	public void setAdresse(Adresse adresse) {
		this.adresse = adresse;
	}

	/**
	 * @return the nom
	 */
	public String getNom() {
		return nom;
	}

	/**
	 * @param nom
	 *            the nom to set
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	 * @return the direction
	 */
	public String getDirection() {
		return direction;
	}

	/**
	 * @param direction
	 *            the direction to set
	 */
	public void setDirection(String direction) {
		this.direction = direction;
	}

	/**
	 * @return the horaires
	 */
	public Map<DayOfWeek, Duration> getHoraires() {
		return horaires;
	}

	/**
	 * @param horaires the horaires to set
	 */
	public void setHoraires(Map<DayOfWeek, Duration> horaires) {
		this.horaires = horaires;
	}

	// }}}

}
