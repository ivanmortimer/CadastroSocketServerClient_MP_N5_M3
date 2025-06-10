/*
ALTER SEQUENCE dbo.Seq_idPessoa
RESTART WITH 16;
GO
*/

DECLARE @constraint_name NVARCHAR(128);
DECLARE @sql NVARCHAR(300);

SELECT @constraint_name = dc.name
FROM sys.default_constraints dc
JOIN sys.columns c ON c.default_object_id = dc.object_id
WHERE c.object_id = OBJECT_ID('dbo.Pessoa') AND c.name = 'idPessoa';

IF @constraint_name IS NOT NULL
BEGIN
    SET @sql = 'ALTER TABLE dbo.Pessoa DROP CONSTRAINT ' + QUOTENAME(@constraint_name);
    EXEC sp_executesql @sql;
END
GO

DROP SEQUENCE dbo.Seq_idPessoa;
GO

DECLARE @next_id INT;
DECLARE @sql NVARCHAR(400);

-- Step 1: Determine next value
SELECT @next_id = ISNULL(MAX(idPessoa), 0) + 1 FROM dbo.Pessoa;

-- Step 2: Build the CREATE SEQUENCE command
SET @sql = '
CREATE SEQUENCE dbo.Seq_idPessoa AS INT
    START WITH ' + CAST(@next_id AS NVARCHAR) + '
    INCREMENT BY 1;
';

-- Step 3: Execute it
EXEC sp_executesql @sql;
GO

ALTER TABLE dbo.Pessoa
ADD CONSTRAINT DF_Pessoa_idPessoa
DEFAULT NEXT VALUE FOR dbo.Seq_idPessoa FOR idPessoa;
GO
