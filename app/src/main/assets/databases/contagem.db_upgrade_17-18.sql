PRAGMA foreign_keys = 0;

CREATE TABLE sqlitestudio_temp_table AS SELECT *
                                          FROM contagem;

DROP TABLE contagem;

CREATE TABLE contagem (
    uuid         VARCHAR (36) PRIMARY KEY,
    tipo         VARCHAR (36) REFERENCES tipocontagem (uuid),
    loja         VARCHAR (14) REFERENCES loja (cnpj),
    data         DATETIME     NOT NULL,
    finalizada   BOOLEAN,
    criadoem     DATETIME,
    modificadoem DATETIME,
    deletadoem   DATETIME,
    deletado     BOOLEAN      DEFAULT (0) 
);

INSERT INTO contagem (
                         uuid,
                         tipo,
                         loja,
                         data,
                         finalizada,
                         criadoem,
                         modificadoem,
                         deletadoem,
                         deletado
                     )
                     SELECT uuid,
                            tipo,
                            loja,
                            data,
                            finalizada,
                            criadoem,
                            modificadoem,
                            deletadoem,
                            deletado
                       FROM sqlitestudio_temp_table;

DROP TABLE sqlitestudio_temp_table;

CREATE TABLE sqlitestudio_temp_table AS SELECT *
                                          FROM contagemproduto;

DROP TABLE contagemproduto;

CREATE TABLE contagemproduto (
    uuid          VARCHAR (36) PRIMARY KEY,
    contagem      VARCHAR (36) REFERENCES contagem (uuid),
    produto_grade VARCHAR (36) REFERENCES produto_grade (uuid),
    quant         INTEGER,
    criadoem      DATETIME,
    modificadoem  DATETIME,
    deletadoem    DATETIME,
    deletado      BOOLEAN      DEFAULT (0) 
);

INSERT INTO contagemproduto (
                                uuid,
                                contagem,
                                produto_grade,
                                quant,
                                criadoem,
                                modificadoem,
                                deletadoem,
                                deletado
                            )
                            SELECT uuid,
                                   contagem,
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
    cnpj          VARCHAR (14)  PRIMARY KEY,
    representante VARCHAR (36)  REFERENCES representante (uuid),
    nome          VARCHAR (150) NOT NULL,
    fantasia      VARCHAR (60),
    email         VARCHAR (50),
    telefone      VARCHAR (100),
    criadoem      DATETIME,
    modificadoem  DATETIME,
    deletadoem    DATETIME,
    deletado      BOOLEAN       DEFAULT (0) 
);

INSERT INTO fornecedor (
                           cnpj,
                           representante,
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
                              representante,
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
                                          FROM grade;

DROP TABLE grade;

CREATE TABLE grade (
    uuid         VARCHAR (36) PRIMARY KEY,
    tipo         VARCHAR (36) REFERENCES tipo_grade (uuid),
    nome         VARCHAR (45) NOT NULL,
    criadoem     DATETIME,
    modificadoem DATETIME,
    deletadoem   DATETIME,
    deletado     BOOLEAN      DEFAULT (0) 
);

INSERT INTO grade (
                      uuid,
                      tipo,
                      nome,
                      criadoem,
                      modificadoem,
                      deletadoem,
                      deletado
                  )
                  SELECT uuid,
                         tipo,
                         nome,
                         criadoem,
                         modificadoem,
                         deletadoem,
                         deletado
                    FROM sqlitestudio_temp_table;

DROP TABLE sqlitestudio_temp_table;

CREATE TABLE sqlitestudio_temp_table AS SELECT *
                                          FROM loja;

DROP TABLE loja;

CREATE TABLE loja (
    cnpj              VARCHAR (14)  PRIMARY KEY,
    matriz            VARCHAR (14)  REFERENCES loja (cnpj),
    nome              VARCHAR (45)  NOT NULL,
    telefone          VARCHAR (100),
    endereco          VARCHAR (200),
    inscricaoestadual VARCHAR (10),
    aluguel           DOUBLE,
    criadoem          DATETIME,
    modificadoem      DATETIME,
    deletadoem        DATETIME,
    deletado          BOOLEAN       DEFAULT (0) 
);

INSERT INTO loja (
                     cnpj,
                     matriz,
                     nome,
                     telefone,
                     endereco,
                     inscricaoestadual,
                     aluguel,
                     criadoem,
                     modificadoem,
                     deletadoem,
                     deletado
                 )
                 SELECT cnpj,
                        matriz,
                        nome,
                        telefone,
                        endereco,
                        inscricaoestadual,
                        aluguel,
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
    nome         VARCHAR (25) PRIMARY KEY,
    fornecedor   VARCHAR (14) REFERENCES fornecedor (cnpj),
    criadoem     DATETIME,
    modificadoem DATETIME,
    deletadoem   DATETIME,
    deletado     BOOLEAN      DEFAULT (0) 
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
                                          FROM produto;

DROP TABLE produto;

CREATE TABLE produto (
    uuid         VARCHAR (36) PRIMARY KEY,
    fornecedor   VARCHAR (14) REFERENCES fornecedor (cnpj),
    marca        VARCHAR (25) REFERENCES marca (nome),
    descricao    TEXT         NOT NULL,
    cod_barra    TEXT         NOT NULL,
    ncm          TEXT,
    criadoem     DATETIME,
    modificadoem DATETIME,
    deletadoem   DATETIME,
    deletado     BOOLEAN      DEFAULT (0) 
);

INSERT INTO produto (
                        uuid,
                        fornecedor,
                        marca,
                        descricao,
                        cod_barra,
                        ncm,
                        criadoem,
                        modificadoem,
                        deletadoem,
                        deletado
                    )
                    SELECT uuid,
                           fornecedor,
                           marca,
                           descricao,
                           cod_barra,
                           ncm,
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
    uuid                  VARCHAR (36) PRIMARY KEY,
    produto               VARCHAR (36) REFERENCES produto (uuid),
    cod_barra             VARCHAR (45) NOT NULL,
    cod_barra_alternativo VARCHAR (45),
    preco_venda           DOUBLE       NOT NULL,
    preco_custo           DOUBLE,
    criadoem              DATETIME,
    modificadoem          DATETIME,
    deletadoem            DATETIME,
    deletado              BOOLEAN      DEFAULT (0) 
);

INSERT INTO produto_grade (
                              uuid,
                              produto,
                              cod_barra,
                              cod_barra_alternativo,
                              preco_venda,
                              preco_custo,
                              criadoem,
                              modificadoem,
                              deletadoem,
                              deletado
                          )
                          SELECT uuid,
                                 produto,
                                 cod_barra,
                                 cod_barra_alternativo,
                                 preco_venda,
                                 preco_custo,
                                 criadoem,
                                 modificadoem,
                                 deletadoem,
                                 deletado
                            FROM sqlitestudio_temp_table;

DROP TABLE sqlitestudio_temp_table;

CREATE TABLE sqlitestudio_temp_table AS SELECT *
                                          FROM representante;

DROP TABLE representante;

CREATE TABLE representante (
    uuid         VARCHAR (36) PRIMARY KEY,
    nome         VARCHAR (50) NOT NULL,
    whatsapp     VARCHAR (45),
    cidadeestado VARCHAR (45),
    email        VARCHAR (50),
    criadoem     DATETIME,
    modificadoem DATETIME,
    deletadoem   DATETIME,
    deletado     BOOLEAN      DEFAULT (0) 
);

INSERT INTO representante (
                              uuid,
                              nome,
                              whatsapp,
                              cidadeestado,
                              email,
                              criadoem,
                              modificadoem,
                              deletadoem,
                              deletado
                          )
                          SELECT uuid,
                                 nome,
                                 whatsapp,
                                 cidadeestado,
                                 email,
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
    uuid          VARCHAR (36) PRIMARY KEY,
    produto_grade VARCHAR (36) REFERENCES produto_grade (uuid) ON DELETE CASCADE
                                                               ON UPDATE CASCADE,
    grade         VARCHAR (36) REFERENCES grade (uuid),
    criadoem      DATETIME,
    modificadoem  DATETIME,
    deletadoem    DATETIME,
    deletado      BOOLEAN      DEFAULT (0) 
);

INSERT INTO sub_grade (
                          uuid,
                          produto_grade,
                          grade,
                          criadoem,
                          modificadoem,
                          deletadoem,
                          deletado
                      )
                      SELECT uuid,
                             produto_grade,
                             grade,
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
    uuid         VARCHAR (36) PRIMARY KEY,
    nome         VARCHAR (45) NOT NULL,
    criadoem     DATETIME,
    modificadoem DATETIME,
    deletadoem   DATETIME,
    deletado     BOOLEAN      DEFAULT (0) 
);

INSERT INTO tipo_grade (
                           uuid,
                           nome,
                           criadoem,
                           modificadoem,
                           deletadoem,
                           deletado
                       )
                       SELECT uuid,
                              nome,
                              criadoem,
                              modificadoem,
                              deletadoem,
                              deletado
                         FROM sqlitestudio_temp_table;

DROP TABLE sqlitestudio_temp_table;

CREATE TABLE sqlitestudio_temp_table AS SELECT *
                                          FROM tipocontagem;

DROP TABLE tipocontagem;

CREATE TABLE tipocontagem (
    uuid         VARCHAR (36) PRIMARY KEY,
    nome         VARCHAR (50) NOT NULL,
    criadoem     DATETIME,
    modificadoem DATETIME,
    deletadoem   DATETIME,
    deletado     BOOLEAN      DEFAULT (0) 
);

INSERT INTO tipocontagem (
                             uuid,
                             nome,
                             criadoem,
                             modificadoem,
                             deletadoem,
                             deletado
                         )
                         SELECT uuid,
                                nome,
                                criadoem,
                                modificadoem,
                                deletadoem,
                                deletado
                           FROM sqlitestudio_temp_table;

DROP TABLE sqlitestudio_temp_table;

PRAGMA foreign_keys = 1;
