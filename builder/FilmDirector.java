package builder;
public class FilmDirector {
    public FilmIF creaFilm (FilmBuilder builder, int id, DatiFilm dati){
        builder.reset();
        builder.impostaId(id);
        builder.impostaTitolo(dati.titolo);
        builder.impostaRegista(dati.regista);
        builder.impostaAnno(dati.annoUscita);
        builder.impostaGenere(dati.genere);
        builder.impostaValutazione(dati.valutazione);
        builder.impostaStato(dati.statoVisione);
        return builder.ottieniProdotto();
    }

}
