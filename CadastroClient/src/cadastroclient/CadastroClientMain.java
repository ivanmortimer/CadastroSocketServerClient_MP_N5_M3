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
            // Se chegamos aqui, a conexão com o servidor na porta 4321 foi estabelecida,
            // e os canais de entrada e saída foram abertos com sucesso.
            
            // (1) Enviar login e senha
            String login = "op1";
            String senha = "op1";
            out.writeObject(login);
            out.writeObject(senha);
            out.flush();
            
            // (2) Ler resposta de login (agora é uma String enviada pelo servidor)
            //     Se chegamos aqui, usuário foi autenticado com sucesso
            String respostaLogin = (String) in.readObject();
            System.out.println(respostaLogin);

            // (3) Enviar comando "L"
            String comando = "L";
            out.writeObject(comando);
            out.flush();

            // (4) Receber resposta
            Object resposta = in.readObject();
            if (resposta instanceof List) {
                List<Produto> produtos = (List<Produto>) resposta;

                // (5) Exibir nomes dos produtos
                for (Produto p : produtos) {
                    System.out.println(p.getNome());
                }
            } else if (resposta instanceof String) {
                // Se for uma String, exibir mensagem
                System.out.println((String) resposta);
            } else {
                System.out.println("Resposta inesperada do servidor.");
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro no cliente", e);
        }
    }
}
