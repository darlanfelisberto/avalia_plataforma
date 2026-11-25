BEGIN;
ALTER TABLE avaliacao.tipo_questao ALTER COLUMN id DROP IDENTITY;
ALTER TABLE avaliacao.tipo_questao ALTER COLUMN id TYPE varchar(30) USING codigo;
ALTER TABLE avaliacao.tipo_questao ADD CONSTRAINT tipo_questao_id_valido
    CHECK (id IN ('DESCRITIVA', 'ESCALAS', 'UNICA_ESCOLHA', 'MULTIPLA_ESCOLHA'));
COMMIT;
