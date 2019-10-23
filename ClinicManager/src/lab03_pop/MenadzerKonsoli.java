package lab03_pop;

import java.time.DateTimeException;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import wyjatki.TerminNiedostepnyException;
import wyjatki.TerminWolnyException;
import wyjatki.TerminZajetyException;
import wyjatki.ZasobDostepnyException;
import wyjatki.ZasobNiedostepnyException;
import wyjatki.ZleUuidExcpetion;
import wyjatki.ZlyStanWizytyException;

public class MenadzerKonsoli {

	private final Przychodnia przychodnia;

	private enum Operacja {
		PORAZKA, SUKCES
	}

	public static void main(String[] args) {
		MenadzerKonsoli mk = new MenadzerKonsoli();
		mk.glownaPetlaProgramu();
	}

	public MenadzerKonsoli() {
		this.przychodnia = MenadzerDanych.odtworzStanProgramuZPliku();
	}

	public void glownaPetlaProgramu() {
		int liczbaWejsciowa = -1;
		Scanner sc = new Scanner(System.in);
		Operacja status = Operacja.PORAZKA;

		System.out.println("\n\nWitaj w programie do zarzadzania przychodnia!");
		do {
			System.out.println();
			System.out.println("Menu operacji:");
			System.out.println("1. Zarzadzaj zasobami");
			System.out.println("2. Szukaj zasobow");
			System.out.println("3. Zarzadzaj wizytami");
			System.out.println("0. Wyjscie z programu");
			System.out.println();
			System.out.print("Wprowadz numer operacji: ");
			while (status != Operacja.SUKCES) {
				try {
					liczbaWejsciowa = sc.nextInt();
					if (liczbaWejsciowa != 0 && liczbaWejsciowa != 1 && liczbaWejsciowa != 2 && liczbaWejsciowa != 3) {
						System.out.print("Wprowadzono niepoprawny numer operacji, sprobuj ponownie: ");
						continue;
					}
					status = Operacja.SUKCES;
				} catch (InputMismatchException e) {
					System.out.print("Wprowadzono nieprawidlowe dane, sprobuj jeszcze raz: ");
					sc.next();
				}

			}

			switch (liczbaWejsciowa) {
			case 0:
				sc.close();
				MenadzerDanych.zapiszStanProgramu(przychodnia);
				System.out.println("---Program zakonczyl dzialanie---");
				return;
			case 1:
				zarzadzajZasobami(sc);
				break;
			case 2:
				szukajZasobow(sc);
				break;
			case 3:
				zarzadzajWizytami(sc);
				break;
			default:
				break;
			}
			status = Operacja.PORAZKA;
		} while (true);
	}

	private void zarzadzajZasobami(Scanner sc) {
		int liczbaWejsciowa = -1;
		Operacja status = Operacja.PORAZKA;
		do {
			System.out.println();
			System.out.println("Menu operacji:");
			System.out.println("1. Zarzadzaj lekarzami");
			System.out.println("2. Zarzadzaj pacjentami");
			System.out.println("3. Zarzadzaj gabinetami");
			System.out.println("0. Powrot");
			System.out.println();
			System.out.print("Wprowadz numer operacji: ");
			while (status != Operacja.SUKCES) {
				try {
					;
					liczbaWejsciowa = sc.nextInt();
					if (liczbaWejsciowa != 0 && liczbaWejsciowa != 1 && liczbaWejsciowa != 2 && liczbaWejsciowa != 3) {
						System.out.print("Wprowadzono niepoprawny numer operacji, sprobuj ponownie: ");
						continue;
					}
					status = Operacja.SUKCES;
				} catch (InputMismatchException e) {
					System.out.print("Wprowadzono nieprawidlowe dane, sprobuj jeszcze raz: ");
					sc.next();
				}

			}

			switch (liczbaWejsciowa) {
			case 0:
				System.out.println("---Powrot---");
				return;
			case 1:
				zarzadzajLekarzami(sc);
				break;
			case 2:
				zarzadzajPacjentami(sc);
				break;
			case 3:
				zarzadzajGabinetami(sc);
				break;
			default:
				break;
			}
			status = Operacja.PORAZKA;
		} while (true);
	}

	private void zarzadzajGabinetami(Scanner sc) {
		int liczbaWejsciowa = -1;
		Operacja status = Operacja.PORAZKA;
		do {
			System.out.println();
			System.out.println("Menu operacji:");
			System.out.println("1. Dodaj gabinet");
			System.out.println("2. Usun gabinet");
			System.out.println("3. Przywroc gabinet");
			System.out.println("4. Sprawdz, czy dodano gabinet");
			System.out.println("0. Powrot");
			System.out.println();
			System.out.print("Wprowadz numer operacji: ");
			while (status != Operacja.SUKCES) {
				try {
					liczbaWejsciowa = sc.nextInt();
					if (liczbaWejsciowa != 0 && liczbaWejsciowa != 1 && liczbaWejsciowa != 2 && liczbaWejsciowa != 3
							&& liczbaWejsciowa != 4) {
						System.out.print("Wprowadzono niepoprawny numer operacji, sprobuj ponownie: ");
						continue;
					}
					status = Operacja.SUKCES;
				} catch (InputMismatchException e) {
					System.out.print("Wprowadzono nieprawidlowe dane, sprobuj jeszcze raz: ");
					sc.next();
				}

			}

			switch (liczbaWejsciowa) {
			case 0:
				System.out.println("---Powrot---");
				return;
			case 1:
				dodajGabinet(sc);
				break;
			case 2:
				usunGabinet(sc);
				break;
			case 3:
				przywrocGabinet(sc);
				break;
			case 4:
				sprawdzCzyDodanoGabinet(sc);
				break;
			default:
				break;
			}
			status = Operacja.PORAZKA;
		} while (true);
	}

	private void sprawdzCzyDodanoGabinet(Scanner sc) {
		String numer = null;
		System.out.print("Wprowadz numer gabinetu: ");
		while (numer == null) {
			numer = sc.next();
		}
		if (numer.equals("-"))
			numer = null;
		try {
			boolean czyWpr = przychodnia.czyWprowadzonoGabinet(numer);
			if (czyWpr) {
				System.out.println("Gabinet zostal juz dodany");
			} else {
				System.out.println("Gabinet nie byl jeszcze dodawany");
			}
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
	}

	private void przywrocGabinet(Scanner sc) {
		String uuid = null;
		System.out.print("Wprowadz UUID gabinetu: ");
		while (uuid == null) {
			uuid = sc.next();
		}
		try {
			przychodnia.przywrocGabinet(uuid);
			System.out.println("Gabinet zostal przywrocony!");
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		} catch (ZleUuidExcpetion e) {
			System.out.println(e.getMessage());
		}

	}

	private void usunGabinet(Scanner sc) {
		String uuid = null;
		System.out.print("Wprowadz UUID gabinetu: ");
		while (uuid == null) {
			uuid = sc.next();
		}
		try {
			przychodnia.usunGabinet(uuid);
			System.out.println("Gabinet zostal usuniety!");
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		} catch (ZasobNiedostepnyException e) {
			System.out.println(e.getMessage());
		} catch (ZleUuidExcpetion e) {
			System.out.println(e.getMessage());
		}

	}

	private void dodajGabinet(Scanner sc) {
		String numer = null;
		System.out.print("Wprowadz numer gabinetu: ");
		while (numer == null) {
			numer = sc.next();
		}
		if (numer.equals("-"))
			numer = null;
		try {
			przychodnia.dodajGabinet(numer);
			System.out.println("Dodano!");
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		} catch (ZasobDostepnyException e) {
			System.out.println(e.getMessage());
		} catch (ZleUuidExcpetion e) {
			System.out.println(e.getMessage());
		}
	}

	private void zarzadzajPacjentami(Scanner sc) {
		int liczbaWejsciowa = -1;
		Operacja status = Operacja.PORAZKA;
		do {
			System.out.println();
			System.out.println("Menu operacji:");
			System.out.println("1. Zarejestruj pacjenta");
			System.out.println("2. Wyrejestruj pacjenta");
			System.out.println("3. Zarejestruj pacjenta ponownie");
			System.out.println("4. Sprawdz, czy pacjent byl rejestrowany");
			System.out.println("0. Powrot");
			System.out.println();
			System.out.print("Wprowadz numer operacji: ");
			while (status != Operacja.SUKCES) {
				try {
					liczbaWejsciowa = sc.nextInt();
					if (liczbaWejsciowa != 0 && liczbaWejsciowa != 1 && liczbaWejsciowa != 2 && liczbaWejsciowa != 3
							&& liczbaWejsciowa != 4) {
						System.out.print("Wprowadzono niepoprawny numer operacji, sprobuj ponownie: ");
						continue;
					}
					status = Operacja.SUKCES;
				} catch (InputMismatchException e) {
					System.out.print("Wprowadzono nieprawidlowe dane, sprobuj jeszcze raz: ");
					sc.next();
				}

			}

			switch (liczbaWejsciowa) {
			case 0:
				System.out.println("---Powrot---");
				return;
			case 1:
				zarejestrujPacjenta(sc);
				break;
			case 2:
				wyrejestrujPacjenta(sc);
				break;
			case 3:
				zarejestrujPacjentaPonownie(sc);
				break;
			case 4:
				sprawdzCzyRejestrowanoPacjenta(sc);
				break;
			default:
				break;
			}
			status = Operacja.PORAZKA;
		} while (true);

	}

	private void sprawdzCzyRejestrowanoPacjenta(Scanner sc) {
		String imie = null;
		String nazwisko = null;
		String pesel = null;
		System.out.print("Wprowadz imie pacjenta: ");
		while (imie == null) {
			imie = sc.next();
		}
		System.out.print("Wprowadz nazwisko pacjenta: ");
		while (nazwisko == null) {
			nazwisko = sc.next();
		}
		System.out.print("Wprowadz PESEL pacjenta: ");
		while (pesel == null) {
			pesel = sc.next();
		}
		try {
			boolean czyZ = przychodnia.czyWprowadzonoPacjenta(imie, nazwisko, pesel);
			if (czyZ) {
				System.out.println("Pacjent zostal juz zarejestrowany");
			} else {
				System.out.println("Pacjent nie byl jeszcze rejestrowany");
			}
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}

	}

	private void zarejestrujPacjentaPonownie(Scanner sc) {
		String uuid = null;
		System.out.print("Wprowadz UUID pacjenta: ");
		while (uuid == null) {
			uuid = sc.next();
		}
		try {
			przychodnia.przywrocPacjenta(uuid);
			System.out.println("Pacjent zostal ponownie zarejestrowany!");
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		} catch (ZleUuidExcpetion e) {
			System.out.println(e.getMessage());
		} catch (ZasobDostepnyException e) {
			System.out.println(e.getMessage());
		}

	}

	private void wyrejestrujPacjenta(Scanner sc) {
		String uuid = null;
		System.out.print("Wprowadz UUID pacjenta: ");
		while (uuid == null) {
			uuid = sc.next();
		}
		try {
			przychodnia.usunPacjenta(uuid);
			System.out.println("Pacjent zostal wyrejestrowany!");
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		} catch (ZasobNiedostepnyException e) {
			System.out.println(e.getMessage());
		} catch (ZleUuidExcpetion e) {
			System.out.println(e.getMessage());
		}

	}

	private void zarejestrujPacjenta(Scanner sc) {
		String imie = null;
		String nazwisko = null;
		String pesel = null;
		System.out.print("Wprowadz imie pacjenta: ");
		while (imie == null) {
			imie = sc.next();
		}
		System.out.print("Wprowadz nazwisko pacjenta: ");
		while (nazwisko == null) {
			nazwisko = sc.next();
		}
		System.out.print("Wprowadz PESEL pacjenta: ");
		while (pesel == null) {
			pesel = sc.next();
		}
		try {
			przychodnia.dodajPacjenta(imie, nazwisko, pesel);
			System.out.println("Zarejestrowano!");
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		} catch (ZasobDostepnyException e) {
			System.out.println(e.getMessage());
		} catch (ZleUuidExcpetion e) {
			System.out.println(e.getMessage());
		}

	}

	private void zarzadzajLekarzami(Scanner sc) {
		int liczbaWejsciowa = -1;
		Operacja status = Operacja.PORAZKA;
		do {
			System.out.println();
			System.out.println("Menu operacji:");
			System.out.println("1. Zatrudnij lekarza");
			System.out.println("2. Dodaj godziny pracy lekarza");
			System.out.println("3. Dodaj godzine pracy lekarza");
			System.out.println("4. Usun godzine pracy lekarza");
			System.out.println("5. Zwolnij lekarza");
			System.out.println("6. Ponownie zatrudnij lekarza");
			System.out.println("7. Sprawdz, czy lekarz byl zatrudniony");
			System.out.println("0. Powrot");
			System.out.println();
			System.out.print("Wprowadz numer operacji: ");
			while (status != Operacja.SUKCES) {
				try {
					liczbaWejsciowa = sc.nextInt();
					if (liczbaWejsciowa != 0 && liczbaWejsciowa != 1 && liczbaWejsciowa != 2 && liczbaWejsciowa != 3
							&& liczbaWejsciowa != 4 && liczbaWejsciowa != 5 && liczbaWejsciowa != 6
							&& liczbaWejsciowa != 7) {
						System.out.print("Wprowadzono niepoprawny numer operacji, sprobuj ponownie: ");
						continue;
					}
					status = Operacja.SUKCES;
				} catch (InputMismatchException e) {
					System.out.print("Wprowadzono nieprawidlowe dane, sprobuj jeszcze raz: ");
					sc.next();
				}

			}

			switch (liczbaWejsciowa) {
			case 0:
				System.out.println("---Powrot---");
				return;
			case 1:
				zatrudnijLekarza(sc);
				break;
			case 2:
				dodajGodzinyPracyLekarza(sc);
				break;
			case 3:
				dodajGodzinePracyLekarza(sc);
				break;
			case 4:
				usunGodzinePracyLekarza(sc);
				break;
			case 5:
				zwolnijLekarza(sc);
				break;
			case 6:
				ponownieZatrudnijLekarza(sc);
				break;
			case 7:
				sprawdzCzyLekarzBylZatrudniony(sc);
			default:
				break;
			}
			status = Operacja.PORAZKA;
		} while (true);
	}

	private void sprawdzCzyLekarzBylZatrudniony(Scanner sc) {
		String imie = null;
		String nazwisko = null;
		String specjalizacja = null;
		System.out.print("Wprowadz imie lekarza: ");
		while (imie == null) {
			imie = sc.next();
		}
		System.out.print("Wprowadz nazwisko lekarza: ");
		while (nazwisko == null) {
			nazwisko = sc.next();
		}
		System.out.print("Wprowadz specjalizacje lekarza: ");
		while (specjalizacja == null) {
			specjalizacja = sc.next();
		}
		try {
			boolean czyZ = przychodnia.czyWprowadzonoLekarza(imie, nazwisko, specjalizacja);
			if (czyZ) {
				System.out.println("Lekarz byl juz zatrudniony");
			} else {
				System.out.println("Lekarz nie byl jeszcze zatrudniony");
			}
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}

	}

	private void ponownieZatrudnijLekarza(Scanner sc) {
		String uuid = null;
		System.out.print("Wprowadz UUID lekarza: ");
		while (uuid == null) {
			uuid = sc.next();
		}
		try {
			przychodnia.przywrocLekarza(uuid);
			System.out.println("Lekarz zostal ponownie zatrudniony!");
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		} catch (ZleUuidExcpetion e) {
			System.out.println(e.getMessage());
		} catch (ZasobDostepnyException e) {
			System.out.println(e.getMessage());
		}

	}

	private void zwolnijLekarza(Scanner sc) {
		String uuid = null;
		System.out.print("Wprowadz UUID lekarza: ");
		while (uuid == null) {
			uuid = sc.next();
		}
		try {
			przychodnia.usunLekarza(uuid);
			System.out.println("Lekarz zostal zwolniony!");
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		} catch (ZleUuidExcpetion e) {
			System.out.println(e.getMessage());
		} catch (ZasobNiedostepnyException e) {
			System.out.println(e.getMessage());
		}

	}

	private void usunGodzinePracyLekarza(Scanner sc) {
		String uuid = null;
		String terminPracy = null;
		System.out.print("Wprowadz UUID lekarza: ");
		while (uuid == null) {
			uuid = sc.next();
		}
		System.out.print("Wprowadz termin pracy lekarza do usuniecia: ");
		while (terminPracy == null) {
			terminPracy = sc.next();
		}
		try {
			przychodnia.usunGodzinePrzyjecLekarza(uuid, terminPracy, Przychodnia.FORMAT_DATY_I_CZASU);
			System.out.println("Usunieto!");
		} catch (DateTimeParseException e) {
			System.out.println(e.getMessage());
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		} catch (ZasobNiedostepnyException e) {
			System.out.println(e.getMessage());
		} catch (TerminZajetyException e) {
			System.out.println(e.getMessage());
		} catch (TerminNiedostepnyException e) {
			System.out.println(e.getMessage());
		}

	}

	private void dodajGodzinePracyLekarza(Scanner sc) {
		String uuid = null;
		String terminPracy = null;
		String czasWizyty = null;
		System.out.print("Wprowadz UUID lekarza: ");
		while (uuid == null) {
			uuid = sc.next();
		}
		System.out.print("Wprowadz nowy termin pracy lekarza: ");
		while (terminPracy == null) {
			terminPracy = sc.next();
		}

		System.out.print("Wprowadz czas trwania wizyty u lekarza: ");
		while (czasWizyty == null) {
			czasWizyty = sc.next();
		}
		try {
			przychodnia.dodajGodzinePrzyjecLekarza(uuid, terminPracy, czasWizyty, Przychodnia.FORMAT_DATY_I_CZASU);
			System.out.println("Dodano!");
		} catch (DateTimeParseException e) {
			System.out.println(e.getMessage());
		} catch (NumberFormatException e) {
			System.out.println(e.getMessage());
		} catch (DateTimeException e) {
			System.out.println(e.getMessage());
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		} catch (ZasobNiedostepnyException e) {
			System.out.println(e.getMessage());
		} catch (TerminZajetyException e) {
			System.out.println(e.getMessage());
		} catch (TerminNiedostepnyException e) {
			System.out.println(e.getMessage());
		}

	}

	private void dodajGodzinyPracyLekarza(Scanner sc) {
		String uuid = null;
		String dataPocz = null;
		String dataKonc = null;
		String godzinaPocz = null;
		String godzinaKonc = null;
		String dzienTyg = null;
		String czasWizyty = null;
		System.out.print("Wprowadz UUID lekarza: ");
		while (uuid == null) {
			uuid = sc.next();
		}
		System.out.print("Wprowadz date poczatkowa: ");
		while (dataPocz == null) {
			dataPocz = sc.next();
		}
		System.out.print("Wprowadz date koncowa: ");
		while (dataKonc == null) {
			dataKonc = sc.next();
		}
		System.out.print("Wprowadz godzine poczatkowa: ");
		while (godzinaPocz == null) {
			godzinaPocz = sc.next();
		}
		System.out.print("Wprowadz godzine koncowa: ");
		while (godzinaKonc == null) {
			godzinaKonc = sc.next();
		}
		System.out.print("Wprowadz czas trwania wizyty u lekarza: ");
		while (czasWizyty == null) {
			czasWizyty = sc.next();
		}
		System.out.print("Wprowadz dzien tygodnia dla podanych danych: ");
		while (dzienTyg == null) {
			dzienTyg = sc.next();
		}
		try {
			przychodnia.dodajGodzinyPrzyjecLekarza(uuid, dataPocz, dataKonc, godzinaPocz, godzinaKonc, czasWizyty,
					dzienTyg, Przychodnia.FORMAT_DATY, Przychodnia.FORMAT_CZASU);
			System.out.println("Dodano!");
		} catch (DateTimeParseException e) {
			System.out.println(e.getMessage());
		} catch (NumberFormatException e) {
			System.out.println(e.getMessage());
		} catch (DateTimeException e) {
			System.out.println(e.getMessage());
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		} catch (ZasobNiedostepnyException e) {
			System.out.println(e.getMessage());
		}
	}

	private void zatrudnijLekarza(Scanner sc) {
		String imie = null;
		String nazwisko = null;
		String specjalizacja = null;
		System.out.print("Wprowadz imie lekarza: ");
		while (imie == null) {
			imie = sc.next();
		}
		System.out.print("Wprowadz nazwisko lekarza: ");
		while (nazwisko == null) {
			nazwisko = sc.next();
		}
		System.out.print("Wprowadz specjalizacje lekarza: ");
		while (specjalizacja == null) {
			specjalizacja = sc.next();
		}
		try {
			przychodnia.dodajLekarza(imie, nazwisko, specjalizacja);
			System.out.println("Zatrudniono!");
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		} catch (ZasobDostepnyException e) {
			System.out.println(e.getMessage());
		} catch (ZleUuidExcpetion e) {
			System.out.println(e.getMessage());
		}
	}

	private void zarzadzajWizytami(Scanner sc) {
		int liczbaWejsciowa = -1;
		Operacja status = Operacja.PORAZKA;
		do {
			System.out.println();
			System.out.println("Menu operacji:");
			System.out.println("1. Utworz wizyte");
			System.out.println("2. Zakoncz wizyte");
			System.out.println("3. Zarchiwizuj wizyte");
			System.out.println("4. Znajdz aktywna wizyte");
			System.out.println("5. Znajdz archiwalna wizyte");
			System.out.println("6. Znajdz wolne terminy lekarza");
			System.out.println("0. Powrot");
			System.out.println();
			System.out.print("Wprowadz numer operacji: ");
			while (status != Operacja.SUKCES) {
				try {
					liczbaWejsciowa = sc.nextInt();
					if (liczbaWejsciowa != 0 && liczbaWejsciowa != 1 && liczbaWejsciowa != 2 && liczbaWejsciowa != 3
							&& liczbaWejsciowa != 4 && liczbaWejsciowa != 5 && liczbaWejsciowa != 6) {
						System.out.print("Wprowadzono niepoprawny numer operacji, sprobuj ponownie: ");
						continue;
					}
					status = Operacja.SUKCES;
				} catch (InputMismatchException e) {
					System.out.print("Wprowadzono nieprawidlowe dane, sprobuj jeszcze raz: ");
					sc.next();
				}

			}

			switch (liczbaWejsciowa) {
			case 0:
				System.out.println("---Powrot---");
				return;
			case 1:
				utworzWizyte(sc);
				break;
			case 2:
				zakonczWizyte(sc);
				break;
			case 3:
				zarchiwizujWizyte(sc);
				break;
			case 4:
				znajdzAktywnaWizyte(sc);
				break;
			case 5:
				znajdzArchiwalnaWizyte(sc);
				break;
			case 6:
				znajdzWolneTerminyLekarza(sc);
			default:
				break;
			}
			status = Operacja.PORAZKA;
		} while (true);

	}

	private void znajdzArchiwalnaWizyte(Scanner sc) {
		String idPac = null;
		String idLek = null;
		String idGab = null;
		String terminPoczWiz = null;
		String terminKoncWiz = null;
		System.out.print("Wprowadz UUID pacjenta: ");
		while (idPac == null) {
			idPac = sc.next();
		}
		if (idPac.equals("-")) {
			idPac = null;
		}
		System.out.print("Wprowadz UUID lekarza: ");
		while (idLek == null) {
			idLek = sc.next();
		}
		if (idLek.equals("-")) {
			idLek = null;
		}
		System.out.print("Wprowadz UUID gabinetu: ");
		while (idGab == null) {
			idGab = sc.next();
		}
		if (idGab.equals("-")) {
			idGab = null;
		}
		System.out.print("Wprowadz poczatkowy (przyblizony) termin wizyty: ");
		while (terminPoczWiz == null) {
			terminPoczWiz = sc.next();
		}
		if (terminPoczWiz.equals("-")) {
			terminPoczWiz = null;
		}
		System.out.print("Wprowadz koncowy (przyblizony) termin wizyty: ");
		while (terminKoncWiz == null) {
			terminKoncWiz = sc.next();
		}
		if (terminKoncWiz.equals("-")) {
			terminKoncWiz = null;
		}
		int counter = 1;
		List<Wizyta> wizyty = null;
		try {
			wizyty = przychodnia.znajdzArchiwalnaWizyte(idPac, idLek, terminPoczWiz, terminKoncWiz,
					Przychodnia.FORMAT_DATY_I_CZASU, idGab);
			if (wizyty.isEmpty()) {
				System.out.println("Nie znaleziono wizyty o podanych parametrach");
			} else {
				System.out.println("Znalezione wizyty:");
				for (Wizyta w : wizyty) {
					System.out.println(counter++ + ". " + przychodnia.przetlumaczWizyte(w));
				}
			}
		} catch (DateTimeParseException e) {
			System.out.println(e.getMessage());
		} catch (ZleUuidExcpetion e) {
			System.out.println(e.getMessage());
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}

	}

	private void znajdzAktywnaWizyte(Scanner sc) {
		String idPac = null;
		String idLek = null;
		String idGab = null;
		String terminPoczWiz = null;
		String terminKoncWiz = null;
		System.out.print("Wprowadz UUID pacjenta: ");
		while (idPac == null) {
			idPac = sc.next();
		}
		if (idPac.equals("-")) {
			idPac = null;
		}
		System.out.print("Wprowadz UUID lekarza: ");
		while (idLek == null) {
			idLek = sc.next();
		}
		if (idLek.equals("-")) {
			idLek = null;
		}
		System.out.print("Wprowadz UUID gabinetu: ");
		while (idGab == null) {
			idGab = sc.next();
		}
		if (idGab.equals("-")) {
			idGab = null;
		}
		System.out.print("Wprowadz poczatkowy (przyblizony) termin wizyty: ");
		while (terminPoczWiz == null) {
			terminPoczWiz = sc.next();
		}
		if (terminPoczWiz.equals("-")) {
			terminPoczWiz = null;
		}
		System.out.print("Wprowadz koncowy (przyblizony) termin wizyty: ");
		while (terminKoncWiz == null) {
			terminKoncWiz = sc.next();
		}
		if (terminKoncWiz.equals("-")) {
			terminKoncWiz = null;
		}
		int counter = 1;
		List<Wizyta> wizyty = null;
		try {
			wizyty = przychodnia.znajdzAktywnaWizyte(idPac, idLek, terminPoczWiz, terminKoncWiz,
					Przychodnia.FORMAT_DATY_I_CZASU, idGab);
			if (wizyty.isEmpty()) {
				System.out.println("Nie znaleziono wizyty o podanych parametrach");
			} else {
				System.out.println("Znalezione wizyty:");
				for (Wizyta w : wizyty) {
					System.out.println(counter++ + ". " + przychodnia.przetlumaczWizyte(w));
				}
			}
		} catch (DateTimeParseException e) {
			System.out.println(e.getMessage());
		} catch (ZleUuidExcpetion e) {
			System.out.println(e.getMessage());
		}

	}

	private void zarchiwizujWizyte(Scanner sc) {
		String idWiz = null;
		System.out.print("Wprowadz UUID wizyty: ");
		while (idWiz == null) {
			idWiz = sc.next();
		}
		try {
			przychodnia.zarchiwizujWizyte(idWiz);
			System.out.println("Zarchiwizowano wizyte!");
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		} catch (ZasobNiedostepnyException e) {
			System.out.println(e.getMessage());
		} catch (ZlyStanWizytyException e) {
			System.out.println(e.getMessage());
		}

	}

	private void zakonczWizyte(Scanner sc) {
		String idWiz = null;
		String stanZ = null;
		System.out.print("Wprowadz UUID wizyty: ");
		while (idWiz == null) {
			idWiz = sc.next();
		}
		System.out.print("Wprowadz stan zakonczenia wizyty: ");
		while (stanZ == null) {
			stanZ = sc.next();
		}
		try {
			przychodnia.zakonczWizyte(idWiz, stanZ);
			System.out.println("Zakonczono wizyte!");
		} catch (DateTimeParseException e) {
			System.out.println(e.getMessage());
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		} catch (ZasobNiedostepnyException e) {
			System.out.println(e.getMessage());
		} catch (ZlyStanWizytyException e) {
			System.out.println(e.getMessage());
		} catch (TerminNiedostepnyException e) {
			System.out.println(e.getMessage());
		} catch (TerminWolnyException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	private void utworzWizyte(Scanner sc) {
		String idPac = null;
		String idLek = null;
		String idGab = null;
		String terminWiz = null;
		String czasWizyty = null;
		System.out.print("Wprowadz UUID pacjenta: ");
		while (idPac == null) {
			idPac = sc.next();
		}
		System.out.print("Wprowadz UUID lekarza: ");
		while (idLek == null) {
			idLek = sc.next();
		}
		System.out.print("Wprowadz UUID gabinetu: ");
		while (idGab == null) {
			idGab = sc.next();
		}
		System.out.print("Wprowadz termin wizyty: ");
		while (terminWiz == null) {
			terminWiz = sc.next();
		}
		System.out.print("Wprowadz czas trwania wizyty: ");
		while (czasWizyty == null) {
			czasWizyty = sc.next();
		}
		try {
			przychodnia.utworzWizyte(idPac, idLek, idGab, terminWiz, czasWizyty, Przychodnia.FORMAT_DATY_I_CZASU);
			System.out.println("Utworzono wizyte!");
		} catch (DateTimeParseException e) {
			System.out.println(e.getMessage());
		} catch (NumberFormatException e) {
			System.out.println(e.getMessage());
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		} catch (ZleUuidExcpetion e) {
			System.out.println(e.getMessage());
		} catch (TerminZajetyException e) {
			System.out.println(e.getMessage());
		} catch (TerminNiedostepnyException e) {
			System.out.println(e.getMessage());
		} catch (ZasobNiedostepnyException e) {
			System.out.println(e.getMessage());
		} catch (ZlyStanWizytyException e) {
			System.out.println(e.getMessage());
		}

	}

	private void szukajZasobow(Scanner sc) {
		int liczbaWejsciowa = -1;
		Operacja status = Operacja.PORAZKA;
		do {
			System.out.println();
			System.out.println("Menu operacji:");
			System.out.println("1. Znajdz pacjenta");
			System.out.println("2. Znajdz lekarza");
			System.out.println("3. Znajdz gabinet");
			System.out.println("0. Powrot");
			System.out.println();
			System.out.print("Wprowadz numer operacji: ");
			while (status != Operacja.SUKCES) {
				try {
					liczbaWejsciowa = sc.nextInt();
					if (liczbaWejsciowa != 0 && liczbaWejsciowa != 1 && liczbaWejsciowa != 2 && liczbaWejsciowa != 3) {
						System.out.print("Wprowadzono niepoprawny numer operacji, sprobuj ponownie: ");
						continue;
					}
					status = Operacja.SUKCES;
				} catch (InputMismatchException e) {
					System.out.print("Wprowadzono nieprawidlowe dane, sprobuj jeszcze raz: ");
					sc.next();
				}

			}

			switch (liczbaWejsciowa) {
			case 0:
				System.out.println("---Powrot---");
				return;
			case 1:
				znajdzPacjenta(sc);
				break;
			case 2:
				znajdzLekarza(sc);
				break;
			case 3:
				znajdzGabinet(sc);
				break;
			default:
				break;
			}
			status = Operacja.PORAZKA;
		} while (true);

	}

	private void znajdzGabinet(Scanner sc) {
		String numer = null;
		System.out.print("Wprowadz numer gabinetu: ");
		while (numer == null) {
			numer = sc.next();
		}
		if (numer.equals("-"))
			numer = null;
		System.out.println("\nZnalezione gabinety: ");
		List<Gabinet> gabinety = przychodnia.znajdzGabinet(numer);
		if (gabinety.isEmpty()) {
			System.out.println("---Brak dopasowan---");
		}
		int licznik = 1;
		for (Gabinet gabinet : gabinety) {
			System.out.println(licznik++ + ". " + gabinet);
		}

	}

	private void znajdzLekarza(Scanner sc) {
		String imie = null;
		String nazwisko = null;
		String specjalizacja = null;
		System.out.print("Wprowadz imie lekarza: ");
		while (imie == null) {
			imie = sc.next();
		}
		if (imie.equals("-"))
			imie = null;
		System.out.print("Wprowadz nazwisko lekarza: ");
		while (nazwisko == null) {
			nazwisko = sc.next();
		}
		if (nazwisko.equals("-"))
			nazwisko = null;
		System.out.print("Wprowadz specjalizacje lekarza: ");
		while (specjalizacja == null) {
			specjalizacja = sc.next();
		}
		if (specjalizacja.equals("-"))
			specjalizacja = null;
		System.out.println("\nZnalezieni lekarze: ");
		List<Lekarz> lekarze = przychodnia.znajdzLekarza(imie, nazwisko, specjalizacja);
		if (lekarze.isEmpty()) {
			System.out.println("---Brak dopasowan---");
		}
		int licznik = 1;
		for (Lekarz lekarz : lekarze) {
			System.out.println(licznik++ + ". " + lekarz);
		}

	}

	private void znajdzWolneTerminyLekarza(Scanner sc) {
		String uuid = null;
		String terminPocz = null;
		String terminKonc = null;
		System.out.print("Wprowadz UUID lekarza: ");
		while (uuid == null) {
			uuid = sc.next();
		}
		System.out.print("Wprowadz termin poczatkowy dla wyszukiwania: ");
		while (terminPocz == null) {
			terminPocz = sc.next();
		}
		System.out.print("Wprowadz termin koncowy dla wyszukiwania: ");
		while (terminKonc == null) {
			terminKonc = sc.next();
		}
		List<String> terminy = null;
		int counter = 1;
		try {
			terminy = przychodnia.znajdzWolneTerminyLekarza(uuid, terminPocz, terminKonc,
					Przychodnia.FORMAT_DATY_I_CZASU);
			if (terminy.isEmpty()) {
				System.out.println("Nie znaleziono wolnych terminow w podanym przedziale czasu");
			} else {
				System.out.println("Znalezione wolne terminy:");
				for (String s : terminy) {
					System.out.println(counter++ + ". " + s);
				}
			}
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		} catch (ZleUuidExcpetion e) {
			System.out.println(e.getMessage());
		} catch (ZasobNiedostepnyException e) {
			System.out.println(e.getMessage());
		} catch (DateTimeParseException e) {
			System.out.println(e.getMessage());
		} catch (DateTimeException e) {
			System.out.println(e.getMessage());
		}

	}

	private void znajdzPacjenta(Scanner sc) {
		String imie = null;
		String nazwisko = null;
		String pesel = null;
		System.out.print("Wprowadz imie pacjenta: ");
		while (imie == null) {
			imie = sc.next();
		}
		if (imie.equals("-"))
			imie = null;
		System.out.print("Wprowadz nazwisko pacjenta: ");
		while (nazwisko == null) {
			nazwisko = sc.next();
		}
		if (nazwisko.equals("-"))
			nazwisko = null;
		System.out.print("Wprowadz PESEL pacjenta: ");
		while (pesel == null) {
			pesel = sc.next();
		}
		if (pesel.equals("-"))
			pesel = null;
		System.out.println("\nZnalezieni pacjenci: ");
		List<Pacjent> pacjenci = null;
		try {
			pacjenci = przychodnia.znajdzPacjenta(imie, nazwisko, pesel);
		} catch (NumberFormatException e) {
			System.out.println(e.getMessage());
			return;
		}

		if (pacjenci.isEmpty()) {
			System.out.println("---Brak dopasowan---");
		}
		int licznik = 1;
		for (Pacjent pacjent : pacjenci) {
			System.out.println(licznik++ + ". " + pacjent);
		}
	}

}
