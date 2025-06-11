/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cadastroclient;

import java.awt.Window;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.SocketException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import model.Produto;

public class ThreadClient extends Thread {

    private static final Logger LOGGER = Logger.getLogger(ThreadClient.class.getName());

    private final ObjectInputStream in;
    private final SaidaFrame frame;

    public ThreadClient(ObjectInputStream in, SaidaFrame frame) {
        this.in = in;
        this.frame = frame;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Object obj = in.readObject();

                if (obj instanceof String msg) {

                    // Se for mensagem de Lista de produtos → limpar texto antes e trazer frame para frente
                    if (msg.startsWith("Lista de produtos:")) {
                        SwingUtilities.invokeLater(() -> {
                            frame.getTexto().setText("");
                            bringFrameToFront(frame);
                        });
                    }

                    SwingUtilities.invokeLater(() -> frame.getTexto().append(msg + "\n"));

                } else if (obj instanceof List<?> lista) {

                    // Supondo que seja lista de Produto
                    for (Object o : lista) {
                        if (o instanceof Produto p) {
                            String linha = String.format(
                                    "Produto: %s (ID: %d) | Quantidade: %d",
                                    p.getNome(), p.getIdProduto(), p.getQuantidade());
                            SwingUtilities.invokeLater(() -> frame.getTexto().append(linha + "\n"));
                        }
                    }

                    // Espaço em branco entre listagens
                    SwingUtilities.invokeLater(() -> frame.getTexto().append("\n"));
                }
            }
        } catch (SocketException e) {
            LOGGER.log(Level.INFO, "Conexão encerrada.");
        } catch (EOFException e) {
            LOGGER.log(Level.INFO, "Conexão encerrada pelo servidor.");
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Erro na ThreadClient", e);
        }
    }

    // Método helper para trazer a janela para frente de forma segura
    private void bringFrameToFront(Window window) {
        window.setVisible(true);
        window.setAlwaysOnTop(true);
        window.toFront();
        window.requestFocus();
        window.setAlwaysOnTop(false); // Remove always on top para não "grudar" no topo da tela
    }
}
