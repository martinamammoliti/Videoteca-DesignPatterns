package command;
import model.*;

public class ModificaFilmCommand implements Command {
    private final Videoteca videoteca;
    private final int id;
    private final DatiFilm nuoviDati;

    public ModificaFilmCommand(Videoteca videoteca, int id, DatiFilm dati){
        this.videoteca=videoteca;
        this.id=id;
        this.nuoviDati=dati;
    }

    @Override
    public void execute(){
        FilmIF film = videoteca.trovaFilmPerId(id);
        if (film != null) {
            film.aggiornaDati(nuoviDati);
        }
    }
}
