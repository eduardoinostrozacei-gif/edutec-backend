
CREATE TABLE IF NOT EXISTS notificacion (
                                            id_notificacion     BIGINT PRIMARY KEY AUTO_INCREMENT,
                                            tipo                VARCHAR(50)   NOT NULL,
    destino             VARCHAR(255)  NOT NULL,
    asunto              VARCHAR(255)  NOT NULL,
    cuerpo              TEXT          NOT NULL,
    estado              VARCHAR(20)   NOT NULL,
    intentos            INT           NOT NULL DEFAULT 0,
    error               TEXT          NULL,
    fecha_creacion      DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_envio         DATETIME      NULL
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
