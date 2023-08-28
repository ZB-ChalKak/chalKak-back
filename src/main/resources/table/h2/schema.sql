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
    keyword      VARCHAR(100) UNIQUE NOT NULL
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
    password       VARCHAR(60) ,
    profile_img    VARCHAR(255),
    height         DOUBLE,
    weight         DOUBLE,
    gender         VARCHAR(20),
    status         VARCHAR(20),
    role           VARCHAR(20),
    provider           VARCHAR(20),
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
    post_id        BIGINT PRIMARY KEY AUTO_INCREMENT,
    member_id      BIGINT,
    content        TEXT,
    view_count     BIGINT               DEFAULT 0,
    like_count     BIGINT               DEFAULT 0,
    privacy_height BOOLEAN,
    privacy_weight BOOLEAN,
    location       VARCHAR(100),
    status         VARCHAR(20) NOT NULL,
    created_at     TIMESTAMP   NOT NULL DEFAULT NOW(),
    updated_at     TIMESTAMP   NOT NULL DEFAULT NOW(),
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

-- spring batch SEQUENCE 설정 테이블 (H2)

CREATE SEQUENCE BATCH_STEP_EXECUTION_SEQ;
CREATE SEQUENCE BATCH_JOB_EXECUTION_SEQ;
CREATE SEQUENCE BATCH_JOB_SEQ;

-- spring batch SEQUENCE 설정 테이블 (mysql)
--
-- CREATE TABLE BATCH_STEP_EXECUTION_SEQ (ID BIGINT NOT NULL) type=InnoDB;
-- INSERT INTO BATCH_STEP_EXECUTION_SEQ values(0);
-- CREATE TABLE BATCH_JOB_EXECUTION_SEQ (ID BIGINT NOT NULL) type=InnoDB;
-- INSERT INTO BATCH_JOB_EXECUTION_SEQ values(0);
-- CREATE TABLE BATCH_JOB_SEQ (ID BIGINT NOT NULL) type=InnoDB;
-- INSERT INTO BATCH_JOB_SEQ values(0);

-- BATCH_JOB_INSTANCE table BATCH_JOB_INSTANCE 테이블에는 JobInstance에 관련된 모든 정보가 포함되어 있습니다. 또한 해당 Table은 전체 계층 구조의 최상위 역할을 합니다.

CREATE TABLE BATCH_JOB_INSTANCE
(
    JOB_INSTANCE_ID BIGINT PRIMARY KEY,
    VERSION         BIGINT,
    JOB_NAME        VARCHAR(100) NOT NULL,
    JOB_KEY         VARCHAR(2500)
);

-- BATCH_JOB_EXECUTION테이블에는 JobExcution에 관련된 모든 정보를 저장하고 있습니다.
-- JobExcution은 JobInstance가 실행 될 때마다 시작시간, 종료시간, 종료코드 등 다양한 정보를 가지고 있습니다.

CREATE TABLE BATCH_JOB_EXECUTION
(
    JOB_EXECUTION_ID           BIGINT PRIMARY KEY,
    VERSION                    BIGINT,
    JOB_INSTANCE_ID            BIGINT    NOT NULL,
    CREATE_TIME                TIMESTAMP NOT NULL,
    START_TIME                 TIMESTAMP DEFAULT NULL,
    END_TIME                   TIMESTAMP DEFAULT NULL,
    STATUS                     VARCHAR(10),
    EXIT_CODE                  VARCHAR(20),
    EXIT_MESSAGE               VARCHAR(2500),
    LAST_UPDATED               TIMESTAMP,
    JOB_CONFIGURATION_LOCATION VARCHAR(2500) NULL,
    constraint JOB_INSTANCE_EXECUTION_FK foreign key (JOB_INSTANCE_ID)
        references BATCH_JOB_INSTANCE (JOB_INSTANCE_ID)
);

-- BATCH_JOB_EXECUTION_PARAMS 테이블에는 Job을 실행 시킬 때 사용했던 JobParameters에 대한 정보를 저장하고 있습니다.

CREATE TABLE BATCH_JOB_EXECUTION_PARAMS
(
    JOB_EXECUTION_ID BIGINT       NOT NULL,
    TYPE_CD          VARCHAR(6)   NOT NULL,
    KEY_NAME         VARCHAR(100) NOT NULL,
    STRING_VAL       VARCHAR(250),
    DATE_VAL         DATETIME DEFAULT NULL,
    LONG_VAL         BIGINT,
    DOUBLE_VAL       DOUBLE PRECISION,
    IDENTIFYING      CHAR(1)      NOT NULL,
    constraint JOB_EXEC_PARAMS_FK foreign key (JOB_EXECUTION_ID)
        references BATCH_JOB_EXECUTION (JOB_EXECUTION_ID)
);



-- BATCH_JOB_EXECUTION테이블에는 StepExecution에 대한 정보를 저장하고 있습니다.
-- BATCH_JOB_EXECUTION 테이블과 여러 면에서 유사하며 STEP을 EXECUTION 정보인 읽은 수, 커밋 수, 스킵 수 등 다양한 정보를 추가로 담고 있습니다.

CREATE TABLE BATCH_STEP_EXECUTION
(
    STEP_EXECUTION_ID  BIGINT PRIMARY KEY,
    VERSION            BIGINT       NOT NULL,
    STEP_NAME          VARCHAR(100) NOT NULL,
    JOB_EXECUTION_ID   BIGINT       NOT NULL,
    START_TIME         TIMESTAMP    NOT NULL,
    END_TIME           TIMESTAMP DEFAULT NULL,
    STATUS             VARCHAR(10),
    COMMIT_COUNT       BIGINT,
    READ_COUNT         BIGINT,
    FILTER_COUNT       BIGINT,
    WRITE_COUNT        BIGINT,
    READ_SKIP_COUNT    BIGINT,
    WRITE_SKIP_COUNT   BIGINT,
    PROCESS_SKIP_COUNT BIGINT,
    ROLLBACK_COUNT     BIGINT,
    EXIT_CODE          VARCHAR(20),
    EXIT_MESSAGE       VARCHAR(2500),
    LAST_UPDATED       TIMESTAMP,
    constraint JOB_EXECUTION_STEP_FK foreign key (JOB_EXECUTION_ID)
        references BATCH_JOB_EXECUTION (JOB_EXECUTION_ID)
);

-- BATCH_JOB_EXECUTION_CONTEXT테이블에는 JobExecution의ExecutionContext 정보가 들어있습니다.
-- 이 ExecutionContext 데이터는 일반적으로 JobInstance가 실패 시 중단된 위치에서 다시 시작할 수 있는 정보를 저장하고 있습니다.
CREATE TABLE BATCH_JOB_EXECUTION_CONTEXT
(
    JOB_EXECUTION_ID   BIGINT PRIMARY KEY,
    SHORT_CONTEXT      VARCHAR(2500) NOT NULL,
    SERIALIZED_CONTEXT CLOB,
    constraint JOB_EXEC_CTX_FK foreign key (JOB_EXECUTION_ID)
        references BATCH_JOB_EXECUTION (JOB_EXECUTION_ID)
);

-- BATCH_STEP_EXECUTION_CONTEXT테이블에는 StepExecution의 ExecutionContext 정보가 들어있습니다.
-- 이 ExecutionContext 데이터는 일반적으로 JobInstance가 실패 시 중단된 위치에서 다시 시작할 수 있는 정보를 저장하고 있습니다.

CREATE TABLE BATCH_STEP_EXECUTION_CONTEXT
(
    STEP_EXECUTION_ID  BIGINT PRIMARY KEY,
    SHORT_CONTEXT      VARCHAR(2500) NOT NULL,
    SERIALIZED_CONTEXT CLOB,
    constraint STEP_EXEC_CTX_FK foreign key (STEP_EXECUTION_ID)
        references BATCH_STEP_EXECUTION (STEP_EXECUTION_ID)
);