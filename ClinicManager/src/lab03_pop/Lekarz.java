package lab03_pop;

import java.io.Serializable;
import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import wyjatki.TerminNiedostepnyException;
import wyjatki.TerminWolnyException;
import wyjatki.TerminZajetyException;
import wyjatki.ZasobNiedostepnyException;

public class Lekarz implements Serializable{

	private static final long serialVersionUID = 3836731943268688940L;
	private final UUID id;
	private Boolean czyZatrudniony;

	private final String imie;
	private final String nazwisko;
	private final String specjalizacja;

	/**
	 * Przechowuje pary: termin wizyty, informacje czy termin jest zajety
	 */
	private final Map<LocalDateTime, Boolean> godzinyPrzyjec = new TreeMap<>();

	public Lekarz(String imie, String nazwisko, String specjalizacja) {
		this.id = UUID.randomUUID();
		this.czyZatrudniony = true;
		this.imie = imie;
		this.nazwisko = nazwisko;
		this.specjalizacja = specjalizacja;
	}

	public void dodajGodzinyPrzyjec(String dataPoczatkowa, String dataKoncowa, String godzinaPoczatkowa,
			String godzinaKoncowa, Long czasTrwaniaWizyty, DayOfWeek dzienTyg, DateTimeFormatter formatDaty,
			DateTimeFormatter formatGodziny)
			throws DateTimeException, DateTimeParseException, ZasobNiedostepnyException {
		if (!czyZatrudniony) {
			throw new ZasobNiedostepnyException("Lekarz nie jest zatrudniony");
		}
		LocalTime godzPocz = LocalTime.parse(godzinaPoczatkowa, formatGodziny);
		LocalTime godzKonc = LocalTime.parse(godzinaKoncowa, formatGodziny);
		if (!godzPocz.isBefore(godzKonc)) {
			throw new DateTimeException("Godzina poczatkowa nie jest wczesniejsza od koncowej");
		}
		LocalDate dataPocz = LocalDate.parse(dataPoczatkowa, formatDaty);
		LocalDate dataKonc = LocalDate.parse(dataKoncowa, formatDaty);
		if (!dataPocz.isBefore(dataKonc)) {
			throw new DateTimeException("Data poczatkowa nie jest wczesniejsza od koncowej");
		}
		for (LocalDate data = dataPocz; data.isBefore(dataKonc); data = data.plusDays(1)) {
			if (data.getDayOfWeek() != dzienTyg) {
				continue;
			}
			for (LocalTime czas = godzPocz; czas.isBefore(godzKonc); czas = czas.plusMinutes(czasTrwaniaWizyty)) {
				godzinyPrzyjec.putIfAbsent(LocalDateTime.of(data, czas), false);
			}
		}
	}

	public void dodajGodzinePrzyjecia(String dataZGodzina, Long czasTrwaniaWizyty, DateTimeFormatter format)
			throws DateTimeParseException, TerminZajetyException, TerminNiedostepnyException,
			ZasobNiedostepnyException {
		if (!czyZatrudniony) {
			throw new ZasobNiedostepnyException("Lekarz nie jest zatrudniony");
		}
		LocalDateTime terminPrzyjecia = LocalDateTime.parse(dataZGodzina, format);
		if (godzinyPrzyjec.containsKey(terminPrzyjecia)) {
			throw new TerminNiedostepnyException("Podany termin juz istnieje");
		}
		godzinyPrzyjec.put(terminPrzyjecia, false);
		LocalDateTime hk = ((TreeMap<LocalDateTime, Boolean>) godzinyPrzyjec).higherKey(terminPrzyjecia);
		LocalDateTime lk = ((TreeMap<LocalDateTime, Boolean>) godzinyPrzyjec).lowerKey(terminPrzyjecia);
		if ((hk != null && hk.minusMinutes(czasTrwaniaWizyty).isBefore(terminPrzyjecia))
				|| (lk != null && lk.plusMinutes(czasTrwaniaWizyty).isAfter(terminPrzyjecia))) {
			godzinyPrzyjec.remove(terminPrzyjecia);
			throw new TerminZajetyException("Wprowadzona godzina przyjecia koliduje z obecnymi terminami");
		}
	}

	public void usunGodzinePrzyjecia(String dataZGodzina, DateTimeFormatter format) throws DateTimeParseException,
			TerminZajetyException, TerminNiedostepnyException, ZasobNiedostepnyException {
		if (!czyZatrudniony) {
			throw new ZasobNiedostepnyException("Lekarz nie jest zatrudniony");
		}
		LocalDateTime terminPrzyjecia = LocalDateTime.parse(dataZGodzina, format);
		if (!godzinyPrzyjec.containsKey(terminPrzyjecia)) {
			throw new TerminNiedostepnyException("Nie mozna znalezc zadanego terminu przyjecia");
		} else if (godzinyPrzyjec.get(terminPrzyjecia) == true) {
			throw new TerminZajetyException("Usuwany termin jest umowiona wizyta, aby usunac: odwolaj wizyte");
		}
		godzinyPrzyjec.remove(terminPrzyjecia);
	}

	public void dodajGodzineWizyty(String dataZGodzina, DateTimeFormatter format) throws DateTimeParseException,
			TerminZajetyException, TerminNiedostepnyException, ZasobNiedostepnyException {
		if (!czyZatrudniony) {
			throw new ZasobNiedostepnyException("Lekarz nie jest zatrudniony");
		}
		LocalDateTime terminPrzyjecia = LocalDateTime.parse(dataZGodzina, format);
		if (!godzinyPrzyjec.containsKey(terminPrzyjecia)) {
			throw new TerminNiedostepnyException("Nie mozna znalezc zadanego terminu przyjecia");
		} else if (godzinyPrzyjec.get(terminPrzyjecia) == true) {
			throw new TerminZajetyException("Na podany termin wizyta zostala juz umowiona");
		}
		godzinyPrzyjec.put(terminPrzyjecia, true);
	}

	public void usunGodzineWizyty(String dataZGodzina, DateTimeFormatter format)
			throws DateTimeParseException, TerminNiedostepnyException, TerminWolnyException, ZasobNiedostepnyException {
		if (!czyZatrudniony) {
			throw new ZasobNiedostepnyException("Lekarz nie jest zatrudniony");
		}
		LocalDateTime terminPrzyjecia = LocalDateTime.parse(dataZGodzina, format);
		if (!godzinyPrzyjec.containsKey(terminPrzyjecia)) {
			throw new TerminNiedostepnyException("Nie mozna znalezc zadanego terminu przyjecia");
		} else if (godzinyPrzyjec.get(terminPrzyjecia) == false) {
			throw new TerminWolnyException("Termin nie ma przypisanej wizyty");
		}
		godzinyPrzyjec.put(terminPrzyjecia, false);
	}

	public boolean czyTerminDostepny(String dataZGodzina, DateTimeFormatter format)
			throws DateTimeParseException, TerminNiedostepnyException, ZasobNiedostepnyException {
		if (!czyZatrudniony) {
			throw new ZasobNiedostepnyException("Lekarz nie jest ztrudniony");
		}
		LocalDateTime terminPrzyjecia = LocalDateTime.parse(dataZGodzina, format);
		if (!godzinyPrzyjec.containsKey(terminPrzyjecia)) {
			throw new TerminNiedostepnyException("Nie mozna znalezc zadanego terminu przyjecia");
		}
		return !godzinyPrzyjec.get(terminPrzyjecia);
	}

	public List<LocalDateTime> znajdzWolneTerminy(String terminPoczatkowy, String terminKoncowy,
			DateTimeFormatter format) throws DateTimeParseException, DateTimeException, ZasobNiedostepnyException {
		if (!czyZatrudniony) {
			throw new ZasobNiedostepnyException("Lekarz nie jest zatrudniony");
		}
		LocalDateTime terminP = LocalDateTime.parse(terminPoczatkowy, format);
		LocalDateTime terminK = LocalDateTime.parse(terminKoncowy, format);
		if (!terminP.isBefore(terminK)) {
			throw new DateTimeException("Termin poczatkowy nie moze byc wczesniejszy od koncowego");
		}
		List<LocalDateTime> wolneTerminy = new LinkedList<>();

		Iterator<Map.Entry<LocalDateTime, Boolean>> pierwszyWolnyTermin = godzinyPrzyjec.entrySet().iterator();
		while (pierwszyWolnyTermin.hasNext()) {
			Map.Entry<LocalDateTime, Boolean> termin = pierwszyWolnyTermin.next();
			if (termin.getValue() != true && !termin.getKey().isBefore(terminP) && !termin.getKey().isAfter(terminK)) {
				wolneTerminy.add(termin.getKey());
			}
		}
		return wolneTerminy;
	}

	public boolean czyMaWizyty() {
		LocalDateTime teraz = LocalDateTime.now();
		for (Map.Entry<LocalDateTime, Boolean> godzinaPrzyjecia : godzinyPrzyjec.entrySet()) {
			if (godzinaPrzyjecia.getKey().isAfter(teraz) && godzinaPrzyjecia.getValue() == true) {
				return true;
			}
		}
		return false;
	}

	public UUID getId() {
		return id;
	}

	public String getImie() {
		return imie;
	}

	public String getNazwisko() {
		return nazwisko;
	}

	public String getSpecjalizacja() {
		return specjalizacja;
	}

	public Boolean getCzyZatrudniony() {
		return czyZatrudniony;
	}

	public void setCzyZatrudniony(Boolean czyZatrudniony) {
		this.czyZatrudniony = czyZatrudniony;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("UUID: " + this.id + ", ");
		sb.append(" " + this.imie + ", ");
		sb.append(" " + this.nazwisko + ", ");
		sb.append(" " + this.specjalizacja + ", ");
		sb.append("Zatrudniony: " + (this.czyZatrudniony ? "tak" : "nie"));
		return sb.toString();
	}
}
