package lab03_pop;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import wyjatki.ZlyStanWizytyException;

class PacjentTest {

	private Pacjent pacjent;
	private UUID id1;

	@BeforeEach
	void setUp() throws Exception {
		pacjent = new Pacjent("Michal", "Adamski", 98563221548L);
		id1 = UUID.randomUUID();
	}

	@Test
	void testZapiszWizyte() throws ZlyStanWizytyException {
		Wizyta w = new Wizyta("2018-11-04T10:25", DateTimeFormatter.ISO_LOCAL_DATE_TIME, 20L, id1, id1, id1);
		w.zakoncz();
		w.zarchiwizuj();
		pacjent.zapiszWizyte(w);
	}
}
