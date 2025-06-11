/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cadastroclient;

import java.io.ObjectInputStream;
import java.util.List;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 *
 * @author Ivan
 */
public class ThreadClient extends Thread {

    private final ObjectInputStream in;
    private final JTextArea texto;
    private final SaidaFrame frame;

    public ThreadClient(ObjectInputStream in, JTextArea texto, SaidaFrame frame) {
        this.in = in;
        this.texto = texto;
        this.frame = frame;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Object obj = in.readObject();

                if (obj instanceof String) {
                    String msg = (String) obj;

                    // Trazer a janela para frente quando listar produtos ou ao receber uma mensagem
                    SwingUtilities.invokeLater(() -> {
                        frame.setVisible(true);
                        frame.toFront();
                        frame.requestFocus();
                    });

                    // Se for "Lista de produtos:", incluir espaçamento
                    if (msg.equalsIgnoreCase("Lista de produtos:")) {
                        SwingUtilities.invokeLater(() -> texto.append("\nLista de produtos:\n"));
                    } else {
                        SwingUtilities.invokeLater(() -> texto.append(msg + "\n"));
                    }
                } else if (obj instanceof List) {
                    List<?> lista = (List<?>) obj;

                    SwingUtilities.invokeLater(() -> {
                        for (Object item : lista) {
                            if (item instanceof model.Produto produto) {
                                texto.append("Id: " + produto.getIdProduto()
                                        + " | Produto: " + produto.getNome()
                                        + " | Quantidade: " + produto.getQuantidade() + "\n");
                            } else {
                                texto.append(item.toString() + "\n");
                            }
                        }
                        texto.append("\n"); // espaçamento entre listagens
                    });
                }
            }
        } catch (Exception e) {
            SwingUtilities.invokeLater(() -> texto.append("Conexão encerrada.\n"));
        }
    }
}
