-- 테스트를 위한 DML 작성

-- 멤버 데이터 삽입
INSERT INTO member (email, nickname, password, profile_img, height, weight, privacy_height, privacy_weight, gender, status, role)
VALUES
    ('user@test.com', '테스트', '$2a$10$wMXNvfMMkAmIaOHy7NbE4OTqKz0F12tNe1xulO06oHdcA40p4c8Te', '/path/to/profile.jpg', 170.5, 65.2, TRUE, FALSE, 'MALE', 'ACTIVE', 'USER'),
    ('admin@test.com', '어드민', '$2a$10$wMXNvfMMkAmIaOHy7NbE4OTqKz0F12tNe1xulO06oHdcA40p4c8Te', null, 162.0, 55.7, FALSE, TRUE, 'FEMALE', 'ACTIVE', 'USER')
;

-- 정적 태그 데이터 삽입
INSERT INTO style_tag (category, keyword, count)
VALUES
    ('STYLE', '아메카지', 2),
    ('STYLE', '원마일웨어', 1),
    ('STYLE', '미니멀', 0),
    ('STYLE', '댄디', 1),
    ('STYLE', '비즈니스캐주얼', 0),
    ('STYLE', '빈티지', 0),
    ('STYLE', '스트릿', 0),
    ('STYLE', '스포티', 0),
    ('TPO', '데이트', 0),
    ('TPO', '하객', 1),
    ('TPO', '여행', 0),
    ('TPO', '출근', 0)
;

-- 조인 테이블 데이터 삽입
INSERT INTO member_style_tag (member_id, style_tag_id)
VALUES
    (1, 1),
    (1, 4),
    (1, 10),
    (2, 2)
;

-- 해시 태그 정보 삽입
INSERT INTO hash_tag (keyword, count)
VALUES ('휴가', 1),
       ('여름', 0),
       ('뷰티', 0),
       ('커피', 0),
       ('패션', 0)
;

-- 포스트 정보 삽입
INSERT INTO post (content, hit_count, like_count, status, member_id)
VALUES ('첫 번째 포스트 내용', 0, 0, 'PUBLIC', 1)
;

-- 포스트와 스타일 태그 조인 테이블 삽입
INSERT INTO post_style_tag (post_id, style_tag_id)
VALUES (1, 1);

-- 포스트와 해시 태그 조인 테이블 삽입
INSERT INTO post_hash_tag (post_id, hash_tag_id)
VALUES (1, 1);