/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package cadastroclient;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ivan
 */
public class CadastroClientV2Main {

    private static final Logger LOGGER = Logger.getLogger(CadastroClientV2Main.class.getName());

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try (
            Socket socket = new Socket("localhost", 4321);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
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

            // Criar a janela de saída
            SaidaFrame frame = new SaidaFrame();

            // Iniciar a ThreadClient para receber mensagens de forma assíncrona
            ThreadClient threadClient = new ThreadClient(in, frame);
            threadClient.start();

            // Loop do menu
            String comando = "";
            while (!comando.equalsIgnoreCase("X")) {
                // Exibir menu
                System.out.println("\nMENU:");
                System.out.println("L - Listar produtos");
                System.out.println("E - Entrada de produtos");
                System.out.println("S - Saída de produtos");
                System.out.println("X - Finalizar");
                System.out.print("Digite a opção desejada: ");

                // Ler comando
                comando = teclado.readLine().trim().toUpperCase();

                // Validar comando
                if (!(comando.equals("L") || comando.equals("E") || comando.equals("S") || comando.equals("X"))) {
                    System.out.println("Opção inválida. As opções válidas são: L (Listar), E (Entrada), S (Saída), X (Finalizar).");
                    continue; // volta ao menu
                }

                // Enviar comando
                out.writeObject(comando);
                out.flush();

                if (comando.equals("E") || comando.equals("S")) {
                    // Entrada ou saída → ler dados e enviar

                    // Id da pessoa
                    Integer idPessoa = lerInteiroPositivo("Digite o Id da Pessoa: ", teclado);
                    out.writeObject(idPessoa.toString());
                    out.flush();

                    // Id do produto
                    Integer idProduto = lerInteiroPositivo("Digite o Id do Produto: ", teclado);
                    out.writeObject(idProduto.toString());
                    out.flush();

                    // Quantidade
                    Integer quantidade = lerInteiroPositivo("Digite a quantidade: ", teclado);
                    out.writeObject(quantidade.toString());
                    out.flush();

                    // Valor unitário
                    Float valorUnitario = lerFloatPositivo("Digite o valor unitario (use ponto ou vírgula): ", teclado);
                    out.writeObject(valorUnitario.toString());
                    out.flush();
                }

                // No caso do "L" e do "X", já enviamos o comando → a ThreadClient cuida da resposta
            }

            System.out.println("Cliente encerrado.");

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro no cliente V2", e);
        }
    }

    // NOVO → método auxiliar para validar inteiro positivo
    private static Integer lerInteiroPositivo(String prompt, BufferedReader teclado) {
        while (true) {
            try {
                System.out.print(prompt);
                String entrada = teclado.readLine().trim();
                int valor = Integer.parseInt(entrada);
                if (valor < 0) {
                    System.out.println("Valor inválido. Digite um número inteiro positivo.");
                } else {
                    return valor;
                }
            } catch (Exception e) {
                System.out.println("Valor inválido. Digite um número inteiro positivo.");
            }
        }
    }

    // NOVO → método auxiliar para validar float positivo
    private static Float lerFloatPositivo(String prompt, BufferedReader teclado) {
        while (true) {
            try {
                System.out.print(prompt);
                String entrada = teclado.readLine().trim().replace(",", ".");
                float valor = Float.parseFloat(entrada);
                if (valor < 0) {
                    System.out.println("Valor inválido. Digite um número decimal positivo (ex: 5.00).");
                } else {
                    return valor;
                }
            } catch (IOException | NumberFormatException e) {
                System.out.println("Valor inválido. Digite um número decimal positivo (ex: 5.00).");
            }
        }
    }
}
