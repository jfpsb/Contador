PRAGMA foreign_keys = 0;

CREATE TABLE sqlitestudio_temp_table AS SELECT *
                                          FROM loja;

DROP TABLE loja;

CREATE TABLE loja (
    cnpj TEXT (15) PRIMARY KEY,
    nome TEXT      NOT NULL
                   UNIQUE
);

INSERT INTO loja (
                     cnpj,
                     nome
                 )
                 SELECT idloja,
                        nome
                   FROM sqlitestudio_temp_table;

CREATE TABLE sqlitestudio_temp_table0 AS SELECT *
                                           FROM contagem;

DROP TABLE contagem;

CREATE TABLE contagem (
    idcontagem INTEGER PRIMARY KEY AUTOINCREMENT,
    loja       INTEGER REFERENCES loja (cnpj) 
                       NOT NULL,
    datainicio DATE    NOT NULL,
    datafinal  DATE
);

INSERT INTO contagem (
                         idcontagem,
                         loja,
                         datainicio,
                         datafinal
                     )
                     SELECT idcontagem,
                            loja,
                            datainicio,
                            datafinal
                       FROM sqlitestudio_temp_table0;

CREATE TABLE sqlitestudio_temp_table1 AS SELECT *
                                           FROM contagem_produto;

DROP TABLE contagem_produto;

CREATE TABLE contagem_produto (
    id       INTEGER PRIMARY KEY AUTOINCREMENT,
    contagem INTEGER REFERENCES contagem ON DELETE CASCADE,
    produto  INTEGER REFERENCES produto (cod_barra) ON DELETE CASCADE,
    quant    INTEGER DEFAULT 0
);

INSERT INTO contagem_produto (
                                 id,
                                 contagem,
                                 produto,
                                 quant
                             )
                             SELECT id,
                                    contagem,
                                    produto,
                                    quant
                               FROM sqlitestudio_temp_table1;

DROP TABLE sqlitestudio_temp_table1;

DROP TABLE sqlitestudio_temp_table0;

DROP TABLE sqlitestudio_temp_table;

PRAGMA foreign_keys = 1;

PRAGMA foreign_keys = 0;

CREATE TABLE sqlitestudio_temp_table AS SELECT *
                                          FROM contagem;

DROP TABLE contagem;

CREATE TABLE contagem (
    idcontagem INTEGER PRIMARY KEY AUTOINCREMENT,
    loja       INTEGER REFERENCES loja (cnpj) ON UPDATE CASCADE
                       NOT NULL,
    datainicio DATE    NOT NULL,
    datafinal  DATE
);

INSERT INTO contagem (
                         idcontagem,
                         loja,
                         datainicio,
                         datafinal
                     )
                     SELECT idcontagem,
                            loja,
                            datainicio,
                            datafinal
                       FROM sqlitestudio_temp_table;

DROP TABLE sqlitestudio_temp_table;

PRAGMA foreign_keys = 1;

