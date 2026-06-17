package command;
import model.*;

public class ModificaFilmCommand implements Command {
    private final Videoteca videoteca;
    private final int id;
    private final FilmIF nuovoFilm;

    public ModificaFilmCommand(Videoteca videoteca, int id, FilmIF nuovoFilm){
        this.videoteca=videoteca;
        this.id=id;
        this.nuovoFilm=nuovoFilm;
    }

    @Override
    public void execute(){
        videoteca.modifica(id, nuovoFilm);
    }
}
