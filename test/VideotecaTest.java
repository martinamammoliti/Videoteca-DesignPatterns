package test;

import facade.VideotecaFacade;
import model.DatiFilm;
import model.FilmIF;
import model.StatoVisione;
import model.Videoteca;
import observer.Observer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Suite di Test di Robustezza - Videoteca Completa")
public class VideotecaTest {
    private Videoteca videoteca;
    private VideotecaFacade facade;

    @BeforeEach
    public void setUp() {
        // Inizializzazione pulita del modello e della Facade prima di ogni test
        this.videoteca = new Videoteca();
        this.facade = new VideotecaFacade(videoteca);
    }

    @Test
    @DisplayName("1. Inserimento e consistenza ID sequenziali")
    public void testInserimentoFilmEIdCrescente() {
        DatiFilm film1 = new DatiFilm("Inception", "Nolan", 2010, "Sci-Fi", 5, StatoVisione.VISTO);
        DatiFilm film2 = new DatiFilm("Interstellar", "Nolan", 2014, "Sci-Fi", 4, StatoVisione.DA_VEDERE);

        facade.inserisciFilm(film1);
        facade.inserisciFilm(film2);

        List<FilmIF> elenco = facade.ottieniCatalogoCompleto();

        assertAll("Verifica integrità inserimenti",
            () -> assertEquals(2, elenco.size(), "Il catalogo deve contenere esattamente 2 film"),
            () -> assertEquals(1, elenco.get(0).getId(), "Il primo film deve avere ID = 1"),
            () -> assertEquals(2, elenco.get(1).getId(), "Il secondo film deve avere ID = 2")
        );
    }

    @Test
    @DisplayName("2. Robustezza: Gestione ID inesistenti (Fail-Safe)")
    public void testOperazioniSuFilmInesistenteNonDevonoCrashare() {
        DatiFilm film = new DatiFilm("Matrix", "Wachowski", 1999, "Azione", 5, StatoVisione.VISTO);
        facade.inserisciFilm(film); // Genera ID = 1

        // Verifichiamo che i metodi non crashino
        assertDoesNotThrow(() -> {
            DatiFilm nuoviDati = new DatiFilm("Ghost", "Test", 1990, "Drama", 3, StatoVisione.VISTO);
            facade.modificaFilm(999, nuoviDati); // ID 999 non esiste
        }, "La modifica di un ID inesistente non deve lanciare eccezioni");

        assertDoesNotThrow(() -> {
            facade.rimuoviFilm(999); // ID 999 non esiste
        }, "La rimozione di un ID inesistente non deve lanciare eccezioni");

        // Lo stato del catalogo deve essere rimasto intatto con solo il primo film
        assertEquals(1, facade.ottieniCatalogoCompleto().size(), "Il catalogo non deve variare per ID errati");
    }

    @Test
    @DisplayName("3. Qualità del Software: Verifica dell'Incapsulamento (Copia Difensiva)")
    public void testProtezioneStatoInternoDefensiveCopy() {
        DatiFilm film = new DatiFilm("Avatar", "Cameron", 2009, "Sci-Fi", 4, StatoVisione.VISTO);
        facade.inserisciFilm(film);

        List<FilmIF> elencoModificabileEsternamente = facade.ottieniCatalogoCompleto();
        
        // Tentativo di manipolazione della lista restituita
        assertDoesNotThrow(() -> elencoModificabileEsternamente.clear(), 
            "La lista restituita deve poter essere svuotata senza lanciare eccezioni");

        List<FilmIF> elencoReale = facade.ottieniCatalogoCompleto();
        assertEquals(1, elencoReale.size(), 
            "Lo stato interno della videoteca è stato corrotto! Manca la copia difensiva nel modello.");
    }

    @Test
    @DisplayName("4. Logica di Business: Algoritmo di calcolo prossimo ID")
    public void testAlgoritmoProssimoIdDopoRimozioni() {
        facade.inserisciFilm(new DatiFilm("Film 1", "Regista 1", 2000, "Azione", 3, StatoVisione.VISTO)); // ID 1
        facade.inserisciFilm(new DatiFilm("Film 2", "Regista 2", 2005, "Sci-Fi", 4, StatoVisione.VISTO));  // ID 2
        facade.inserisciFilm(new DatiFilm("Film 3", "Regista 3", 2010, "Commedia", 5, StatoVisione.VISTO)); // ID 3

        // Rimuovo l'ID 2. Rimangono ID 1 e 3. Il prossimo ID calcolato deve essere comunque 4 (max + 1)
        facade.rimuoviFilm(2);
        
        facade.inserisciFilm(new DatiFilm("Film 4", "Regista 4", 2020, "Thriller", 4, StatoVisione.DA_VEDERE));
        List<FilmIF> elenco = facade.ottieniCatalogoCompleto();
        
        assertEquals(4, elenco.get(2).getId(), "L'algoritmo prossimoId deve basarsi sul valore massimo degli ID presenti");
    }

    @Test
    @DisplayName("5. Integrazione Pattern: Funzionamento del ciclo di Undo e Redo")
    public void testFlussoEsecuzioneEAnnullamentoComandi() {
        DatiFilm film = new DatiFilm("Memento", "Nolan", 2000, "Thriller", 5, StatoVisione.VISTO);
        
        facade.inserisciFilm(film);
        assertEquals(1, facade.ottieniCatalogoCompleto().size());
        assertTrue(facade.canUndo());

        // Test dell'Undo
        facade.undo();
        assertTrue(facade.ottieniCatalogoCompleto().isEmpty(), "L'undo deve ripristinare il catalogo vuoto");

        // Test del Redo
        facade.redo();
        assertEquals(1, facade.ottieniCatalogoCompleto().size(), "Il redo deve reinserire il film precedentemente rimosso");
    }

}