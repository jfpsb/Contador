PRAGMA foreign_keys = 0;

CREATE TABLE sqlitestudio_temp_table AS SELECT *
                                          FROM tipo_contagem;

DROP TABLE tipo_contagem;

CREATE TABLE tipo_contagem (
    id   BIGINT    PRIMARY KEY,
    nome TEXT (50) NOT NULL
                   UNIQUE
);

INSERT INTO tipo_contagem (
                              id,
                              nome
                          )
                          SELECT id,
                                 nome
                            FROM sqlitestudio_temp_table;

DROP TABLE sqlitestudio_temp_table;

CREATE TABLE sqlitestudio_temp_table AS SELECT *
                                          FROM operadorabancoid;

DROP TABLE operadorabancoid;

CREATE TABLE operadorabancoid (
    identificador   TEXT (15) PRIMARY KEY,
    operadoracartao TEXT (25) REFERENCES operadoracartao (nome) ON DELETE CASCADE
);

INSERT INTO operadorabancoid (
                                 identificador,
                                 operadoracartao
                             )
                             SELECT identificador,
                                    operadoracartao
                               FROM sqlitestudio_temp_table;

DROP TABLE sqlitestudio_temp_table;

CREATE TABLE sqlitestudio_temp_table AS SELECT *
                                          FROM recebimentocartao;

DROP TABLE recebimentocartao;

CREATE TABLE recebimentocartao (
    mes             INTEGER,
    ano             INTEGER,
    loja            TEXT (14)  REFERENCES loja (cnpj),
    operadoracartao TEXT (25)  REFERENCES operadoracartao (nome),
    recebido        DOUBLE     NOT NULL,
    valorOperadora  DOUBLE     NOT NULL,
    observacao      TEXT (200),
    PRIMARY KEY (
        mes,
        ano,
        loja,
        operadoracartao
    )
);

INSERT INTO recebimentocartao (
                                  mes,
                                  ano,
                                  loja,
                                  operadoracartao,
                                  recebido,
                                  valorOperadora,
                                  observacao
                              )
                              SELECT mes,
                                     ano,
                                     loja,
                                     operadoracartao,
                                     recebido,
                                     valorOperadora,
                                     observacao
                                FROM sqlitestudio_temp_table;

DROP TABLE sqlitestudio_temp_table;

PRAGMA foreign_keys = 1;
