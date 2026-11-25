package br.com.avalia.dao.avaliacao;

import br.com.avalia.dao.InjectEntityDAO;
import br.com.avalia.modelo.academico.Pessoa;
import br.com.avalia.modelo.avaliacao.*;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@RequestScoped
public class InstrumentoCadastroDAO extends InjectEntityDAO<InstrumentoAvaliativo> {
    public InstrumentoCadastroDAO() { super(InstrumentoAvaliativo.class); }

    public InstrumentoAvaliativo buscar(UUID id) {
        return entityManager.createQuery("select i from InstrumentoAvaliativo i join fetch i.status left join fetch i.pessoa where i.instrumentoAvaliativoId=:id", InstrumentoAvaliativo.class)
                .setParameter("id", id).getResultStream().findFirst().orElse(null);
    }

    public List<TipoEntidade> tiposAvaliados() {
        return entityManager.createQuery("from TipoEntidade where podeSerAvaliada=true order by nome", TipoEntidade.class).getResultList();
    }

    public List<ConjuntoOpcao> conjuntos() {
        return entityManager.createQuery("from ConjuntoOpcao order by nome", ConjuntoOpcao.class).getResultList();
    }

    @Transactional
    public InstrumentoAvaliativo gravar(InstrumentoAvaliativo dados, String codigoStatus, Integer pessoaId) {
        exigirTexto(dados.getTitulo(), 200, "Título");
        exigirTexto(dados.getCicloReferencia(), 20, "Ciclo");
        exigirTexto(dados.getCriadoPor(), 60, "Responsável");
        if (dados.getInicioAplicacao()==null || dados.getFimAplicacao()==null || !dados.getFimAplicacao().isAfter(dados.getInicioAplicacao()))
            throw new IllegalArgumentException("O fim da aplicação deve ser posterior ao início.");
        var tipo = entityManager.find(TipoEntidade.class, dados.getTipoEntidadeAvaliadaId());
        if (tipo==null || !Boolean.TRUE.equals(tipo.getPodeSerAvaliada())) throw new IllegalArgumentException("Selecione um tipo de entidade avaliada válido.");
        var status = entityManager.createQuery("from Status where codigo=:codigo", Status.class).setParameter("codigo", codigoStatus)
                .getResultStream().findFirst().orElseThrow(() -> new IllegalArgumentException("Selecione um status válido."));
        var pessoa = pessoaId==null ? null : entityManager.find(Pessoa.class, pessoaId);
        if (pessoaId!=null && pessoa==null) throw new IllegalArgumentException("Pessoa responsável não encontrada.");
        var destino = dados.getId()==null ? new InstrumentoAvaliativo() : entityManager.find(InstrumentoAvaliativo.class, dados.getId());
        if (destino==null) throw new IllegalArgumentException("Instrumento não encontrado.");
        destino.setTitulo(dados.getTitulo().strip()); destino.setDescricao(dados.getDescricao());
        destino.setCicloReferencia(dados.getCicloReferencia().strip()); destino.setCriadoPor(dados.getCriadoPor().strip());
        destino.setInicioAplicacao(dados.getInicioAplicacao()); destino.setFimAplicacao(dados.getFimAplicacao());
        destino.setConsolidadoEm(dados.getConsolidadoEm()); destino.setTipoEntidadeAvaliadaId(tipo.getId());
        destino.setStatus(status); destino.setPessoa(pessoa);
        destino.setAnonimo(Boolean.TRUE.equals(dados.getAnonimo())); destino.setPermiteEdicao(Boolean.TRUE.equals(dados.getPermiteEdicao()));
        destino.setAtualizadoEm(OffsetDateTime.now());
        if (destino.getId()==null) { destino.setCriadoEm(OffsetDateTime.now()); entityManager.persist(destino); }
        entityManager.flush();
        return destino;
    }

    public List<InstrumentoSessao> sessoes(UUID instrumentoId) {
        return entityManager.createQuery("from InstrumentoSessao s where s.instrumentoAvaliativo.instrumentoAvaliativoId=:id order by s.ordem,s.instrumentoSessaoId", InstrumentoSessao.class).setParameter("id", instrumentoId).getResultList();
    }

    public List<SessaoQuestao> questoes(UUID instrumentoId, UUID sessaoId) {
        return entityManager.createQuery("select s from SessaoQuestao s join fetch s.questao q left join fetch q.conjuntoOpcao where s.instrumentoSessao.instrumentoSessaoId=:sessao and s.instrumentoSessao.instrumentoAvaliativo.instrumentoAvaliativoId=:instrumento order by s.ordem,s.sessaoQuestaoId", SessaoQuestao.class)
                .setParameter("sessao", sessaoId).setParameter("instrumento", instrumentoId).getResultList();
    }

    public List<SessaoQuestao> questoesDoInstrumento(UUID instrumentoId) {
        return entityManager.createQuery("""
                select sq from SessaoQuestao sq
                join fetch sq.instrumentoSessao s
                join fetch sq.questao q
                left join fetch q.conjuntoOpcao
                where s.instrumentoAvaliativo.instrumentoAvaliativoId=:instrumento
                order by s.ordem, s.instrumentoSessaoId, sq.ordem, sq.sessaoQuestaoId
                """, SessaoQuestao.class).setParameter("instrumento", instrumentoId).getResultList();
    }

    @Transactional
    public InstrumentoSessao gravarSessao(UUID instrumentoId, InstrumentoSessao dados) {
        var instrumento = instrumentoSalvo(instrumentoId);
        exigirTexto(dados.getTitulo(), 200, "Título da sessão");
        if (dados.getOrdem()==null || dados.getOrdem()<1) throw new IllegalArgumentException("A ordem deve ser maior que zero.");
        if (!List.of("SESSAO", "PAGINA").contains(dados.getTipo())) throw new IllegalArgumentException("Tipo de sessão inválido.");
        var destino = dados.getId()==null ? new InstrumentoSessao() : sessaoDoInstrumento(instrumentoId, dados.getId());
        destino.setInstrumentoAvaliativo(instrumento); destino.setTitulo(dados.getTitulo().strip()); destino.setDescricao(dados.getDescricao());
        destino.setOrdem(dados.getOrdem()); destino.setTipo(dados.getTipo()); destino.setTiposessao(dados.getTiposessao());
        if (destino.getTiposessao()!=null && !List.of("SESSAO", "PAGINA").contains(destino.getTiposessao())) throw new IllegalArgumentException("Classificação da sessão inválida.");
        if (destino.getId()==null) entityManager.persist(destino);
        entityManager.flush(); return destino;
    }

    @Transactional
    public SessaoQuestao gravarQuestao(UUID instrumentoId, UUID sessaoId, SessaoQuestao dados, UUID conjuntoId) {
        return gravarQuestao(instrumentoId, sessaoId, dados, conjuntoId, List.of());
    }

    public List<OpcaoQuestao> opcoes(UUID questaoId) {
        return entityManager.createQuery("from OpcaoQuestao where questao.questaoId=:id order by ordem, opcaoQuestaoId", OpcaoQuestao.class)
                .setParameter("id", questaoId).getResultList();
    }

    public List<OpcaoQuestao> opcoesDoInstrumento(UUID instrumentoId) {
        return entityManager.createQuery("""
                select o from OpcaoQuestao o join fetch o.conjuntoOpcao left join fetch o.questao
                where exists (select sq from SessaoQuestao sq where
                  sq.instrumentoSessao.instrumentoAvaliativo.instrumentoAvaliativoId=:instrumento
                  and ((sq.questao.tipo='ESCALAS' and sq.questao.conjuntoOpcao=o.conjuntoOpcao)
                       or (sq.questao.tipo in ('UNICA_ESCOLHA','MULTIPLA_ESCOLHA') and sq.questao=o.questao)))
                order by o.ordem, o.opcaoQuestaoId
                """, OpcaoQuestao.class).setParameter("instrumento", instrumentoId).getResultList();
    }

    @Transactional
    public SessaoQuestao gravarQuestao(UUID instrumentoId, UUID sessaoId, SessaoQuestao dados, UUID conjuntoId, List<OpcaoQuestao> alternativas) {
        var instrumento = instrumentoSalvo(instrumentoId);
        var sessao = sessaoDoInstrumento(instrumentoId, sessaoId);
        exigirTexto(dados.getQuestao().getEnunciado(), Integer.MAX_VALUE, "Enunciado");
        exigirTexto(dados.getQuestao().getTipo(), 16, "Tipo da questão");
        if (dados.getQuestao().getAlinhamento()==null || !List.of(1,2).contains(dados.getQuestao().getAlinhamento()))
            throw new IllegalArgumentException("Selecione um alinhamento válido: vertical ou horizontal.");
        TipoQuestaoId tipoId;
        try { tipoId = TipoQuestaoId.valueOf(dados.getQuestao().getTipo()); }
        catch (IllegalArgumentException e) { throw new IllegalArgumentException("Selecione um tipo de questão válido."); }
        if (entityManager.find(TipoQuestao.class, tipoId)==null)
            throw new IllegalArgumentException("Tipo de questão não cadastrado.");
        boolean escolha = tipoId==TipoQuestaoId.UNICA_ESCOLHA || tipoId==TipoQuestaoId.MULTIPLA_ESCOLHA;
        if (escolha) {
            if (alternativas.size()<2) throw new IllegalArgumentException("Cadastre pelo menos duas opções para a questão.");
            var ordens = new java.util.HashSet<Integer>();
            for (var alternativa : alternativas) {
                exigirTexto(alternativa.getTexto(), 300, "Texto da opção");
                if (alternativa.getOrdem()==null || alternativa.getOrdem()<1 || !ordens.add(alternativa.getOrdem()))
                    throw new IllegalArgumentException("Informe uma ordem positiva e diferente para cada opção.");
                var valor = alternativa.getValor();
                if (valor!=null && (valor.scale()>2 || valor.precision()-valor.scale()>4))
                    throw new IllegalArgumentException("O valor da opção deve estar entre -9999,99 e 9999,99, com até duas casas decimais.");
            }
        }
        if (dados.getOrdem()==null || dados.getOrdem()<1) throw new IllegalArgumentException("A ordem deve ser maior que zero.");
        if (dados.getPeso()!=null && (dados.getPeso().signum()<0 || dados.getPeso().scale()>2 || dados.getPeso().precision()-dados.getPeso().scale()>4))
            throw new IllegalArgumentException("O peso deve estar entre 0 e 9999,99, com até duas casas decimais.");
        var conjunto = conjuntoId==null ? null : entityManager.find(ConjuntoOpcao.class, conjuntoId);
        if (conjuntoId!=null && conjunto==null) throw new IllegalArgumentException("Conjunto de opções não encontrado.");
        SessaoQuestao destino;
        if (dados.getId()==null) { destino = new SessaoQuestao(); destino.setQuestao(new Questao()); }
        else {
            destino = entityManager.find(SessaoQuestao.class, dados.getId());
            if (destino==null || !destino.getInstrumentoSessao().getId().equals(sessaoId)) throw new IllegalArgumentException("Questão não pertence à sessão selecionada.");
        }
        var questao = destino.getQuestao();
        questao.setAlinhamento(dados.getQuestao().getAlinhamento());
        questao.setEnunciado(dados.getQuestao().getEnunciado().strip()); questao.setAssunto(dados.getQuestao().getAssunto());
        questao.setTipo(dados.getQuestao().getTipo().strip()); questao.setConjuntoOpcao(conjunto);
        if (questao.getId()==null) { questao.setCriadoEm(OffsetDateTime.now()); questao.setCriadoPor(instrumento.getCriadoPor()); entityManager.persist(questao); }
        if (escolha) {
            String nomeConjunto = "Questão " + questao.getId();
            var proprio = entityManager.createQuery("from ConjuntoOpcao where nome=:nome", ConjuntoOpcao.class)
                    .setParameter("nome", nomeConjunto).getResultStream().findFirst().orElse(null);
            if (proprio==null) { proprio=new ConjuntoOpcao(); proprio.setNome(nomeConjunto); entityManager.persist(proprio); }
            proprio.setExclusivo(tipoId==TipoQuestaoId.UNICA_ESCOLHA);
            questao.setConjuntoOpcao(proprio);
            var anteriores = opcoes(questao.getId());
            var mantidas = new java.util.HashSet<UUID>();
            for (var alternativa : alternativas) {
                var destinoOpcao = alternativa.getId()==null ? new OpcaoQuestao() : anteriores.stream()
                        .filter(o -> o.getId().equals(alternativa.getId())).findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Opção não pertence à questão."));
                destinoOpcao.setQuestao(questao); destinoOpcao.setConjuntoOpcao(proprio);
                destinoOpcao.setOrdem(alternativa.getOrdem()); destinoOpcao.setTexto(alternativa.getTexto().strip());
                destinoOpcao.setCorreta(Boolean.TRUE.equals(alternativa.getCorreta()));
                destinoOpcao.setValor(alternativa.getValor());
                if (destinoOpcao.getId()==null) entityManager.persist(destinoOpcao);
                mantidas.add(destinoOpcao.getId());
            }
            anteriores.stream().filter(o -> !mantidas.contains(o.getId())).forEach(entityManager::remove);
        }
        destino.setInstrumentoSessao(sessao); destino.setOrdem(dados.getOrdem()); destino.setPeso(dados.getPeso());
        destino.setObrigatoria(Boolean.TRUE.equals(dados.getObrigatoria()));
        if (destino.getId()==null) entityManager.persist(destino);
        entityManager.flush(); return destino;
    }

    @Transactional
    public void removerQuestaoDaSessao(UUID instrumentoId, UUID sessaoId, UUID vinculoId) {
        sessaoDoInstrumento(instrumentoId, sessaoId);
        if (vinculoId==null) throw new IllegalArgumentException("Questão não encontrada na sessão.");
        var vinculo = entityManager.find(SessaoQuestao.class, vinculoId);
        if (vinculo==null || !vinculo.getInstrumentoSessao().getId().equals(sessaoId))
            throw new IllegalArgumentException("Questão não pertence à sessão selecionada.");
        entityManager.remove(vinculo);
        entityManager.flush();
    }

    private InstrumentoAvaliativo instrumentoSalvo(UUID id) {
        if (id==null) throw new IllegalArgumentException("Salve o instrumento antes de cadastrar sessões ou questões.");
        var instrumento = entityManager.find(InstrumentoAvaliativo.class, id);
        if (instrumento==null) throw new IllegalArgumentException("Instrumento não encontrado.");
        return instrumento;
    }

    private InstrumentoSessao sessaoDoInstrumento(UUID instrumentoId, UUID sessaoId) {
        if (sessaoId==null) throw new IllegalArgumentException("Selecione uma sessão salva.");
        var sessao = entityManager.find(InstrumentoSessao.class, sessaoId);
        if (sessao==null || !sessao.getInstrumentoAvaliativo().getId().equals(instrumentoId)) throw new IllegalArgumentException("Sessão não pertence ao instrumento.");
        return sessao;
    }

    private void exigirTexto(String valor, int limite, String nome) {
        if (valor==null || valor.isBlank() || valor.strip().length()>limite) throw new IllegalArgumentException(nome + " é obrigatório e deve respeitar o tamanho permitido.");
    }
}
