CREATE TABLE tb_motorcycle(
    id UUID NOT NULL PRIMARY KEY,
    license_plate VARCHAR(14) NOT NULL UNIQUE,
    chassis VARCHAR(17) NOT NULL UNIQUE,
    problem_description VARCHAR(255),
    model VARCHAR(12) NOT NULL,
    problem VARCHAR(15) NOT NULL,
    tag_id UUID NOT NULL REFERENCES tb_tag(id),
    CONSTRAINT chk_model CHECK (model in ('MOTTUPOP', 'MOTTUSPORT', 'MOTTUE')),
    CONSTRAINT chk_problem CHECK (problem in ('MECHANICAL', 'ELECTRICAL', 'DOCUMENTATION', 'AESTHETIC', 'SAFETY', 'MULTIPLE', 'COMPLIANT'))
);