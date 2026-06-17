package view;

import java.util.List;
import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import facade.VideotecaFacade;
import model.DatiFilm;
import model.FilmIF;
import model.StatoVisione;
import model.Videoteca;

public class VideotecaGUI extends JFrame implements Observer{

    private final Videoteca videoteca;
    private final VideotecaFacade facade;

    private JTable tabellaFilm;
    private DefaultTableModel tableModel;
    private JTextField txtTitolo, txtRegista, txtAnno, txtGenere, txtVoto;
    private JTextField txtFiltroTitolo;
    private JComboBox<StatoVisione> comboStato;

    private String filtroAttivo = "";

    public VideotecaGUI(Videoteca videoteca, VideotecaFacade facade) {
        this.videoteca = videoteca;
        this.facade = facade;

        this.videoteca.attach(this);

        setTitle("🎬 Gestore Videoteca - Design Patterns");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra lo schermo
        setLayout(new BorderLayout(10, 10));

        JPanel panelRicerca = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelRicerca.setBorder(BorderFactory.createTitledBorder("Filtra Catalogo (Strategy Pattern)"));
        
        txtFiltroTitolo = new JTextField(20);
        JButton btnCerca = new JButton("Cerca per Titolo");
        JButton btnResettaFiltro = new JButton("Mostra Tutti");

        panelRicerca.add(new JLabel("Digita Titolo:"));
        panelRicerca.add(txtFiltroTitolo);
        panelRicerca.add(btnCerca);
        panelRicerca.add(btnResettaFiltro);
        add(panelRicerca, BorderLayout.NORTH);

        String[] colonne = {"ID", "Titolo", "Regista", "Anno", "Genere", "Voto", "Stato"};
        tableModel = new DefaultTableModel(colonne, 0);
        tabellaFilm = new JTable(tableModel);
        add(new JScrollPane(tabellaFilm), BorderLayout.CENTER);

        JPanel panelInput = new JPanel(new GridLayout(2, 6, 5, 5));
        panelInput.setBorder(BorderFactory.createTitledBorder("Inserisci Nuovo Film"));

        txtTitolo = new JTextField();
        txtRegista = new JTextField();
        txtAnno = new JTextField();
        txtGenere = new JTextField();
        txtVoto = new JTextField();
        comboStato = new JComboBox<>(StatoVisione.values());

        panelInput.add(new JLabel("Titolo:"));
        panelInput.add(new JLabel("Regista:"));
        panelInput.add(new JLabel("Anno:"));
        panelInput.add(new JLabel("Genere:"));
        panelInput.add(new JLabel("Voto (1-10):"));
        panelInput.add(new JLabel("Stato:"));

        panelInput.add(txtTitolo);
        panelInput.add(txtRegista);
        panelInput.add(txtAnno);
        panelInput.add(txtGenere);
        panelInput.add(txtVoto);
        panelInput.add(comboStato);

        JPanel panelBottoni = new JPanel(new GridLayout(4, 1, 5, 5)); // Spazio per 4 bottoni ora
        JButton btnAggiungi = new JButton("Aggiungi Film");
        JButton btnRimuovi = new JButton("Rimuovi Selezionato");
        JButton btnSalvaBackup = new JButton("Salva in Archivio");
        JButton btnCaricaBackup = new JButton("Carica da Archivio");

        panelBottoni.add(btnAggiungi);
        panelBottoni.add(btnRimuovi);
        panelBottoni.add(btnSalvaBackup);
        panelBottoni.add(btnCaricaBackup);

        JPanel southContainer = new JPanel(new BorderLayout());
        southContainer.add(panelInput, BorderLayout.CENTER);
        southContainer.add(panelBottoni, BorderLayout.EAST);
        add(southContainer, BorderLayout.SOUTH);

        btnCerca.addActionListener(e -> {
            filtroAttivo = txtFiltroTitolo.getText().trim();
            update();
        });

        btnResettaFiltro.addActionListener(e -> {
            txtFiltroTitolo.setText("");
            filtroAttivo = "";
            update();
        });

        btnAggiungi.addActionListener(e -> {
            gestioneInserimento();
            facade.salvaDati(); 
        });

        btnRimuovi.addActionListener(e -> {
            int rigaSelezionata = tabellaFilm.getSelectedRow();
            if (rigaSelezionata != -1) {
                int id = (int) tableModel.getValueAt(rigaSelezionata, 0);
                facade.rimuoviFilm(id);
                facade.salvaDati(); 
            } else {
                JOptionPane.showMessageDialog(this, "Seleziona un film dalla tabella per rimuoverlo.");
            }
        });

        btnSalvaBackup.addActionListener(e -> {
            facade.salvaDati();
            JOptionPane.showMessageDialog(this, "Catalogo esportato con successo in 'catalogo.csv'!");
        });

        btnCaricaBackup.addActionListener(e -> {
            facade.caricaDati();
        });

        update();
    }

    private void gestioneInserimento() {
        try {
            String titolo = txtTitolo.getText();
            String regista = txtRegista.getText();
            int anno = Integer.parseInt(txtAnno.getText());
            String genere = txtGenere.getText();
            int voto = Integer.parseInt(txtVoto.getText());
            StatoVisione stato = (StatoVisione) comboStato.getSelectedItem();

            DatiFilm nuoviDati = new DatiFilm(titolo, regista, anno, genere, voto, stato);

            facade.inserisciFilm(nuoviDati);

            txtTitolo.setText(""); txtRegista.setText(""); txtAnno.setText(""); 
            txtGenere.setText(""); txtVoto.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Controlla i campi Anno e Voto (devono essere numeri!)");
        }
    }

    @Override
    public void update() {
        tableModel.setRowCount(0);

        List<FilmIF> datiDaMostrare;
        
        if (!filtroAttivo.isEmpty()) {
            System.out.println("[GUI] Richiesta di filtraggio per titolo via Strategy: " + filtroAttivo);
            datiDaMostrare = facade.cercaPerTitolo(filtroAttivo);
        } else {
            System.out.println("[GUI] Richiesta catalogo completo.");
            datiDaMostrare = facade.ottieniCatalogoCompleto();
        }

        for (FilmIF f : datiDaMostrare) {
            Object[] riga = {
                f.getId(),
                f.getTitolo(),
                f.getRegista(),
                f.getAnnoUscita(),
                f.getGenere(),
                f.getValutazione(),
                f.getStatoVisione()
            };
            tableModel.addRow(riga);
        }
    }

}
