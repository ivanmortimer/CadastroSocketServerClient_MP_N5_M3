/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package cadastroclient;

import javax.swing.JDialog;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;
import java.awt.BorderLayout;

public class SaidaFrame extends JDialog {

    private final JTextArea texto;  // agora private com getter

    public SaidaFrame() {
        setTitle("Mensagens do Servidor");
        setBounds(100, 100, 500, 400);
        setModal(false);  // janela NÃO modal
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        texto = new JTextArea();
        texto.setEditable(false);
        texto.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(texto);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    // Getter para ThreadClient acessar o JTextArea
    public JTextArea getTexto() {
        return texto;
    }
}
