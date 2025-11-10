CREATE DATABASE CasinoDB;
GO
USE CasinoDB;
GO
CREATE TABLE jugadores (
    id INT IDENTITY PRIMARY KEY,
    nombre VARCHAR(50),
    apodo VARCHAR(50),
    tipo VARCHAR(50),
    dinero DECIMAL(10,2),
    victorias INT
);

CREATE TABLE partidas (
    id INT IDENTITY PRIMARY KEY,
    fecha DATETIME DEFAULT GETDATE(),
    ganador_apodo VARCHAR(50),
    rondas INT,
    pozo DECIMAL(10,2)
);


CREATE LOGIN casino WITH PASSWORD = 'casino';
CREATE USER casino FOR LOGIN casino;
ALTER ROLE db_owner ADD MEMBER casino;
