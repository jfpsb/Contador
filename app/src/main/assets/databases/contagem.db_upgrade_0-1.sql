--
-- File generated with SQLiteStudio v3.1.1 on dom jul 22 14:51:47 2018
--
-- Text encoding used: UTF-8
--
PRAGMA foreign_keys = off;
BEGIN TRANSACTION;

-- Table: contagem
DROP TABLE IF EXISTS contagem;
CREATE TABLE contagem (idcontagem INTEGER PRIMARY KEY AUTOINCREMENT, loja TEXT (15) REFERENCES loja (cnpj) ON UPDATE CASCADE NOT NULL, datainicio DATE NOT NULL, datafinal DATE);

-- Table: contagem_produto
DROP TABLE IF EXISTS contagem_produto;
CREATE TABLE contagem_produto (id INTEGER PRIMARY KEY AUTOINCREMENT, contagem INTEGER REFERENCES contagem ON DELETE CASCADE, produto INTEGER REFERENCES produto (cod_barra) ON DELETE CASCADE, quant INTEGER DEFAULT 0);

-- Table: fornecedor
DROP TABLE IF EXISTS fornecedor;
CREATE TABLE fornecedor (cnpj TEXT PRIMARY KEY, nome TEXT NOT NULL UNIQUE, fantasia TEXT);

-- Table: loja
DROP TABLE IF EXISTS loja;
CREATE TABLE loja (cnpj TEXT (15) PRIMARY KEY, nome TEXT NOT NULL UNIQUE);
INSERT INTO loja (cnpj, nome) VALUES ('17361873000122', 'JOÃO PAULO 1');
INSERT INTO loja (cnpj, nome) VALUES ('13938943000101', 'JOÃO PAULO 2');
INSERT INTO loja (cnpj, nome) VALUES ('17361873000203', 'CIDADE OPERÁRIA');
INSERT INTO loja (cnpj, nome) VALUES ('13938943000292', 'CENTRO');
INSERT INTO loja (cnpj, nome) VALUES ('20970327000284', 'COHAB 1');
INSERT INTO loja (cnpj, nome) VALUES ('20970327000101', 'COHAB 2');

-- Table: produto
DROP TABLE IF EXISTS produto;
CREATE TABLE produto (cod_barra TEXT PRIMARY KEY NOT NULL, fornecedor TEXT REFERENCES fornecedor (cnpj) ON DELETE SET NULL, descricao TEXT NOT NULL, preco DOUBLE NOT NULL);

COMMIT TRANSACTION;
PRAGMA foreign_keys = on;
