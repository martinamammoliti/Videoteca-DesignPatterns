package persistence;

import model.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import builder.*;

public class ArchivioFilm {
    private static final String FILE_NAME = "catalogo.csv";
    private final FilmBuilder builder;
    private final FilmDirector director;

    public ArchivioFilm() {
        this.builder = new ConcreteFilmBuilder();
        this.director = new FilmDirector();
    }

    public void salva(List<FilmIF> listaFilm) {
        System.out.println("Salvataggio dati su " + FILE_NAME + "...");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (FilmIF f : listaFilm) {
                // Costruiamo la riga CSV usando il punto e virgola come separatore
                String riga = String.format("%d;%s;%s;%d;%s;%d;%s",
                        f.getId(),
                        f.getTitolo(),
                        f.getRegista(),
                        f.getAnnoUscita(),
                        f.getGenere(),
                        f.getValutazione(),
                        f.getStatoVisione().name()
                );
                writer.write(riga);
                writer.newLine();
            }
            System.out.println("Salvataggio completato con successo.");
        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio: " + e.getMessage());
        }
    }

   
    public List<FilmIF> carica() {
        System.out.println("Lettura dati da " + FILE_NAME + "...");
        List<FilmIF> listaRipristinata = new ArrayList<>();
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            System.out.println("Nessun file di backup trovato. Inizializzazione vuota.");
            return listaRipristinata;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;

                String[] token = linea.split(";");
                
                int id = Integer.parseInt(token[0]);
                String titolo = token[1];
                String regista = token[2];
                int anno = Integer.parseInt(token[3]);
                String genere = token[4];
                int valutazione = Integer.parseInt(token[5]);
                StatoVisione stato = StatoVisione.valueOf(token[6]);

                DatiFilm dati = new DatiFilm(titolo, regista, anno, genere, valutazione, stato);
                FilmIF film = director.creaFilm(builder, id, dati);
                
                listaRipristinata.add(film);
            }
            System.out.println("Caricamento completato. Ripristinati " + listaRipristinata.size() + " film.");
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Errore durante il caricamento o parsing: " + e.getMessage());
        }

        return listaRipristinata;
    }
}
