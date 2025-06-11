/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cadastroclient;

import javax.swing.JDialog;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

/**
 *
 * @author Ivan
 */
public class SaidaFrame extends JDialog {

    public JTextArea texto;

    public SaidaFrame() {
        setTitle("Mensagens do Servidor");
        setBounds(100, 100, 400, 300); // posição x,y e tamanho w,h
        setModal(false); // Não bloqueia a thread principal

        texto = new JTextArea();
        texto.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(texto);
        add(scrollPane);

        setVisible(true);
    }
}
