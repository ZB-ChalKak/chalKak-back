-- 테스트를 위한 DML 작성

-- 멤버 데이터 삽입
INSERT INTO member (email, nickname, password, profile_img, height, weight, gender, status, role, provider)
VALUES
    ('user@test.com', '테스트', '$2a$10$wMXNvfMMkAmIaOHy7NbE4OTqKz0F12tNe1xulO06oHdcA40p4c8Te', '/path/to/profile.jpg', 170.5, 65.2, 'MALE', 'ACTIVE', 'USER','CHALKAK'),
    ('admin@test.com', '어드민', '$2a$10$wMXNvfMMkAmIaOHy7NbE4OTqKz0F12tNe1xulO06oHdcA40p4c8Te', null, 162.0, 55.7, 'FEMALE', 'ACTIVE', 'USER', 'CHALKAK')
;

-- 정적 태그 데이터 삽입
-- STYLE 태그 데이터 삽입
INSERT INTO style_tag (category, keyword_img, keyword)
VALUES
    ('STYLE', 'https://spring-photo-bucket.s3.ap-south-1.amazonaws.com/%EC%95%84%EB%A9%94%EC%B9%B4%EC%A7%80.png', '아메카지'),
    ('STYLE', 'https://spring-photo-bucket.s3.ap-south-1.amazonaws.com/%EC%9B%90%EB%A7%88%EC%9D%BC%EC%9B%A8%EC%96%B4.png', '원마일웨어'),
    ('STYLE', 'https://spring-photo-bucket.s3.ap-south-1.amazonaws.com/%EB%AF%B8%EB%8B%88%EB%A9%80.png', '미니멀'),
    ('STYLE', 'https://spring-photo-bucket.s3.ap-south-1.amazonaws.com/%EB%8C%84%EB%94%94.png', '댄디'),
    ('STYLE', 'https://spring-photo-bucket.s3.ap-south-1.amazonaws.com/%EB%B9%84%EC%A6%88%EB%8B%88%EC%8A%A4%EC%BA%90%EC%A3%BC%EC%96%BC.png', '비즈니스캐주얼'),
    ('STYLE', 'https://spring-photo-bucket.s3.ap-south-1.amazonaws.com/%EB%B9%88%ED%8B%B0%EC%A7%80.png', '빈티지'),
    ('STYLE', 'https://spring-photo-bucket.s3.ap-south-1.amazonaws.com/%EC%8A%A4%ED%8A%B8%EB%A6%BF.png', '스트릿'),
    ('STYLE', 'https://spring-photo-bucket.s3.ap-south-1.amazonaws.com/%EC%8A%A4%ED%8F%AC%ED%8B%B0.png', '스포티');

-- TPO 태그 데이터 삽입
INSERT INTO style_tag (category, keyword_img, keyword)
VALUES
    ('TPO', 'https://spring-photo-bucket.s3.ap-south-1.amazonaws.com/%EB%8D%B0%EC%9D%B4%ED%8A%B8%EB%A3%A9.png', '데이트'),
    ('TPO', 'https://spring-photo-bucket.s3.ap-south-1.amazonaws.com/%ED%95%98%EA%B0%9D%EB%A3%A9.png', '하객'),
    ('TPO', 'https://spring-photo-bucket.s3.ap-south-1.amazonaws.com/%EC%97%AC%ED%96%89%EB%A3%A9.png', '여행'),
    ('TPO', 'https://spring-photo-bucket.s3.ap-south-1.amazonaws.com/%EC%B6%9C%EA%B7%BC%EB%A3%A9.png', '출근');

-- SEASON 태그 데이터 삽입
INSERT INTO style_tag (category, keyword_img, keyword)
VALUES
    ('SEASON', '', '봄'),
    ('SEASON', '', '여름'),
    ('SEASON', '', '가을'),
    ('SEASON', '', '겨울');

-- WEATHER 태그 데이터 삽입
INSERT INTO style_tag (category, keyword_img, keyword)
VALUES
    ('WEATHER', '', '맑음'),
    ('WEATHER', '', '흐림'),
    ('WEATHER', '', '비'),
    ('WEATHER', '', '눈');

-- 조인 테이블 데이터 삽입
INSERT INTO member_style_tag (member_id, style_tag_id)
VALUES
    (1, 1),
    (1, 4),
    (1, 10),
    (2, 2)
;

-- 해시 태그 정보 삽입
INSERT INTO hash_tag (keyword)
VALUES ('휴가'),
       ('여름'),
       ('뷰티'),
       ('커피'),
       ('패션')
;

-- 포스트 정보 삽입
INSERT INTO post (member_id, content, view_count, like_count, privacy_height, privacy_weight, location, status)
VALUES (1, '첫번째 포스트 내용', 0, 0, true, true, '경기도 부천시 소사구', 'PUBLIC'),
VALUES (1, '두번째 포스트 내용', 0, 0, true, true, '경기도 부천시 소사구', 'PUBLIC'),
VALUES (1, '세번째 포스트 내용', 0, 0, true, true, '경기도 부천시 소사구', 'PUBLIC')
;

-- 포스트와 스타일 태그 조인 테이블 삽입
INSERT INTO post_style_tag (post_id, style_tag_id)
VALUES (1, 1);

-- 포스트와 해시 태그 조인 테이블 삽입
INSERT INTO post_hash_tag (post_id, hash_tag_id)
VALUES (1, 1);

-- 댓글 정보 삽입
INSERT INTO comments (member_id, post_id, comment)
VALUES  (1, 1, '첫번째 댓글'),
        (1, 1, '두번째 댓글'),
        (2, 1, '세번째 댓글'),
        (2, 1, '네번째 댓글')
;
