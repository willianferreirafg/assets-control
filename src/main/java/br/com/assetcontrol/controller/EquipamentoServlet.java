package br.com.assetcontrol.controller;

import br.com.assetcontrol.dao.EquipamentoDAO;
import br.com.assetcontrol.model.Equipamento;
import br.com.assetcontrol.model.Setor;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

@WebServlet("/equipamentos")
public class EquipamentoServlet extends HttpServlet {

    private final EquipamentoDAO equipamentoDAO =
            new EquipamentoDAO();

    private static final Set<String> STATUS_VALIDOS = Set.of(
            "DISPONIVEL",
            "EM_USO",
            "EM_MANUTENCAO",
            "BAIXADO"
    );

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        System.out.println(
                "### EQUIPAMENTO SERVLET ATUALIZADO ###"
        );

        String termo = request.getParameter("busca");

        try {
            List<Equipamento> equipamentos;

            if (termo == null || termo.isBlank()) {
                equipamentos = equipamentoDAO.listarTodos();
            } else {
                equipamentos = equipamentoDAO.buscar(termo);
            }

            List<Setor> setores = equipamentoDAO.listarSetores();

            request.setAttribute("equipamentos", equipamentos);
            request.setAttribute("setores", setores);
            request.setAttribute("busca", termo == null ? "" : termo);
            request.setAttribute(
                    "sucesso",
                    "1".equals(request.getParameter("sucesso"))
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/equipamentos.jsp"
            ).forward(request, response);

        } catch (SQLException e) {
            throw new ServletException(
                    "Erro ao consultar os equipamentos.", e
            );
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String patrimonio = valor(request, "patrimonio");
        String categoria = valor(request, "categoria");
        String marca = valor(request, "marca");
        String modelo = valor(request, "modelo");
        String numeroSerie = valor(request, "numeroSerie");
        String status = valor(request, "status");
        String setorIdTexto = valor(request, "setorId");
        String observacoes = valor(request, "observacoes");

        if (patrimonio.isBlank() || categoria.isBlank()) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Patrimônio e categoria são obrigatórios."
            );
            return;
        }

        if (patrimonio.length() > 50 || categoria.length() > 50
                || marca.length() > 80 || modelo.length() > 100
                || numeroSerie.length() > 100) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Um ou mais campos excedem o tamanho permitido."
            );
            return;
        }

        if (status.isBlank()) {
            status = "DISPONIVEL";
        }

        if (!STATUS_VALIDOS.contains(status)) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Status inválido."
            );
            return;
        }

        Long setorId = null;

        if (!setorIdTexto.isBlank()) {
            try {
                setorId = Long.parseLong(setorIdTexto);

                if (setorId <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "O setor informado é inválido."
                );
                return;
            }
        }

        Equipamento equipamento = new Equipamento();
        equipamento.setPatrimonio(patrimonio);
        equipamento.setCategoria(categoria);
        equipamento.setMarca(nuloSeVazio(marca));
        equipamento.setModelo(nuloSeVazio(modelo));
        equipamento.setNumeroSerie(nuloSeVazio(numeroSerie));
        equipamento.setStatus(status);
        equipamento.setSetorId(setorId);
        equipamento.setObservacoes(nuloSeVazio(observacoes));

        try {
            equipamentoDAO.cadastrar(equipamento);

            response.sendRedirect(
                    request.getContextPath()
                            + "/equipamentos?sucesso=1"
            );

        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                response.sendError(
                        HttpServletResponse.SC_CONFLICT,
                        "Já existe um equipamento com esse patrimônio "
                                + "ou número de série."
                );
                return;
            }

            if ("23503".equals(e.getSQLState())) {
                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "O setor informado não existe."
                );
                return;
            }

            throw new ServletException(
                    "Erro ao cadastrar o equipamento.", e
            );
        }
    }

    private String valor(
            HttpServletRequest request,
            String nome
    ) {
        String valor = request.getParameter(nome);
        return valor == null ? "" : valor.trim();
    }

    private String nuloSeVazio(String valor) {
        return valor == null || valor.isBlank() ? null : valor;
    }
}
