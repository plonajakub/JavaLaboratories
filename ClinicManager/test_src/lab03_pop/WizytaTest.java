package lab03_pop;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import wyjatki.ZlyStanWizytyException;

class WizytaTest {

	private Wizyta wizyta;
	private UUID id1, id2;

	@BeforeEach
	void setUp() throws Exception {
		id1 = UUID.randomUUID();
		id2 = UUID.randomUUID();
		wizyta = new Wizyta();
		wizyta.setTermin("2018-11-05T08:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		wizyta.setCzasTrwania(20L);
		wizyta.setLekarzId(id1);
		wizyta.setPacjentId(id1);
		wizyta.setGabinetId(id1);
	}

	@Test
	void testWizyta() {
		wizyta = new Wizyta();
		assertNull(wizyta.getTermin());
		assertNull(wizyta.getCzasTrwania());
		assertNull(wizyta.getLekarzId());
		assertNull(wizyta.getPacjentId());
		assertNull(wizyta.getGabinetId());
		assertEquals(Wizyta.StanWizyty.NIEWLASCIWY, wizyta.getStanWizyty());
	}

	@Test
	void testWizytaStringDateTimeFormatterLongLongLongLong() {
		wizyta = new Wizyta("2018-11-05T08:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME, 20L, id1, id1, id1);
		assertEquals("2018-11-05T08:00", wizyta.getTermin());
		assertEquals(new Long(20L), wizyta.getCzasTrwania());
		assertEquals(id1, wizyta.getLekarzId());
		assertEquals(id1, wizyta.getPacjentId());
		assertEquals(id1, wizyta.getGabinetId());
		assertEquals(Wizyta.StanWizyty.UTWORZONA, wizyta.getStanWizyty());
	}

	@Test
	void testUtworz() throws ZlyStanWizytyException {
		wizyta.utworz();
	}

	@Test
	void testZakoncz() throws ZlyStanWizytyException {
		wizyta.utworz();
		wizyta.zakoncz();
	}

	@Test
	void testOznaczJakoNieodbyta() throws ZlyStanWizytyException {
		wizyta.utworz();
		wizyta.oznaczJakoNieodbyta();
	}

	@Test
	void testZarchiwizuj() throws ZlyStanWizytyException {
		wizyta.utworz();
		wizyta.zakoncz();
		wizyta.zarchiwizuj();
	}

	@Test
	void testGetStanWizyty() throws ZlyStanWizytyException {
		wizyta.utworz();
		assertEquals(Wizyta.StanWizyty.UTWORZONA, wizyta.getStanWizyty());
	}

	@Test
	void testGetTermin() {
		assertEquals("2018-11-05T08:00", wizyta.getTermin());
	}

	@Test
	void testGetCzasTrwania() {
		assertEquals(new Long(20L), wizyta.getCzasTrwania());
	}

	@Test
	void testGetLekarzId() {
		assertEquals(id1, wizyta.getLekarzId());
	}

	@Test
	void testGetPacjentId() {
		assertEquals(id1, wizyta.getPacjentId());
	}

	@Test
	void testGetGabinetId() {
		assertEquals(id1, wizyta.getGabinetId());
	}

	@Test
	void testSetTermin() {
		wizyta.setTermin("2018-11-05T09:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		assertEquals("2018-11-05T09:00", wizyta.getTermin());
	}

	@Test
	void testSetCzasTrwania() {
		wizyta.setCzasTrwania(30L);
		assertEquals(new Long(30L), wizyta.getCzasTrwania());
	}

	@Test
	void testSetLekarzId() {
		wizyta.setLekarzId(id2);
		assertEquals(id2, wizyta.getLekarzId());
	}

	@Test
	void testSetPacjentId() {
		wizyta.setPacjentId(id2);
		assertEquals(id2, wizyta.getPacjentId());
	}

	@Test
	void testSetGabinetId() {
		wizyta.setGabinetId(id2);
		assertEquals(id2, wizyta.getGabinetId());
	}

	@Test
	void testCompareToWizyta() {
		// TODO to be implemented
	}

}
