package application;

import java.time.LocalDate;
import java.time.Month;

public class AnneeScolaire {

	String anneeSco = "";
	boolean isit = false;
	LocalDate today;
	 LocalDate anneeStart;
	 LocalDate anneeEnd;

	public AnneeScolaire(LocalDate today) {

		this.today = today;

		anneeSco = (today.isAfter(LocalDate.of(today.getYear(), Month.AUGUST.getValue(), 1)))
				? ("\t" + String.valueOf(today.getYear())) + " - " + (String.valueOf(today.getYear() + 1))
				: ("\t" + String.valueOf(today.getYear() - 1)) + " - " + (String.valueOf(today.getYear()));

		anneeStart = LocalDate.of(today.minusMonths(7).getYear(), 9, 1);
		anneeEnd = LocalDate.of(today.plusMonths(5).getYear(), 8, 31);

	}

	public String getAnneeSco() {

		return anneeSco;

	}

	public boolean isAnneeSco(LocalDate thisDay) {

		return (thisDay.isAfter(anneeStart) && thisDay.isBefore(anneeEnd));

	}

	/**
	 * @return the anneeStart
	 */
	public LocalDate getAnneeStart() {
		return anneeStart;
	}

	/**
	 * @param anneeStart
	 *            the anneeStart to set
	 */
	public void setAnneeStart(LocalDate anneeStart) {
		this.anneeStart = anneeStart;
	}

	/**
	 * @return the anneeEnd
	 */
	public LocalDate getAnneeEnd() {
		return anneeEnd;
	}

	/**
	 * @param anneeEnd
	 *            the anneeEnd to set
	 */
	public void setAnneeEnd(LocalDate anneeEnd) {
		this.anneeEnd = anneeEnd;
	}

}
