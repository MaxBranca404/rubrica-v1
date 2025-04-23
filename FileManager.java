import java.io.*;
import java.util.*;

public class FileManager {
    private static final String FILENAME = "informazioni.txt";

    public static Vector<Persona> caricaPersone() {
        Vector<Persona> persone = new Vector<>();
        File file = new File(FILENAME);

        if (!file.exists()) {
            return persone;
        }

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(";");

                if (parts.length == 5) {
                    String nome = parts[0];
                    String cognome = parts[1];
                    String indirizzo = parts[2];
                    String telefono = parts[3];
                    int eta = Integer.parseInt(parts[4]);

                    persone.add(new Persona(nome, cognome, indirizzo, telefono, eta));
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File non trovato: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Errore durante la lettura del file: " + e.getMessage());
        }

        return persone;
    }

    public static void salvaPersone(Vector<Persona> persone) {
        try (PrintStream ps = new PrintStream(new FileOutputStream(FILENAME))) {
            for (Persona persona : persone) {
                ps.println(persona.toString());
            }
        } catch (FileNotFoundException e) {
            System.err.println("Impossibile scrivere sul file: " + e.getMessage());
        }
    }
}
