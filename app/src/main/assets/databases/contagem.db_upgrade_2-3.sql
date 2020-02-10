PRAGMA foreign_keys = 0;

CREATE TABLE sqlitestudio_temp_table AS SELECT *
                                          FROM fornecedor;

DROP TABLE fornecedor;

CREATE TABLE fornecedor (
    cnpj     TEXT (14) PRIMARY KEY,
    nome     TEXT      NOT NULL
                       UNIQUE,
    fantasia TEXT,
    email    TEXT,
    telefone TEXT
);

INSERT INTO fornecedor (
                           cnpj,
                           nome,
                           fantasia,
                           email
                       )
                       SELECT cnpj,
                              nome,
                              fantasia,
                              email
                         FROM sqlitestudio_temp_table;

DROP TABLE sqlitestudio_temp_table;

PRAGMA foreign_keys = 1;
