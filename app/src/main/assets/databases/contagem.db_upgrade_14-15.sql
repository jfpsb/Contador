PRAGMA foreign_keys = 0;

CREATE TABLE sqlitestudio_temp_table AS SELECT *
                                          FROM estoque;

DROP TABLE estoque;

CREATE TABLE estoque (
    produto_grade TEXT    REFERENCES produto_grade (id),
    loja          TEXT    REFERENCES loja (cnpj),
    quantidade    INTEGER DEFAULT (0),
    PRIMARY KEY (
        produto_grade,
        loja
    )
);

INSERT INTO estoque (
                        produto_grade,
                        loja,
                        quantidade
                    )
                    SELECT produto_grade,
                           loja,
                           quantidade
                      FROM sqlitestudio_temp_table;

DROP TABLE sqlitestudio_temp_table;

PRAGMA foreign_keys = 1;
