package br.com.avalia.dao.avaliacao;

import br.com.avalia.modelo.avaliacao.InstrumentoAvaliativo;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import org.hibernate.Session;

/** Verificação de paginação real, usando a transação somente leitura do validador. */
public final class VerificarPaginacaoInstrumento {

    private VerificarPaginacaoInstrumento() {
    }

    public static void validar(Session session) {
        var dao = new DAOTeste(session);
        long total = dao.contar(Map.of());
        long esperado = session.createNativeQuery(
                "select count(*) from avaliacao.instrumento_avaliativo", Long.class).getSingleResult();
        check(total == esperado, "Contagem deve refletir o banco");

        var primeira = dao.listarPagina(0, 2, Map.of(), null, false);
        var segunda = dao.listarPagina(2, 2, Map.of(), null, false);
        check(primeira.size() == Math.min(total, 2), "Limite da primeira página");
        check(segunda.size() == Math.min(Math.max(total - 2, 0), 2), "Offset da segunda página");
        var ids = new ArrayList<UUID>();
        primeira.forEach(item -> ids.add(item.getId()));
        segunda.forEach(item -> ids.add(item.getId()));
        var idsEsperados = session.createNativeQuery("""
                select instrumento_avaliativo_id from avaliacao.instrumento_avaliativo
                order by atualizado_em desc, instrumento_avaliativo_id limit 4
                """, UUID.class).getResultList();
        check(ids.equals(idsEsperados), "Páginas devem preservar a ordem sem repetir registros");

        var ordenados = dao.listarPagina(0, 2, Map.of(), "titulo", true).stream()
                .map(InstrumentoAvaliativo::getId).toList();
        var ordemEsperada = session.createNativeQuery("""
                select instrumento_avaliativo_id from avaliacao.instrumento_avaliativo
                order by titulo, instrumento_avaliativo_id limit 2
                """, UUID.class).getResultList();
        check(ordenados.equals(ordemEsperada), "Ordenação por título deve ser executada no banco");

        for (String literal : new String[]{"%", "_"}) {
            long literalEsperado = session.createNativeQuery("""
                    select count(*) from avaliacao.instrumento_avaliativo
                    where position(:literal in titulo) > 0
                    """, Long.class).setParameter("literal", literal).getSingleResult();
            check(dao.contar(Map.of("titulo", literal)) == literalEsperado,
                    "Caracteres de LIKE devem ser tratados como texto literal");
        }
        String inexistente = "sem-registro-" + UUID.randomUUID();
        check(dao.contar(Map.of("titulo", inexistente)) == 0, "Filtro sem correspondência deve retornar zero");
        check(dao.listarPagina(0, 2, Map.of("titulo", inexistente), null, false).isEmpty(),
                "Contagem e página devem aplicar o mesmo filtro");
        if (!primeira.isEmpty()) {
            String status = primeira.getFirst().getStatus().getCodigo();
            var filtrados = dao.listarPagina(0, 100, Map.of("status", status), null, false);
            check(!filtrados.isEmpty() && filtrados.stream().allMatch(item -> item.getStatus()
                    .getCodigo().equals(status)), "Filtro de status deve usar o código exato");
            long totalStatus = session.createNativeQuery("select count(*) from avaliacao.instrumento_avaliativo where status = :status",
                    Long.class).setParameter("status", status).getSingleResult();
            check(dao.contar(Map.of("status", status)) == totalStatus, "Contagem deve aplicar o status selecionado");
            check(dao.contar(Map.of("status", "RASC")) == 0, "Status não deve aceitar busca parcial");
        }
        var statusDAO = new StatusDAOTeste(session);
        var statusDisponiveis = statusDAO.listarTodos();
        check(!statusDisponiveis.isEmpty(), "Menu deve ter status cadastrados");
        var codigos = statusDisponiveis.stream().map(item -> item.getCodigo()).toList();
        check(codigos.containsAll(java.util.List.of("RASCUNHO", "EM_APLICACAO", "ENCERRADO")),
                "Status existentes devem ser preservados");
        var porStatus = dao.listarPagina(0, 100, Map.of(), "status", true).stream()
                .map(InstrumentoAvaliativo::getId).toList();
        var ordemStatus = session.createNativeQuery("""
                select i.instrumento_avaliativo_id from avaliacao.instrumento_avaliativo i
                join avaliacao.status s on s.codigo = i.status
                order by s.descricao, i.instrumento_avaliativo_id limit 100
                """, UUID.class).getResultList();
        check(porStatus.equals(ordemStatus), "Ordenação de status deve usar a descrição");
        check(primeira.stream().allMatch(item -> org.hibernate.Hibernate.isInitialized(item.getStatus())),
                "Status deve estar carregado para exibição na tela");
        expectInvalid(() -> dao.listarPagina(-1, 10, Map.of(), null, false));
        expectInvalid(() -> dao.listarPagina(0, 101, Map.of(), null, false));
        expectInvalid(() -> dao.listarPagina(0, 10, Map.of(), "campo_inexistente", true));
        expectInvalid(() -> dao.contar(Map.of("campo_inexistente", "valor")));
        System.out.println("Paginação SQL, contagem, ordenação, filtros e limites OK.");
    }

    private static void expectInvalid(Runnable action) {
        try {
            action.run();
        } catch (IllegalArgumentException expected) {
            return;
        }
        throw new AssertionError("A consulta deve rejeitar parâmetros inválidos");
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private static final class DAOTeste extends InstrumentoAvaliativoDAO {
        private DAOTeste(Session session) {
            entityManager = session;
        }
    }

    private static final class StatusDAOTeste extends StatusDAO {
        private StatusDAOTeste(Session session) {
            entityManager = session;
        }
    }
}
