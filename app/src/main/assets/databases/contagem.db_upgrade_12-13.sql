PRAGMA foreign_keys = 0;

CREATE TABLE sqlitestudio_temp_table AS SELECT *
                                          FROM produto_grade;

DROP TABLE produto_grade;

CREATE TABLE produto_grade (
    id        INTEGER PRIMARY KEY AUTOINCREMENT,
    produto   TEXT    REFERENCES produto (cod_barra) ON DELETE CASCADE,
    cod_barra TEXT    NOT NULL,
    preco     DOUBLE  NOT NULL
);

INSERT INTO produto_grade (
                              produto,
                              cod_barra,
                              preco
                          )
                          SELECT produto,
                                 cod_barra,
                                 preco
                            FROM sqlitestudio_temp_table;

DROP TABLE sqlitestudio_temp_table;

PRAGMA foreign_keys = 1;