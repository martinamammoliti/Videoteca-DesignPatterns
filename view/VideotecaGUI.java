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
    
    // Componenti di Ricerca, Filtro e Ordinamento (North Panel)
    private JTextField txtSearch;
    private JComboBox<String> comboTipoSearch;
    private JTextField txtFiltroGenere;
    private JComboBox<String> comboFiltroStato;
    private JComboBox<String> comboOrdinamento;

    public VideotecaGUI(Videoteca videoteca, VideotecaFacade facade) {
        this.videoteca = videoteca;
        this.facade = facade;
        
        this.videoteca.attach(this);

        setTitle("🎬 Sistema di Gestione Videoteca Professionale");
        setSize(1000, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        
        JPanel panelNord = new JPanel(new GridLayout(2, 1, 5, 5));
        panelNord.setBorder(BorderFactory.createTitledBorder("Pannello di Controllo Visualizzazione"));

        JPanel riga1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        txtSearch = new JTextField(15);
        comboTipoSearch = new JComboBox<>(new String[]{"Titolo", "Regista"});
        riga1.add(new JLabel("Cerca testo:"));
        riga1.add(txtSearch);
        riga1.add(new JLabel("In campo:"));
        riga1.add(comboTipoSearch);

        JPanel riga2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        txtFiltroGenere = new JTextField(10);
        
        comboFiltroStato = new JComboBox<>(new String[]{"Tutti", "VISTO", "DA_VEDERE", "IN_VISIONE"});
        comboOrdinamento = new JComboBox<>(new String[]{"Nessuno", "Titolo", "Anno", "Valutazione"});
        
        JButton btnApplica = new JButton("🔄 Applica Filtri e Ordina");
        JButton btnReset = new JButton("❌ Resetta");

        riga2.add(new JLabel("Filtra Genere:"));
        riga2.add(txtFiltroGenere);
        riga2.add(new JLabel("Stato Visione:"));
        riga2.add(comboFiltroStato);
        riga2.add(new JLabel("Ordina per:"));
        riga2.add(comboOrdinamento);
        riga2.add(btnApplica);
        riga2.add(btnReset);

        panelNord.add(riga1);
        panelNord.add(riga2);
        add(panelNord, BorderLayout.NORTH);

        String[] colonne = {"ID", "Titolo", "Regista", "Anno", "Genere", "Voto (1-5 ★)", "Stato"};
        tableModel = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabellaFilm = new JTable(tableModel);
        add(new JScrollPane(tabellaFilm), BorderLayout.CENTER);

        JPanel panelInput = new JPanel(new GridLayout(2, 6, 5, 5));
        panelInput.setBorder(BorderFactory.createTitledBorder("Dettagli Informativi Film"));

        txtTitolo = new JTextField();
        txtRegista = new JTextField();
        txtAnno = new JTextField();
        txtGenere = new JTextField();
        txtVoto = new JTextField();
        JComboBox<StatoVisione> comboStatoInserimento = new JComboBox<>(StatoVisione.values());

        panelInput.add(new JLabel("Titolo:"));
        panelInput.add(new JLabel("Regista:"));
        panelInput.add(new JLabel("Anno Uscita:"));
        panelInput.add(new JLabel("Genere:"));
        panelInput.add(new JLabel("Valutazione (1-5):"));
        panelInput.add(new JLabel("Stato Visione:"));

        panelInput.add(txtTitolo);
        panelInput.add(txtRegista);
        panelInput.add(txtAnno);
        panelInput.add(txtGenere);
        panelInput.add(txtVoto);
        panelInput.add(comboStatoInserimento);

        JPanel panelBottoni = new JPanel(new GridLayout(5, 1, 5, 5));
        JButton btnAggiungi = new JButton("Aggiungi Film");
        JButton btnModifica = new JButton("Modifica Selezionato");
        JButton btnRimuovi = new JButton("Rimuovi Selezionato");
        JButton btnSalvaBackup = new JButton("Salva in Archivio");
        JButton btnCaricaBackup = new JButton("Carica da Archivio");

        panelBottoni.add(btnAggiungi);
        panelBottoni.add(btnModifica);
        panelBottoni.add(btnRimuovi);
        panelBottoni.add(btnSalvaBackup);
        panelBottoni.add(btnCaricaBackup);

        JPanel southContainer = new JPanel(new BorderLayout());
        southContainer.add(panelInput, BorderLayout.CENTER);
        southContainer.add(panelBottoni, BorderLayout.EAST);
        add(southContainer, BorderLayout.SOUTH);


        tabellaFilm.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabellaFilm.getSelectedRow() != -1) {
                int riga = tabellaFilm.getSelectedRow();
                txtTitolo.setText(tableModel.getValueAt(riga, 1).toString());
                txtRegista.setText(tableModel.getValueAt(riga, 2).toString());
                txtAnno.setText(tableModel.getValueAt(riga, 3).toString());
                txtGenere.setText(tableModel.getValueAt(riga, 4).toString());
                txtVoto.setText(tableModel.getValueAt(riga, 5).toString());
                comboStatoInserimento.setSelectedItem(tableModel.getValueAt(riga, 6));
            }
        });

        btnApplica.addActionListener(e -> update()); // Rinfresca applicando i parametri attuali
        
        btnReset.addActionListener(e -> {
            txtSearch.setText("");
            txtFiltroGenere.setText("");
            comboFiltroStato.setSelectedIndex(0);
            comboOrdinamento.setSelectedIndex(0);
            update();
        });

        btnAggiungi.addActionListener(e -> {
            if (gestioneSalvataggioOModifica(-1, comboStatoInserimento)) {
                facade.salvaDati();
                pulisciCampi(comboStatoInserimento);
            }
        });

        btnModifica.addActionListener(e -> {
            int rigaSelezionata = tabellaFilm.getSelectedRow();
            if (rigaSelezionata != -1) {
                int id = (int) tableModel.getValueAt(rigaSelezionata, 0);
                if (gestioneSalvataggioOModifica(id, comboStatoInserimento)) {
                    facade.salvaDati();
                    pulisciCampi(comboStatoInserimento);
                    tabellaFilm.clearSelection();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleziona un film per modificarlo.");
            }
        });

        btnRimuovi.addActionListener(e -> {
            int rigaSelezionata = tabellaFilm.getSelectedRow();
            if (rigaSelezionata != -1) {
                int id = (int) tableModel.getValueAt(rigaSelezionata, 0);
                facade.rimuoviFilm(id);
                facade.salvaDati();
                pulisciCampi(comboStatoInserimento);
            } else {
                JOptionPane.showMessageDialog(this, "Seleziona un film da rimuovere.");
            }
        });

        btnSalvaBackup.addActionListener(e -> facade.salvaDati());
        btnCaricaBackup.addActionListener(e -> facade.caricaDati());

        update();
    }

    private boolean gestioneSalvataggioOModifica(int id, JComboBox<StatoVisione> comboStato) {
        try {
            String titolo = txtTitolo.getText().trim();
            String regista = txtRegista.getText().trim();
            int anno = Integer.parseInt(txtAnno.getText().trim());
            String genere = txtGenere.getText().trim();
            int voto = Integer.parseInt(txtVoto.getText().trim());
            StatoVisione stato = (StatoVisione) comboStato.getSelectedItem();

            if (titolo.isEmpty() || regista.isEmpty() || genere.isEmpty() || voto < 1 || voto > 5) {
                JOptionPane.showMessageDialog(this, "Verifica i dati (Voto ammesso: 1-5 stelle).");
                return false;
            }

            DatiFilm dati = new DatiFilm(titolo, regista, anno, genere, voto, stato);
            if (id == -1) facade.inserisciFilm(dati);
            else facade.modificaFilm(id, dati);
            return true;
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Controlla i campi numerici.");
            return false;
        }
    }

    private void pulisciCampi(JComboBox<StatoVisione> comboStato) {
        txtTitolo.setText(""); txtRegista.setText(""); txtAnno.setText(""); 
        txtGenere.setText(""); txtVoto.setText(""); comboStato.setSelectedIndex(0);
    }

    @Override
    public void update() {
        tableModel.setRowCount(0);

        // Estrazione in tempo reale dello stato dei controlli grafici
        String querySearch = txtSearch.getText().trim();
        String tipoSearch = comboTipoSearch.getSelectedItem().toString();
        String genereFiltro = txtFiltroGenere.getText().trim();
        String statoFiltro = comboFiltroStato.getSelectedItem().toString();
        String criterioOrdine = comboOrdinamento.getSelectedItem().toString();


        List<FilmIF> datiFiltrati = facade.ottieniCatalogoFiltratoEOrdinato(
                querySearch, tipoSearch, genereFiltro, statoFiltro, criterioOrdine
        );


        for (FilmIF f : datiFiltrati) {
            Object[] riga = {
                f.getId(), f.getTitolo(), f.getRegista(), f.getAnnoUscita(), f.getGenere(), f.getValutazione(), f.getStatoVisione()
            };
            tableModel.addRow(riga);
        }
    }
}
