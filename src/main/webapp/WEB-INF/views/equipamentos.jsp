<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>AssetControl - Equipamentos</title>

    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 1200px;
            margin: 30px auto;
            padding: 0 20px;
            color: #222;
        }

        form {
            display: grid;
            grid-template-columns: repeat(2, minmax(0, 1fr));
            gap: 12px;
            margin-bottom: 30px;
        }

        label {
            display: flex;
            flex-direction: column;
            gap: 5px;
        }

        input, select, textarea, button {
            padding: 10px;
            font: inherit;
        }

        textarea {
            resize: vertical;
        }

        .full {
            grid-column: 1 / -1;
        }

        table {
            width: 100%;
            border-collapse: collapse;
        }

        th, td {
            padding: 10px;
            border-bottom: 1px solid #ddd;
            text-align: left;
        }

        th {
            background: #f2f4f7;
        }

        .success {
            color: #176b35;
        }

        @media (max-width: 650px) {
            form {
                grid-template-columns: 1fr;
            }

            .full {
                grid-column: auto;
            }

            .table-wrapper {
                overflow-x: auto;
            }
        }
    </style>
</head>

<body>

<header>
    <h1>AssetControl</h1>
    <p>Gestão de equipamentos de TI</p>

    <a href="${pageContext.request.contextPath}/">
        Voltar ao início
    </a>
</header>

<main>

    <c:if test="${sucesso}">
        <p class="success" role="status">
            Equipamento cadastrado com sucesso!
        </p>
    </c:if>

    <h2>
        <c:choose>
            <c:when test="${not empty equipamentoEdicao}">
                Editar equipamento
            </c:when>
            <c:otherwise>
                Cadastrar equipamento
            </c:otherwise>
        </c:choose>
    </h2>

    <form method="post"
          action="${pageContext.request.contextPath}/equipamentos">

        <c:choose>
            <c:when test="${not empty equipamentoEdicao}">
                <input type="hidden" name="acao" value="atualizar">

                <input type="hidden" name="id"
                       value="<c:out value='${equipamentoEdicao.id}'/>">
            </c:when>

            <c:otherwise>
                <input type="hidden" name="acao" value="cadastrar">
            </c:otherwise>
        </c:choose>

        <label>
            Patrimônio *
            <input type="text"
                   name="patrimonio"
                   maxlength="50"
                   required
                   value="<c:out value='${equipamentoEdicao.patrimonio}'/>">
        </label>

        <label>
            Categoria *
            <select name="categoria" required>
                <option value="">Selecione</option>

                <option value="Desktop"
                    ${equipamentoEdicao.categoria == 'Desktop' ? 'selected' : ''}>
                    Desktop
                </option>

                <option value="Notebook"
                    ${equipamentoEdicao.categoria == 'Notebook' ? 'selected' : ''}>
                    Notebook
                </option>

                <option value="Monitor"
                    ${equipamentoEdicao.categoria == 'Monitor' ? 'selected' : ''}>
                    Monitor
                </option>

                <option value="Impressora"
                    ${equipamentoEdicao.categoria == 'Impressora' ? 'selected' : ''}>
                    Impressora
                </option>

                <option value="Celular"
                    ${equipamentoEdicao.categoria == 'Celular' ? 'selected' : ''}>
                    Celular
                </option>

                <option value="Roteador"
                    ${equipamentoEdicao.categoria == 'Roteador' ? 'selected' : ''}>
                    Roteador
                </option>

                <option value="Switch"
                    ${equipamentoEdicao.categoria == 'Switch' ? 'selected' : ''}>
                    Switch
                </option>

                <option value="Outro"
                    ${equipamentoEdicao.categoria == 'Outro' ? 'selected' : ''}>
                    Outro
                </option>
            </select>
        </label>

        <label>
            Marca
            <input type="text"
                   name="marca"
                   maxlength="80"
                   value="<c:out value='${equipamentoEdicao.marca}'/>">
        </label>

        <label>
            Modelo
            <input type="text"
                   name="modelo"
                   maxlength="100"
                   value="<c:out value='${equipamentoEdicao.modelo}'/>">
        </label>

        <label>
            Número de série
            <input type="text"
                   name="numeroSerie"
                   maxlength="100"
                   value="<c:out value='${equipamentoEdicao.numeroSerie}'/>">
        </label>

        <label>
            Status
            <select name="status" required>

                <option value="DISPONIVEL"
                    ${equipamentoEdicao.status == 'DISPONIVEL' ? 'selected' : ''}>
                    Disponível
                </option>

                <option value="EM_USO"
                    ${equipamentoEdicao.status == 'EM_USO' ? 'selected' : ''}>
                    Em uso
                </option>

                <option value="EM_MANUTENCAO"
                    ${equipamentoEdicao.status == 'EM_MANUTENCAO' ? 'selected' : ''}>
                    Em manutenção
                </option>

                <option value="BAIXADO"
                    ${equipamentoEdicao.status == 'BAIXADO' ? 'selected' : ''}>
                    Baixado
                </option>

            </select>
        </label>

        <label>
            Setor
            <select name="setorId">

                <option value=""
                    ${empty equipamentoEdicao.setorId ? 'selected' : ''}>
                    Sem setor definido
                </option>

                <c:forEach var="setor" items="${setores}">
                    <option value="${setor.id}"
                        ${equipamentoEdicao.setorId == setor.id ? 'selected' : ''}>
                        <c:out value="${setor.nome}"/>
                    </option>
                </c:forEach>

            </select>
        </label>

        <label class="full">
            Observações
            <textarea name="observacoes"
                      rows="3"><c:out value="${equipamentoEdicao.observacoes}"/></textarea>
        </label>

        <div class="full">
            <button type="submit">
                <c:choose>
                    <c:when test="${not empty equipamentoEdicao}">
                        Salvar alterações
                    </c:when>

                    <c:otherwise>
                        Cadastrar equipamento
                    </c:otherwise>
                </c:choose>
            </button>

            <c:if test="${not empty equipamentoEdicao}">
                <a href="${pageContext.request.contextPath}/equipamentos">
                    Cancelar edição
                </a>
            </c:if>
        </div>

    </form>

    <h2>Equipamentos cadastrados</h2>

    <form method="get"
          action="${pageContext.request.contextPath}/equipamentos">

        <label class="full">
            Pesquisar por patrimônio, categoria, marca ou modelo

            <input
                type="search"
                name="busca"
                value="<c:out value='${busca}'/>"
                placeholder="Ex.: PAT-0001, Dell ou Notebook">
        </label>

        <div>
            <button type="submit">Pesquisar</button>

            <a href="${pageContext.request.contextPath}/equipamentos">
                Limpar pesquisa
            </a>
        </div>

    </form>

    <div class="table-wrapper">

        <table>
            <thead>
                <tr>
                    <th>Patrimônio</th>
                    <th>Categoria</th>
                    <th>Marca</th>
                    <th>Modelo</th>
                    <th>Status</th>
                    <th>Setor</th>
                    <th>Ações</th>
                </tr>
            </thead>

            <tbody>

                <c:forEach var="equipamento" items="${equipamentos}">
                    <tr>
                        <td>
                            <c:out value="${equipamento.patrimonio}"/>
                        </td>

                        <td>
                            <c:out value="${equipamento.categoria}"/>
                        </td>

                        <td>
                            <c:out value="${equipamento.marca}"/>
                        </td>

                        <td>
                            <c:out value="${equipamento.modelo}"/>
                        </td>

                        <td>
                            <c:out value="${equipamento.status}"/>
                        </td>

                        <td>
                            <c:out value="${equipamento.setorNome}"
                                   default="Sem setor"/>
                        </td>

                        <td>
                            <a href="${pageContext.request.contextPath}/equipamentos?editar=${equipamento.id}">
                                Editar
                            </a>
                        </td>
                    </tr>
                </c:forEach>

                <c:if test="${empty equipamentos}">
                    <tr>
                        <td colspan="7">
                            Nenhum equipamento cadastrado.
                        </td>
                    </tr>
                </c:if>

            </tbody>
        </table>

    </div>

</main>

</body>
</html>