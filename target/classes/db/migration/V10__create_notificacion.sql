-- Ajusta la versión V10 al siguiente número libre en tu proyecto
CREATE TABLE IF NOT EXISTS notificacion (
                                            id_notificacion     BIGINT PRIMARY KEY AUTO_INCREMENT,
                                            tipo                VARCHAR(50)   NOT NULL,     -- ej: 'CREACION', 'CAMBIO', 'CANCELACION'
    destino             VARCHAR(255)  NOT NULL,     -- email destino u otro canal
    asunto              VARCHAR(255)  NOT NULL,
    cuerpo              TEXT          NOT NULL,
    estado              VARCHAR(20)   NOT NULL,     -- 'PENDIENTE', 'ENVIADA', 'ERROR'
    intentos            INT           NOT NULL DEFAULT 0,
    error               TEXT          NULL,
    fecha_creacion      DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_envio         DATETIME      NULL
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
