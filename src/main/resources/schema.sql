DROP TABLE IF EXISTS post;
CREATE TABLE post (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    author VARCHAR(100),
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
);

DROP TABLE IF EXISTS history;
CREATE TABLE history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    entity_type VARCHAR(50) NOT NULL,
    entity_id BIGINT NOT NULL,
    action VARCHAR(20) NOT NULL,
    before_data TEXT,
    after_data TEXT,
    modified_by VARCHAR(50) NOT NULL,
    modified_at DATETIME NOT NULL
); 