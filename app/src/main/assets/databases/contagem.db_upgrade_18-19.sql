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
                        id,
                        fornecedor,
                        marca,
                        cod_barra,
                        descricao,
                        preco,
                        precocusto,
                        ncm,
                        criadoem,
                        modificadoem,
                        deletadoem,
                        deletado
                    )
                    SELECT id,
                    fornecedor,
                           marca,
                           cod_barra,
                           descricao,
                           preco,
                           precocusto,
                           ncm,criadoem,modificadoem,deletadoem,deletado
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

INSERT INTO contagem (id,
                         loja,
                         data,
                         tipo,
                         finalizada,
                                                           criadoem,
                                                           modificadoem,
                                                           deletadoem,
                                                           deletado
                     )
                     SELECT id, loja,
                            data,
                            tipo,
                            finalizada,
                                                              criadoem,
                                                              modificadoem,
                                                              deletadoem,
                                                              deletado
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
                                 quant,
                                                              criadoem,
                                                              modificadoem,
                                                              deletadoem,
                                                              deletado
                             )
                             SELECT id,
                                    contagem,
                                    produto,
                                    produto_grade,
                                    quant,
                                                                 criadoem,
                                                                 modificadoem,
                                                                 deletadoem,
                                                                 deletado
                               FROM sqlitestudio_temp_table;

DROP TABLE sqlitestudio_temp_table;

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
                           telefone,
                                                           criadoem,
                                                           modificadoem,
                                                           deletadoem,
                                                           deletado
                       )
                       SELECT cnpj,
                              nome,
                              fantasia,
                              email,
                              telefone,
                                                              criadoem,
                                                              modificadoem,
                                                              deletadoem,
                                                              deletado
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
                      fornecedor,
                                                        criadoem,
                                                        modificadoem,
                                                        deletadoem,
                                                        deletado
                  )
                  SELECT nome,
                         fornecedor,
                                                           criadoem,
                                                           modificadoem,
                                                           deletadoem,
                                                           deletado
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
                      nome,
                                                  criadoem,
                                                  modificadoem,
                                                  deletadoem,
                                                  deletado
                  )
                  SELECT id,
                         tipo,
                         nome,
                                                     criadoem,
                                                     modificadoem,
                                                     deletadoem,
                                                     deletado
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
                           nome,
                                                       criadoem,
                                                       modificadoem,
                                                       deletadoem,
                                                       deletado
                       )
                       SELECT id,
                              nome,
                                                          criadoem,
                                                          modificadoem,
                                                          deletadoem,
                                                          deletado
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

INSERT INTO sub_grade (id,
                          produto_grade,
                          grade,
                                                       criadoem,
                                                       modificadoem,
                                                       deletadoem,
                                                       deletado
                      )
                      SELECT id, produto_grade,
                             grade,
                                                          criadoem,
                                                          modificadoem,
                                                          deletadoem,
                                                          deletado
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
    criadoem     DATETIME,
    modificadoem DATETIME,
    deletadoem   DATETIME,
    deletado     BOOLEAN
);

INSERT INTO produto_grade (
                              id,
                              produto,
                              cod_barra,
                              preco_venda,
                              preco_custo,
                                                                 criadoem,
                                                                 modificadoem,
                                                                 deletadoem,
                                                                 deletado
                          )
                          SELECT id,
                                 produto,
                                 cod_barra,
                                 preco_venda,
                                 preco_custo,
                                                                    criaodem,
                                                                    modificadoem,
                                                                    deletadoem,
                                                                    deletado
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
                              nome,
                                                          criadoem,
                                                          modificadoem,
                                                          deletadoem,
                                                          deletado
                          )
                          SELECT id,
                                 nome,
                                                             criadoem,
                                                             modificadoem,
                                                             deletadoem,
                                                             deletado
                            FROM sqlitestudio_temp_table;

DROP TABLE sqlitestudio_temp_table;

PRAGMA foreign_keys = 1;