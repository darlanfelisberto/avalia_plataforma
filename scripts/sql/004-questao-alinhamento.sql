BEGIN;
ALTER TABLE avaliacao.questao ADD COLUMN alinhamento integer NOT NULL DEFAULT 2;
ALTER TABLE avaliacao.questao ADD CONSTRAINT questao_alinhamento_valido CHECK (alinhamento IN (1,2));
COMMIT;
