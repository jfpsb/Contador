PRAGMA foreign_keys = 0;

CREATE TABLE sqlitestudio_temp_table AS SELECT id as produto_grade, grade FROM sub_grade inner join produto_grade on produto_grade.cod_barra = sub_grade.produto_grade;

DROP TABLE sub_grade;

CREATE TABLE sub_grade (
    produto_grade TEXT    REFERENCES produto_grade (id) ON DELETE CASCADE,
    grade         INTEGER REFERENCES grade (id),
    PRIMARY KEY (
        produto_grade,
        grade
    )
);

INSERT INTO sub_grade (
                          produto_grade,
                          grade
                      )
                      SELECT produto_grade,
                             grade
                        FROM sqlitestudio_temp_table;

DROP TABLE sqlitestudio_temp_table;

PRAGMA foreign_keys = 1;