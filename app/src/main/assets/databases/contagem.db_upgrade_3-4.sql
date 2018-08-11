PRAGMA foreign_keys = 0;

CREATE TABLE sqlitestudio_temp_table AS SELECT *
                                          FROM marca;

DROP TABLE marca;

CREATE TABLE marca (
    id   BIGINT    PRIMARY KEY
                   NOT NULL
                   DEFAULT (ABS(RANDOM())),
    nome TEXT (25) NOT NULL
);

INSERT INTO marca (
                      nome
                  )
                  SELECT nome
                    FROM sqlitestudio_temp_table;

DROP TABLE sqlitestudio_temp_table;

CREATE TABLE sqlitestudio_temp_table1 AS SELECT *
                                          FROM produto;

DROP TABLE produto;

CREATE TABLE produto (
    cod_barra  TEXT      PRIMARY KEY
                         NOT NULL,
    fornecedor TEXT      REFERENCES fornecedor (cnpj) ON DELETE SET NULL
                                                      ON UPDATE CASCADE,
    marca      BIGINT    REFERENCES marca (id) ON DELETE SET NULL,
    descricao  TEXT (60) NOT NULL,
    preco      DOUBLE    NOT NULL
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
                      FROM sqlitestudio_temp_table1;

DROP TABLE sqlitestudio_temp_table1;

PRAGMA foreign_keys = 1;