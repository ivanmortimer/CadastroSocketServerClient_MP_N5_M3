
# Missão Prática | Nível 5 | Mundo 3  
## 1º Procedimento | Criando o Servidor e Cliente de Teste

**Disciplina:** Nível 5: Por que não paralelizar? (RPG0018)  
**Curso:** Desenvolvimento Full Stack  
**Universidade:** Universidade Estácio de Sá  
**Campus:** Estácio - Castelo - Belo Horizonte - MG  
**Turma:** 9002  
**Semestre Letivo:** 2025.1  
**Aluno:** Ivan de Ávila Carvalho Fleury Mortimer  

---

## Objetivo da Prática

- Criar um servidor Java baseado em Socket, com atendimento concorrente a clientes utilizando Threads.
- Implementar um cliente de teste capaz de se conectar ao servidor, autenticar-se e enviar comandos.
- Garantir o acesso ao banco de dados SQL Server através de JPA, com isolamento do cliente quanto ao banco de dados.
- Demonstrar habilidades básicas no uso de Threads em ambientes cliente/servidor.

---

## Estrutura do Projeto

### CadastroServer

- Implementado como aplicação console (Ant padrão).
- Classes principais:
    - `CadastroServerMain` → classe principal (main).
    - `CadastroThread` → Thread de atendimento a cada cliente.
    - `controller` → pacotes com os JPA Controllers.
    - `model` → entidades JPA geradas a partir do banco de dados `loja`.

- Funcionalidade:
    - Autentica o usuário.
    - Atende a comando `"L"` retornando a lista de produtos.

### CadastroClient

- Implementado como aplicação console (Ant padrão).
- Classes principais:
    - `CadastroClientMain` → classe principal (main).

- Funcionalidade:
    - Conecta-se ao servidor.
    - Envia `login` e `senha`.
    - Envia comando `"L"`.
    - Recebe e exibe a lista de produtos.

---

## Banco de Dados

- Banco: `loja`
- SGBD: Microsoft SQL Server (executado em container Docker no macOS).
- Entidades utilizadas:
    - `Produto`
    - `Usuario`
    - `Movimento`
    - `Pessoa`
    - `PessoaFisica`
    - `PessoaJuridica`

---

## Execução

### Passos:

1. Executar `CadastroServerMain` → aguardar mensagem `CadastroServer iniciado na porta 4321.`
2. Executar `CadastroClientMain`.

### Saída esperada do cliente:

```
run:
Usuario conectado com sucesso
Banana
Laranja
Manga
BUILD SUCCESSFUL (total time: X seconds)
```

---

## Observações

- O cliente **não acessa diretamente** o banco de dados.
- As entidades JPA são utilizadas no cliente apenas para transporte de dados (objetos serializados).
- O servidor mantém total controle da persistência via `EntityManagerFactory`.

---

## Requisitos atendidos

✅ Código organizado  
✅ Uso de funcionalidades do NetBeans (geração de entidades, controllers, organização dos pacotes)  
✅ Uso prático de Threads em ambientes cliente/servidor

---

## Licença

Este repositório foi criado para fins educacionais como parte da disciplina **Nível 5: Por que não paralelizar? (RPG0018)** — Universidade Estácio de Sá.

---
