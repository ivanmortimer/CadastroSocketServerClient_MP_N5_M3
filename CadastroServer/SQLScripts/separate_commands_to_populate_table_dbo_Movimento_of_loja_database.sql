-- Reestabelecer a SEED (i.e., semente) o modificador de propriedade IDENTITY da coluna 'idMovimento' da tabela 'dbo.Movimento',
-- para que tenha um valor de 3, de modo que seu valor seja 4, após o próximo comando INSERT na tabela 'dbo.Movimento'
DBCC CHECKIDENT ('dbo.Movimento', RESEED, 3);

-- Refazer a inserção da movimentação "Joao comprando Laranja via usuário 1" com o preço correto do produto de nome 'Laranja',
-- e separadamente das outras inserções de movimentações de compra (cujos valores unitários atribuídos a determinados produtos 
-- não correspondiam aos preços de venda desses produtos que se encontram na tabela 'dbo.Produto')
-- para que o comando INSERT execute normalmente (sem que seja gerado um ROLLBACK TRANSACTION seguido de um RAISERROR)
INSERT INTO dbo.Movimento (idUsuario, idPessoa, idProduto, quantidade, tipo, valorUnitario) VALUES
(1, 7, 3, 15, 'S', 2.00);  -- Joao comprando Laranja via operador (i.e., usuário) 1

-- Reestabelecer a SEED (i.e., semente) o modificador de propriedade IDENTITY da coluna 'idMovimento' da tabela 'dbo.Movimento',
-- para que tenha um valor de 6, de modo que seus valores sejam, respectivamente, 7 e 8, 
-- após os dois próximos comandos INSERT na tabela 'dbo.Movimento'
DBCC CHECKIDENT ('dbo.Movimento', RESEED, 6);

-- Refazer a inserção das movimentações "JJC fornecendo Laranja via usuário) 1" e "JJC fornecendo Manga via operador (i.e., usuário) 1"
-- separadamente das inserções de movimentações de venda (i.e., do tipo 'S'), 
-- para que não seja possível a ocorrência de um ROLLBACK TRANSACTION seguido de um RAISERROR (que podem ocorrer somente em inserções de movimentações do tipo 'S'),
-- de modo que as operações de compra (i.e., do tipo 'E') não corram o risco de não serem efetuadas por meio de um comando INSERT, devido a esse tipo de ocorrência.
INSERT INTO dbo.Movimento (idUsuario, idPessoa, idProduto, quantidade, tipo, valorUnitario) VALUES
(1, 15, 3, 15, 'E', 5.00), -- JJC fornecendo Laranja via operador (i.e, usuário) 1
(1, 15, 4, 20, 'E', 4.00); -- JJC fornecendo Manga via operador (i.e., usuário) 1
