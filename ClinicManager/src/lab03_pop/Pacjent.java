package lab03_pop;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import wyjatki.ZlyStanWizytyException;

public class Pacjent implements Serializable {

	private static final long serialVersionUID = -7511484740461157315L;
	private final UUID id;
	private Boolean czyZarejestrowany;
	
	private final String imie;
	private final String nazwisko;
	private final Long pesel;

	private final Set<Wizyta> historiaWizyt = new TreeSet<Wizyta>();

	public Pacjent(String imie, String nazwisko, Long pesel) {
		this.id = UUID.randomUUID();
		this.czyZarejestrowany = true;
		this.imie = imie;
		this.nazwisko = nazwisko;
		this.pesel = pesel;
	}

	public void zapiszWizyte(Wizyta w) throws ZlyStanWizytyException {
		if (w.getStanWizyty() != Wizyta.StanWizyty.HISTORIA) {
			throw new ZlyStanWizytyException("Wizyta nie moze byc zapisana do historii ze wzgeldu na zly stan");
		}
		historiaWizyt.add(new Wizyta(w));
	}

	public UUID getId () {
		return id;
	}
	
	public String getImie() {
		return imie;
	}

	public String getNazwisko() {
		return nazwisko;
	}

	public Long getPesel() {
		return pesel;
	}

	public Set<Wizyta> getHistoriaWizyt() {
		return historiaWizyt;
	}

	public Boolean getCzyZarejestrowany() {
		return czyZarejestrowany;
	}

	public void setCzyZarejestrowany(Boolean czyZarejestrowany) {
		this.czyZarejestrowany = czyZarejestrowany;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("UUID: " + this.id + ", ");
		sb.append(" " + this.imie + ", ");
		sb.append(" " + this.nazwisko +", ");
		sb.append(" " + this.pesel + ", ");
		sb.append("Zarejestrowany: " + (this.czyZarejestrowany? "tak":"nie"));
		return sb.toString();
	}
}
