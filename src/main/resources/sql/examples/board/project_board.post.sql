CREATE TABLE IF NOT EXISTS posts (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     title VARCHAR(200) NOT NULL,
                                     content TEXT NOT NULL,
                                     author VARCHAR(50) NOT NULL,
                                     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                     updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
                                     INDEX idx_posts_author (author)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


