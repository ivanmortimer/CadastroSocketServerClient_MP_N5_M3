SELECT 
    name,
    current_value,
    start_value,
    increment,
    minimum_value,
    maximum_value,
    is_cycling,
    cache_size
FROM sys.sequences
WHERE name = 'Seq_idPessoa' AND SCHEMA_NAME(schema_id) = 'dbo';
