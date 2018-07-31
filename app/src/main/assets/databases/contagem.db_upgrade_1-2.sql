PRAGMA foreign_keys = 0;

CREATE TABLE marca (
    id         INTEGER PRIMARY KEY AUTOINCREMENT,
    nome       TEXT    UNIQUE
                       NOT NULL
);


CREATE TABLE sqlitestudio_temp_table AS SELECT *
                                          FROM produto;

DROP TABLE produto;

CREATE TABLE produto (
    cod_barra            TEXT    PRIMARY KEY
                                 NOT NULL,
    fornecedor           TEXT    REFERENCES fornecedor (cnpj) ON DELETE SET NULL
                                                              ON UPDATE CASCADE,
    descricao            TEXT    NOT NULL,
    preco                DOUBLE  NOT NULL,
    cod_barra_fornecedor TEXT    UNIQUE,
    marca                INTEGER REFERENCES marca (id) ON DELETE SET DEFAULT
                                 DEFAULT (0) 
);

INSERT INTO produto (
                        cod_barra,
                        fornecedor,
                        descricao,
                        preco,
                        cod_barra_fornecedor,
                        marca
                    )
                    SELECT cod_barra,
                           fornecedor,
                           descricao,
                           preco,
                           null,
                           0
                      FROM sqlitestudio_temp_table;

DROP TABLE sqlitestudio_temp_table;

PRAGMA foreign_keys = 1;
