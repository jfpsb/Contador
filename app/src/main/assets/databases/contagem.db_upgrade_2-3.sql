PRAGMA foreign_keys = 0;

CREATE TABLE sqlitestudio_temp_table AS SELECT *
                                          FROM produto;

DROP TABLE produto;

CREATE TABLE produto (
    cod_barra  TEXT    PRIMARY KEY
                       NOT NULL,
    fornecedor INTEGER REFERENCES fornecedor (id) ON DELETE SET NULL,
    descricao  TEXT    NOT NULL,
    preco      DOUBLE  NOT NULL
);

INSERT INTO produto (
                        cod_barra,
                        fornecedor,
                        descricao,
                        preco
                    )
                    SELECT cod_barra,
                           fornecedor,
                           descricao,
                           preco FROM sqlitestudio_temp_table;
					  
DROP TABLE sqlitestudio_temp_table;

PRAGMA foreign_keys = 1;