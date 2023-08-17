-- 테스트를 위한 DDL 작성
DROP TABLE IF EXISTS static_tag;
DROP TABLE IF EXISTS member;
DROP TABLE IF EXISTS member_static_tag;

-- 정적 태그 (스타일 태그)
CREATE TABLE static_tag
(
    static_tag_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    category      VARCHAR(20)         NOT NULL,
    keyword       VARCHAR(100) UNIQUE NOT NULL,
    count         BIGINT
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

-- 멤버와 정적 태그 조인 테이블
CREATE TABLE member_static_tag
(
    member_id     BIGINT NOT NULL,
    static_tag_id BIGINT NOT NULL,
    PRIMARY KEY (member_id, static_tag_id),
    FOREIGN KEY (member_id) REFERENCES member (member_id),
    FOREIGN KEY (static_tag_id) REFERENCES static_tag (static_tag_id)
);

