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

CREATE TABLE tipo_grade (
    id   INTEGER PRIMARY KEY AUTOINCREMENT,
    nome TEXT    NOT NULL
                 UNIQUE
);

CREATE TABLE grade (
    id   INTEGER PRIMARY KEY AUTOINCREMENT,
    tipo INTEGER REFERENCES tipo_grade (id),
    nome TEXT    NOT NULL
                 UNIQUE
);

CREATE TABLE produto_grade (
    cod_barra TEXT   PRIMARY KEY,
    produto   TEXT   REFERENCES produto (cod_barra),
    preco     DOUBLE NOT NULL
);

CREATE TABLE sub_grade (
    produto_grade TEXT    REFERENCES produto_grade (cod_barra) ON DELETE CASCADE,
    grade         INTEGER REFERENCES grade (id),
    PRIMARY KEY (
        produto_grade,
        grade
    )
);

CREATE TABLE estoque (
    produto_grade TEXT    REFERENCES produto_grade (cod_barra),
    loja          TEXT    REFERENCES loja (cnpj),
    quantidade    INTEGER DEFAULT (0),
    PRIMARY KEY (
        produto_grade,
        loja
    )
);