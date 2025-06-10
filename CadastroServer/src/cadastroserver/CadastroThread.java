/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cadastroserver;

import controller.ProdutoJpaController;
import controller.UsuarioJpaController;
import model.Produto;
import model.Usuario;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ivan
 */
public class CadastroThread extends Thread implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(CadastroThread.class.getName());

    private final ProdutoJpaController ctrl;
    private final UsuarioJpaController ctrlUsu;
    private final Socket s1;

    public CadastroThread(ProdutoJpaController ctrl, UsuarioJpaController ctrlUsu, Socket s1) {
        this.ctrl = ctrl;
        this.ctrlUsu = ctrlUsu;
        this.s1 = s1;
    }

    @Override
    public void run() {
        try (
            ObjectOutputStream out = new ObjectOutputStream(s1.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(s1.getInputStream());
        ) {
            // (1) Receber login e senha
            String login = (String) in.readObject();
            String senha = (String) in.readObject();

            // (2) Validar usuário
            Usuario usuario = ctrlUsu.findUsuario(login, senha);

            if (usuario == null) {
                // Usuário inválido → encerrar conexão
                LOGGER.log(Level.INFO, "Login inválido: {0}", login);
                s1.close();
                return;
            }

            LOGGER.log(Level.INFO, "Usuário autenticado: {0}", login);

            // (3) Loop de comandos
            boolean running = true;
            while (running) {
                String comando = (String) in.readObject();

                if (comando == null) {
                    running = false;
                    continue;
                }

                switch (comando) {
                    case "L" -> {
                        // Enviar lista de produtos
                        List<Produto> produtos = ctrl.findProdutoEntities();
                        out.writeObject(produtos);
                        out.flush();
                        LOGGER.log(Level.INFO, "Lista de produtos enviada para: {0}", login);
                    }
                    default -> {
                        // Comando não reconhecido → encerrar conexão
                        running = false;
                    }
                }
            }

            // (4) Fechar conexão
            s1.close();
            LOGGER.log(Level.INFO, "Conexão encerrada para usuário: {0}", login);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro na thread de atendimento", e);
        }
    }
}
