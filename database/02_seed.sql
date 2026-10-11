/*
Descrição: Inserção de dados nas tabelas do BD
Autor: Willian Ferreira Gonçalves
 */

INSERT INTO unidade (nome, codigo)
VALUES ('Matriz', 'MATRIZ');

INSERT INTO setor (unidade_id, nome, codigo)
SELECT id, 'Infraestrutura de TI', 'TI'
FROM unidade
WHERE codigo = 'MATRIZ';

INSERT INTO setor (unidade_id, nome, codigo)
SELECT id, 'Financeiro', 'FIN'
FROM unidade
WHERE codigo = 'MATRIZ';

INSERT INTO equipamento (
    patrimonio,
    categoria,
    marca,
    modelo,
    numero_serie,
    status,
    setor_id,
    observacoes
)
SELECT
    'PAT-0001',
    'Notebook',
    'Dell',
    'Latitude',
    'SERIE-TESTE-001',
    'EM_USO',
    s.id,
    'Equipamento inicial para teste do sistema'
FROM setor s
WHERE s.codigo = 'TI'
  AND s.unidade_id = (
    SELECT id FROM unidade WHERE codigo = 'MATRIZ'
);