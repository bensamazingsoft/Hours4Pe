package model;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Utilisateur {

	private static int id = 0;
	private int userId;
	private String nom, prenom;
	private Map<LocalDate, Ecole> planning = new TreeMap<>();
	private Set<Ecole> ecoles = new TreeSet<>();

	public Utilisateur() {
		nom = "";
		prenom = "";
		userId = ++id;
		
	}

	public Utilisateur(String nom, String prenom) {
		this.nom = nom;
		this.prenom = prenom;
		userId = ++id;
		
	}

	// {{{ fold start
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
	 * @return the prenom
	 */
	public String getPrenom() {
		return prenom;
	}

	/**
	 * @param prenom
	 *            the prenom to set
	 */
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	/**
	 * @return the planning
	 */
	public Map<LocalDate, Ecole> getPlanning() {
		return planning;
	}

	/**
	 * @param planning
	 *            the planning to set
	 */
	public void setPlanning(Map<LocalDate, Ecole> planning) {
		this.planning = planning;
	}

	/**
	 * @return the ecoles
	 */
	public Set<Ecole> getEcoles() {
		return ecoles;
	}

	/**
	 * @param ecoles
	 *            the ecoles to set
	 */
	public void setEcoles(Set<Ecole> ecoles) {
		this.ecoles = ecoles;
	}

	/**
	 * @return the id
	 */
	public static int getId() {
		return id;
	}

	/**
	 * @return the userId
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	// }}}

}
