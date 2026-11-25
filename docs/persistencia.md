# DAO genérico

`br.com.avalia.dao.InjectEntityDAO<T extends Model<?>>` oferece as operações
de escrita comuns às entidades. O `EntityManager` é injetado com
`@PersistenceContext(unitName = "avaliaPU")`.
As entidades são registradas explicitamente em `META-INF/persistence.xml` para
o Hibernate fornecido como biblioteca do Liberty. Ao adicionar uma entidade,
inclua sua classe nesse descritor. `scripts/Validar-Modelo.ps1` verifica se esse
registro corresponde às entidades presentes no projeto.

Cada DAO concreto informa sua entidade no construtor e declara um escopo CDI:

```java
package br.com.avalia.dao.academico;

import br.com.avalia.dao.InjectEntityDAO;
import br.com.avalia.modelo.academico.Pessoa;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PessoaDAO extends InjectEntityDAO<Pessoa> {

    public PessoaDAO() {
        super(Pessoa.class);
    }
}
```

Injete os DAOs com `@Inject`. Os métodos públicos herdados usam `@Transactional`
com propagação `REQUIRED`: participam da transação existente ou abrem uma nova.
O contexto de persistência e a transação são gerenciados pelo Liberty.
Não instancie DAOs com `new` no código da aplicação, pois a injeção e os
interceptadores de transação dependem do CDI.

| Método | Comportamento |
| --- | --- |
| `salvar(entidade)` | Executa `persist` e retorna a própria entidade nova |
| `atualizar(entidade)` | Exige ID e executa `merge`; use a instância retornada |
| `deletar(entidade)` | Exige ID e remove a instância gerenciada ou busca pelo ID antes de remover |

Todas as operações rejeitam uma entidade nula. Atualizar e deletar também
rejeitam um identificador nulo. Se o registro a deletar não existir, a operação
não altera dados. A exclusão de uma entidade detached não mescla seu estado,
evitando copiar alterações pendentes antes da remoção. As restrições de chave
estrangeira continuam sendo aplicadas pelo banco.

`merge` segue as regras da Jakarta Persistence; conforme o estado da entidade,
pode resultar em inserção ou atualização. Ele não garante, por si só, que um
registro já exista. Não há `flush` forçado: erros do banco podem surgir durante
o flush ou na conclusão da transação. Exceções de persistência são propagadas
para que o chamador possa tratá-las e o contêiner aplique o rollback.

O campo protegido `entityManager` fica disponível às subclasses para consultas
específicas. O DAO não armazena a entidade corrente nem estado de requisição.
