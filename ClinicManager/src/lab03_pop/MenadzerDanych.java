package lab03_pop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class MenadzerDanych {
	
	private static final String sciezkaDanych = "przychodnia.ser";

	public static void zapiszStanProgramu(Przychodnia p) {
		try {
			FileOutputStream fos = new FileOutputStream(sciezkaDanych);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(p);
			oos.close();
			fos.close();
			System.out.println("Stan programu zapisano w " + sciezkaDanych);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static Przychodnia odtworzStanProgramuZPliku() {
		Przychodnia p = null;
		File f = new File(sciezkaDanych);
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			p = new Przychodnia("Moja przychodnia");
			System.out.println("\nNie znaleziono pliku z danymi programu, progam zaladuje sie na czysto...");
			return p;
		}
		try {
			FileInputStream fis = new FileInputStream(sciezkaDanych);
			ObjectInputStream ois = new ObjectInputStream(fis);
			p = (Przychodnia) ois.readObject();
			ois.close();
			fis.close();
			System.out.println("\nDane programu zaladowano pomyslnie...");
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return p;
	}
}
