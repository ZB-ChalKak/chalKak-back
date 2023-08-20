-- 테스트를 위한 DDL 작성
DROP TABLE IF EXISTS style_tag;
DROP TABLE IF EXISTS hash_tag;
DROP TABLE IF EXISTS member;
DROP TABLE IF EXISTS member_style_tag;
DROP TABLE IF EXISTS post;
DROP TABLE IF EXISTS post_style_tag;
DROP TABLE IF EXISTS post_hash_tag;

-- 정적 태그 (스타일 태그)
CREATE TABLE style_tag
(
    style_tag_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    category     VARCHAR(20)         NOT NULL,
    keyword      VARCHAR(100) UNIQUE NOT NULL,
    created_at   TIMESTAMP           NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMP           NOT NULL DEFAULT NOW()
);

-- 동적 태그 (해시 태그)
CREATE TABLE hash_tag
(
    hash_tag_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    keyword     VARCHAR(100) UNIQUE NOT NULL,
    created_at  TIMESTAMP           NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP           NOT NULL DEFAULT NOW()
);

-- 멤버 정보
CREATE TABLE member
(
    member_id      BIGINT PRIMARY KEY AUTO_INCREMENT,
    email          VARCHAR(100) UNIQUE NOT NULL,
    nickname       VARCHAR(100)        NOT NULL,
    password       VARCHAR(60)         NOT NULL,
    profile_img    VARCHAR(255),
    height         DOUBLE,
    weight         DOUBLE,
    privacy_height BOOLEAN,
    privacy_weight BOOLEAN,
    gender         VARCHAR(20),
    status         VARCHAR(20),
    role           VARCHAR(20),
    created_at     TIMESTAMP           NOT NULL DEFAULT NOW(),
    updated_at     TIMESTAMP           NOT NULL DEFAULT NOW()
);

-- 멤버와 스타일 태그 조인 테이블
CREATE TABLE member_style_tag
(
    member_id    BIGINT NOT NULL,
    style_tag_id BIGINT NOT NULL,
    PRIMARY KEY (member_id, style_tag_id),
    FOREIGN KEY (member_id) REFERENCES member (member_id),
    FOREIGN KEY (style_tag_id) REFERENCES style_tag (style_tag_id)
);

-- 포스트 정보
CREATE TABLE post
(
    post_id    BIGINT PRIMARY KEY AUTO_INCREMENT,
    content    TEXT,
    view_count  BIGINT               DEFAULT 0,
    like_count BIGINT               DEFAULT 0,
    status     VARCHAR(20) NOT NULL,
    member_id  BIGINT,
    created_at TIMESTAMP   NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP   NOT NULL DEFAULT NOW(),
    FOREIGN KEY (member_id) REFERENCES member (member_id)
);

-- 포스트와 스타일 태그 조인 테이블
CREATE TABLE post_style_tag
(
    post_id      BIGINT NOT NULL,
    style_tag_id BIGINT NOT NULL,
    PRIMARY KEY (post_id, style_tag_id),
    FOREIGN KEY (post_id) REFERENCES post (post_id),
    FOREIGN KEY (style_tag_id) REFERENCES style_tag (style_tag_id)
);

-- 포스트와 해시 태그 조인 테이블
CREATE TABLE post_hash_tag
(
    post_id     BIGINT NOT NULL,
    hash_tag_id BIGINT NOT NULL,
    PRIMARY KEY (post_id, hash_tag_id),
    FOREIGN KEY (post_id) REFERENCES post (post_id),
    FOREIGN KEY (hash_tag_id) REFERENCES hash_tag (hash_tag_id)
);