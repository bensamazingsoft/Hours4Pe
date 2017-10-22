package model;

public class Adresse {

	private int numero, codePostal;
	private String ville, rue;

	public Adresse() {
		this.numero = 0;
		this.codePostal = 0;
		this.rue = "";
		this.ville = "";
	}

	public Adresse(int numero, String rue, int codePostal, String ville) {
		this.numero = numero;
		this.rue = rue;
		this.codePostal = codePostal;
		this.ville = ville;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String str = "";
		str += (numero != 0) ? (numero + " " + rue + " " + codePostal + " " + ville)
				: (rue + " " + codePostal + " " + ville);

		return str;
	}

	// {{{ fold start
	/**
	 * @return the numero
	 */
	public int getNumero() {
		return numero;
	}

	/**
	 * @param numero
	 *            the numero to set
	 */
	public void setNumero(int numero) {
		this.numero = numero;
	}

	/**
	 * @return the codePostal
	 */
	public int getCodePostal() {
		return codePostal;
	}

	/**
	 * @param codePostal
	 *            the codePostal to set
	 */
	public void setCodePostal(int codePostal) {
		this.codePostal = codePostal;
	}

	/**
	 * @return the ville
	 */
	public String getVille() {
		return ville;
	}

	/**
	 * @param ville
	 *            the ville to set
	 */
	public void setVille(String ville) {
		this.ville = ville;
	}

	/**
	 * @return the rue
	 */
	public String getRue() {
		return rue;
	}

	/**
	 * @param rue
	 *            the rue to set
	 */
	public void setRue(String rue) {
		this.rue = rue;
	}
	// }}}

}
