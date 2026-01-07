-- Disability Categories
INSERT INTO disability_category (disability_category_id, type) VALUES (1, '지체장애');
INSERT INTO disability_category (disability_category_id, type) VALUES (2, '시각장애');
INSERT INTO disability_category (disability_category_id, type) VALUES (3, '청각장애');
INSERT INTO disability_category (disability_category_id, type) VALUES (4, '발달장애');
INSERT INTO disability_category (disability_category_id, type) VALUES (5, '내부기관장애');
INSERT INTO disability_category (disability_category_id, type) VALUES (6, '기타장애');

-- Help Categories
-- V4 changed 'name' to 'help_type'
INSERT INTO help_category (help_category_id, help_type) VALUES (1, '외출 동행');
INSERT INTO help_category (help_category_id, help_type) VALUES (2, '방문 목욕');
INSERT INTO help_category (help_category_id, help_type) VALUES (3, '방문 간호');
INSERT INTO help_category (help_category_id, help_type) VALUES (4, '가사 지원');
INSERT INTO help_category (help_category_id, help_type) VALUES (5, '정서적 지원');
INSERT INTO help_category (help_category_id, help_type) VALUES (6, '식사 도움');
INSERT INTO help_category (help_category_id, help_type) VALUES (7, '학습 지원');
INSERT INTO help_category (help_category_id, help_type) VALUES (8, '기타 지원');
