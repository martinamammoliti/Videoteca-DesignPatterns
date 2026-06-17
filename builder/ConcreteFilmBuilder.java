package builder;

import model.*;

public class ConcreteFilmBuilder implements FilmBuilder{
    private int id;
    private String titolo;
    private String regista;
    private int anno;
    private String genere;
    private int valutazione;
    private StatoVisione stato;

    @Override
    public void reset(){
        this.id=0;
        this.titolo=null;
        this.regista=null;
        this.anno=0;
        this.genere=null;
        this.valutazione=0;
        this.stato=null;
    }

    @Override 
    public void impostaId(int id){
        this.id=id;
    }

    @Override 
    public void impostaTitolo(String titolo){
        this.titolo=titolo;
    }

    @Override 
    public void impostaRegista(String regista){
        this.regista=regista;
    }

    @Override 
    public void impostaAnno(int anno){
        this.anno=anno;
    }

    @Override 
    public void impostaGenere(String genere){
        this.genere=genere;
    }

    @Override 
    public void impostaValutazione(int valutazione){
        this.valutazione=valutazione;
    }

    @Override 
    public void impostaStato(StatoVisione stato){
        this.stato=stato;
    }

    @Override
    public FilmIF ottieniProdotto(){
        Film film=new Film(id, titolo, regista, anno, genere, valutazione, stato);
        this.reset();
        return film;
    }
    
}
