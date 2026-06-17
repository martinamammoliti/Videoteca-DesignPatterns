package persistence;

import model.*;

import java.util.ArrayList;
import java.util.List;

import builder.*;

public class ArchivioFilm {
    private final FilmBuilder builder;
    private final FilmDirector director;

    public ArchivioFilm(){
        this.builder=new ConcreteFilmBuilder();
        this.director=new FilmDirector();
    }

    public List<FilmIF> carica(){
        System.out.println("Apertura file 'catalogo.dat' in corso...");
        System.out.println("Lettura e deserializzazione dei dati...");

        List<FilmIF> listaRipristinata = new ArrayList<>();

        DatiFilm d1 = new DatiFilm("Pulp Fiction", "Quentin Tarantino", 1994, "Crime", 10, StatoVisione.VISTO);
        DatiFilm d2 = new DatiFilm("Matrix", "Lana e Lilly Wachowski", 1999, "Sci-Fi", 9, StatoVisione.VISTO);

        listaRipristinata.add(director.creaFilm(builder, 99, d1)); // ID letto da "disco"
        listaRipristinata.add(director.creaFilm(builder, 100, d2));

        System.out.println("Caricamento completato. Trovati 2 film.");
        return listaRipristinata;
    }
}
