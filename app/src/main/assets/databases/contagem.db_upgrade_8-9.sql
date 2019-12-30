CREATE TABLE tipo_contagem (
    id   INTEGER   PRIMARY KEY AUTOINCREMENT,
    nome TEXT (50) NOT NULL
                   UNIQUE
);

PRAGMA foreign_keys = 0;

CREATE TABLE sqlitestudio_temp_table AS SELECT *
                                          FROM contagem;

DROP TABLE contagem;

CREATE TABLE contagem (
    loja       TEXT (14) REFERENCES loja (cnpj) ON DELETE CASCADE,
    data       DATETIME  NOT NULL,
    finalizada BOOLEAN   DEFAULT (0),
    tipo       INTEGER   REFERENCES tipo_contagem (id),
    PRIMARY KEY (
        loja,
        data
    )
);

INSERT INTO contagem (
                         loja,
                         data,
                         finalizada
                     )
                     SELECT loja,
                            data,
                            finalizada
                       FROM sqlitestudio_temp_table;

DROP TABLE sqlitestudio_temp_table;

PRAGMA foreign_keys = 1;