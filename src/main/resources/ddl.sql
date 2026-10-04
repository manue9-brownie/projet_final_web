-- SGM medication table (MySQL 8+)
-- Matches com.SGM.model.Medicamento and the database name configured in application.yaml.

CREATE DATABASE IF NOT EXISTS sgm
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE sgm;

CREATE TABLE IF NOT EXISTS medicamento (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(255),
    principio_ativo VARCHAR(255),
    quantidade INT NOT NULL,
    validade VARCHAR(255),
    horario VARCHAR(255),
    PRIMARY KEY (id)
) ENGINE=InnoDB
  DEFAULT CHARACTER SET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;
