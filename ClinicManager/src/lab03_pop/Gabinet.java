package lab03_pop;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import wyjatki.TerminWolnyException;
import wyjatki.TerminZajetyException;
import wyjatki.ZasobNiedostepnyException;

public class Gabinet implements Serializable{

	private static final long serialVersionUID = 4748652360993387410L;
	private final UUID id;
	private Boolean czyIstnieje;

	private final String numer;
	private final Set<LocalDateTime> godzinyRezerwacji = new TreeSet<>();

	public Gabinet(String numer) {
		this.id = UUID.randomUUID();
		this.czyIstnieje = true;
		this.numer = numer;
	}

	public void dodajGodzineRezerwacji(String dataZGodzina, DateTimeFormatter format)
			throws DateTimeParseException, TerminZajetyException, ZasobNiedostepnyException {
		if (!czyIstnieje) {
			throw new ZasobNiedostepnyException("Gabinet zosta³ wyrejestrowany");
		}
		LocalDateTime rezerwacja = LocalDateTime.parse(dataZGodzina, format);
		if (godzinyRezerwacji.contains(rezerwacja)) {
			throw new TerminZajetyException("Gabinet " + numer + " jest w tym terminie niedostepny");
		}
		godzinyRezerwacji.add(rezerwacja);
	}

	public void usunGodzineRezerwacji(String dataZGodzina, DateTimeFormatter format)
			throws DateTimeParseException, TerminWolnyException, ZasobNiedostepnyException {
		if (!czyIstnieje) {
			throw new ZasobNiedostepnyException("Gabinet zosta³ wyrejestrowany");
		}
		LocalDateTime rezerwacja = LocalDateTime.parse(dataZGodzina, format);
		if (!godzinyRezerwacji.contains(rezerwacja)) {
			throw new TerminWolnyException("Gabinet " + numer + "  nie zostal zarezerwowany na podany termin");
		}
		godzinyRezerwacji.remove(rezerwacja);
	}

	public boolean czyGodzinaDostepna(String dataZGodzina, DateTimeFormatter format)
			throws DateTimeParseException, ZasobNiedostepnyException {
		if (!czyIstnieje) {
			throw new ZasobNiedostepnyException("Gabinet zosta³ wyrejestrowany");
		}
		LocalDateTime rezerwacja = LocalDateTime.parse(dataZGodzina, format);
		return !godzinyRezerwacji.contains(rezerwacja);
	}
	
	public boolean czyGabinetUzywany() {
		return !godzinyRezerwacji.isEmpty();
	}

	public UUID getId() {
		return id;
	}

	public String getNumer() {
		return numer;
	}

	public Boolean getCzyIstnieje() {
		return czyIstnieje;
	}

	public void setCzyIstnieje(Boolean czyIstnieje) {
		this.czyIstnieje = czyIstnieje;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("UUID: " + this.id + ", ");
		sb.append("Nr. " + this.numer + ", ");
		sb.append("Czy istnieje: " + (this.czyIstnieje? "tak" : "nie"));
		return sb.toString();
	}

}
