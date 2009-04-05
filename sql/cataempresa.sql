create table EMPRESA (
    ID INT(10) auto_increment not null,
    NOME VARCHAR(50) not null,
    SITE VARCHAR(100) null,
    EMAIL VARCHAR(100) null,
    CONTATO VARCHAR(50) null,
    EMAIL_CONTATO VARCHAR(100) null,
    LOCAL CHAR(3) NOT NULL,
    LINK VARCHAR(200) not null,
    PRIMARY KEY  (id),
    KEY id (id),    
    UNIQUE KEY UNIQUE_NOME (NOME)    
)