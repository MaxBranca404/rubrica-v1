import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class RubricaMain extends JFrame {
    private Vector<Persona> persone;
    private JTable tabella;
    private DefaultTableModel model;

    public RubricaMain() {
        super("Rubrica Telefonica");

        persone = FileManager.caricaPersone();

        initUI();

        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void initUI() {
        String[] columns = {"Nome", "Cognome", "Telefono"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        updateTable();

        tabella = new JTable(model);
        tabella.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tabella);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnNuovo = new JButton("Nuovo");
        JButton btnModifica = new JButton("Modifica");
        JButton btnElimina = new JButton("Elimina");

        btnNuovo.addActionListener(e -> nuovaPersona());
        btnModifica.addActionListener(e -> modificaPersona());
        btnElimina.addActionListener(e -> eliminaPersona());

        buttonPanel.add(btnNuovo);
        buttonPanel.add(btnModifica);
        buttonPanel.add(btnElimina);

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void updateTable() {
        model.setRowCount(0);

        for (Persona p : persone) {
            model.addRow(new Object[] {p.getNome(), p.getCognome(), p.getTelefono()});
        }
    }

    private void nuovaPersona() {
        EditorPersona editor = new EditorPersona(this, null);
        editor.setVisible(true);

        if (editor.isSaved()) {
            Persona nuovaPersona = editor.getPersona();
            persone.add(nuovaPersona);
            updateTable();

            FileManager.salvaPersone(persone);
        }
    }

    private void modificaPersona() {
        int selectedRow = tabella.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Per modificare è necessario prima selezionare una persona",
                "Attenzione",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        Persona personaSelezionata = persone.get(selectedRow);
        EditorPersona editor = new EditorPersona(this, personaSelezionata);
        editor.setVisible(true);

        if (editor.isSaved()) {
            updateTable();

            FileManager.salvaPersone(persone);
        }
    }

    private void eliminaPersona() {
        int selectedRow = tabella.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Per eliminare è necessario prima selezionare una persona",
                "Attenzione",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        Persona personaSelezionata = persone.get(selectedRow);
        String messaggio = "Eliminare la persona " + personaSelezionata.getNome() + " " +
                           personaSelezionata.getCognome() + "?";

        int response = JOptionPane.showConfirmDialog(this,
            messaggio,
            "Conferma eliminazione",
            JOptionPane.YES_NO_OPTION);

        if (response == JOptionPane.YES_OPTION) {
            persone.remove(selectedRow);
            updateTable();

            FileManager.salvaPersone(persone);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RubricaMain app = new RubricaMain();
            app.setVisible(true);
        });
    }
}
