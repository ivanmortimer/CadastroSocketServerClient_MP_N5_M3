/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cadastroserver;

import controller.MovimentoJpaController;
import controller.PessoaJpaController;
import controller.ProdutoJpaController;
import controller.UsuarioJpaController;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Movimento;
import model.Pessoa;
import model.Produto;
import model.Usuario;

/**
 *
 * @author Ivan
 */
public class CadastroThreadV2 extends Thread {

    private static final Logger LOGGER = Logger.getLogger(CadastroThreadV2.class.getName());

    private final ProdutoJpaController ctrlProd;
    private final UsuarioJpaController ctrlUsu;
    private final MovimentoJpaController ctrlMov;
    private final PessoaJpaController ctrlPessoa;
    private final Socket socket;

    public CadastroThreadV2(
            ProdutoJpaController ctrlProd,
            UsuarioJpaController ctrlUsu,
            MovimentoJpaController ctrlMov,
            PessoaJpaController ctrlPessoa,
            Socket socket) {

        this.ctrlProd = ctrlProd;
        this.ctrlUsu = ctrlUsu;
        this.ctrlMov = ctrlMov;
        this.ctrlPessoa = ctrlPessoa;
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());) {

            // (1) Ler login e senha
            String login = (String) in.readObject();
            String senha = (String) in.readObject();

            // (2) Validar usuário
            Usuario usuario = ctrlUsu.findUsuario(login, senha);

            if (usuario == null) {
                out.writeObject("Usuário inválido.");
                out.flush();
                return;
            }

            out.writeObject("Usuario conectado com sucesso");
            out.flush();

            // (3) Loop de atendimento
            while (true) {
                String comando = (String) in.readObject();

                if (comando.equalsIgnoreCase("L")) {
                    // Listar produtos
                    List<Produto> produtos = ctrlProd.findProdutoEntities();
                    // out.writeObject("Lista de produtos:");
                    out.writeObject(produtos);
                    out.flush();
                } else if (comando.equalsIgnoreCase("E") || comando.equalsIgnoreCase("S")) {
                    // Processar entrada ou saída
                    processarMovimento(comando, usuario, in, out);
                } else if (comando.equalsIgnoreCase("X")) {
                    // Encerrar
                    out.writeObject("Conexão encerrada.");
                    out.flush();
                    break;
                }
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro na thread", e);
        }
    }

    private void processarMovimento(
            String tipo, Usuario usuario,
            ObjectInputStream in, ObjectOutputStream out) {

        try {
            // (1) Ler dados
            Integer idPessoa = Integer.valueOf(((String) in.readObject()).trim());
            Integer idProduto = Integer.valueOf(((String) in.readObject()).trim());
            Integer quantidade = Integer.valueOf(((String) in.readObject()).trim());
            Float valorUnitario = Float.valueOf(((String) in.readObject()).trim());

            // (2) Validar pessoa e produto
            Pessoa pessoa = ctrlPessoa.findPessoa(idPessoa);
            Produto produto = ctrlProd.findProduto(idProduto);

            if (pessoa == null) {
                out.writeObject("Pessoa com id " + idPessoa + " não encontrada.");
                out.flush();
                return;
            }

            if (produto == null) {
                out.writeObject("Produto com id " + idProduto + " não encontrado.");
                out.flush();
                return;
            }

            // (3) Criar Movimento
            Movimento mov = new Movimento();
            mov.setIdUsuario(usuario);
            mov.setTipo(tipo.charAt(0)); // CORREÇÃO AQUI
            mov.setIdPessoa(pessoa);
            mov.setIdProduto(produto);
            mov.setQuantidade(quantidade);
            mov.setValorUnitario(new BigDecimal(Float.toString(valorUnitario))); // CORREÇÃO AQUI

            ctrlMov.create(mov);

            // (4) Atualizar quantidade de produto
            int novaQuantidade = produto.getQuantidade();

            if (tipo.equals("E")) {
                novaQuantidade += quantidade;
            } else if (tipo.equals("S")) {
                novaQuantidade -= quantidade;
            }

            produto.setQuantidade(novaQuantidade);
            ctrlProd.edit(produto);

            // (5) Responder sucesso
            out.writeObject("Movimento de tipo " + tipo + " registrado com sucesso.");
            out.flush();

        } catch (IOException | ClassNotFoundException | NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Erro ao processar movimento", e);
            try {
                out.writeObject("Erro ao processar movimento: " + e.getMessage());
                out.flush();
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, "Erro ao enviar mensagem de erro", ex);
            }
        }
    }
}
