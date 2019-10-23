package lab03_pop;

import java.io.Serializable;
import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import wyjatki.TerminNiedostepnyException;
import wyjatki.TerminWolnyException;
import wyjatki.TerminZajetyException;
import wyjatki.ZasobDostepnyException;
import wyjatki.ZasobNiedostepnyException;
import wyjatki.ZleUuidExcpetion;
import wyjatki.ZlyStanWizytyException;

public class Przychodnia implements Serializable {

	private static final long serialVersionUID = 2929553252476045213L;
	public static final DateTimeFormatter FORMAT_DATY_I_CZASU = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
	public static final DateTimeFormatter FORMAT_DATY = DateTimeFormatter.ISO_LOCAL_DATE;
	public static final DateTimeFormatter FORMAT_CZASU = DateTimeFormatter.ISO_LOCAL_TIME;

	private String nazwa;
	private final Map<UUID, Gabinet> gabinety = new HashMap<>();
	private final Map<UUID, Lekarz> lekarze = new HashMap<>();
	private final Map<UUID, Pacjent> pacjenci = new HashMap<>();

	private final Set<Wizyta> wizyty = new TreeSet<>();

	public Przychodnia(String nazwa) {
		this.nazwa = nazwa;
	}

	public void dodajLekarza(String imie, String nazwisko, String specjalizacja)
			throws IllegalArgumentException, ZasobDostepnyException, ZleUuidExcpetion {
		if (imie == null || imie.isEmpty()) {
			throw new IllegalArgumentException("Imie nie moze byc puste");
		}
		if (nazwisko == null || nazwisko.isEmpty()) {
			throw new IllegalArgumentException("Nazwisko nie moze byc puste");
		}
		if (specjalizacja == null || specjalizacja.isEmpty()) {
			throw new IllegalArgumentException("Specjalizacja nie moze byc pusta");
		}
		for (Lekarz lekarz : lekarze.values()) {
			if (lekarz.getImie().equals(imie) && lekarz.getNazwisko().equals(nazwisko)
					&& lekarz.getSpecjalizacja().equals(specjalizacja)) {
				throw new ZasobDostepnyException("Lekarz z wprowadzonymi danymi juz istnieje");
			}
		}
		Lekarz nowyLekarz = new Lekarz(imie, nazwisko, specjalizacja);
		if (lekarze.containsKey(nowyLekarz.getId())) {
			throw new ZleUuidExcpetion("Cos poszlo nie tak, sprobuj ponownie");
		}
		lekarze.put(nowyLekarz.getId(), nowyLekarz);
	}

	public void przywrocLekarza(String leakarzId)
			throws IllegalArgumentException, ZasobDostepnyException, ZleUuidExcpetion {
		UUID lekId = UUID.fromString(leakarzId);
		if (!lekarze.containsKey(lekId)) {
			throw new ZleUuidExcpetion("Podane UUID nie odpowiada zadnemu lekarzowi");
		}
		Lekarz lekarz = lekarze.get(lekId);
		if (lekarz.getCzyZatrudniony()) {
			throw new ZasobDostepnyException(
					"Lekarz nie moze byc przywrocony do pracy, gdyz jest aktualnie zatrudniony");
		}
		lekarz.setCzyZatrudniony(true);
	}

	public void usunLekarza(String leakarzId)
			throws IllegalArgumentException, ZasobNiedostepnyException, ZleUuidExcpetion {
		UUID lekId = UUID.fromString(leakarzId);
		if (!lekarze.containsKey(lekId)) {
			throw new ZleUuidExcpetion("Podane UUID nie odpowiada zadnemu lekarzowi");
		}
		Lekarz lekarz = lekarze.get(lekId);
		if (lekarz.czyMaWizyty()) {
			throw new ZasobNiedostepnyException("Nie mozna usunac lekarza, gdyz ma niezakonczone wizyty");
		}
		lekarz.setCzyZatrudniony(false);
	}

	public void dodajGodzinyPrzyjecLekarza(String idLekarza, String dataPoczatkowa, String dataKoncowa,
			String godzinaPoczatkowa, String godzinaKoncowa, String czasTrwaniaWizyty, String dzienTyg,
			DateTimeFormatter formatDaty, DateTimeFormatter formatGodziny) throws DateTimeException,
			DateTimeParseException, ZasobNiedostepnyException, IllegalArgumentException, NumberFormatException {
		Long lCzasTrwaniaWizyty = new Long(czasTrwaniaWizyty);
		UUID lekId = UUID.fromString(idLekarza);
		if (!lekarze.containsKey(lekId)) {
			throw new ZasobNiedostepnyException("Nie mozna znalezc lekarze z podanym UUID");
		}
		if (czasTrwaniaWizyty == null) {
			throw new IllegalArgumentException("Czas trwania wizyty musi zostac podany");
		}
		DayOfWeek dzien = null;
		if (dzienTyg != null && !dzienTyg.isEmpty()) {
			if (dzienTyg.toLowerCase().equals("poniedzialek"))
				dzien = DayOfWeek.MONDAY;
			else if (dzienTyg.toLowerCase().equals("wtorek"))
				dzien = DayOfWeek.TUESDAY;
			else if (dzienTyg.toLowerCase().equals("sroda"))
				dzien = DayOfWeek.WEDNESDAY;
			else if (dzienTyg.toLowerCase().equals("czwartek"))
				dzien = DayOfWeek.THURSDAY;
			else if (dzienTyg.toLowerCase().equals("piatek"))
				dzien = DayOfWeek.FRIDAY;
			else if (dzienTyg.toLowerCase().equals("sobota"))
				dzien = DayOfWeek.SATURDAY;
			else if (dzienTyg.toLowerCase().equals("niedziela"))
				dzien = DayOfWeek.SUNDAY;
			else
				throw new IllegalArgumentException("Podano niepoprawny dzien tygodnia");
		} else {
			throw new IllegalArgumentException("Dzien tygodnia musi zostac podany");
		}
		lekarze.get(lekId).dodajGodzinyPrzyjec(dataPoczatkowa, dataKoncowa, godzinaPoczatkowa, godzinaKoncowa,
				lCzasTrwaniaWizyty, dzien, formatDaty, formatGodziny);
	}

	public void dodajGodzinePrzyjecLekarza(String idLekarza, String dataZGodzina, String czasTrwaniaWizyty,
			DateTimeFormatter format) throws ZasobNiedostepnyException, DateTimeParseException, TerminZajetyException,
			TerminNiedostepnyException, IllegalArgumentException, NumberFormatException {
		Long lCzasTrwaniaWizyty = new Long(czasTrwaniaWizyty);
		UUID lekId = UUID.fromString(idLekarza);
		if (!lekarze.containsKey(lekId)) {
			throw new ZasobNiedostepnyException("Nie mozna znalezc lekarze z podanym UUID");
		}
		if (czasTrwaniaWizyty == null) {
			throw new IllegalArgumentException("Czas trwania wizyty musi zostac podany");
		}
		lekarze.get(lekId).dodajGodzinePrzyjecia(dataZGodzina, lCzasTrwaniaWizyty, format);
	}

	public void usunGodzinePrzyjecLekarza(String idLekarza, String dataZGodzina, DateTimeFormatter format)
			throws DateTimeParseException, TerminZajetyException, TerminNiedostepnyException, ZasobNiedostepnyException,
			IllegalArgumentException {
		UUID lekId = UUID.fromString(idLekarza);
		if (!lekarze.containsKey(lekId)) {
			throw new ZasobNiedostepnyException("Nie mozna znalezc lekarze z podanym UUID");
		}
		lekarze.get(lekId).usunGodzinePrzyjecia(dataZGodzina, format);
	}

	public Boolean czyWprowadzonoLekarza(String imie, String nazwisko, String specjalizacja)
			throws IllegalArgumentException {
		if (imie == null || imie.isEmpty()) {
			throw new IllegalArgumentException("Imie nie moze byc puste");
		}
		if (nazwisko == null || nazwisko.isEmpty()) {
			throw new IllegalArgumentException("Nazwisko nie moze byc puste");
		}
		if (specjalizacja == null || specjalizacja.isEmpty()) {
			throw new IllegalArgumentException("Specjalizacja nie moze byc pusta");
		}
		for (Lekarz lekarz : lekarze.values()) {
			if (lekarz.getImie().equals(imie) && lekarz.getNazwisko().equals(nazwisko)
					&& lekarz.getSpecjalizacja().equals(specjalizacja)) {
				return true;
			}
		}
		return false;
	}

	public void dodajGabinet(String numerGabinetu)
			throws ZasobDostepnyException, IllegalArgumentException, ZleUuidExcpetion {
		if (numerGabinetu == null || numerGabinetu.isEmpty()) {
			throw new IllegalArgumentException("Numer gabinetu nie moze byc pusty");
		}
		for (Gabinet gabinet : gabinety.values()) {
			if (gabinet.getNumer().equals(numerGabinetu)) {
				throw new ZasobDostepnyException("Gabinet o numerze " + numerGabinetu + " juz istnieje");
			}
		}
		Gabinet nowyGabinet = new Gabinet(numerGabinetu);
		if (gabinety.containsKey(nowyGabinet.getId())) {
			throw new ZleUuidExcpetion("Cos poszlo nie tak, sprobuj ponownie");
		}
		gabinety.put(nowyGabinet.getId(), nowyGabinet);
	}

	public void przywrocGabinet(String gabinetId) throws ZleUuidExcpetion, IllegalArgumentException {
		UUID gabId = UUID.fromString(gabinetId);
		if (!gabinety.containsKey(gabId)) {
			throw new ZleUuidExcpetion("Podane UUID nie odpowiada zadnemu gabinetowi");
		}
		gabinety.get(gabId).setCzyIstnieje(true);
	}

	public void usunGabinet(String gabinetId)
			throws ZasobNiedostepnyException, ZleUuidExcpetion, IllegalArgumentException {
		UUID gabId = UUID.fromString(gabinetId);
		if (!gabinety.containsKey(gabId)) {
			throw new ZleUuidExcpetion("Podane UUID nie odpowiada zadnemu gabinetowi");
		}
		Gabinet gabinet = gabinety.get(gabId);
		if (gabinet.czyGabinetUzywany()) {
			throw new ZasobNiedostepnyException("Gabinetu nie mozna usunac, gdyz jest w uzyciu");
		}
		gabinet.setCzyIstnieje(false);
	}

	public Boolean czyWprowadzonoGabinet(String numerGabinetu) throws IllegalArgumentException {
		if (numerGabinetu == null || numerGabinetu.isEmpty()) {
			throw new IllegalArgumentException("Numer gabinetu nie moze byc pusty");
		}
		for (Gabinet gabinet : gabinety.values()) {
			if (gabinet.getNumer().equals(numerGabinetu)) {
				return true;
			}
		}
		return false;
	}

	public void dodajPacjenta(String imie, String nazwisko, String pesel)
			throws IllegalArgumentException, ZasobDostepnyException, ZleUuidExcpetion {
		if (imie == null || imie.isEmpty()) {
			throw new IllegalArgumentException("Imie nie moze byc puste");
		}
		if (nazwisko == null || nazwisko.isEmpty()) {
			throw new IllegalArgumentException("Nazwisko nie moze byc puste");
		}
		PeselValidator pv = new PeselValidator(pesel.toString());
		if (!pv.isValid()) {
			throw new IllegalArgumentException("Podany PESEL nie jest poprawny");
		}
		for (Pacjent pacjent : pacjenci.values()) {
			if (pacjent.getImie().equals(imie) && pacjent.getNazwisko().equals(nazwisko)
					&& pacjent.getPesel().equals(new Long(pesel))) {
				throw new ZasobDostepnyException("Pacjent z wprowadzonymi danymi juz istnieje");
			}
		}
		Pacjent nowyPacjent = new Pacjent(imie, nazwisko, new Long(pesel));
		if (pacjenci.containsKey(nowyPacjent.getId())) {
			throw new ZleUuidExcpetion("Cos poszlo nie tak, sprobuj ponownie");
		}
		pacjenci.put(nowyPacjent.getId(), nowyPacjent);
	}

	public void przywrocPacjenta(String idPacjenta)
			throws IllegalArgumentException, ZasobDostepnyException, ZleUuidExcpetion {
		UUID pacId = UUID.fromString(idPacjenta);
		if (!pacjenci.containsKey(pacId)) {
			throw new ZleUuidExcpetion("Podane UUID nie odnosi sie do zadnego pacjenta");
		}
		Pacjent pacjent = pacjenci.get(pacId);
		if (pacjent.getCzyZarejestrowany()) {
			throw new ZasobDostepnyException(
					"Nie mozna zarejestrowac pacjenta ponownie, gdyz pacjent jest juz zarejestrowany");
		}
		pacjent.setCzyZarejestrowany(true);
	}

	public void usunPacjenta(String idPacjenta)
			throws IllegalArgumentException, ZasobNiedostepnyException, ZleUuidExcpetion {
		UUID pacId = UUID.fromString(idPacjenta);
		if (!pacjenci.containsKey(pacId)) {
			throw new ZleUuidExcpetion("Podane UUID nie odnosi sie do zadnego pacjenta");
		}
		for (Wizyta wizyta : wizyty) {
			if (wizyta.getPacjentId().equals(pacId)) {
				throw new ZasobNiedostepnyException(
						"Pacjent nie moze zostac wyrejestrowany, gdyz ma umowiona(e) wizyte(y)");
			}
		}
		pacjenci.get(pacId).setCzyZarejestrowany(false);
	}

	public Boolean czyWprowadzonoPacjenta(String imie, String nazwisko, String pesel) throws IllegalArgumentException {
		if (imie == null || imie.isEmpty()) {
			throw new IllegalArgumentException("Imie nie moze byc puste");
		}
		if (nazwisko == null || nazwisko.isEmpty()) {
			throw new IllegalArgumentException("Nazwisko nie moze byc puste");
		}
		PeselValidator pv = new PeselValidator(pesel);
		if (!pv.isValid()) {
			throw new IllegalArgumentException("Podany PESEL nie jest poprawny");
		}
		for (Pacjent pacjent : pacjenci.values()) {
			if (pacjent.getImie().equals(imie) && pacjent.getNazwisko().equals(nazwisko)
					&& pacjent.getPesel().equals(new Long(pesel))) {
				return true;
			}
		}
		return false;
	}

	public List<Lekarz> znajdzLekarza(String imie, String nazwisko, String specjalizacja) {
		List<Lekarz> dopasowaniLekarze = new LinkedList<>();
		for (Lekarz lekarz : lekarze.values()) {
			if (imie != null)
				if (!lekarz.getImie().equals(imie))
					continue;
			if (nazwisko != null)
				if (!lekarz.getNazwisko().equals(nazwisko))
					continue;
			if (specjalizacja != null)
				if (!lekarz.getSpecjalizacja().equals(specjalizacja))
					continue;
			dopasowaniLekarze.add(lekarz);
		}
		return dopasowaniLekarze;
	}

	public List<String> znajdzWolneTerminyLekarza(String lekarzId, String dataPoczatkowa, String dataKoncowa,
			DateTimeFormatter format) throws DateTimeParseException, DateTimeException, ZasobNiedostepnyException,
			IllegalArgumentException, ZleUuidExcpetion {
		UUID lekId = UUID.fromString(lekarzId);
		if (!lekarze.containsKey(lekId)) {
			throw new ZleUuidExcpetion("Podane UUID nie odpowiada zadnemu lekarzowi");
		}
		List<LocalDateTime> wolneTerminy = lekarze.get(lekId).znajdzWolneTerminy(dataPoczatkowa, dataKoncowa, format);
		List<String> tekstoweWolneTerminy = new LinkedList<>();
		for (LocalDateTime termin : wolneTerminy) {
			tekstoweWolneTerminy.add(termin.toString());
		}
		return tekstoweWolneTerminy;
	}

	public List<Pacjent> znajdzPacjenta(String imie, String nazwisko, String pesel) throws NumberFormatException {
		Long lPesel = null;
		if (pesel != null) {
			lPesel = new Long(pesel);
		}
		List<Pacjent> dopasowaniPacjenci = new LinkedList<>();
		for (Pacjent pacjent : pacjenci.values()) {
			if (imie != null)
				if (!pacjent.getImie().equals(imie))
					continue;
			if (nazwisko != null)
				if (!pacjent.getNazwisko().equals(nazwisko))
					continue;
			if (pesel != null)
				if (!pacjent.getPesel().equals(lPesel))
					continue;
			dopasowaniPacjenci.add(pacjent);
		}
		return dopasowaniPacjenci;
	}

	public List<Gabinet> znajdzGabinet(String numer) {
		List<Gabinet> dopasowaneGabinety = new LinkedList<>();
		for (Gabinet gabinet : gabinety.values()) {
			if (numer != null)
				if (!gabinet.getNumer().equals(numer))
					continue;
			dopasowaneGabinety.add(gabinet);
		}
		return dopasowaneGabinety;
	}

	public List<Wizyta> znajdzAktywnaWizyte(String idPacjenta, String idLekarza, String terminPoczatkuWizyty,
			String terminKoncaWizyty, DateTimeFormatter formatTerminu, String idGabinetu)
			throws ZleUuidExcpetion, DateTimeParseException {
		UUID pacId = null, lekId = null, gabId = null;
		LocalDateTime terminPocz = null;
		LocalDateTime terminKonc = null;
		if (idPacjenta != null) {
			pacId = UUID.fromString(idPacjenta);
			if (!pacjenci.containsKey(pacId)) {
				throw new ZleUuidExcpetion("Podane UUID nie odnosi sie do zadnego pacjenta");
			}
		}
		if (idLekarza != null) {
			lekId = UUID.fromString(idLekarza);
			if (!lekarze.containsKey(lekId)) {
				throw new ZleUuidExcpetion("Podane UUID nie odpowiada zadnemu lekarzowi");
			}
		}
		if (idGabinetu != null) {
			gabId = UUID.fromString(idGabinetu);
			if (!gabinety.containsKey(gabId)) {
				throw new ZleUuidExcpetion("Podane UUID nie odpowiada zadnemu gabinetowi");
			}
		}
		if (terminPoczatkuWizyty != null) {
			terminPocz = LocalDateTime.parse(terminPoczatkuWizyty, formatTerminu);
		}
		if (terminKoncaWizyty != null) {
			terminKonc = LocalDateTime.parse(terminKoncaWizyty, formatTerminu);
		}
		List<Wizyta> dopasowaneWizyty = new LinkedList<>();
		for (Wizyta wizyta : wizyty) {
			if (idPacjenta != null)
				if (!wizyta.getPacjentId().equals(pacId))
					continue;
			if (idLekarza != null)
				if (!wizyta.getLekarzId().equals(lekId))
					continue;
			if (idGabinetu != null)
				if (!wizyta.getGabinetId().equals(gabId))
					continue;
			if (terminPoczatkuWizyty != null)
				if (!LocalDateTime.parse(wizyta.getTermin(), FORMAT_DATY_I_CZASU).isBefore(terminPocz))
					continue;
			if (terminKoncaWizyty != null)
				if (!LocalDateTime.parse(wizyta.getTermin(), FORMAT_DATY_I_CZASU).isAfter(terminKonc))
					continue;
			dopasowaneWizyty.add(wizyta);
		}
		return dopasowaneWizyty;
	}

	public List<Wizyta> znajdzArchiwalnaWizyte(String idPacjenta, String idLekarza, String terminPoczatkuWizyty,
			String terminKoncaWizyty, DateTimeFormatter formatTerminu, String idGabinetu)
			throws ZleUuidExcpetion, DateTimeParseException {
		UUID pacId = null, lekId = null, gabId = null;
		LocalDateTime terminPocz = null;
		LocalDateTime terminKonc = null;
		if (idPacjenta == null || idPacjenta.isEmpty()) {
			throw new IllegalArgumentException("UUID pacjenta musi zostac podane");
		}
		pacId = UUID.fromString(idPacjenta);
		if (!pacjenci.containsKey(pacId)) {
			throw new ZleUuidExcpetion("Podane UUID nie odnosi sie do zadnego pacjenta");
		}
		if (idLekarza != null) {
			lekId = UUID.fromString(idLekarza);
			if (!lekarze.containsKey(lekId)) {
				throw new ZleUuidExcpetion("Podane UUID nie odpowiada zadnemu lekarzowi");
			}
		}
		if (idGabinetu != null) {
			gabId = UUID.fromString(idGabinetu);
			if (!gabinety.containsKey(gabId)) {
				throw new ZleUuidExcpetion("Podane UUID nie odpowiada zadnemu gabinetowi");
			}
		}
		if (terminPoczatkuWizyty != null) {
			terminPocz = LocalDateTime.parse(terminPoczatkuWizyty, formatTerminu);
		}
		if (terminKoncaWizyty != null) {
			terminKonc = LocalDateTime.parse(terminKoncaWizyty, formatTerminu);
		}
		Pacjent pacjent = pacjenci.get(pacId);
		List<Wizyta> dopasowaneWizyty = new LinkedList<>();
		for (Wizyta wizyta : pacjent.getHistoriaWizyt()) {
			if (idLekarza != null)
				if (!wizyta.getLekarzId().equals(lekId))
					continue;
			if (idGabinetu != null)
				if (!wizyta.getGabinetId().equals(gabId))
					continue;
			if (terminPoczatkuWizyty != null)
				if (!LocalDateTime.parse(wizyta.getTermin(), FORMAT_DATY_I_CZASU).isBefore(terminPocz))
					continue;
			if (terminKoncaWizyty != null)
				if (!LocalDateTime.parse(wizyta.getTermin(), FORMAT_DATY_I_CZASU).isAfter(terminKonc))
					continue;
			dopasowaneWizyty.add(wizyta);
		}
		return dopasowaneWizyty;
	}

	public void utworzWizyte(String idPacjenta, String idLekarza, String idGabinetu, String terminWizyty,
			String czasTrwaniaWizyty, DateTimeFormatter formatTerminu)
			throws ZleUuidExcpetion, IllegalArgumentException, DateTimeParseException, TerminZajetyException,
			TerminNiedostepnyException, ZasobNiedostepnyException, ZlyStanWizytyException, NumberFormatException {
		Long lCzasTrwaniaWizyty = new Long(czasTrwaniaWizyty);
		UUID pacId = null, lekId = null, gabId = null;
		LocalDateTime.parse(terminWizyty, formatTerminu);
		if (czasTrwaniaWizyty == null) {
			throw new IllegalArgumentException("Czas trwania wizyty musi zostac podany");
		}
		if (idPacjenta == null || idPacjenta.isEmpty()) {
			throw new IllegalArgumentException("UUID pacjenta musi zostac podane");
		}
		pacId = UUID.fromString(idPacjenta);
		if (!pacjenci.containsKey(pacId)) {
			throw new ZleUuidExcpetion("Podane UUID nie odnosi sie do zadnego pacjenta");
		}
		if (idLekarza == null || idLekarza.isEmpty()) {
			throw new IllegalArgumentException("UUID lekarza musi zostac podane");
		}
		lekId = UUID.fromString(idLekarza);
		if (!lekarze.containsKey(lekId)) {
			throw new ZleUuidExcpetion("Podane UUID nie odnosi sie do zadnego lekarza");
		}
		if (idGabinetu == null || idGabinetu.isEmpty()) {
			throw new IllegalArgumentException("UUID gabinetu musi zostac podane");
		}
		gabId = UUID.fromString(idGabinetu);
		if (!gabinety.containsKey(gabId)) {
			throw new ZleUuidExcpetion("Podane UUID nie odnosi sie do zadnego gabinetu");
		}
		Wizyta nowaWizyta = new Wizyta();
		for (Wizyta w : wizyty) {
			if (w.getId().equals(nowaWizyta.getId())) {
				throw new ZleUuidExcpetion("Cos poszlo nie tak, sprobuj ponownie");
			}
		}
		nowaWizyta.setPacjentId(pacId);
		nowaWizyta.setTermin(terminWizyty, formatTerminu);
		nowaWizyta.setCzasTrwania(lCzasTrwaniaWizyty);
		lekarze.get(lekId).dodajGodzineWizyty(terminWizyty, formatTerminu);
		nowaWizyta.setLekarzId(lekId);
		gabinety.get(gabId).dodajGodzineRezerwacji(terminWizyty, formatTerminu);
		nowaWizyta.setGabinetId(gabId);
		nowaWizyta.utworz();
		wizyty.add(nowaWizyta);
	}

	public void zakonczWizyte(String idWizyty, String stanZakonczenia)
			throws ZasobNiedostepnyException, ZlyStanWizytyException, DateTimeParseException,
			TerminNiedostepnyException, TerminWolnyException, IllegalArgumentException, Exception {
		UUID idWiz = UUID.fromString(idWizyty);
		Wizyta wizyta = null;
		for (Wizyta w : wizyty) {
			if (w.getId().equals(idWiz)) {
				wizyta = w;
				break;
			}
		}
		if (wizyta == null) {
			throw new ZasobNiedostepnyException("Brak aktywnej wizyty o podanym UUID");
		}
		Wizyta.StanWizyty stanZakWiz = null;
		if (stanZakonczenia.toLowerCase().equals("zakonczona")) {
			stanZakWiz = Wizyta.StanWizyty.ZAKONCZONA;
		} else if (stanZakonczenia.toLowerCase().equals("nieodbyta")) {
			stanZakWiz = Wizyta.StanWizyty.NIEODBYTA;
		} else {
			throw new IllegalArgumentException(
					"Podany stan zakonczenia nie jest poprawny, poprawne stany to: zakonczona, nieodbyta");
		}
		switch (stanZakWiz) {
		case ZAKONCZONA:
			wizyta.zakoncz();
			break;
		case NIEODBYTA:
			lekarze.get(wizyta.getLekarzId()).usunGodzineWizyty(wizyta.getTermin(), FORMAT_DATY_I_CZASU);
			gabinety.get(wizyta.getGabinetId()).usunGodzineRezerwacji(wizyta.getTermin(), FORMAT_DATY_I_CZASU);
			wizyta.oznaczJakoNieodbyta();
			break;
		default:
			throw new Exception("Something went wrong");
		}
	}

	public void zarchiwizujWizyte(String idWizyty)
			throws ZasobNiedostepnyException, ZlyStanWizytyException, IllegalArgumentException {
		UUID idWiz = UUID.fromString(idWizyty);
		Wizyta wizyta = null;
		for (Wizyta w : wizyty) {
			if (w.getId().equals(idWiz)) {
				wizyta = w;
				break;
			}
		}
		if (wizyta == null) {
			throw new ZasobNiedostepnyException("Brak aktywnej wizyty o podanym UUID");
		}
		Pacjent pacjent = pacjenci.get(wizyta.getPacjentId());
		wizyta.zarchiwizuj();
		pacjent.zapiszWizyte(wizyta);
		wizyty.remove(wizyta);
	}

	public String getNazwa() {
		return nazwa;
	}

	public void setNazwa(String nazwa) {
		this.nazwa = nazwa;
	}

	public String przetlumaczWizyte(Wizyta w) {
		StringBuilder sb = new StringBuilder();
		sb.append("UUID: " + w.getId() + "\n");
		sb.append("   Termin wizyty: " + w.getTermin() + "\n");
		sb.append("   Pacjent: " + pacjenci.get(w.getPacjentId()).toString() + "\n");
		sb.append("   Lekarz: " + lekarze.get(w.getLekarzId()).toString() + "\n");
		sb.append("   Gabinet: " + gabinety.get(w.getGabinetId()).toString() + "\n");
		sb.append("   Czas trwania: " + w.getCzasTrwania() + "\n");
		sb.append("   Stan wizyty: ");
		switch (w.getStanWizyty()) {
		case HISTORIA:
			sb.append("zarchiwizowana");
			break;
		case NIEODBYTA:
			sb.append("nie odbyla sie lub odwolana");
			break;
		case NIEWLASCIWY:
			sb.append("nieokreslony");
			break;
		case UTWORZONA:
			sb.append("utworzona");
			break;
		case ZAKONCZONA:
			sb.append("zakonczona");
			break;
		default:
			break;
		}
		return sb.toString();
	}
}
