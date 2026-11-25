package br.com.avalia.dao.avaliacao;

import br.com.avalia.modelo.avaliacao.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;
import javax.xml.parsers.DocumentBuilderFactory;
import org.hibernate.Session;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/** Testa gravações reais em uma transação revertida ao final. */
public final class ValidarCadastroInstrumento {
    public static void main(String[] args) throws Exception {
        var registry = new StandardServiceRegistryBuilder()
                .applySetting("hibernate.connection.url", "jdbc:postgresql://localhost:5432/avalia?connectTimeout=10&socketTimeout=30")
                .applySetting("hibernate.connection.username", "postgres")
                .applySetting("hibernate.connection.password", "postgres")
                .applySetting("hibernate.hbm2ddl.auto", "validate").build();
        try {
            var sources = new MetadataSources(registry);
            var parser = DocumentBuilderFactory.newInstance(); parser.setNamespaceAware(true);
            parser.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            try (var xml = ValidarCadastroInstrumento.class.getClassLoader().getResourceAsStream("META-INF/persistence.xml")) {
                var nodes = parser.newDocumentBuilder().parse(xml).getElementsByTagNameNS("https://jakarta.ee/xml/ns/persistence", "class");
                for (int i=0;i<nodes.getLength();i++) sources.addAnnotatedClass(Class.forName(nodes.item(i).getTextContent().strip()));
            }
            try (var factory = sources.buildMetadata().buildSessionFactory(); var session = factory.openSession()) {
                long totalAntes = session.createQuery("select count(i) from InstrumentoAvaliativo i", Long.class).getSingleResult();
                var tx = session.beginTransaction();
                UUID id;
                try {
                    var dao = new DAOTeste(session);
                    var dados = novo(dao.tiposAvaliados().getFirst().getId());
                    var instrumento = dao.gravar(dados, "RASCUNHO", null); id = instrumento.getId();
                    check(id!=null && dados.getId()==null, "ID deve ser atribuído só ao resultado persistido");
                    session.clear();
                    var salvo = dao.buscar(id);
                    check(salvo!=null && salvo.getStatus().getCodigo().equals("RASCUNHO"), "Gravação de instrumento");
                    check(salvo.getCriadoEm()!=null && salvo.getAtualizadoEm()!=null, "Auditoria gerada");
                    session.detach(salvo); salvo.setTitulo("[TESTE ROLLBACK] Editado");
                    dao.gravar(salvo, "ENCERRADO", null); session.clear();
                    check(dao.buscar(id).getTitulo().endsWith("Editado"), "Atualização do instrumento");
                    var sessao = new InstrumentoSessao(); sessao.setTitulo("Sessão de teste"); sessao.setOrdem(1); sessao.setTipo("SESSAO");
                    invalido(() -> dao.gravarSessao(null, sessao));
                    var salva = dao.gravarSessao(id, sessao);
                    UUID sessaoId = salva.getId(); session.clear();
                    check(dao.sessoes(id).size()==1, "Sessão vinculada ao instrumento");
                    var questao = new SessaoQuestao(); var q = new Questao(); q.setEnunciado("Como você avalia?"); q.setTipo("DESCRITIVA");
                    questao.setQuestao(q); questao.setOrdem(1); questao.setObrigatoria(true); questao.setPeso(new BigDecimal("1.50"));
                    invalido(() -> dao.gravarQuestao(id, null, questao, null));
                    var outra = dao.gravar(novo(dados.getTipoEntidadeAvaliadaId()), "RASCUNHO", null);
                    invalido(() -> dao.gravarQuestao(outra.getId(), sessaoId, questao, null));
                    var vinculada = dao.gravarQuestao(id, sessaoId, questao, null);
                    UUID vinculoId = vinculada.getId(); session.clear();
                    var lista = dao.questoes(id, sessaoId);
                    check(dao.questoesDoInstrumento(id).size()==1
                            && dao.questoesDoInstrumento(id).getFirst().getInstrumentoSessao().getId().equals(sessaoId),
                            "Visão completa deve agrupar a questão na sessão correta");
                    check(dao.questoesDoInstrumento(outra.getId()).isEmpty(), "Visão completa deve isolar os instrumentos");
                    check(lista.size()==1 && lista.getFirst().getQuestao().getCriadoPor().equals("validacao_rollback"), "Questão e vínculo gravados atomicamente");
                    var editar = new SessaoQuestao(); editar.setSessaoQuestaoId(vinculoId); editar.setQuestao(new Questao());
                    editar.getQuestao().setTipo("DESCRITIVA"); editar.getQuestao().setEnunciado("Questão editada");
                    editar.setOrdem(2); editar.setObrigatoria(false);
                    dao.gravarQuestao(id, sessaoId, editar, null); session.clear();
                    check(dao.questoes(id, sessaoId).getFirst().getQuestao().getEnunciado().equals("Questão editada"), "Edição da questão");
                    var periodoInvalido = novo(dados.getTipoEntidadeAvaliadaId()); periodoInvalido.setFimAplicacao(periodoInvalido.getInicioAplicacao());
                    invalido(() -> dao.gravar(periodoInvalido, "RASCUNHO", null));
                    invalido(() -> dao.gravar(novo(dados.getTipoEntidadeAvaliadaId()), "INEXISTENTE", null));
                    System.out.println("Cadastro, edição, auditoria, sessões, questões, validações e vínculos OK.");
                } finally { tx.rollback(); }
                session.clear();
                check(totalAntes==session.createQuery("select count(i) from InstrumentoAvaliativo i", Long.class).getSingleResult(), "Rollback deve preservar os dados existentes");
                System.out.println("Rollback confirmado: nenhum instrumento de teste mantido na base.");
            }
        } finally { StandardServiceRegistryBuilder.destroy(registry); }
    }

    private static InstrumentoAvaliativo novo(UUID tipo) {
        var i = new InstrumentoAvaliativo(); i.setTitulo("[TESTE ROLLBACK] Cadastro"); i.setCicloReferencia("2026.2");
        i.setCriadoPor("validacao_rollback"); i.setAnonimo(true); i.setPermiteEdicao(true); i.setTipoEntidadeAvaliadaId(tipo);
        i.setInicioAplicacao(OffsetDateTime.now()); i.setFimAplicacao(OffsetDateTime.now().plusDays(1)); return i;
    }
    private static void check(boolean valor, String mensagem) { if (!valor) throw new AssertionError(mensagem); }
    private static void invalido(Runnable acao) { try { acao.run(); } catch (IllegalArgumentException esperado) { return; } throw new AssertionError("Operação inválida deve ser recusada"); }
    private static final class DAOTeste extends InstrumentoCadastroDAO { DAOTeste(Session s) { entityManager=s; } }
}
