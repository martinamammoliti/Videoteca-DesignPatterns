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

        setTitle("🎬 Gestore Videoteca - Conforme alle Specifiche");
        setSize(900, 580);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
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

    
        String[] colonne = {"ID", "Titolo", "Regista", "Anno", "Genere", "Voto (1-5)", "Stato"};
        tableModel = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Rende la tabella non editabile direttamente con un doppio click
            }
        };
        tabellaFilm = new JTable(tableModel);
        add(new JScrollPane(tabellaFilm), BorderLayout.CENTER);

        JPanel panelInput = new JPanel(new GridLayout(2, 6, 5, 5));
        panelInput.setBorder(BorderFactory.createTitledBorder("Dettagli Informativi Film"));

        txtTitolo = new JTextField();
        txtRegista = new JTextField();
        txtAnno = new JTextField();
        txtGenere = new JTextField();
        txtVoto = new JTextField(); // Gestirà la scala 1-5 stelle
        comboStato = new JComboBox<>(StatoVisione.values());

        panelInput.add(new JLabel("Titolo:"));
        panelInput.add(new JLabel("Regista:"));
        panelInput.add(new JLabel("Anno Uscita:"));
        panelInput.add(new JLabel("Genere:"));
        panelInput.add(new JLabel("Valutazione (1-5 ★):")); // Modificato scala come da traccia
        panelInput.add(new JLabel("Stato Visione:"));

        panelInput.add(txtTitolo);
        panelInput.add(txtRegista);
        panelInput.add(txtAnno);
        panelInput.add(txtGenere);
        panelInput.add(txtVoto);
        panelInput.add(comboStato);

    
        JPanel panelBottoni = new JPanel(new GridLayout(5, 1, 5, 5)); // Spazio per 5 bottoni
        JButton btnAggiungi = new JButton("Aggiungi Film");
        JButton btnModifica = new JButton("Modifica Selezionato"); // <-- NUOVO: Richiesto dalla traccia
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
                comboStato.setSelectedItem(tableModel.getValueAt(riga, 6));
            }
        });

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
            if (gestioneSalvataggioOModifica(-1)) {
                facade.salvaDati();
                pulisciCampi();
            }
        });

    
        btnModifica.addActionListener(e -> {
            int rigaSelezionata = tabellaFilm.getSelectedRow();
            if (rigaSelezionata != -1) {
                int id = (int) tableModel.getValueAt(rigaSelezionata, 0);
                if (gestioneSalvataggioOModifica(id)) {
                    facade.salvaDati();
                    pulisciCampi();
                    tabellaFilm.clearSelection();
                    JOptionPane.showMessageDialog(this, "Informazioni del film aggiornate con successo!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleziona prima un film dalla tabella per modificarlo.");
            }
        });

        btnRimuovi.addActionListener(e -> {
            int rigaSelezionata = tabellaFilm.getSelectedRow();
            if (rigaSelezionata != -1) {
                int id = (int) tableModel.getValueAt(rigaSelezionata, 0);
                facade.rimuoviFilm(id);
                facade.salvaDati();
                pulisciCampi();
            } else {
                JOptionPane.showMessageDialog(this, "Seleziona un film dalla tabella per rimuoverlo.");
            }
        });

        btnSalvaBackup.addActionListener(e -> {
            facade.salvaDati();
            JOptionPane.showMessageDialog(this, "Catalogo salvato in memoria secondaria (catalogo.csv).");
        });

        btnCaricaBackup.addActionListener(e -> facade.caricaDati());

        update();
    }

    private boolean gestioneSalvataggioOModifica(int id) {
        try {
            String titolo = txtTitolo.getText().trim();
            String regista = txtRegista.getText().trim();
            int anno = Integer.parseInt(txtAnno.getText().trim());
            String genere = txtGenere.getText().trim();
            int voto = Integer.parseInt(txtVoto.getText().trim());
            StatoVisione stato = (StatoVisione) comboStato.getSelectedItem();

            if (titolo.isEmpty() || regista.isEmpty() || genere.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tutti i campi testuali devono essere compilati.");
                return false;
            }

            if (voto < 1 || voto > 5) {
                JOptionPane.showMessageDialog(this, "La valutazione personale deve essere compresa tra 1 e 5 stelle.");
                return false;
            }

            DatiFilm dati = new DatiFilm(titolo, regista, anno, genere, voto, stato);

            if (id == -1) {
                facade.inserisciFilm(dati); // Inserimento
            } else {
                facade.modificaFilm(id, dati); // Modifica (Command Pattern + Modello aggiornato)
            }
            return true;

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "I campi Anno e Valutazione richiedono un valore numerico valido.");
            return false;
        }
    }

    private void pulisciCampi() {
        txtTitolo.setText("");
        txtRegista.setText("");
        txtAnno.setText("");
        txtGenere.setText("");
        txtVoto.setText("");
        comboStato.setSelectedIndex(0);
    }

    @Override
    public void update() {
        tableModel.setRowCount(0);
        List<FilmIF> datiDaMostrare = filtroAttivo.isEmpty() ? facade.ottieniCatalogoCompleto() : facade.cercaPerTitolo(filtroAttivo);

        for (FilmIF f : datiDaMostrare) {
            Object[] riga = {
                f.getId(), f.getTitolo(), f.getRegista(), f.getAnnoUscita(), f.getGenere(), f.getValutazione(), f.getStatoVisione()
            };
            tableModel.addRow(riga);
        }
    }
}
