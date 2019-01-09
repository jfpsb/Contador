PRAGMA foreign_keys = 0;

CREATE TABLE sqlitestudio_temp_table AS SELECT *
                                          FROM contagem_produto;

DROP TABLE contagem_produto;

CREATE TABLE contagem_produto (
    id            BIGINT    PRIMARY KEY,
    produto       TEXT (15) REFERENCES produto (cod_barra) ON DELETE CASCADE,
    contagem_data DATETIME,
    contagem_loja TEXT (14),
    quant         INTEGER   DEFAULT 0
                            NOT NULL,
    FOREIGN KEY (
        contagem_data,
        contagem_loja
    )
    REFERENCES contagem (data,
    loja) ON DELETE CASCADE
);

INSERT INTO contagem_produto (
                                 id,
                                 produto,
                                 contagem_data,
                                 contagem_loja,
                                 quant
                             )
                             SELECT id,
                                    produto,
                                    contagem_data,
                                    contagem_loja,
                                    quant
                               FROM sqlitestudio_temp_table;

DROP TABLE sqlitestudio_temp_table;

PRAGMA foreign_keys = 1;
