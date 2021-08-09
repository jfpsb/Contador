PRAGMA foreign_keys = 0;

CREATE TABLE sqlitestudio_temp_table AS SELECT *
                                          FROM produto;

DROP TABLE produto;

CREATE TABLE produto (
    id           BIGINT   PRIMARY KEY,
    fornecedor   TEXT      REFERENCES fornecedor (cnpj) ON DELETE SET NULL
                                                        ON UPDATE CASCADE,
    marca        TEXT (25) REFERENCES marca (nome) ON DELETE SET NULL,
    cod_barra    TEXT      NOT NULL,
    descricao    TEXT (60) NOT NULL,
    preco        DOUBLE    NOT NULL,
    precocusto   DOUBLE,
    ncm          TEXT (10),
    criadoem     DATETIME,
    modificadoem DATETIME,
    deletadoem   DATETIME,
    deletado     BOOLEAN
);

INSERT INTO produto (
                        fornecedor,
                        marca,
                        cod_barra,
                        descricao,
                        preco,
                        ncm
                    )
                    SELECT fornecedor,
                           marca,
                           cod_barra,
                           descricao,
                           preco,
                           ncm
                      FROM sqlitestudio_temp_table;

DROP TABLE sqlitestudio_temp_table;

CREATE TABLE sqlitestudio_temp_table AS SELECT *
                                          FROM contagem;

DROP TABLE contagem;

CREATE TABLE contagem (
    id           BIGINT    PRIMARY KEY,
    loja         TEXT (14) REFERENCES loja (cnpj) ON DELETE CASCADE,
    data         DATETIME  NOT NULL,
    tipo         INTEGER   REFERENCES tipo_contagem (id),
    finalizada   BOOLEAN   DEFAULT (0),
    criadoem     DATETIME,
    modificadoem DATETIME,
    deletadoem   DATETIME,
    deletado     BOOLEAN
);

INSERT INTO contagem (
                         loja,
                         data,
                         tipo,
                         finalizada
                     )
                     SELECT loja,
                            data,
                            tipo,
                            finalizada
                       FROM sqlitestudio_temp_table;

DROP TABLE sqlitestudio_temp_table;

CREATE TABLE sqlitestudio_temp_table AS SELECT *
                                          FROM contagem_produto;

DROP TABLE contagem_produto;

CREATE TABLE contagem_produto (
    id            BIGINT    PRIMARY KEY,
    contagem      BIGINT    REFERENCES contagem (id) ON DELETE CASCADE,
    produto       BIGINT REFERENCES produto (id) ON DELETE CASCADE,
    produto_grade INTEGER   REFERENCES produto_grade (id) ON DELETE SET NULL,
    quant         INTEGER   DEFAULT 0
                            NOT NULL,
    criadoem      DATETIME,
    modificadoem  DATETIME,
    deletadoem    DATETIME,
    deletado      BOOLEAN
);

INSERT INTO contagem_produto (
                                 id,
                                 contagem,
                                 produto,
                                 produto_grade,
                                 quant
                             )
                             SELECT id,
                                    contagem_data,
                                    produto,
                                    produto_grade,
                                    quant
                               FROM sqlitestudio_temp_table;

DROP TABLE sqlitestudio_temp_table;

CREATE TABLE representante (
    id           BIGINT PRIMARY KEY,
    nome         TEXT,
    whatsapp     TEXT,
    cidadestado  TEXT,
    email        TEXT,
    criadoem     DATETIME,
    modificadoem DATETIME,
    deletadoem   DATETIME,
    deletado     BOOLEAN
);

CREATE TABLE sqlitestudio_temp_table AS SELECT *
                                          FROM fornecedor;

DROP TABLE fornecedor;

CREATE TABLE fornecedor (
    cnpj          TEXT (14) PRIMARY KEY,
    representante BIGINT    REFERENCES representante (id) ON DELETE SET NULL,
    nome          TEXT      NOT NULL
                            UNIQUE,
    fantasia      TEXT,
    email         TEXT,
    telefone      TEXT,
    criadoem      DATETIME,
    modificadoem  DATETIME,
    deletadoem    DATETIME,
    deletado      BOOLEAN
);

INSERT INTO fornecedor (
                           cnpj,
                           nome,
                           fantasia,
                           email,
                           telefone
                       )
                       SELECT cnpj,
                              nome,
                              fantasia,
                              email,
                              telefone
                         FROM sqlitestudio_temp_table;

DROP TABLE sqlitestudio_temp_table;

CREATE TABLE sqlitestudio_temp_table AS SELECT *
                                          FROM marca;

DROP TABLE marca;

CREATE TABLE marca (
    nome         TEXT (25) NOT NULL
                           PRIMARY KEY,
    fornecedor   TEXT (14) REFERENCES fornecedor (cnpj) ON DELETE SET NULL,
    criadoem     DATETIME,
    modificadoem DATETIME,
    deletadoem   DATETIME,
    deletado     BOOLEAN
);

INSERT INTO marca (
                      nome,
                      fornecedor
                  )
                  SELECT nome,
                         fornecedor
                    FROM sqlitestudio_temp_table;

DROP TABLE sqlitestudio_temp_table;

CREATE TABLE sqlitestudio_temp_table AS SELECT *
                                          FROM grade;

DROP TABLE grade;

CREATE TABLE grade (
    id           INTEGER  PRIMARY KEY AUTOINCREMENT,
    tipo         INTEGER  REFERENCES tipo_grade (id),
    nome         TEXT     NOT NULL
                          UNIQUE,
    criadoem     DATETIME,
    modificadoem DATETIME,
    deletadoem   DATETIME,
    deletado     BOOLEAN
);

INSERT INTO grade (
                      id,
                      tipo,
                      nome
                  )
                  SELECT id,
                         tipo,
                         nome
                    FROM sqlitestudio_temp_table;

DROP TABLE sqlitestudio_temp_table;

CREATE TABLE sqlitestudio_temp_table AS SELECT *
                                          FROM tipo_grade;

DROP TABLE tipo_grade;

CREATE TABLE tipo_grade (
    id           INTEGER  PRIMARY KEY AUTOINCREMENT,
    nome         TEXT     NOT NULL
                          UNIQUE,
    criadoem     DATETIME,
    modificadoem DATETIME,
    deletadoem   DATETIME,
    deletado     BOOLEAN
);

INSERT INTO tipo_grade (
                           id,
                           nome
                       )
                       SELECT id,
                              nome
                         FROM sqlitestudio_temp_table;

DROP TABLE sqlitestudio_temp_table;

CREATE TABLE sqlitestudio_temp_table AS SELECT *
                                          FROM sub_grade;

DROP TABLE sub_grade;

CREATE TABLE sub_grade (
    id            INTEGER  PRIMARY KEY AUTOINCREMENT,
    produto_grade TEXT     REFERENCES produto_grade (id) ON DELETE CASCADE,
    grade         INTEGER  REFERENCES grade (id),
    criadoem      DATETIME,
    modificadoem  DATETIME,
    deletadoem    DATETIME,
    deletado      BOOLEAN
);

INSERT INTO sub_grade (
                          produto_grade,
                          grade
                      )
                      SELECT produto_grade,
                             grade
                        FROM sqlitestudio_temp_table;

DROP TABLE sqlitestudio_temp_table;

CREATE TABLE sqlitestudio_temp_table AS SELECT *
                                          FROM produto_grade;

DROP TABLE produto_grade;

CREATE TABLE produto_grade (
    id           INTEGER  PRIMARY KEY AUTOINCREMENT,
    produto      BIGINT     REFERENCES produto (id) ON DELETE CASCADE,
    cod_barra    TEXT     NOT NULL,
    preco_venda  DOUBLE   NOT NULL,
    preco_custo  DOUBLE,
    criaodem     DATETIME,
    modificadoem DATETIME,
    deletadoem   DATETIME,
    deletado     BOOLEAN
);

INSERT INTO produto_grade (
                              id,
                              produto,
                              cod_barra,
                              preco_venda
                          )
                          SELECT id,
                                 produto,
                                 cod_barra,
                                 preco
                            FROM sqlitestudio_temp_table;

DROP TABLE sqlitestudio_temp_table;

CREATE TABLE sqlitestudio_temp_table AS SELECT *
                                          FROM estoque;

DROP TABLE estoque;

CREATE TABLE estoque (
    id            INTEGER PRIMARY KEY AUTOINCREMENT,
    produto       BIGINT  REFERENCES produto (id) ON DELETE CASCADE,
    loja          TEXT    REFERENCES loja (cnpj) ON DELETE CASCADE,
    produto_grade INTEGER REFERENCES produto_grade (id) ON DELETE CASCADE,
    quantidade    INTEGER DEFAULT (0) 
);

INSERT INTO estoque (
                        id,
                        produto,
                        loja,
                        produto_grade,
                        quantidade
                    )
                    SELECT id,
                           produto,
                           loja,
                           produto_grade,
                           quantidade
                      FROM sqlitestudio_temp_table;

DROP TABLE sqlitestudio_temp_table;

CREATE TABLE sqlitestudio_temp_table AS SELECT *
                                          FROM tipo_contagem;

DROP TABLE tipo_contagem;

CREATE TABLE tipo_contagem (
    id           BIGINT    PRIMARY KEY,
    nome         TEXT (50) NOT NULL
                           UNIQUE,
    criadoem     DATETIME,
    modificadoem DATETIME,
    deletadoem   DATETIME,
    deletado     BOOLEAN
);

INSERT INTO tipo_contagem (
                              id,
                              nome
                          )
                          SELECT id,
                                 nome
                            FROM sqlitestudio_temp_table;

DROP TABLE sqlitestudio_temp_table;



PRAGMA foreign_keys = 1;