package lab03_pop;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;

import wyjatki.ZlyStanWizytyException;

public class Wizyta implements Comparable<Wizyta>, Serializable {

	private static final long serialVersionUID = 6913682407517179611L;
	private final UUID id;

	private String termin;

	// W minutach
	private Long czasTrwania;

	private UUID lekarzId;
	private UUID pacjentId;
	private UUID gabinetId;

	public enum StanWizyty {
		NIEWLASCIWY, UTWORZONA, ZAKONCZONA, NIEODBYTA, HISTORIA
	};

	private StanWizyty stanWizyty;

	Wizyta() {
		id = UUID.randomUUID();
		termin = null;
		czasTrwania = null;
		lekarzId = null;
		pacjentId = null;
		gabinetId = null;
		stanWizyty = StanWizyty.NIEWLASCIWY;
	}

	public Wizyta(Wizyta o) {
		this.id = UUID.fromString(o.getId().toString());
		this.termin = new String(o.getTermin());
		this.czasTrwania = new Long(o.getCzasTrwania());
		this.lekarzId = UUID.fromString(o.getLekarzId().toString());
		this.pacjentId = UUID.fromString(o.getPacjentId().toString());
		this.gabinetId = UUID.fromString(o.getGabinetId().toString());
		this.stanWizyty = o.getStanWizyty();
	}

	Wizyta(String termin, DateTimeFormatter format, Long czasTrwania, UUID lekarzId, UUID pacjentId, UUID gabinetId)
			throws DateTimeParseException {
		LocalDateTime.parse(termin, format);
		id = UUID.randomUUID();
		this.termin = termin;
		this.czasTrwania = czasTrwania;
		this.lekarzId = lekarzId;
		this.pacjentId = pacjentId;
		this.gabinetId = gabinetId;
		stanWizyty = StanWizyty.UTWORZONA;
	}

	public void utworz() throws ZlyStanWizytyException {
		if (termin == null || czasTrwania == null || lekarzId == null || pacjentId == null || gabinetId == null) {
			throw new ZlyStanWizytyException("Wizyta zwiera puste pola");
		}
		stanWizyty = StanWizyty.UTWORZONA;
	}

	public void zakoncz() throws ZlyStanWizytyException {
		if (stanWizyty != StanWizyty.UTWORZONA) {
			throw new ZlyStanWizytyException("Wizyta ma stan inny, niz \"Utworzona\"");
		}
		stanWizyty = StanWizyty.ZAKONCZONA;
	}

	public void oznaczJakoNieodbyta() throws ZlyStanWizytyException {
		if (stanWizyty != StanWizyty.UTWORZONA) {
			throw new ZlyStanWizytyException("Wizyta ma stan inny, niz \"Utworzona\"");
		}
		stanWizyty = StanWizyty.NIEODBYTA;
	}

	public void zarchiwizuj() throws ZlyStanWizytyException {
		if (stanWizyty != StanWizyty.ZAKONCZONA && stanWizyty != StanWizyty.NIEODBYTA) {
			throw new ZlyStanWizytyException("Wizyta ma stan inny, niz \"Utworzona\" lub \"NIEODBYTA\"");
		}
		stanWizyty = StanWizyty.HISTORIA;
	}

	public UUID getId() {
		return id;
	}

	public StanWizyty getStanWizyty() {
		return stanWizyty;
	}

	public String getTermin() {
		return termin;
	}

	public Long getCzasTrwania() {
		return czasTrwania;
	}

	public UUID getLekarzId() {
		return lekarzId;
	}

	public UUID getPacjentId() {
		return pacjentId;
	}

	public UUID getGabinetId() {
		return gabinetId;
	}

	public void setTermin(String termin, DateTimeFormatter format) throws DateTimeParseException {
		LocalDateTime.parse(termin, format);
		this.termin = termin;
	}

	public void setCzasTrwania(Long czasTrwania) {
		this.czasTrwania = czasTrwania;
	}

	public void setLekarzId(UUID lekarzId) {
		this.lekarzId = lekarzId;
	}

	public void setPacjentId(UUID pacjentId) {
		this.pacjentId = pacjentId;
	}

	public void setGabinetId(UUID gabinetId) {
		this.gabinetId = gabinetId;
	}

	@Override
	public int compareTo(Wizyta wizyta) {
		return LocalDateTime.parse(termin, Przychodnia.FORMAT_DATY_I_CZASU)
				.compareTo(LocalDateTime.parse(wizyta.getTermin(), Przychodnia.FORMAT_DATY_I_CZASU));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((czasTrwania == null) ? 0 : czasTrwania.hashCode());
		result = prime * result + ((gabinetId == null) ? 0 : gabinetId.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((lekarzId == null) ? 0 : lekarzId.hashCode());
		result = prime * result + ((pacjentId == null) ? 0 : pacjentId.hashCode());
		result = prime * result + ((termin == null) ? 0 : termin.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Wizyta other = (Wizyta) obj;
		if (czasTrwania == null) {
			if (other.czasTrwania != null)
				return false;
		} else if (!czasTrwania.equals(other.czasTrwania))
			return false;
		if (gabinetId == null) {
			if (other.gabinetId != null)
				return false;
		} else if (!gabinetId.equals(other.gabinetId))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (lekarzId == null) {
			if (other.lekarzId != null)
				return false;
		} else if (!lekarzId.equals(other.lekarzId))
			return false;
		if (pacjentId == null) {
			if (other.pacjentId != null)
				return false;
		} else if (!pacjentId.equals(other.pacjentId))
			return false;
		if (termin == null) {
			if (other.termin != null)
				return false;
		} else if (!termin.equals(other.termin))
			return false;
		return true;
	}
}
