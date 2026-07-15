package facade;

import model.*;

import java.util.List;

import builder.*;
import command.*;
import strategy.*;
import persistence.ArchivioFilm;

public class VideotecaFacade {
    private final Videoteca videoteca;
    private final FilmDirector director;
    private final FilmBuilder builder;
    private final CommandManager commandManager;
    private final ArchivioFilm archivio;

    public VideotecaFacade(Videoteca videoteca) {
        this.videoteca = videoteca;
        this.director = new FilmDirector();
        this.builder = new ConcreteFilmBuilder();
        this.commandManager = new CommandManager();
        this.archivio = new ArchivioFilm();
    }

    public void undo() {
        commandManager.undo();
    }

    public void redo() {
        commandManager.redo();
    }

    public void inserisciFilm(DatiFilm dati) {
        int id = videoteca.prossimoId(); 
        FilmIF nuovoFilm = director.creaFilm(builder, id, dati);
        Command cmd = new InserisciFilmCommand(videoteca, nuovoFilm);
        commandManager.eseguiComando(cmd);
    }

    public void modificaFilm(int id, DatiFilm nuoviDati) {
        Command cmd = new ModificaFilmCommand(videoteca, id, nuoviDati);
        commandManager.eseguiComando(cmd);
    }

    public void rimuoviFilm(int id) {
        Command cmd = new RimuoviFilmCommand(videoteca, id);
        commandManager.eseguiComando(cmd);
    }

    public boolean canUndo() {
        return commandManager.canUndo(); 
    }

    public boolean canRedo() {
        return commandManager.canRedo();
    }

    public List<FilmIF> ottieniCatalogoFiltratoEOrdinato(String query, String tipo, String genere, String stato, String ordine) {
        List<FilmIF> risultato = videoteca.getElenco();

        // 1. Cerca per testo (Titolo o Regista)
        if (query != null && !query.trim().isEmpty()) {
            FilmQueryStrategy strategiaRicerca = tipo.equalsIgnoreCase("Titolo") 
                    ? new RicercaTitoloStrategy(query) 
                    : new RicercaRegistaStrategy(query);
            risultato = strategiaRicerca.eseguiQuery(risultato);
        }

        // 2. Filtra per Genere
        if (genere != null && !genere.trim().isEmpty()) {
            risultato = new FiltroGenereStrategy(genere).eseguiQuery(risultato);
        }

        // 3. Filtra per Stato Visione
        if (stato != null && !stato.equalsIgnoreCase("Tutti")) {
            StatoVisione sv = StatoVisione.valueOf(stato);
            risultato = new FiltroStatoStrategy(sv).eseguiQuery(risultato);
        }

        // 4. Ordina i risultati
        if (ordine != null && !ordine.equalsIgnoreCase("Nessuno")) {
            FilmQueryStrategy strategiaOrdine = null;
            if (ordine.equalsIgnoreCase("Titolo")) {
                strategiaOrdine = new OrdinamentoTitoloStrategy();
            } else if (ordine.equalsIgnoreCase("Anno")) {
                strategiaOrdine = new OrdinamentoAnnoStrategy();
            } else if (ordine.equalsIgnoreCase("Valutazione")) {
                strategiaOrdine = new OrdinamentoValutazioneStrategy();
            }
            
            if (strategiaOrdine != null) {
                risultato = strategiaOrdine.eseguiQuery(risultato);
            }
        }

        return risultato;
    }

    public List<FilmIF> ottieniCatalogoCompleto(){
        return videoteca.getElenco();
    }

    public void salvaDati() {
        System.out.println("Richiesta di salvataggio del catalogo attuale...");
        archivio.salva(videoteca.getElenco());
    }

    public void caricaDati() {
        List<FilmIF> filmCaricati = archivio.carica();
        commandManager.svuotaCronologia();
        videoteca.setElenco(filmCaricati);
    }
}