package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals; 
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import facade.*;
import model.*;

public class VideotecaTest {
    private VideotecaFacade facade;

    /**
     * Questo metodo viene eseguito PRIMA di ogni singolo test.
     * Garantisce l'isolamento della memoria (RAM pulita per ogni test).
     */
    @BeforeEach
    public void setUp() {
        Videoteca videoteca = new Videoteca();
        this.facade = new VideotecaFacade(videoteca);
        if (videoteca.getElenco() != null) {
        videoteca.getElenco().clear(); 
        }
    }

    @Test
    public void testInserimentoFilmEIdCrescente() {
        DatiFilm film1 = new DatiFilm("Inception", "Nolan", 2010, "Sci-Fi", 5, StatoVisione.VISTO);
        DatiFilm film2 = new DatiFilm("Interstellar", "Nolan", 2014, "Sci-Fi", 4, StatoVisione.DA_VEDERE);

        facade.inserisciFilm(film1);
        facade.inserisciFilm(film2);

        List<FilmIF> elenco = facade.ottieniCatalogoCompleto();

        // Verifica che siano stati inseriti esattamente due film
        assertEquals(2, elenco.size(), "La videoteca dovrebbe contenere 2 film");

        // Verifica che gli ID siano stati assegnati in modo progressivo (1 e 2)
        assertEquals(1, elenco.get(0).getId(), "Il primo film deve avere ID = 1");
        assertEquals(2, elenco.get(1).getId(), "Il secondo film deve avere ID = 2");
        
        // Verifica la corretta memorizzazione dei dati del primo film
        assertEquals("Inception", elenco.get(0).getTitolo());
    }

    @Test
    public void testModificaFilm() {
        // 1. Inseriamo un film iniziale
        DatiFilm datiIniziali = new DatiFilm("Matrix", "Wachowski", 1999, "Azione", 3, StatoVisione.DA_VEDERE);
        facade.inserisciFilm(datiIniziali);

        // 2. Prepariamo i nuovi dati modificati (cambiamo voto e stato)
        DatiFilm nuoviDati = new DatiFilm("Matrix", "Wachowski", 1999, "Azione", 5, StatoVisione.VISTO);
        
        // Eseguiamo la modifica sull'ID 1 tramite Facade
        facade.modificaFilm(1, nuoviDati);

        List<FilmIF> elenco = facade.ottieniCatalogoCompleto();
        
        assertEquals(1, elenco.size(), "La dimensione del catalogo non deve cambiare dopo una modifica");
        assertEquals(5, elenco.get(0).getValutazione(), "Il voto avrebbe dovuto aggiornarsi a 5");
        assertEquals(StatoVisione.VISTO, elenco.get(0).getStatoVisione(), "Lo stato avrebbe dovuto aggiornarsi a VISTO");
    }

    @Test
    public void testRimuoviFilm() {
        System.out.println("=== INVESTIGAZIONE FILM FANTASMA ===");
        for (FilmIF f : facade.ottieniCatalogoCompleto()) {
            System.out.println(" -> Trovato: " + f.getTitolo() + " (ID: " + f.getId() + ")");
        }
        System.out.println("====================================");
        DatiFilm film = new DatiFilm("Avatar", "Cameron", 2009, "Sci-Fi", 4, StatoVisione.VISTO);
        facade.inserisciFilm(film);
        
        assertEquals(1, facade.ottieniCatalogoCompleto().size());

        // Rimozione del film appena inserito (ID = 1)
        facade.rimuoviFilm(1);

        assertTrue(facade.ottieniCatalogoCompleto().isEmpty(), "L'elenco dovrebbe essere vuoto dopo la rimozione");
    }

    @Test
    public void testFiltroStrategiaStatoVisione() {
        // Inseriamo tre film con stati differenti
        facade.inserisciFilm(new DatiFilm("Film A", "Regista 1", 2020, "Drammatico", 4, StatoVisione.VISTO));
        facade.inserisciFilm(new DatiFilm("Film B", "Regista 2", 2021, "Azione", 5, StatoVisione.VISTO));
        facade.inserisciFilm(new DatiFilm("Film C", "Regista 3", 2022, "Commedia", 2, StatoVisione.DA_VEDERE));

        // Richiediamo tramite Facade solo i film con stato "VISTO" 
        List<FilmIF> filtrati = facade.ottieniCatalogoFiltratoEOrdinato("", "", "", "VISTO", "");

        // Ci aspettiamo 2 film (Film A e Film B)
        assertEquals(2, filtrati.size(), "Dovrebbero esserci solo 2 film nello stato VISTO");
        assertEquals("Film A", filtrati.get(0).getTitolo());
        assertEquals("Film B", filtrati.get(1).getTitolo());
    }

    @Test
    public void testOttieniCatalogoCompletoProtezioneNull() {
        facade.inserisciFilm(new DatiFilm("Pulp Fiction", "Tarantino", 1994, "Thriller", 5, StatoVisione.VISTO));

        // Questo test verifica che il fix nel Context funzioni e non lanci una NullPointerException
        assertDoesNotThrow(() -> {
            List<FilmIF> completo = facade.ottieniCatalogoCompleto();
            assertEquals(1, completo.size(), "Il catalogo completo deve restituire tutti i film presenti");
            assertEquals("Pulp Fiction", completo.get(0).getTitolo());
        }, "Il metodo ottieniCatalogoCompleto ha lanciato un'eccezione (controlla la gestione della strategia null nel Context)");
    }
}
