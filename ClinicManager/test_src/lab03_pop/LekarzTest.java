package lab03_pop;

import static org.junit.jupiter.api.Assertions.*;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import wyjatki.TerminNiedostepnyException;
import wyjatki.TerminWolnyException;
import wyjatki.TerminZajetyException;
import wyjatki.ZasobNiedostepnyException;

class LekarzTest {

	private Lekarz lekarz;

	@BeforeEach
	void setUp() throws Exception {
		lekarz = new Lekarz("Jan", "Kowalski", "Internista");
	}

	@Test
	void testLekarz() {
		assertEquals("Jan", lekarz.getImie());
		assertEquals("Kowalski", lekarz.getNazwisko());
		assertEquals("Internista", lekarz.getSpecjalizacja());
	}

	@Test
	void testDodajGodzinyPrzyjec() throws DateTimeParseException, DateTimeException, ZasobNiedostepnyException {
		lekarz.dodajGodzinyPrzyjec("2018-11-05", "2023-11-06", "08:00", "16:00", 20L, DayOfWeek.MONDAY,
				DateTimeFormatter.ISO_LOCAL_DATE, DateTimeFormatter.ISO_LOCAL_TIME);
		lekarz.dodajGodzinyPrzyjec("2018-11-05", "2023-11-06", "09:00", "14:00", 20L, DayOfWeek.TUESDAY,
				DateTimeFormatter.ISO_LOCAL_DATE, DateTimeFormatter.ISO_LOCAL_TIME);
		assertThrows(DateTimeException.class, () -> {
			lekarz.dodajGodzinyPrzyjec("2018-11-05", "2018-11-05", "09:00", "18:00", 20L, DayOfWeek.TUESDAY,
					DateTimeFormatter.ISO_LOCAL_DATE, DateTimeFormatter.ISO_LOCAL_TIME);
		}, "Data poczatkowa nie jest wczesniejsza od koncowej");
		assertThrows(DateTimeException.class, () -> {
			lekarz.dodajGodzinyPrzyjec("2018-11-05", "2023-11-06", "09:00", "09:00", 20L, DayOfWeek.TUESDAY,
					DateTimeFormatter.ISO_LOCAL_DATE, DateTimeFormatter.ISO_LOCAL_TIME);
		}, "Godzina poczatkowa nie jest wczesniejsza od koncowej");
		assertThrows(DateTimeParseException.class, () -> {
			lekarz.dodajGodzinyPrzyjec("2018-11-05", "2023-11-06", "9:00", "18:00", 20L, DayOfWeek.TUESDAY,
					DateTimeFormatter.ISO_LOCAL_DATE, DateTimeFormatter.ISO_LOCAL_TIME);
		});
	}

	@Test
	void testDodajGodzinePrzyjecia() throws DateTimeParseException, TerminZajetyException, TerminNiedostepnyException, ZasobNiedostepnyException {
		lekarz.dodajGodzinePrzyjecia("2018-11-04T10:19", 20L, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		lekarz.dodajGodzinePrzyjecia("2018-11-04T09:40", 20L, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		assertThrows(TerminNiedostepnyException.class, () -> {
			lekarz.dodajGodzinePrzyjecia("2018-11-04T10:19", 20L, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		}, "Podany termin juz isnieje");
		assertThrows(TerminZajetyException.class, () -> {
			lekarz.dodajGodzinePrzyjecia("2018-11-04T10:00", 20L, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		}, "Wprowadzona godzina przyjecia koliduje z obecnymi terminami");
		assertThrows(TerminZajetyException.class, () -> {
			lekarz.dodajGodzinePrzyjecia("2018-11-04T09:59", 20L, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		}, "Wprowadzona godzina przyjecia koliduje z obecnymi terminami");
	}

	@Test
	void testUsunGodzinePrzyjecia() throws DateTimeParseException, TerminZajetyException, TerminNiedostepnyException, ZasobNiedostepnyException {
		lekarz.dodajGodzinePrzyjecia("2018-11-05T08:00", 20L, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		lekarz.usunGodzinePrzyjecia("2018-11-05T08:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		// TODO write more test cases
	}

	@Test
	void testDodajGodzineWizyty() throws DateTimeParseException, TerminZajetyException, TerminNiedostepnyException, ZasobNiedostepnyException {
		lekarz.dodajGodzinePrzyjecia("2018-11-05T08:00", 20L, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		lekarz.dodajGodzineWizyty("2018-11-05T08:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		// TODO write more test cases
	}

	@Test
	void testUsunGodzineWizyty()
			throws DateTimeParseException, TerminNiedostepnyException, TerminWolnyException, TerminZajetyException, ZasobNiedostepnyException {
		lekarz.dodajGodzinePrzyjecia("2018-11-05T08:00", 20L, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		lekarz.dodajGodzineWizyty("2018-11-05T08:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		lekarz.usunGodzineWizyty("2018-11-05T08:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		// TODO write more test cases
	}

	@Test
	void testCzyTerminDostepny() throws DateTimeParseException, TerminNiedostepnyException, TerminZajetyException, ZasobNiedostepnyException {
		lekarz.dodajGodzinePrzyjecia("2018-11-05T08:00", 20L, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		assertTrue(lekarz.czyTerminDostepny("2018-11-05T08:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME));
		// TODO write more test cases
	}

	@Test
	void testPodajWolneTermiy() throws DateTimeParseException, DateTimeException, ZasobNiedostepnyException {
		lekarz.dodajGodzinyPrzyjec("2018-11-05", "2018-11-06", "08:00", "16:00", 20L, DayOfWeek.MONDAY,
				DateTimeFormatter.ISO_LOCAL_DATE, DateTimeFormatter.ISO_LOCAL_TIME);
		List<LocalDateTime> terminy = new LinkedList<>();
		LocalDateTime terminP = LocalDateTime.parse("2018-11-05T08:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		while (terminP.isBefore(LocalDateTime.parse("2018-11-05T16:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME))) {
			terminy.add(terminP);
			terminP = terminP.plusMinutes(20);
		}
		assertEquals(terminy, lekarz.znajdzWolneTerminy("2018-11-05", "2018-11-06", DateTimeFormatter.ISO_LOCAL_DATE));
	}

	@Test
	void testGetImie() {
		assertEquals("Jan", lekarz.getImie());
	}

	@Test
	void testGetNazwisko() {
		assertEquals("Kowalski", lekarz.getNazwisko());
	}

	@Test
	void testGetSpecjalizacja() {
		assertEquals("Internista", lekarz.getSpecjalizacja());
	}

}
