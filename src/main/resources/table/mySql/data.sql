-- 테스트를 위한 DML 작성

-- 멤버 데이터 삽입
INSERT INTO member (email, nickname, password, profile_img, height, weight, privacy_height, privacy_weight, gender, status, role)
VALUES
    ('user@test.com', '테스트', '$2a$10$wMXNvfMMkAmIaOHy7NbE4OTqKz0F12tNe1xulO06oHdcA40p4c8Te', '/path/to/profile.jpg', 170.5, 65.2, TRUE, FALSE, 'MALE', 'ACTIVE', 'USER'),
    ('admin@test.com', '어드민', '$2a$10$wMXNvfMMkAmIaOHy7NbE4OTqKz0F12tNe1xulO06oHdcA40p4c8Te', null, 162.0, 55.7, FALSE, TRUE, 'FEMALE', 'ACTIVE', 'USER')
;

-- 정적 태그 데이터 삽입
INSERT INTO static_tag (category, keyword, count)
VALUES
    ('Style', '아메카지', 0),
    ('Style', '원마일웨어', 0),
    ('Style', '미니멀', 0),
    ('Style', '댄디', 0),
    ('Style', '비즈니스캐주얼', 0),
    ('Style', '빈티지', 0),
    ('Style', '스트릿', 0),
    ('Style', '스포티', 0),
    ('TPO', '데이트', 0),
    ('TPO', '하객', 0),
    ('TPO', '여행', 0),
    ('TPO', '출근', 0)
;

-- 조인 테이블 데이터 삽입
INSERT INTO MEMBER_STATIC_TAG (member_id, static_tag_id)
VALUES
    (1, 1),
    (1, 4),
    (1, 10),
    (2, 2)
;