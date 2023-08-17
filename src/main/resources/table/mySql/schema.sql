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
    style_tag_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    category     VARCHAR(20)         NOT NULL COMMENT '카테고리',
    keyword      VARCHAR(100) UNIQUE NOT NULL COMMENT '키워드',
    count        BIGINT COMMENT '카운트',
    created_at     TIMESTAMP           NOT NULL DEFAULT NOW(),
    updated_at     TIMESTAMP           NOT NULL DEFAULT NOW()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 동적 태그 (해시 태그)
CREATE TABLE hash_tag
(
    hash_tag_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    keyword     VARCHAR(100) UNIQUE NOT NULL COMMENT '키워드',
    count       BIGINT COMMENT '카운트',
    created_at     TIMESTAMP           NOT NULL DEFAULT NOW(),
    updated_at     TIMESTAMP           NOT NULL DEFAULT NOW()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 멤버 정보
CREATE TABLE member
(
    member_id      BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    email          VARCHAR(100) UNIQUE NOT NULL COMMENT '이메일',
    nickname       VARCHAR(100)        NOT NULL COMMENT '닉네임',
    password       VARCHAR(60)         NOT NULL COMMENT '비밀번호',
    profile_img    VARCHAR(255) COMMENT '프로필 이미지 경로',
    height         DOUBLE COMMENT '키',
    weight         DOUBLE COMMENT '몸무게',
    privacy_height BOOLEAN COMMENT '키 공개 여부',
    privacy_weight BOOLEAN COMMENT '몸무게 공개 여부',
    gender         VARCHAR(20) COMMENT '성별',
    status         VARCHAR(20) COMMENT '회원 상태',
    role           VARCHAR(20) COMMENT '권한',
    created_at     TIMESTAMP           NOT NULL DEFAULT NOW() COMMENT '생성일',
    updated_at     TIMESTAMP           NOT NULL DEFAULT NOW() COMMENT '수정일'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 멤버와 정적 태그 조인 테이블
CREATE TABLE member_style_tag
(
    member_id    BIGINT NOT NULL,
    style_tag_id BIGINT NOT NULL,
    PRIMARY KEY (member_id, style_tag_id),
    FOREIGN KEY (member_id) REFERENCES member (member_id),
    FOREIGN KEY (style_tag_id) REFERENCES style_tag (style_tag_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 포스트 정보
CREATE TABLE post
(
    post_id    BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    content    TEXT COMMENT '내용',
    hit_count  BIGINT COMMENT '조회수',
    like_count BIGINT COMMENT '좋아요 수',
    status     VARCHAR(20) NOT NULL COMMENT '상태',
    member_id  BIGINT COMMENT '멤버 ID',
    created_at     TIMESTAMP           NOT NULL DEFAULT NOW(),
    updated_at     TIMESTAMP           NOT NULL DEFAULT NOW(),
    FOREIGN KEY (member_id) REFERENCES member (member_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 포스트와 스타일 태그 조인 테이블
CREATE TABLE post_style_tag
(
    post_id      BIGINT NOT NULL,
    style_tag_id BIGINT NOT NULL,
    PRIMARY KEY (post_id, style_tag_id),
    FOREIGN KEY (post_id) REFERENCES post (post_id),
    FOREIGN KEY (style_tag_id) REFERENCES style_tag (style_tag_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 포스트와 해시 태그 조인 테이블
CREATE TABLE post_hash_tag
(
    post_id     BIGINT NOT NULL,
    hash_tag_id BIGINT NOT NULL,
    PRIMARY KEY (post_id, hash_tag_id),
    FOREIGN KEY (post_id) REFERENCES post (post_id),
    FOREIGN KEY (hash_tag_id) REFERENCES hash_tag (hash_tag_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
