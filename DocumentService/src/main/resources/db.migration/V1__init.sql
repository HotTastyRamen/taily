-- V1__init.sql

-- Создание таблицы документов
CREATE TABLE documents (
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    status VARCHAR(50) NOT NULL
);

-- Создание таблицы версий документов
CREATE TABLE document_versions (
    id BIGSERIAL PRIMARY KEY,
    version_number INT NOT NULL,
    uploaded_at TIMESTAMP WITH TIME ZONE NOT NULL,
    file_path TEXT NOT NULL,
    document_id BIGINT NOT NULL,
    CONSTRAINT fk_document_versions_document
        FOREIGN KEY (document_id) REFERENCES documents (id)
        ON DELETE CASCADE
);

-- Создание таблицы подписей
CREATE TABLE signatures (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL,
    signed_at TIMESTAMP WITH TIME ZONE,
    document_id BIGINT NOT NULL,
    CONSTRAINT fk_signatures_document
        FOREIGN KEY (document_id) REFERENCES documents (id)
        ON DELETE CASCADE
);
