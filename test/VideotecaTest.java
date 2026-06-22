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

    @BeforeEach
    public void setUp() {
        Videoteca videoteca = new Videoteca();
        this.facade = new VideotecaFacade(videoteca);
    }

    @Test
    public void testInserimentoFilmEIdCrescente() {
        DatiFilm film1 = new DatiFilm("Inception", "Nolan", 2010, "Sci-Fi", 5, StatoVisione.VISTO);
        DatiFilm film2 = new DatiFilm("Interstellar", "Nolan", 2014, "Sci-Fi", 4, StatoVisione.DA_VEDERE);

        facade.inserisciFilm(film1);
        facade.inserisciFilm(film2);

        List<FilmIF> elenco = facade.ottieniCatalogoCompleto();

        assertEquals(2, elenco.size(), "La videoteca dovrebbe contenere 2 film");
        assertEquals(1, elenco.get(0).getId(), "Il primo film deve avere ID = 1");
        assertEquals(2, elenco.get(1).getId(), "Il secondo film deve avere ID = 2");
        assertEquals("Inception", elenco.get(0).getTitolo());
    }

    @Test
    public void testModificaFilm() {
        DatiFilm datiIniziali = new DatiFilm("Matrix", "Wachowski", 1999, "Azione", 3, StatoVisione.DA_VEDERE);
        facade.inserisciFilm(datiIniziali);

        DatiFilm nuoviDati = new DatiFilm("Matrix", "Wachowski", 1999, "Azione", 5, StatoVisione.VISTO);
        
        facade.modificaFilm(1, nuoviDati);

        List<FilmIF> elenco = facade.ottieniCatalogoCompleto();
        
        assertEquals(1, elenco.size(), "La dimensione del catalogo non deve cambiare dopo una modifica");
        assertEquals(5, elenco.get(0).getValutazione(), "Il voto avrebbe dovuto aggiornarsi a 5");
        assertEquals(StatoVisione.VISTO, elenco.get(0).getStatoVisione(), "Lo stato avrebbe dovuto aggiornarsi a VISTO");
    }

    @Test
    public void testRimuoviFilm() {
        DatiFilm film = new DatiFilm("Avatar", "Cameron", 2009, "Sci-Fi", 4, StatoVisione.VISTO);
        facade.inserisciFilm(film);
        
        assertEquals(1, facade.ottieniCatalogoCompleto().size(), "Il film deve essere inserito correttamente");

        // Rimozione del film appena inserito (ID = 1)
        facade.rimuoviFilm(1);

        assertTrue(facade.ottieniCatalogoCompleto().isEmpty(), "L'elenco dovrebbe essere vuoto dopo la rimozione");
    }

    @Test
    public void testFiltroStrategiaStatoVisione() {
        facade.inserisciFilm(new DatiFilm("Film A", "Regista 1", 2020, "Drammatico", 4, StatoVisione.VISTO));
        facade.inserisciFilm(new DatiFilm("Film B", "Regista 2", 2021, "Azione", 5, StatoVisione.VISTO));
        facade.inserisciFilm(new DatiFilm("Film C", "Regista 3", 2022, "Commedia", 2, StatoVisione.DA_VEDERE));

        // Filtriamo per lo stato VISTO passandolo al metodo della Facade
        List<FilmIF> filtrati = facade.ottieniCatalogoFiltratoEOrdinato("", "", "", "VISTO", "Nessuno");

        assertEquals(2, filtrati.size(), "Dovrebbero esserci solo 2 film nello stato VISTO");
        assertEquals("Film A", filtrati.get(0).getTitolo());
        assertEquals("Film B", filtrati.get(1).getTitolo());
    }

    @Test
    public void testOttieniCatalogoCompletoProtezioneNull() {
        facade.inserisciFilm(new DatiFilm("Pulp Fiction", "Tarantino", 1994, "Thriller", 5, StatoVisione.VISTO));

        assertDoesNotThrow(() -> {
            List<FilmIF> completo = facade.ottieniCatalogoCompleto();
            assertEquals(1, completo.size(), "Il catalogo completo deve restituire tutti i film presenti");
            assertEquals("Pulp Fiction", completo.get(0).getTitolo());
        }, "Il metodo ottieniCatalogoCompleto ha lanciato un'eccezione (gestione della strategia null fallita)");
    }
}