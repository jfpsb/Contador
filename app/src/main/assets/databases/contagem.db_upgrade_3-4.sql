PRAGMA foreign_keys = 0;

CREATE TABLE sqlitestudio_temp_table AS SELECT *
                                          FROM loja;

DROP TABLE loja;

CREATE TABLE loja (
    cnpj              TEXT (14)  PRIMARY KEY,
    matriz            TEXT (14)  REFERENCES loja (cnpj) ON DELETE SET NULL,
    nome              TEXT       NOT NULL
                                 UNIQUE,
    telefone          TEXT (12),
    endereco          TEXT (200),
    inscricaoestadual TEXT (10) 
);

INSERT INTO loja (
                     cnpj,
                     matriz,
                     nome,
                     telefone
                 )
                 SELECT cnpj,
                        matriz,
                        nome,
                        telefone
                   FROM sqlitestudio_temp_table;

DROP TABLE sqlitestudio_temp_table;

PRAGMA foreign_keys = 1;

PRAGMA foreign_keys = 0;

CREATE TABLE sqlitestudio_temp_table AS SELECT *
                                          FROM produto;

DROP TABLE produto;

CREATE TABLE produto (
    cod_barra  TEXT      PRIMARY KEY
                         NOT NULL,
    fornecedor TEXT      REFERENCES fornecedor (cnpj) ON DELETE SET NULL
                                                      ON UPDATE CASCADE,
    marca      TEXT (25) REFERENCES marca (nome) ON DELETE SET NULL,
    descricao  TEXT (60) NOT NULL,
    preco      DOUBLE    NOT NULL,
    ncm        TEXT (10) 
);

INSERT INTO produto (
                        cod_barra,
                        fornecedor,
                        marca,
                        descricao,
                        preco
                    )
                    SELECT cod_barra,
                           fornecedor,
                           marca,
                           descricao,
                           preco
                      FROM sqlitestudio_temp_table;

DROP TABLE sqlitestudio_temp_table;

PRAGMA foreign_keys = 1;

PRAGMA foreign_keys = 0;

CREATE TABLE sqlitestudio_temp_table AS SELECT *
                                          FROM marca;

DROP TABLE marca;

CREATE TABLE marca (
    nome       TEXT (25) NOT NULL
                         PRIMARY KEY,
    fornecedor TEXT (14) REFERENCES fornecedor (cnpj) ON DELETE SET NULL
);

INSERT INTO marca (
                      nome
                  )
                  SELECT nome
                    FROM sqlitestudio_temp_table;

DROP TABLE sqlitestudio_temp_table;

PRAGMA foreign_keys = 1;
