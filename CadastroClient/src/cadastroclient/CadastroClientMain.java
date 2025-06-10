/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package cadastroclient;

import model.Produto;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
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
            
            // (1) Se chegamos aqui, a conexão com o servidor na porta 4321 foi estabelecida,
            //     e os canais de entrada e saída foram abertos com sucesso.
           
            // (2) Enviar login e senha
            String login = "op1";
            String senha = "op1";
            out.writeObject(login);
            out.writeObject(senha);
            out.flush();
            
            // (3) Se chegamos aqui, usuário foi autenticado com sucesso
            System.out.println("Usuario conectado com sucesso");

            // (4) Enviar comando "L"
            String comando = "L";
            out.writeObject(comando);
            out.flush();

            // (5) Receber lista de produtos
            List<Produto> produtos = (List<Produto>) in.readObject();

            // (6) Exibir nomes dos produtos
            for (Produto p : produtos) {
                System.out.println(p.getNome());
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro no cliente", e);
        }
    }
}
