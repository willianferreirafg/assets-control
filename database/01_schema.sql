/*
Descrição: Queries para criação das tabelas no BD
Autor: Willian Ferreira Gonçalves
 */

CREATE TABLE unidade (
                         id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                         nome VARCHAR(100) NOT NULL,
                         codigo VARCHAR(30) NOT NULL UNIQUE,
                         ativo BOOLEAN NOT NULL DEFAULT TRUE,
                         criado_em TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE setor (
                       id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                       unidade_id BIGINT NOT NULL REFERENCES unidade(id),
                       nome VARCHAR(100) NOT NULL,
                       codigo VARCHAR(30),
                       ativo BOOLEAN NOT NULL DEFAULT TRUE,
                       criado_em TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                       CONSTRAINT uq_setor_unidade_nome
                           UNIQUE (unidade_id, nome)
);

CREATE TABLE equipamento (
                             id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                             patrimonio VARCHAR(50) NOT NULL UNIQUE,
                             categoria VARCHAR(50) NOT NULL,
                             marca VARCHAR(80),
                             modelo VARCHAR(100),
                             numero_serie VARCHAR(100) UNIQUE,
                             status VARCHAR(20) NOT NULL DEFAULT 'DISPONIVEL',
                             setor_id BIGINT REFERENCES setor(id),
                             observacoes TEXT,
                             criado_em TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             atualizado_em TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                             CONSTRAINT ck_equipamento_status
                                 CHECK (
                                     status IN (
                                                'DISPONIVEL',
                                                'EM_USO',
                                                'EM_MANUTENCAO',
                                                'BAIXADO'
                                         )
                                     )
);