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

    <h2>Cadastrar equipamento</h2>

    <form method="post"
          action="${pageContext.request.contextPath}/equipamentos">

        <label>
            Patrimônio *
            <input type="text" name="patrimonio"
                   maxlength="50" required>
        </label>

        <label>
            Categoria *
            <select name="categoria" required>
                <option value="">Selecione</option>
                <option value="Desktop">Desktop</option>
                <option value="Notebook">Notebook</option>
                <option value="Monitor">Monitor</option>
                <option value="Impressora">Impressora</option>
                <option value="Celular">Celular</option>
                <option value="Roteador">Roteador</option>
                <option value="Switch">Switch</option>
                <option value="Outro">Outro</option>
            </select>
        </label>

        <label>
            Marca
            <input type="text" name="marca" maxlength="80">
        </label>

        <label>
            Modelo
            <input type="text" name="modelo" maxlength="100">
        </label>

        <label>
            Número de série
            <input type="text" name="numeroSerie" maxlength="100">
        </label>

        <label>
            Status
            <select name="status" required>
                <option value="DISPONIVEL">Disponível</option>
                <option value="EM_USO">Em uso</option>
                <option value="EM_MANUTENCAO">Em manutenção</option>
                <option value="BAIXADO">Baixado</option>
            </select>
        </label>

        <label>
            Setor
            <select name="setorId">
                <option value="">Sem setor definido</option>
                <c:forEach var="setor" items="${setores}">
                    <option value="${setor.id}">
                        <c:out value="${setor.nome}"/>
                    </option>
                </c:forEach>
            </select>
        </label>

        <label class="full">
            Observações
            <textarea name="observacoes" rows="3"></textarea>
        </label>

        <div class="full">
            <button type="submit">Cadastrar equipamento</button>
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
                placeholder="Ex.: PAT-0001, Dell ou Notebook"
            >
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
                </tr>
            </thead>
            <tbody>
                <c:forEach var="equipamento" items="${equipamentos}">
                    <tr>
                        <td><c:out value="${equipamento.patrimonio}"/></td>
                        <td><c:out value="${equipamento.categoria}"/></td>
                        <td><c:out value="${equipamento.marca}"/></td>
                        <td><c:out value="${equipamento.modelo}"/></td>
                        <td><c:out value="${equipamento.status}"/></td>
                        <td>
                            <c:out value="${equipamento.setorNome}"
                                   default="Sem setor"/>
                        </td>
                    </tr>
                </c:forEach>

                <c:if test="${empty equipamentos}">
                    <tr>
                        <td colspan="6">Nenhum equipamento cadastrado.</td>
                    </tr>
                </c:if>
            </tbody>
        </table>
    </div>
</main>

</body>
</html>