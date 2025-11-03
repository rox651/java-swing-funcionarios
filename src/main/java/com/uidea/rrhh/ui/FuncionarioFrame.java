package com.uidea.rrhh.ui;

import com.uidea.rrhh.dao.FuncionarioDAO;
import com.uidea.rrhh.dao.FuncionarioDAOImpl;
import com.uidea.rrhh.exception.ValidationException;
import com.uidea.rrhh.model.Funcionario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class FuncionarioFrame extends JFrame {

    private final FuncionarioDAO funcionarioDAO = new FuncionarioDAOImpl();

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtId;
    private JComboBox<String> cbTipoId;
    private JTextField txtNumId;
    private JTextField txtNombres;
    private JTextField txtApellidos;
    private JTextField txtEstadoCivil;
    private JComboBox<String> cbSexo;
    private JTextField txtDireccion;
    private JTextField txtTelefono;
    private JTextField txtFechaNac; // yyyy-MM-dd

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public FuncionarioFrame() {
        setTitle("RRHH - Funcionarios (CRUD)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(980, 640);
        setLocationRelativeTo(null);
        initComponents();
        loadTable();
    }

    private void initComponents() {
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 4);
        c.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        txtId = new JTextField();
        txtId.setEditable(false);
        addFormRow(form, c, row++, "ID", txtId);

        cbTipoId = new JComboBox<>(new String[] { "CC", "TI", "CE", "PA" });
        addFormRow(form, c, row++, "Tipo Ident.", cbTipoId);

        txtNumId = new JTextField();
        addFormRow(form, c, row++, "Num. Ident.", txtNumId);

        txtNombres = new JTextField();
        addFormRow(form, c, row++, "Nombres", txtNombres);

        txtApellidos = new JTextField();
        addFormRow(form, c, row++, "Apellidos", txtApellidos);

        txtEstadoCivil = new JTextField();
        addFormRow(form, c, row++, "Estado Civil", txtEstadoCivil);

        cbSexo = new JComboBox<>(new String[] { "M", "F", "O" });
        addFormRow(form, c, row++, "Sexo", cbSexo);

        txtDireccion = new JTextField();
        addFormRow(form, c, row++, "Direccion", txtDireccion);

        txtTelefono = new JTextField();
        addFormRow(form, c, row++, "Telefono", txtTelefono);

        txtFechaNac = new JTextField();
        txtFechaNac.setToolTipText("Formato: yyyy-MM-dd");
        addFormRow(form, c, row++, "Fecha Nac.", txtFechaNac);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnNuevo = new JButton("Nuevo");
        JButton btnGuardar = new JButton("Guardar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnRefrescar = new JButton("Refrescar");
        JButton btnLimpiar = new JButton("Limpiar");
        buttons.add(btnNuevo);
        buttons.add(btnGuardar);
        buttons.add(btnEliminar);
        buttons.add(btnRefrescar);
        buttons.add(btnLimpiar);

        c.gridx = 0;
        c.gridy = row;
        c.gridwidth = 2;
        form.add(buttons, c);

        tableModel = new DefaultTableModel(new Object[] {
                "ID", "TipoId", "NumId", "Nombres", "Apellidos", "EstadoCivil", "Sexo", "Direccion", "Telefono",
                "FechaNac"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(table);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, form, scroll);
        split.setDividerLocation(380);
        getContentPane().add(split, BorderLayout.CENTER);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int i = table.getSelectedRow();
                if (i >= 0) {
                    txtId.setText(tableModel.getValueAt(i, 0).toString());
                    cbTipoId.setSelectedItem(s(tableModel.getValueAt(i, 1)));
                    txtNumId.setText(s(tableModel.getValueAt(i, 2)));
                    txtNombres.setText(s(tableModel.getValueAt(i, 3)));
                    txtApellidos.setText(s(tableModel.getValueAt(i, 4)));
                    txtEstadoCivil.setText(s(tableModel.getValueAt(i, 5)));
                    cbSexo.setSelectedItem(s(tableModel.getValueAt(i, 6)));
                    txtDireccion.setText(s(tableModel.getValueAt(i, 7)));
                    txtTelefono.setText(s(tableModel.getValueAt(i, 8)));
                    txtFechaNac.setText(s(tableModel.getValueAt(i, 9)));
                }
            }
        });

        btnNuevo.addActionListener(e -> onNuevo());
        btnLimpiar.addActionListener(e -> clearForm());
        btnRefrescar.addActionListener(e -> loadTable());
        btnEliminar.addActionListener(e -> onEliminar());
        btnGuardar.addActionListener(e -> onGuardar());
    }

    private static void addFormRow(JPanel panel, GridBagConstraints c, int row, String label, JComponent field) {
        c.gridx = 0;
        c.gridy = row;
        c.weightx = 0.0;
        c.gridwidth = 1;
        panel.add(new JLabel(label), c);
        c.gridx = 1;
        c.gridy = row;
        c.weightx = 1.0;
        c.gridwidth = 1;
        panel.add(field, c);
    }

    private void loadTable() {
        tableModel.setRowCount(0);
        List<Funcionario> funcionarios = funcionarioDAO.findAll();
        for (Funcionario f : funcionarios) {
            tableModel.addRow(new Object[] {
                    f.getId(), f.getTipoIdentificacion(), f.getNumeroIdentificacion(), f.getNombres(), f.getApellidos(),
                    f.getEstadoCivil(), f.getSexo(), f.getDireccion(), f.getTelefono(),
                    f.getFechaNacimiento() != null ? f.getFechaNacimiento().format(DATE_FMT) : ""
            });
        }
    }

    private void clearForm() {
        txtId.setText("");
        cbTipoId.setSelectedIndex(0);
        txtNumId.setText("");
        txtNombres.setText("");
        txtApellidos.setText("");
        txtEstadoCivil.setText("");
        cbSexo.setSelectedIndex(0);
        txtDireccion.setText("");
        txtTelefono.setText("");
        txtFechaNac.setText("");
        table.clearSelection();
    }

    private void onNuevo() {
        clearForm();
        cbTipoId.requestFocusInWindow();
    }

    private void onEliminar() {
        String idText = txtId.getText().trim();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un funcionario de la tabla", "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        int r = JOptionPane.showConfirmDialog(this, "Confirma eliminar el funcionario?", "Confirmar",
                JOptionPane.YES_NO_OPTION);
        if (r == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(idText);
                funcionarioDAO.delete(id);
                loadTable();
                clearForm();
                JOptionPane.showMessageDialog(this, "Eliminado exitosamente");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onGuardar() {
        try {
            Funcionario f = readFromForm();
            if (f.getId() == null) {
                funcionarioDAO.create(f);
                JOptionPane.showMessageDialog(this, "Creado exitosamente");
            } else {
                funcionarioDAO.update(f);
                JOptionPane.showMessageDialog(this, "Actualizado exitosamente");
            }
            loadTable();
            selectByNumeroIdentificacion(f.getNumeroIdentificacion());
        } catch (ValidationException ve) {
            JOptionPane.showMessageDialog(this, ve.getMessage(), "Validacion", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            String msg = ex.getMessage();
            Throwable cause = ex.getCause();
            if (cause != null && cause.getMessage() != null && !cause.getMessage().isBlank()) {
                msg = msg + " - " + cause.getMessage();
            }
            JOptionPane.showMessageDialog(this, "Error: " + msg, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Funcionario readFromForm() {
        Funcionario f = new Funcionario();
        String idText = txtId.getText().trim();
        if (!idText.isEmpty()) {
            f.setId(Integer.parseInt(idText));
        }
        String tipoId = (String) cbTipoId.getSelectedItem();
        String numId = txtNumId.getText().trim();
        String nombres = txtNombres.getText().trim();
        String apellidos = txtApellidos.getText().trim();
        String estadoCivil = txtEstadoCivil.getText().trim();
        String sexo = (String) cbSexo.getSelectedItem();
        String direccion = txtDireccion.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String fecha = txtFechaNac.getText().trim();

        if (tipoId == null || tipoId.isEmpty() || numId.isEmpty() || nombres.isEmpty() || apellidos.isEmpty()
                || estadoCivil.isEmpty() || fecha.isEmpty()) {
            throw new ValidationException(
                    "Campos obligatorios: TipoId, NumId, Nombres, Apellidos, EstadoCivil, FechaNac");
        }
        // Sanitiza numero de identificacion (evita caracteres extraños como `)
        numId = numId.replaceAll("[^A-Za-z0-9_-]", "");
        LocalDate fechaNac;
        try {
            fechaNac = LocalDate.parse(fecha, DATE_FMT);
        } catch (DateTimeParseException e) {
            throw new ValidationException("Fecha de nacimiento invalida. Use yyyy-MM-dd");
        }

        f.setTipoIdentificacion(tipoId);
        f.setNumeroIdentificacion(numId);
        f.setNombres(nombres);
        f.setApellidos(apellidos);
        f.setEstadoCivil(estadoCivil);
        f.setSexo(sexo);
        f.setDireccion(direccion);
        f.setTelefono(telefono);
        f.setFechaNacimiento(fechaNac);
        return f;
    }

    private void selectByNumeroIdentificacion(String numero) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (numero.equals(String.valueOf(tableModel.getValueAt(i, 2)))) {
                table.setRowSelectionInterval(i, i);
                table.scrollRectToVisible(table.getCellRect(i, 0, true));
                break;
            }
        }
    }

    private static String s(Object o) {
        return o == null ? "" : o.toString();
    }
}
