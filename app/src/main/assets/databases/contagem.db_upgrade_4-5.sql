CREATE TABLE operadoracartao (
    nome TEXT (25) PRIMARY KEY
);

CREATE TABLE operadorabancoid (
    identificador   TEXT (15) PRIMARY KEY,
    operadoracartao TEXT (25) REFERENCES operadoracartao (noem) ON DELETE CASCADE
);

CREATE TABLE recebimentocartao (
    mes             INTEGER,
    ano             INTEGER,
    loja            TEXT (14)  REFERENCES loja (cnpj),
    operadoracartao TEXT (25)  REFERENCES operadoracartao (noem),
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

