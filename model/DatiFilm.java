package model;
public final class DatiFilm {
    private final String titolo;
    private final String regista;
    private final int annoUscita;
    private final String genere;
    private final int valutazione;
    private final StatoVisione statoVisione;

    public DatiFilm(String titolo, String regista, int annoUscita, String genere, int valutazione, StatoVisione statoVisione){
        this.titolo=titolo;
        this.regista=regista;
        this.annoUscita=annoUscita;
        this.genere=genere;
        this.valutazione=valutazione;
        this.statoVisione=statoVisione;
    }

    public String getTitolo() { return titolo; }
    public String getRegista() { return regista; }
    public int getAnnoUscita() { return annoUscita; }
    public String getGenere() { return genere; }
    public int getValutazione() { return valutazione; }
    public StatoVisione getStatoVisione() { return statoVisione; }
}
