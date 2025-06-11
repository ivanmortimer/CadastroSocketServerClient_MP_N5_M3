/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package cadastroserver;

import controller.*;
import java.io.IOException;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ivan
 */
public class CadastroServerMain {

    private static final Logger LOGGER = Logger.getLogger(CadastroServerMain.class.getName());

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // (1) Instanciar EntityManagerFactory
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("CadastroServerPU");

            // (2) Instanciar os controllers
            ProdutoJpaController ctrlProduto = new ProdutoJpaController(emf);
            UsuarioJpaController ctrlUsuario = new UsuarioJpaController(emf);
            MovimentoJpaController ctrlMovimento = new MovimentoJpaController(emf);
            PessoaJpaController ctrlPessoa = new PessoaJpaController(emf);

            // (3) Criar o ServerSocket na porta 4321
            ServerSocket serverSocket = new ServerSocket(4321);
            LOGGER.info("CadastroServer iniciado na porta 4321.");

            // (4) Loop infinito de atendimento
            while (true) {
                Socket clientSocket = serverSocket.accept();
                LOGGER.log(Level.INFO, "Cliente conectado: {0}", clientSocket.getInetAddress().getHostAddress());

                // Usar a nova Thread V2
                CadastroThreadV2 thread = new CadastroThreadV2(
                        ctrlProduto, ctrlUsuario, ctrlMovimento, ctrlPessoa, clientSocket
                );

                thread.start();
            }

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erro no servidor", e);
        }
    }
}
