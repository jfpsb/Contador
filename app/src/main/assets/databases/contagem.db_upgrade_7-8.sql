PRAGMA foreign_keys = 0;

CREATE TABLE sqlitestudio_temp_table AS SELECT *
                                          FROM cod_barra_fornecedor;

DROP TABLE cod_barra_fornecedor;

CREATE TABLE cod_barra_fornecedor (
    produto TEXT (15) REFERENCES produto (cod_barra) ON DELETE CASCADE,
    codigo  TEXT (15) NOT NULL,
    PRIMARY KEY (
        produto,
        codigo
    )
);

INSERT INTO cod_barra_fornecedor (
                                     produto,
                                     codigo
                                 )
                                 SELECT produto,
                                        codigo
                                   FROM sqlitestudio_temp_table;

DROP TABLE sqlitestudio_temp_table;

PRAGMA foreign_keys = 1;