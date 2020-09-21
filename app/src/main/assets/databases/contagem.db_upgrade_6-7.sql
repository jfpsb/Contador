PRAGMA foreign_keys = 0;

CREATE TABLE sqlitestudio_temp_table AS SELECT *
                                          FROM produto;

DROP TABLE produto;

CREATE TABLE produto (
    cod_barra            TEXT      PRIMARY KEY
                                   NOT NULL,
    cod_barra_fornecedor TEXT,
    fornecedor           TEXT      REFERENCES fornecedor (cnpj) ON DELETE SET NULL
                                                                ON UPDATE CASCADE,
    marca                TEXT (25) REFERENCES marca (nome) ON DELETE SET NULL,
    descricao            TEXT (60) NOT NULL,
    preco                DOUBLE    NOT NULL,
    ncm                  TEXT (10) 
);

INSERT INTO produto (
                        cod_barra,
                        fornecedor,
                        marca,
                        descricao,
                        preco,
                        ncm
                    )
                    SELECT cod_barra,
                           fornecedor,
                           marca,
                           descricao,
                           preco,
                           ncm
                      FROM sqlitestudio_temp_table;

DROP TABLE sqlitestudio_temp_table;

PRAGMA foreign_keys = 1;
