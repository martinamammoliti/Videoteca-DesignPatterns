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
    private int prossimoId=1;

    public VideotecaFacade(Videoteca videoteca){
        this.videoteca=videoteca;
        this.director=new FilmDirector();
        this.builder=new ConcreteFilmBuilder();
        this.commandManager=new CommandManager();
        this.queryContext=new FilmQueryContext();
        this.archivio=new ArchivioFilm();
    }

    public void inserisciFilm(DatiFilm dati){
        int id=prossimoId++;
        FilmIF nuovoFilm=director.creaFilm(builder, id, dati);
        Command cmd=new InserisciFilmCommand(videoteca, nuovoFilm);
        commandManager.eseguiComando(cmd);
    }

    public void modificaFilm(int id, DatiFilm nuoviDati){
        FilmIF filmModificato=director.creaFilm(builder, id, nuoviDati);
        Command cmd=new ModificaFilmCommand(videoteca, id, filmModificato);
        commandManager.eseguiComando(cmd);
    }

    public void rimuoviFilm(int id){
        Command cmd=new RimuoviFilmCommand(videoteca, id);
        commandManager.eseguiComando(cmd);
    }

    public List<FilmIF> ottieniCatalogoFiltratoEOrdinato(
        String testo, String tipo, 
        String genereFiltro, String statoFiltro, 
        String criterioOrdinamento) {
        
        List<FilmIF> risultato = videoteca.getElenco();

        // 1. Gestione Ricerca Testuale tramite Context
        if (testo != null && !testo.isEmpty()) {
            if ("Titolo".equalsIgnoreCase(tipo)) {
                queryContext.setStrategy(new RicercaTitoloStrategy(testo));
            } else if ("Regista".equalsIgnoreCase(tipo)) {
                queryContext.setStrategy(new RicercaRegistaStrategy(testo));
            }
            risultato = queryContext.eseguiQuery(risultato);
        }

        // 2. Gestione Filtro Genere tramite Context
        if (genereFiltro != null && !genereFiltro.isEmpty() && !"Tutti".equalsIgnoreCase(genereFiltro)) {
            queryContext.setStrategy(new FiltroGenereStrategy(genereFiltro));
            risultato = queryContext.eseguiQuery(risultato);
        }

        // 3. Gestione Filtro Stato tramite Context
        if (statoFiltro != null && !"Tutti".equalsIgnoreCase(statoFiltro)) {
            StatoVisione stato = StatoVisione.valueOf(statoFiltro);
            queryContext.setStrategy(new FiltroStatoStrategy(stato));
            risultato = queryContext.eseguiQuery(risultato);
        }

        // 4. Gestione Ordinamento tramite Context
        if (criterioOrdinamento != null && !"Nessuno".equalsIgnoreCase(criterioOrdinamento)) {
            queryContext.setStrategy(new OrdinamentoStrategy(criterioOrdinamento));
            risultato = queryContext.eseguiQuery(risultato);
        }

        return risultato;
    }

    public List<FilmIF> ottieniCatalogoCompleto(){
        queryContext.setStrategy(null);
        return queryContext.eseguiQuery(videoteca.getElenco());
    }

    public void salvaDati() {
        System.out.println("[Facade] Richiesta di salvataggio del catalogo attuale...");
        archivio.salva(videoteca.getElenco());
    }

    public void caricaDati() {
        System.out.println("[Facade] Richiesta di caricamento dati dall'archivio...");
        List<FilmIF> datiDaDisco = archivio.carica();
        
        for (FilmIF f : datiDaDisco) {
            if (f.getId() >= prossimoId) {
                prossimoId = f.getId() + 1;
            }
        }
        
        videoteca.setElenco(datiDaDisco);
    }
}
