package model;

public class Film implements FilmIF {
    private final int id;
    private String titolo;
    private String regista;
    private int annoUscita;
    private String genere;
    private int valutazione;
    private StatoVisione statoVisione;

    public Film(int id, String titolo, String regista, int annoUscita, String genere, int valutazione, StatoVisione statoVisione) {
        this.id = id;
        this.titolo = titolo;
        this.regista = regista;
        this.annoUscita = annoUscita;
        this.genere = genere;
        this.valutazione = valutazione;
        this.statoVisione = statoVisione;
    }

    @Override
    public int getId() { return id; }
    @Override
    public String getTitolo() { return titolo; }
    @Override
    public String getRegista() { return regista; }
    @Override
    public int getAnnoUscita() { return annoUscita; }
    @Override
    public String getGenere() { return genere; }
    @Override
    public int getValutazione() { return valutazione; }
    @Override
    public StatoVisione getStatoVisione() { return statoVisione; }

    @Override
    public void aggiornaDati(DatiFilm nuoviDati) {
        this.titolo = nuoviDati.getTitolo();
        this.regista = nuoviDati.getRegista();
        this.annoUscita = nuoviDati.getAnnoUscita();
        this.genere = nuoviDati.getGenere();
        this.valutazione = nuoviDati.getValutazione();
        this.statoVisione = nuoviDati.getStatoVisione();
    }
}