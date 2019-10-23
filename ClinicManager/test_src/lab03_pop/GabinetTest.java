package lab03_pop;

import static org.junit.jupiter.api.Assertions.*;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import wyjatki.TerminWolnyException;
import wyjatki.TerminZajetyException;
import wyjatki.ZasobNiedostepnyException;

class GabinetTest {

	private Gabinet gabinet;

	@BeforeEach
	void setUp() throws Exception {
		gabinet = new Gabinet("C-201L");
	}

	@Test
	void testGabinet() {
		assertEquals("C-201L", gabinet.getNumer());
	}

	@Test
	void testDodajGodzineRezerwacji() throws DateTimeParseException, TerminZajetyException, ZasobNiedostepnyException {
		gabinet.dodajGodzineRezerwacji("2018-11-05T10:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		assertThrows(DateTimeParseException.class, () -> {
			gabinet.dodajGodzineRezerwacji("2018-11-5T10:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		});
		assertThrows(TerminZajetyException.class, () -> {
			gabinet.dodajGodzineRezerwacji("2018-11-05T10:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		});
	}

	@Test
	void testUsunGodzineRezerwacji() throws DateTimeParseException, TerminZajetyException, TerminWolnyException, ZasobNiedostepnyException {
		gabinet.dodajGodzineRezerwacji("2018-11-05T10:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		gabinet.usunGodzineRezerwacji("2018-11-05T10:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		assertThrows(TerminWolnyException.class, () -> {
			gabinet.usunGodzineRezerwacji("2018-11-05T10:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		});
	}

	@Test
	void testCzyGodzinaDostepna() throws DateTimeParseException, TerminZajetyException, ZasobNiedostepnyException {
		gabinet.dodajGodzineRezerwacji("2018-11-05T10:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		assertFalse(gabinet.czyGodzinaDostepna("2018-11-05T10:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME));
		assertTrue(gabinet.czyGodzinaDostepna("2018-11-05T09:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME));
	}

	@Test
	void testGetNumer() {
		assertEquals("C-201L", gabinet.getNumer());
	}

}
