package application;

import static application.Main.prop;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;;

public class Holidays {

	public enum ZONE {
		A, B, C
	};

	private LocalDate Astart;
	private LocalDate Aend;
	private LocalDate Bstart;
	private LocalDate Bend;
	private LocalDate Cstart;
	private LocalDate Cend;

	private static Set<Integer> Adepts = new HashSet<Integer>();
	private static Set<Integer> Bdepts = new HashSet<Integer>();
	private static Set<Integer> Cdepts = new HashSet<Integer>();

	public static Map<Holidays.ZONE, Set<Integer>> zoneDept = new HashMap<Holidays.ZONE, Set<Integer>>();

	public Holidays(LocalDate astart, LocalDate aend, LocalDate bstart, LocalDate bend, LocalDate cstart,
			LocalDate cend) {

		Astart = astart;
		Aend = aend;
		Bstart = bstart;
		Bend = bend;
		Cstart = cstart;
		Cend = cend;

	}

	public LocalDate start(Holidays.ZONE zone) {

		switch (zone) {
		case A:
			return Astart;
		case B:
			return Bstart;
		case C:
			return Cstart;
		default:
			return Astart;
		}
	}

	public LocalDate end(Holidays.ZONE zone) {

		switch (zone) {
		case A:
			return Aend;
		case B:
			return Bend;
		case C:
			return Cend;
		default:
			return Aend;
		}
	}

	public boolean isHolyDay(Holidays.ZONE zone, LocalDate date) {

		return (date.isBefore(this.end(zone)) && date.isAfter(this.start(zone))) ? true : false;

	}

	public static Map<Holidays.ZONE, Set<Integer>> getZoneDept() {

		String[] AdeptList = prop.getProperty("Adept").split(",");
		for (String str : AdeptList) {
			Adepts.add(Integer.parseInt(str));
		}

		String[] BdeptList = prop.getProperty("Bdept").split(",");
		for (String str : BdeptList) {
			Bdepts.add(Integer.parseInt(str));
		}

		String[] CdeptList = prop.getProperty("Cdept").split(",");
		for (String str : CdeptList) {
			Cdepts.add(Integer.parseInt(str));
		}

		zoneDept.put(Holidays.ZONE.A, Adepts);
		zoneDept.put(Holidays.ZONE.B, Bdepts);
		zoneDept.put(Holidays.ZONE.C, Cdepts);

		return zoneDept;
	}

}
