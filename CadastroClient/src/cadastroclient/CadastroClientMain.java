/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package cadastroclient;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import model.Produto;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ivan
 */
public class CadastroClientMain {

    private static final Logger LOGGER = Logger.getLogger(CadastroClientMain.class.getName());

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try (
            Socket socket = new Socket("localhost", 4321);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        ) {
            // Enviar login e senha
            String login = "op1";
            String senha = "op1";
            out.writeObject(login);
            out.writeObject(senha);
            out.flush();

            // Ler resposta do servidor
            String respostaLogin = (String) in.readObject();
            System.out.println(respostaLogin);

            // Enviar comando "L" (Listar produtos)
            out.writeObject("L");
            out.flush();

            // Ler "Lista de produtos:"
            String msg = (String) in.readObject();
            System.out.println(msg);

            // Ler a lista de produtos
            List<Produto> produtos = (List<Produto>) in.readObject();
            for (Produto p : produtos) {
                System.out.println("Produto: " + p.getNome() + " (ID: " + p.getIdProduto() + ") | Quantidade: " + p.getQuantidade());
            }

            // Encerrar (enviar "X")
            out.writeObject("X");
            out.flush();

            System.out.println("Cliente encerrado com sucesso.");

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro no cliente", e);
        }
    }
}
