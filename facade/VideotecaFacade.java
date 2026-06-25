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
    private final FilmQueryContext queryContext;
    private final ArchivioFilm archivio;

    public VideotecaFacade(Videoteca videoteca) {
        this.videoteca = videoteca;
        this.director = new FilmDirector();
        this.builder = new ConcreteFilmBuilder();
        this.commandManager = new CommandManager();
        this.queryContext = new FilmQueryContext();
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
        if (!query.isEmpty()) {
            if (tipo.equalsIgnoreCase("Titolo")) {
                queryContext.setStrategy(new RicercaTitoloStrategy(query));
            } else if (tipo.equalsIgnoreCase("Regista")) {
                queryContext.setStrategy(new RicercaRegistaStrategy(query));
            }
            risultato = queryContext.eseguiQuery(risultato);
        }

        // 2. Filtra per Genere
        if (!genere.isEmpty()) {
            queryContext.setStrategy(new FiltroGenereStrategy(genere));
            risultato = queryContext.eseguiQuery(risultato);
        }

        // 3. Filtra per Stato Visione
        if (!stato.equalsIgnoreCase("Tutti")) {
            StatoVisione sv = StatoVisione.valueOf(stato);
            queryContext.setStrategy(new FiltroStatoStrategy(sv));
            risultato = queryContext.eseguiQuery(risultato);
        }

        // 4. Ordina i risultati
        if (!ordine.equalsIgnoreCase("Nessuno")) {
            if (ordine.equalsIgnoreCase("Titolo")) {
                queryContext.setStrategy(new OrdinamentoTitoloStrategy());
            } else if (ordine.equalsIgnoreCase("Anno")) {
                queryContext.setStrategy(new OrdinamentoAnnoStrategy());
            } else if (ordine.equalsIgnoreCase("Valutazione")) {
                queryContext.setStrategy(new OrdinamentoValutazioneStrategy());
            }
            risultato = queryContext.eseguiQuery(risultato);
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
        // Carica la lista da file e ripristina lo stato interno del modello videoteca
        List<FilmIF> filmCaricati = archivio.carica();
        videoteca.setElenco(filmCaricati);
        commandManager.svuotaCronologia();
    }

}
