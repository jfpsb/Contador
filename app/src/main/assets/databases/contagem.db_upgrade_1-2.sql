PRAGMA foreign_keys = 0;

CREATE TABLE sqlitestudio_temp_table AS SELECT * FROM fornecedor;

DROP TABLE fornecedor;

CREATE TABLE fornecedor (id INTEGER PRIMARY KEY AUTOINCREMENT, cnpj TEXT NOT NULL UNIQUE, nome TEXT NOT NULL UNIQUE);

INSERT INTO fornecedor (cnpj, nome) SELECT cnpj, nome FROM sqlitestudio_temp_table;

DROP TABLE sqlitestudio_temp_table;

PRAGMA foreign_keys = 1;