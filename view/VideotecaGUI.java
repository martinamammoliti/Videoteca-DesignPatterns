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
    
    private JTextField txtSearch;
    private JComboBox<String> comboTipoSearch;
    private JTextField txtFiltroGenere;
    private JComboBox<String> comboFiltroStato;
    private JComboBox<String> comboOrdinamento;
    private JComboBox<StatoVisione> comboStatoInserimento;

    public VideotecaGUI(Videoteca videoteca, VideotecaFacade facade) {
        this.videoteca = videoteca;
        this.facade = facade;
        
        this.videoteca.attach(this);

        setTitle("🎬 Sistema di Gestione Videoteca - Layout Intelligente");
        setSize(1020, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel panelNord = new JPanel(new WrapLayout(FlowLayout.LEFT, 10, 5));
        panelNord.setBorder(BorderFactory.createTitledBorder("Pannello di Controllo Visualizzazione"));

        txtSearch = new JTextField(12);
        comboTipoSearch = new JComboBox<>(new String[]{"Titolo", "Regista"});
        txtFiltroGenere = new JTextField(10);
        comboFiltroStato = new JComboBox<>(new String[]{"Tutti", "VISTO", "DA_VEDERE", "IN_VISIONE"});
        comboOrdinamento = new JComboBox<>(new String[]{"Nessuno", "Titolo", "Anno", "Valutazione"});
        JButton btnApplica = new JButton("🔄 Applica Filtri e Ordina");
        JButton btnReset = new JButton("❌ Resetta");

        panelNord.add(new JLabel("Cerca testo:"));
        panelNord.add(txtSearch);
        panelNord.add(new JLabel("In campo:"));
        panelNord.add(comboTipoSearch);
        
        panelNord.add(new JLabel(" | ")); // Separatore visivo

        panelNord.add(new JLabel("Filtra Genere:"));
        panelNord.add(txtFiltroGenere);
        panelNord.add(new JLabel("Stato Visione:"));
        panelNord.add(comboFiltroStato);
        panelNord.add(new JLabel("Ordina per:"));
        panelNord.add(comboOrdinamento);
        panelNord.add(btnApplica);
        panelNord.add(btnReset);

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
        comboStatoInserimento = new JComboBox<>(StatoVisione.values());

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

       
        comboStatoInserimento.addActionListener(e -> {
            StatoVisione statocorrente = (StatoVisione) comboStatoInserimento.getSelectedItem();
            if (statocorrente == StatoVisione.DA_VEDERE || statocorrente == StatoVisione.IN_VISIONE) {
                txtVoto.setText("");
                txtVoto.setEnabled(false);
            } else {
                txtVoto.setEnabled(true);
            }
        });

       
        tabellaFilm.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabellaFilm.getSelectedRow() != -1) {
                int riga = tabellaFilm.getSelectedRow();
                txtTitolo.setText(tableModel.getValueAt(riga, 1).toString());
                txtRegista.setText(tableModel.getValueAt(riga, 2).toString());
                txtAnno.setText(tableModel.getValueAt(riga, 3).toString());
                txtGenere.setText(tableModel.getValueAt(riga, 4).toString());
                
                StatoVisione statoCaricato = (StatoVisione) tableModel.getValueAt(riga, 6);
                comboStatoInserimento.setSelectedItem(statoCaricato);

                if (statoCaricato == StatoVisione.DA_VEDERE || statoCaricato == StatoVisione.IN_VISIONE) {
                    txtVoto.setText("");
                    txtVoto.setEnabled(false);
                } else {
                    txtVoto.setText(tableModel.getValueAt(riga, 5).toString());
                    txtVoto.setEnabled(true);
                }
            }
        });

        btnApplica.addActionListener(e -> update());
        
        btnReset.addActionListener(e -> {
            txtSearch.setText(""); txtFiltroGenere.setText("");
            comboFiltroStato.setSelectedIndex(0); comboOrdinamento.setSelectedIndex(0);
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
                pulisciCampi();
            } else {
                JOptionPane.showMessageDialog(this, "Seleziona un film da rimuovere.");
            }
        });

        btnSalvaBackup.addActionListener(e -> facade.salvaDati());
        btnCaricaBackup.addActionListener(e -> facade.caricaDati());

        update();
    }

    private boolean gestioneSalvataggioOModifica(int id) {
        try {
            String titolo = txtTitolo.getText().trim();
            String regista = txtRegista.getText().trim();
            int anno = Integer.parseInt(txtAnno.getText().trim());
            String genere = txtGenere.getText().trim();
            StatoVisione stato = (StatoVisione) comboStatoInserimento.getSelectedItem();
            int voto = 0;

            if (titolo.isEmpty() || regista.isEmpty() || genere.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tutti i campi testuali devono essere compilati.");
                return false;
            }

            if (stato == StatoVisione.VISTO) {
                voto = Integer.parseInt(txtVoto.getText().trim());
                if (voto < 1 || voto > 5) {
                    JOptionPane.showMessageDialog(this, "La valutazione personale deve essere compresa tra 1 e 5 stelle.");
                    return false;
                }
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

    private void pulisciCampi() {
        txtTitolo.setText(""); txtRegista.setText(""); txtAnno.setText(""); 
        txtGenere.setText(""); txtVoto.setText(""); comboStatoInserimento.setSelectedIndex(0);
        txtVoto.setEnabled(true);
    }

    @Override
    public void update() {
        tableModel.setRowCount(0);
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

class WrapLayout extends FlowLayout {
    public WrapLayout(int align, int hgap, int vgap) { super(align, hgap, vgap); }
    @Override public Dimension preferredLayoutSize(Container target) { return layoutSize(target, true); }
    @Override public Dimension minimumLayoutSize(Container target) { return layoutSize(target, false); }

    private Dimension layoutSize(Container target, boolean preferred) {
        synchronized (target.getTreeLock()) {
            int targetWidth = target.getWidth();
            if (targetWidth == 0) targetWidth = Integer.MAX_VALUE;
            int hgap = getHgap(); int vgap = getVgap();
            Insets insets = target.getInsets();
            int maxwidth = targetWidth - (insets.left + insets.right + hgap * 2);
            int nmembers = target.getComponentCount();
            int x = 0, y = insets.top + vgap;
            int rowHeight = 0;

            for (int i = 0; i < nmembers; i++) {
                Component m = target.getComponent(i);
                if (m.isVisible()) {
                    Dimension d = preferred ? m.getPreferredSize() : m.getMinimumSize();
                    if (x == 0 || x + d.width <= maxwidth) {
                        if (x > 0) x += hgap;
                        x += d.width;
                        rowHeight = Math.max(rowHeight, d.height);
                    } else {
                        x = d.width;
                        y += vgap + rowHeight;
                        rowHeight = d.height;
                    }
                }
            }
            y += rowHeight + vgap + insets.bottom;
            return new Dimension(targetWidth, y);
        }
    }
}
