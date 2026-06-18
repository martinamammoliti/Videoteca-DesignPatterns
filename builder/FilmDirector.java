package builder;

import model.*;
public class FilmDirector {
    public FilmIF creaFilm (FilmBuilder builder, int id, DatiFilm dati){
        builder.reset();
        builder.impostaId(id);
        builder.impostaTitolo(dati.getTitolo());
        builder.impostaRegista(dati.getRegista());
        builder.impostaAnno(dati.getAnnoUscita());
        builder.impostaGenere(dati.getGenere());
        builder.impostaValutazione(dati.getValutazione());
        builder.impostaStato(dati.getStatoVisione());
        return builder.ottieniProdotto();
    }

}
