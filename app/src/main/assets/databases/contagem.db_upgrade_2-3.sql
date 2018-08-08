CREATE TABLE produto1 AS SELECT cod_barra, cod_barra_fornecedor, nome as marca_nome, fornecedor, descricao, preco FROM produto, marca WHERE marca = id OR marca IS NULL OR marca = 0;

CREATE TABLE marca1 AS SELECT * FROM marca;

CREATE TABLE loja1 AS SELECT * FROM loja;

CREATE TABLE fornecedor1 AS SELECT * FROM fornecedor;

DROP TABLE marca;

CREATE TABLE marca (
    nome TEXT (25) NOT NULL PRIMARY KEY
);

INSERT INTO marca (nome) SELECT nome FROM marca1;

DROP TABLE loja;

CREATE TABLE loja (
    cnpj     TEXT (14) PRIMARY KEY,
    matriz   TEXT (14) REFERENCES loja (cnpj) ON DELETE SET NULL,
    nome     TEXT      NOT NULL
                       UNIQUE,
    telefone TEXT (12) 
);

INSERT INTO loja (cnpj, nome) SELECT cnpj, nome FROM loja1;

DROP TABLE fornecedor;

CREATE TABLE fornecedor (
    cnpj     TEXT (14) PRIMARY KEY,
    nome     TEXT      NOT NULL
                       UNIQUE,
    fantasia TEXT,
    email    TEXT
);

INSERT INTO fornecedor (cnpj, nome, fantasia) SELECT cnpj, nome, fantasia FROM fornecedor1;

DROP TABLE contagem;

CREATE TABLE contagem (
    loja       TEXT (14) REFERENCES loja (cnpj) ON DELETE CASCADE,
    data       DATETIME  NOT NULL,
    finalizada BOOLEAN DEFAULT(0),
	PRIMARY KEY (loja, data)
);

DROP TABLE contagem_produto;

CREATE TABLE contagem_produto (
    id            BIGINT    PRIMARY KEY,
    produto       TEXT (15) REFERENCES produto (cod_barra) ON DELETE CASCADE,
    contagem_data DATETIME  REFERENCES contagem (data) ON DELETE CASCADE,
    contagem_loja TEXT (14) REFERENCES contagem (loja) ON DELETE CASCADE,
    quant         INTEGER   DEFAULT 0
                            NOT NULL
);

DROP TABLE produto;

CREATE TABLE produto (
    cod_barra  TEXT      PRIMARY KEY
                         NOT NULL,
    fornecedor TEXT      REFERENCES fornecedor (cnpj) ON DELETE SET NULL
                                                      ON UPDATE CASCADE,
    marca      TEXT (25) REFERENCES marca (nome) ON DELETE SET NULL,
    descricao  TEXT (60) NOT NULL,
    preco      DOUBLE    NOT NULL
);

INSERT INTO produto (cod_barra, fornecedor,	marca, descricao, preco) SELECT cod_barra, fornecedor, marca_nome, descricao, preco FROM produto1;
					  
CREATE TABLE cod_barra_fornecedor (
    produto TEXT (15) REFERENCES produto (cod_barra) ON DELETE CASCADE,
    codigo  TEXT (15) NOT NULL,
	PRIMARY KEY (produto, codigo)
);

INSERT INTO cod_barra_fornecedor (produto, codigo) SELECT cod_barra, cod_barra_fornecedor FROM produto1 WHERE cod_barra_fornecedor IS NOT NULL;

DROP TABLE produto1;
DROP TABLE loja1;
DROP TABLE fornecedor1;
DROP TABLE marca1;
