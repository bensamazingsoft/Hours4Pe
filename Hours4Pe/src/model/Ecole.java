package model;

import java.time.DayOfWeek;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class Ecole implements Comparable<Ecole> {

	private static int idEcole;
	private int id;
	private Adresse adresse;
	private String nom;
	private String direction;
	private int kms;
//	private Long lundi;
//	private Long mardi;
//	private Long mercredi;
//	private Long jeudi;
//	private Long vendredi;
	


	private Map<DayOfWeek, Duration> horaires;

	public Ecole() {

		idEcole++;
		this.id = idEcole;
		this.adresse = null;
		this.nom = "";
		this.direction = "";
		this.kms = 0;

		this.horaires = new HashMap<DayOfWeek, Duration>();
		this.horaires.put(DayOfWeek.MONDAY, null);
		this.horaires.put(DayOfWeek.TUESDAY, null);
		this.horaires.put(DayOfWeek.WEDNESDAY, null);
		this.horaires.put(DayOfWeek.THURSDAY, null);
		this.horaires.put(DayOfWeek.FRIDAY, null);

	}

	public Ecole(Adresse adresse, String nom, String direction, int kms) {

		idEcole++;
		this.id = idEcole;
		this.adresse = adresse;
		this.nom = nom;
		this.direction = direction;
		this.kms = kms;

		this.horaires = new HashMap<DayOfWeek, Duration>();
		this.horaires.put(DayOfWeek.MONDAY, null);
		this.horaires.put(DayOfWeek.TUESDAY, null);
		this.horaires.put(DayOfWeek.WEDNESDAY, null);
		this.horaires.put(DayOfWeek.THURSDAY, null);
		this.horaires.put(DayOfWeek.FRIDAY, null);

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
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		
		return super.equals(obj);
	}

	/**
	 * @return the kms
	 */
	public int getKms() {
		return kms;
	}

	/**
	 * @param kms the kms to set
	 */
	public void setKms(int kms) {
		this.kms = kms;
	}
	
	public Long getLundi(){
		return this.getHoraires().get(DayOfWeek.MONDAY).toHours();
	}

	public Long getMardi(){
		return this.getHoraires().get(DayOfWeek.TUESDAY).toHours();
	}
	
	public Long getMercredi(){
		return this.getHoraires().get(DayOfWeek.WEDNESDAY).toHours();
	}
	public Long getJeudi(){
		return this.getHoraires().get(DayOfWeek.THURSDAY).toHours();
	}
	public Long getVendredi(){
		return this.getHoraires().get(DayOfWeek.FRIDAY).toHours();
	}
	
	@Override
	public int compareTo(Ecole other) {
		int result;
		
		result = Integer.compare(this.getId(),other.getId());
		return result;
	}

	// }}}

}
