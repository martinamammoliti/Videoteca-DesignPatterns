public interface FilmBuilder {
    void reset();
    void impostaId(int id);
    void impostaTitolo(String titolo);
    void impostaRegista(String regista);    
    void impostaAnno(int anno);
    void impostaGenere(String genere);
    void impostaValutazione(int valutazione);
    void impostaStato(StatoVisione stato);
    FilmIF ottieniProdotto();
}
