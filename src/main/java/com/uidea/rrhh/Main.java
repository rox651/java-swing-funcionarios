package com.uidea.rrhh;

import com.uidea.rrhh.ui.FuncionarioFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) { }
            new FuncionarioFrame().setVisible(true);
        });
    }
}


