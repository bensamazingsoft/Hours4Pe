package model;

import java.time.Month;
import java.util.ArrayList;

public class EcoleData {

	Utilisateur user;
	Ecole ecole;

	String nom;
	
	Long sept;
	Long oct;
	Long nov;
	Long dec;
	Long janv;
	Long fev;
	Long mar;
	Long avr;
	Long mai;
	Long juin;
	Long juill;

	public EcoleData(Utilisateur user, Ecole ecole) {

		this.user = user;
		this.ecole = ecole;
		this.nom = ecole.getNom();


	}

	private Long cumulHours(Month month) {
		ArrayList<Long> tempTab = new ArrayList<>();

		this.user.getPlanning().forEach((date, ecole) -> {
			if (date.getMonth().equals(month)){
				tempTab.add((this.user.getPlanning().get(date).getHoraires().get(date.getDayOfWeek())).toHours());
			
			}
		});

		return tempTab.stream().mapToLong(l -> l).sum();

	}

	/**
	 * @return the sept
	 */
	public Long getSept() {
		return cumulHours(Month.SEPTEMBER);
	}

	/**
	 * @param sept
	 *            the sept to set
	 */
	public void setSept(Long sept) {
		this.sept = sept;
	}

	/**
	 * @return the oct
	 */
	public Long getOct() {
		return cumulHours(Month.OCTOBER);
	}

	/**
	 * @param oct
	 *            the oct to set
	 */
	public void setOct(Long oct) {
		this.oct = oct;
	}

	/**
	 * @return the nov
	 */
	public Long getNov() {
		return cumulHours(Month.NOVEMBER);
	}

	/**
	 * @param nov
	 *            the nov to set
	 */
	public void setNov(Long nov) {
		this.nov = nov;
	}

	/**
	 * @return the dec
	 */
	public Long getDec() {
		return cumulHours(Month.DECEMBER);
	}

	/**
	 * @param dec
	 *            the dec to set
	 */
	public void setDec(Long dec) {
		this.dec = dec;
	}

	/**
	 * @return the janv
	 */
	public Long getJanv() {
		return cumulHours(Month.JANUARY);
	}

	/**
	 * @param janv
	 *            the janv to set
	 */
	public void setJanv(Long janv) {
		janv = janv;
	}

	/**
	 * @return the fev
	 */
	public Long getFev() {
		return cumulHours(Month.FEBRUARY);
	}

	/**
	 * @param fev
	 *            the fev to set
	 */
	public void setFev(Long fev) {
		this.fev = fev;
	}

	/**
	 * @return the mar
	 */
	public Long getMar() {
		return cumulHours(Month.MARCH);
	}

	/**
	 * @param mar
	 *            the mar to set
	 */
	public void setMar(Long mar) {
		this.mar = mar;
	}

	/**
	 * @return the avr
	 */
	public Long getAvr() {
		return cumulHours(Month.APRIL);
	}

	/**
	 * @param avr
	 *            the avr to set
	 */
	public void setAvr(Long avr) {
		this.avr = avr;
	}

	/**
	 * @return the mai
	 */
	public Long getMai() {
		return cumulHours(Month.MAY);
	}

	/**
	 * @param mai
	 *            the mai to set
	 */
	public void setMai(Long mai) {
		this.mai = mai;
	}

	/**
	 * @return the juin
	 */
	public Long getJuin() {
		return cumulHours(Month.JUNE);
	}

	/**
	 * @param juin
	 *            the juin to set
	 */
	public void setJuin(Long juin) {
		this.juin = juin;
	}

	/**
	 * @return the juill
	 */
	public Long getJuill() {
		return cumulHours(Month.JULY);
	}

	/**
	 * @param juill
	 *            the juill to set
	 */
	public void setJuill(Long juill) {
		this.juill = juill;
	}

	/**
	 * @return the nom
	 */
	public String getNom() {
		return nom;
	}

	/**
	 * @param nom the nom to set
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}

}
