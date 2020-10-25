PRAGMA foreign_keys = 0;

CREATE TABLE sqlitestudio_temp_table AS SELECT *
                                          FROM produto_grade;

DROP TABLE produto_grade;

CREATE TABLE produto_grade (
    cod_barra TEXT   PRIMARY KEY,
    produto   TEXT   REFERENCES produto (cod_barra) ON DELETE CASCADE,
    preco     DOUBLE NOT NULL
);

INSERT INTO produto_grade (
                              cod_barra,
                              produto,
                              preco
                          )
                          SELECT cod_barra,
                                 produto,
                                 preco
                            FROM sqlitestudio_temp_table;

DROP TABLE sqlitestudio_temp_table;

PRAGMA foreign_keys = 1;