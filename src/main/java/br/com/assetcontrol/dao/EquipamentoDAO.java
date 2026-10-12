package br.com.assetcontrol.dao;

import br.com.assetcontrol.config.DatabaseConnection;
import br.com.assetcontrol.model.Equipamento;
import br.com.assetcontrol.model.Setor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EquipamentoDAO {

    // ============================================================
    // LISTAGEM DE EQUIPAMENTOS
    // ============================================================
    public List<Equipamento> listarTodos() throws SQLException {
        String sql = """
                SELECT
                    e.id,
                    e.patrimonio,
                    e.categoria,
                    e.marca,
                    e.modelo,
                    e.numero_serie,
                    e.status,
                    e.setor_id,
                    s.nome AS setor_nome,
                    e.observacoes
                FROM equipamento e
                LEFT JOIN setor s ON s.id = e.setor_id
                ORDER BY e.id DESC
                """;

        List<Equipamento> equipamentos = new ArrayList<>();

        try (
                Connection connection =
                        DatabaseConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                Equipamento equipamento = new Equipamento();

                equipamento.setId(resultSet.getLong("id"));
                equipamento.setPatrimonio(
                        resultSet.getString("patrimonio"));
                equipamento.setCategoria(
                        resultSet.getString("categoria"));
                equipamento.setMarca(
                        resultSet.getString("marca"));
                equipamento.setModelo(
                        resultSet.getString("modelo"));
                equipamento.setNumeroSerie(
                        resultSet.getString("numero_serie"));
                equipamento.setStatus(
                        resultSet.getString("status"));

                long setorId = resultSet.getLong("setor_id");

                if (!resultSet.wasNull()) {
                    equipamento.setSetorId(setorId);
                }

                equipamento.setSetorNome(
                        resultSet.getString("setor_nome"));
                equipamento.setObservacoes(
                        resultSet.getString("observacoes"));

                equipamentos.add(equipamento);
            }
        }

        return equipamentos;
    }

    // ============================================================
    // CADASTRO DE EQUIPAMENTOS
    // ============================================================
    public void cadastrar(Equipamento equipamento)
            throws SQLException {

        String sql = """
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
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(
                                sql,
                                Statement.RETURN_GENERATED_KEYS
                        )
        ) {
            statement.setString(1, equipamento.getPatrimonio());
            statement.setString(2, equipamento.getCategoria());
            statement.setString(3, equipamento.getMarca());
            statement.setString(4, equipamento.getModelo());
            statement.setString(5, equipamento.getNumeroSerie());

            String status = equipamento.getStatus();

            statement.setString(
                    6,
                    status == null || status.isBlank()
                            ? "DISPONIVEL"
                            : status
            );

            if (equipamento.getSetorId() == null) {
                statement.setNull(7, Types.BIGINT);
            } else {
                statement.setLong(7, equipamento.getSetorId());
            }

            statement.setString(8, equipamento.getObservacoes());

            int linhasAfetadas = statement.executeUpdate();

            if (linhasAfetadas != 1) {
                throw new SQLException(
                        "Não foi possível cadastrar o equipamento."
                );
            }

            try (ResultSet generatedKeys =
                         statement.getGeneratedKeys()) {

                if (generatedKeys.next()) {
                    equipamento.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException(
                            "O banco não retornou o ID gerado."
                    );
                }
            }
        }
    }

    // ============================================================
    // LISTAGEM DE SETORES ONDE ESTÃO OS EQUIPAMENTOS
    // ============================================================
    public List<Setor> listarSetores() throws SQLException {
        String sql = """
            SELECT id, nome, unidade_id
            FROM setor
            WHERE ativo = TRUE
            ORDER BY nome
            """;

        List<Setor> setores = new ArrayList<>();

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                Setor setor = new Setor();
                setor.setId(resultSet.getLong("id"));
                setor.setNome(resultSet.getString("nome"));
                setor.setUnidadeId(resultSet.getLong("unidade_id"));
                setores.add(setor);
            }
        }

        return setores;
    }

    // ============================================================
    // PESQUISA DE EQUIPAMENTOS
    // ============================================================
    public List<Equipamento> buscar(String termo) throws SQLException {
        String sql = """
            SELECT
                e.id,
                e.patrimonio,
                e.categoria,
                e.marca,
                e.modelo,
                e.numero_serie,
                e.status,
                e.setor_id,
                s.nome AS setor_nome,
                e.observacoes
            FROM equipamento e
            LEFT JOIN setor s ON s.id = e.setor_id
            WHERE
                LOWER(e.patrimonio) LIKE LOWER(?)
                OR LOWER(e.categoria) LIKE LOWER(?)
                OR LOWER(COALESCE(e.marca, '')) LIKE LOWER(?)
                OR LOWER(COALESCE(e.modelo, '')) LIKE LOWER(?)
            ORDER BY e.id DESC
            """;

        String filtro = "%" + termo.trim() + "%";

        List<Equipamento> equipamentos = new ArrayList<>();

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            for (int i = 1; i <= 4; i++) {
                statement.setString(i, filtro);
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Equipamento equipamento = new Equipamento();

                    equipamento.setId(resultSet.getLong("id"));
                    equipamento.setPatrimonio(
                            resultSet.getString("patrimonio"));
                    equipamento.setCategoria(
                            resultSet.getString("categoria"));
                    equipamento.setMarca(
                            resultSet.getString("marca"));
                    equipamento.setModelo(
                            resultSet.getString("modelo"));
                    equipamento.setNumeroSerie(
                            resultSet.getString("numero_serie"));
                    equipamento.setStatus(
                            resultSet.getString("status"));

                    long setorId = resultSet.getLong("setor_id");
                    if (!resultSet.wasNull()) {
                        equipamento.setSetorId(setorId);
                    }

                    equipamento.setSetorNome(
                            resultSet.getString("setor_nome"));
                    equipamento.setObservacoes(
                            resultSet.getString("observacoes"));

                    equipamentos.add(equipamento);
                }
            }
        }

        return equipamentos;
    }

    // ============================================================
    // EDIÇÃO DE EQUIPAMENTOS
    // ============================================================
    public Optional<Equipamento> buscarPorId(long id)
            throws SQLException {

        String sql = """
            SELECT
                e.id,
                e.patrimonio,
                e.categoria,
                e.marca,
                e.modelo,
                e.numero_serie,
                e.status,
                e.setor_id,
                s.nome AS setor_nome,
                e.observacoes
            FROM equipamento e
            LEFT JOIN setor s ON s.id = e.setor_id
            WHERE e.id = ?
            """;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }

                Equipamento equipamento = new Equipamento();
                equipamento.setId(resultSet.getLong("id"));
                equipamento.setPatrimonio(
                        resultSet.getString("patrimonio"));
                equipamento.setCategoria(
                        resultSet.getString("categoria"));
                equipamento.setMarca(
                        resultSet.getString("marca"));
                equipamento.setModelo(
                        resultSet.getString("modelo"));
                equipamento.setNumeroSerie(
                        resultSet.getString("numero_serie"));
                equipamento.setStatus(
                        resultSet.getString("status"));
                equipamento.setObservacoes(
                        resultSet.getString("observacoes"));
                equipamento.setSetorNome(
                        resultSet.getString("setor_nome"));

                long setorId = resultSet.getLong("setor_id");
                if (!resultSet.wasNull()) {
                    equipamento.setSetorId(setorId);
                }

                return Optional.of(equipamento);
            }
        }
    }

    // ============================================================
    // ATUALIZAÇÃO DE EQUIPAMENTOS APÓS EDIÇÃO
    // ============================================================
    public boolean atualizar(Equipamento equipamento)
            throws SQLException {

        String sql = """
            UPDATE equipamento
            SET patrimonio = ?,
                categoria = ?,
                marca = ?,
                modelo = ?,
                numero_serie = ?,
                status = ?,
                setor_id = ?,
                observacoes = ?,
                atualizado_em = CURRENT_TIMESTAMP
            WHERE id = ?
            """;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, equipamento.getPatrimonio());
            statement.setString(2, equipamento.getCategoria());
            statement.setString(3, equipamento.getMarca());
            statement.setString(4, equipamento.getModelo());
            statement.setString(5, equipamento.getNumeroSerie());
            statement.setString(6, equipamento.getStatus());

            if (equipamento.getSetorId() == null) {
                statement.setNull(7, Types.BIGINT);
            } else {
                statement.setLong(7, equipamento.getSetorId());
            }

            statement.setString(8, equipamento.getObservacoes());
            statement.setLong(9, equipamento.getId());

            return statement.executeUpdate() == 1;
        }
    }
}
