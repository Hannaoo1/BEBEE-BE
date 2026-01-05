-- ========================================
-- V8: Chatroom 더미 데이터 (match-service Post 기반)
-- ========================================
-- match-service의 실제 Post 데이터를 기반으로 한 채팅방
-- 조건: Post 작성자(장애인) + 도우미, status가 PROCEEDING 또는 MATCHED인 Post만
--
-- Post 작성자 (모두 장애인):
--   - 100 (김철수)
--   - 200 (김민수)
--   - 300 (이영희)
--
-- 도우미:
--   - 700 (강지훈)
--   - 800 (윤서연)
--   - 900 (임동현)
--   - 1000 (한미래)

-- ========================================
-- PROCEEDING 상태 Post 기반 채팅방 (진행 중)
-- ========================================

-- Post 1006: 김철수(100) - 은행 업무 동행 (PROCEEDING)
INSERT INTO chatroom (chatroom_id, member1_id, member2_id, title, post_id, last_message, match_status, created_at, updated_at) VALUES
(1, 100, 700, '은행 업무 동행', 1006, '내일 오전 10시에 만나요', 'PROCEEDING', NOW() - INTERVAL 1 DAY, NOW()),

-- Post 1013: 이영희(300) - 어르신 목욕 보조 (PROCEEDING)
(2, 300, 800, '어르신 목욕 보조', 1013, '목요일 오후 2시 가능하세요?', 'PROCEEDING', NOW() - INTERVAL 4 DAY, NOW()),

-- Post 1019: 이영희(300) - 반신마비 환자 목욕 (PROCEEDING)
(3, 300, 900, '반신마비 환자 목욕', 1019, '경험이 있으신가요?', 'PROCEEDING', NOW() - INTERVAL 6 DAY, NOW()),

-- Post 1024: 김철수(100) - 투약 관리 도움 (PROCEEDING)
(4, 100, 1000, '투약 관리 도움', 1024, '매일 아침 저녁 방문 가능하세요?', 'PROCEEDING', NOW() - INTERVAL 9 DAY, NOW()),

-- Post 1029: 김민수(200) - 위루관 영양 주입 (PROCEEDING)
(5, 200, 700, '위루관 영양 주입', 1029, '간호 경험 있으신 분 찾아요', 'PROCEEDING', NOW() - INTERVAL 10 DAY, NOW()),

-- Post 1034: 이영희(300) - 정리정돈 서비스 (PROCEEDING)
(6, 300, 800, '정리정돈 서비스', 1034, '주 2회 방문 부탁드려요', 'PROCEEDING', NOW() - INTERVAL 13 DAY, NOW()),

-- Post 1040: 이영희(300) - 베란다 정리 (PROCEEDING)
(7, 300, 900, '베란다 정리', 1040, '큰 짐 옮기는 것 도와주세요', 'PROCEEDING', NOW() - INTERVAL 14 DAY, NOW()),

-- Post 1044: 김민수(200) - 반찬 만들기 (PROCEEDING)
(8, 200, 1000, '반찬 만들기', 1044, '다음 주 월요일에 와주세요', 'PROCEEDING', NOW() - INTERVAL 16 DAY, NOW()),

-- Post 1049: 이영희(300) - 간식 준비 (PROCEEDING)
(9, 300, 700, '간식 준비', 1049, '당뇨 환자용 간식 만들어주세요', 'PROCEEDING', NOW() - INTERVAL 17 DAY, NOW()),

-- Post 1053: 김민수(200) - 스마트폰 사용법 (PROCEEDING)
(10, 200, 800, '스마트폰 사용법', 1053, '카카오톡 사용법 알려주세요', 'PROCEEDING', NOW() - INTERVAL 18 DAY, NOW()),

-- Post 1058: 이영희(300) - 음악 감상 지도 (PROCEEDING)
(11, 300, 900, '음악 감상 지도', 1058, '클래식 추천 부탁드려요', 'PROCEEDING', NOW() - INTERVAL 20 DAY, NOW()),

-- Post 1062: 김민수(200) - 산책 동반자 (PROCEEDING)
(12, 200, 1000, '산책 동반자', 1062, '매일 오후 4시에 같이 산책해요', 'PROCEEDING', NOW() - INTERVAL 21 DAY, NOW()),

-- Post 1067: 이영희(300) - 종교 활동 동행 (PROCEEDING)
(13, 300, 700, '종교 활동 동행', 1067, '주일 오전 11시 예배 함께 가요', 'PROCEEDING', NOW() - INTERVAL 22 DAY, NOW()),

-- Post 1072: 김철수(100) - 택배 수령 대행 (PROCEEDING)
(14, 100, 800, '택배 수령 대행', 1072, '낮 시간에 택배 받아주세요', 'PROCEEDING', NOW() - INTERVAL 24 DAY, NOW()),

-- Post 1076: 이영희(300) - 가전제품 사용법 (PROCEEDING)
(15, 300, 900, '가전제품 사용법', 1076, '새로 산 세탁기 사용법 알려주세요', 'PROCEEDING', NOW() - INTERVAL 25 DAY, NOW()),

-- Post 1080: 김민수(200) - 의류 수선 맡기기 (PROCEEDING)
(16, 200, 1000, '의류 수선 맡기기', 1080, '바지 기장 줄여야 해요', 'PROCEEDING', NOW() - INTERVAL 26 DAY, NOW()),

-- Post 1083: 김민수(200) - 오후 돌봄 서비스 (PROCEEDING)
(17, 200, 700, '오후 돌봄 서비스', 1083, '점심 식사 후 산책 도와주세요', 'PROCEEDING', NOW() - INTERVAL 27 DAY, NOW()),

-- Post 1086: 김민수(200) - 병원 케어 서비스 (PROCEEDING)
(18, 200, 800, '병원 케어 서비스', 1086, '병원 동행 및 간호 부탁드려요', 'PROCEEDING', NOW() - INTERVAL 28 DAY, NOW()),

-- Post 1090: 김철수(100) - 맞춤형 돌봄 (PROCEEDING)
(19, 100, 900, '맞춤형 돌봄', 1090, '개인별 맞춤 서비스 상담 원해요', 'PROCEEDING', NOW() - INTERVAL 29 DAY, NOW()),

-- Post 1092: 김민수(200) - 야간 간호 서비스 (PROCEEDING)
(20, 200, 1000, '야간 간호 서비스', 1092, '밤샘 간호 가능하신가요?', 'PROCEEDING', NOW() - INTERVAL 30 DAY, NOW()),

-- Post 1096: 김철수(100) - 퇴원 후 케어 (PROCEEDING)
(21, 100, 700, '퇴원 후 케어', 1096, '퇴원 후 2주간 집중 케어 필요해요', 'PROCEEDING', NOW() - INTERVAL 30 DAY, NOW()),

-- Post 1100: 이영희(300) - 재활 운동 코치 (PROCEEDING)
(22, 300, 800, '재활 운동 코치', 1100, '뇌졸중 환자 재활 경험 있나요?', 'PROCEEDING', NOW() - INTERVAL 30 DAY, NOW())

ON DUPLICATE KEY UPDATE
    member1_id = VALUES(member1_id),
    member2_id = VALUES(member2_id),
    title = VALUES(title),
    post_id = VALUES(post_id),
    last_message = VALUES(last_message),
    match_status = VALUES(match_status),
    updated_at = NOW();


-- ========================================
-- MATCHED 상태 Post 기반 추가 채팅방 (매칭 완료)
-- ========================================
-- 매칭이 완료된 게시글은 이미 도우미가 선정되어 활동 중인 상태

INSERT INTO chatroom (chatroom_id, member1_id, member2_id, title, post_id, last_message, match_status, created_at, updated_at) VALUES
-- 김철수(100)의 매칭 완료 채팅방
(23, 100, 700, '병원 동행 도와주실 분', 1001, '다음 주 화요일 오전 10시에 병원 갈게요', 'MATCHED', NOW() - INTERVAL 3 HOUR, NOW()),
(24, 100, 800, '문화센터 수업 동행', 1003, '월, 목 오후 2시 문화센터 수업이에요', 'MATCHED', NOW() - INTERVAL 15 HOUR, NOW()),
(25, 100, 900, '쇼핑몰 쇼핑 동행', 1009, '이번 주말 백화점 같이 가요', 'MATCHED', NOW() - INTERVAL 2 DAY - INTERVAL 18 HOUR, NOW()),
(26, 100, 1000, '주말 방문 목욕', 1012, '토요일 오전 9시에 와주세요', 'MATCHED', NOW() - INTERVAL 4 DAY - INTERVAL 6 HOUR, NOW()),
(27, 100, 700, '침상 목욕 도움', 1018, '목, 일 오전에 침상 목욕 부탁드려요', 'MATCHED', NOW() - INTERVAL 6 DAY - INTERVAL 10 HOUR, NOW()),
(28, 100, 800, '당뇨 환자 혈당 체크', 1021, '매일 아침 7시 혈당 체크해주세요', 'MATCHED', NOW() - INTERVAL 8 DAY, NOW()),
(29, 100, 900, '기관지 흡인 케어', 1030, '하루 3회 기관지 흡인 처치 필요해요', 'MATCHED', NOW() - INTERVAL 11 DAY, NOW()),
(30, 100, 1000, '설거지 도움', 1033, '저녁 식사 후 설거지 부탁드려요', 'MATCHED', NOW() - INTERVAL 12 DAY - INTERVAL 16 HOUR, NOW()),
(31, 100, 700, '화장실 청소', 1036, '주 2회 화장실 청소 해주세요', 'MATCHED', NOW() - INTERVAL 13 DAY - INTERVAL 12 HOUR, NOW()),
(32, 100, 800, '환기 및 먼지 청소', 1039, '월, 금 오전에 환기하고 청소해요', 'MATCHED', NOW() - INTERVAL 14 DAY - INTERVAL 6 HOUR, NOW()),
(33, 100, 900, '점심 도시락 만들기', 1042, '평일 점심 도시락 준비 부탁해요', 'MATCHED', NOW() - INTERVAL 15 DAY - INTERVAL 7 HOUR, NOW()),
(34, 100, 1000, '식사 보조', 1045, '하루 3끼 식사 보조 필요합니다', 'MATCHED', NOW() - INTERVAL 16 DAY - INTERVAL 6 HOUR, NOW()),
(35, 100, 700, '식재료 손질', 1048, '장 보고 오면 재료 손질 도와주세요', 'MATCHED', NOW() - INTERVAL 17 DAY, NOW()),
(36, 100, 800, '영어 회화 연습', 1051, '주 2회 영어 회화 연습해요', 'MATCHED', NOW() - INTERVAL 18 DAY, NOW()),
(37, 100, 900, '독서 도움', 1054, '매일 저녁 책 읽어주세요', 'MATCHED', NOW() - INTERVAL 19 DAY, NOW()),
(38, 100, 1000, '그림 그리기 교육', 1057, '주말에 같이 그림 그려요', 'MATCHED', NOW() - INTERVAL 19 DAY - INTERVAL 18 HOUR, NOW()),
(39, 100, 700, '글쓰기 연습', 1060, '매일 일기 쓰기 도와주세요', 'MATCHED', NOW() - INTERVAL 20 DAY - INTERVAL 16 HOUR, NOW()),
(40, 100, 800, '게임 상대', 1063, '오후에 보드게임 같이 해요', 'MATCHED', NOW() - INTERVAL 21 DAY - INTERVAL 14 HOUR, NOW()),
(41, 100, 900, '라디오 청취 동반', 1066, '오전 라디오 들으며 수다 떨어요', 'MATCHED', NOW() - INTERVAL 22 DAY - INTERVAL 12 HOUR, NOW()),
(42, 100, 1000, '정원 가꾸기', 1069, '주말에 베란다 화분 가꿔요', 'MATCHED', NOW() - INTERVAL 23 DAY - INTERVAL 8 HOUR, NOW()),
(43, 100, 700, '전구 교체', 1075, '전구 갈아끼우는 거 도와주세요', 'MATCHED', NOW() - INTERVAL 25 DAY - INTERVAL 6 HOUR, NOW()),
(44, 100, 800, '약국 대리 방문', 1078, '약 처방전 가지고 받아와 주세요', 'MATCHED', NOW() - INTERVAL 26 DAY, NOW()),
(45, 100, 900, '종합 돌봄 서비스', 1081, '외출동행, 가사지원, 식사도움 종합 서비스 필요해요', 'MATCHED', NOW() - INTERVAL 27 DAY, NOW()),
(46, 100, 1000, '전일 케어', 1084, '아침부터 저녁까지 돌봄 서비스 부탁드려요', 'MATCHED', NOW() - INTERVAL 28 DAY, NOW()),
(47, 100, 700, '재활 지원 서비스', 1087, '물리치료 동행과 운동 보조 필요합니다', 'MATCHED', NOW() - INTERVAL 28 DAY - INTERVAL 18 HOUR, NOW()),
(48, 100, 800, '휴일 돌봄 서비스', 1093, '공휴일에도 돌봄 가능하신가요?', 'MATCHED', NOW() - INTERVAL 30 DAY - INTERVAL 6 HOUR, NOW()),

-- 김민수(200)의 매칭 완료 채팅방
(49, 200, 900, '주말 공원 산책 동행', 1002, '토요일 오전 공원에서 만나요', 'MATCHED', NOW() - INTERVAL 8 HOUR, NOW()),
(50, 200, 1000, '도서관 이용 도움', 1005, '수요일 오후 도서관 같이 가요', 'MATCHED', NOW() - INTERVAL 1 DAY - INTERVAL 5 HOUR, NOW()),
(51, 200, 700, '정기 통원 치료 동행', 1008, '화, 목 오전 물리치료 동행 부탁해요', 'MATCHED', NOW() - INTERVAL 2 DAY - INTERVAL 6 HOUR, NOW()),
(52, 200, 800, '주 2회 방문 목욕 서비스', 1011, '화, 목 저녁 7시에 와주세요', 'MATCHED', NOW() - INTERVAL 4 DAY, NOW()),
(53, 200, 900, '장애인 샤워 도움', 1014, '주 3회 샤워 보조 필요해요', 'MATCHED', NOW() - INTERVAL 5 DAY, NOW()),
(54, 200, 1000, '전신 목욕 서비스', 1017, '일요일 오전 전신 목욕 부탁드려요', 'MATCHED', NOW() - INTERVAL 6 DAY, NOW()),
(55, 200, 700, '장기 목욕 서비스', 1020, '3개월간 주 2회 목욕 서비스 계약해요', 'MATCHED', NOW() - INTERVAL 7 DAY, NOW()),
(56, 200, 800, '혈압 측정 서비스', 1023, '주 3회 혈압 측정하고 기록해주세요', 'MATCHED', NOW() - INTERVAL 8 DAY - INTERVAL 16 HOUR, NOW()),
(57, 200, 900, '도뇨관 관리', 1026, '도뇨관 교체 시기에 도와주세요', 'MATCHED', NOW() - INTERVAL 9 DAY - INTERVAL 14 HOUR, NOW()),
(58, 200, 1000, '빨래 및 다림질', 1032, '주 1회 빨래와 다림질 해주세요', 'MATCHED', NOW() - INTERVAL 12 DAY - INTERVAL 8 HOUR, NOW()),
(59, 200, 700, '주방 청소 도움', 1035, '화, 금 주방 청소 부탁드려요', 'MATCHED', NOW() - INTERVAL 13 DAY - INTERVAL 6 HOUR, NOW()),
(60, 200, 800, '창문 청소', 1038, '분기별 창문 청소 서비스 신청해요', 'MATCHED', NOW() - INTERVAL 14 DAY, NOW()),
(61, 200, 900, '아침 식사 준비', 1041, '평일 아침 식사 준비 도와주세요', 'MATCHED', NOW() - INTERVAL 15 DAY, NOW()),
(62, 200, 1000, '저녁 식사 보조', 1043, '저녁 식사 먹을 때 보조 필요해요', 'MATCHED', NOW() - INTERVAL 15 DAY - INTERVAL 14 HOUR, NOW()),
(63, 200, 700, '영양식 조리', 1047, '당뇨식 조리 가능하신 분 찾아요', 'MATCHED', NOW() - INTERVAL 16 DAY - INTERVAL 18 HOUR, NOW()),
(64, 200, 800, '특별식 조리', 1050, '저염식 조리 부탁드려요', 'MATCHED', NOW() - INTERVAL 17 DAY - INTERVAL 16 HOUR, NOW()),
(65, 200, 900, '수화 배우기', 1056, '수화 기초부터 가르쳐주세요', 'MATCHED', NOW() - INTERVAL 19 DAY - INTERVAL 12 HOUR, NOW()),
(66, 200, 1000, '악기 연주 배우기', 1059, '피아노 기초 배우고 싶어요', 'MATCHED', NOW() - INTERVAL 20 DAY - INTERVAL 8 HOUR, NOW()),
(67, 200, 700, '취미 활동 함께하기', 1065, '뜨개질 같이 해요', 'MATCHED', NOW() - INTERVAL 22 DAY - INTERVAL 6 HOUR, NOW()),
(68, 200, 800, '운동 보조', 1068, '재활 운동 같이 해주세요', 'MATCHED', NOW() - INTERVAL 23 DAY, NOW()),
(69, 200, 900, '애완동물 산책', 1070, '강아지 산책 대신 해주세요', 'MATCHED', NOW() - INTERVAL 23 DAY - INTERVAL 16 HOUR, NOW()),
(70, 200, 1000, '우편물 확인', 1071, '주 1회 우편물 확인하고 정리해요', 'MATCHED', NOW() - INTERVAL 24 DAY, NOW()),
(71, 200, 700, '화초 물주기', 1074, '화, 목 화분에 물 주세요', 'MATCHED', NOW() - INTERVAL 25 DAY, NOW()),
(72, 200, 800, '휴대폰 충전', 1077, '매일 휴대폰 충전 확인 부탁해요', 'MATCHED', NOW() - INTERVAL 25 DAY - INTERVAL 18 HOUR, NOW()),
(73, 200, 900, '공과금 납부', 1079, '은행 가서 공과금 납부 대행해요', 'MATCHED', NOW() - INTERVAL 26 DAY - INTERVAL 8 HOUR, NOW()),
(74, 200, 1000, '오전 케어 서비스', 1082, '아침 식사 준비와 청소 함께 해주세요', 'MATCHED', NOW() - INTERVAL 27 DAY - INTERVAL 7 HOUR, NOW()),
(75, 200, 700, '주말 돌봄 서비스', 1085, '주말 외출동행과 식사 준비 부탁해요', 'MATCHED', NOW() - INTERVAL 28 DAY - INTERVAL 6 HOUR, NOW()),
(76, 200, 800, '통합 가사 서비스', 1088, '청소, 빨래, 설거지 통합 서비스 원해요', 'MATCHED', NOW() - INTERVAL 29 DAY, NOW()),
(77, 200, 900, '생활 밀착 케어', 1089, '일상생활 전반 도움 필요합니다', 'MATCHED', NOW() - INTERVAL 29 DAY - INTERVAL 8 HOUR, NOW()),

-- 이영희(300)의 매칭 완료 채팅방
(78, 300, 1000, '마트 장보기 동행', 1004, '주 1회 마트 장보기 도와주세요', 'MATCHED', NOW() - INTERVAL 1 DAY, NOW()),
(79, 300, 700, '관공서 방문 동행', 1007, '구청 방문 시 동행 부탁드려요', 'MATCHED', NOW() - INTERVAL 2 DAY, NOW()),
(80, 300, 800, '미용실 방문 동행', 1010, '월 1회 미용실 같이 가요', 'MATCHED', NOW() - INTERVAL 3 DAY, NOW()),
(81, 300, 900, '방문 목욕 정기 서비스', 1015, '월, 수, 금 정기 방문 목욕 부탁해요', 'MATCHED', NOW() - INTERVAL 5 DAY - INTERVAL 8 HOUR, NOW()),
(82, 300, 1000, '주중 오전 목욕 도움', 1016, '월, 수, 금 오전 목욕 보조 요청해요', 'MATCHED', NOW() - INTERVAL 5 DAY - INTERVAL 16 HOUR, NOW()),
(83, 300, 700, '상처 드레싱 케어', 1022, '욕창 상처 드레싱 간호 필요해요', 'MATCHED', NOW() - INTERVAL 8 DAY - INTERVAL 8 HOUR, NOW()),
(84, 300, 800, '방문 물리치료', 1025, '주 2회 방문 물리치료 서비스 원해요', 'MATCHED', NOW() - INTERVAL 9 DAY - INTERVAL 6 HOUR, NOW()),
(85, 300, 900, '욕창 관리 간호', 1028, '욕창 예방 및 관리 간호 서비스 필요해요', 'MATCHED', NOW() - INTERVAL 10 DAY - INTERVAL 8 HOUR, NOW()),
(86, 300, 1000, '주 3회 청소 도움', 1031, '월, 수, 금 오전 집안 청소 부탁해요', 'MATCHED', NOW() - INTERVAL 12 DAY, NOW()),
(87, 300, 700, '이불 빨래 서비스', 1037, '월 1회 이불 빨래 도와주세요', 'MATCHED', NOW() - INTERVAL 13 DAY - INTERVAL 18 HOUR, NOW()),
(88, 300, 800, '유동식 준비', 1046, '유동식 만들어주실 분 찾아요', 'MATCHED', NOW() - INTERVAL 16 DAY - INTERVAL 12 HOUR, NOW()),
(89, 300, 900, '컴퓨터 활용 교육', 1052, '컴퓨터 기초 사용법 알려주세요', 'MATCHED', NOW() - INTERVAL 18 DAY - INTERVAL 7 HOUR, NOW()),
(90, 300, 1000, '점자 학습 지원', 1055, '점자 배우는 것 도와주세요', 'MATCHED', NOW() - INTERVAL 19 DAY - INTERVAL 6 HOUR, NOW()),
(91, 300, 700, '대화 상대', 1061, '말벗이 되어주세요', 'MATCHED', NOW() - INTERVAL 21 DAY, NOW()),
(92, 300, 800, '영화 감상 동행', 1064, '주말 영화 보러 갈 친구 찾아요', 'MATCHED', NOW() - INTERVAL 22 DAY, NOW()),
(93, 300, 900, '쓰레기 배출', 1073, '분리수거 도와주세요', 'MATCHED', NOW() - INTERVAL 24 DAY - INTERVAL 14 HOUR, NOW()),
(94, 300, 1000, '긴급 외출 동행', 1091, '오늘 긴급하게 병원 동행 필요해요', 'MATCHED', NOW() - INTERVAL 30 DAY, NOW()),
(95, 300, 700, '장거리 이동 동행', 1094, '타지역 병원 가는 것 동행해주세요', 'MATCHED', NOW() - INTERVAL 30 DAY - INTERVAL 9 HOUR, NOW()),
(96, 300, 800, '입원 동행 서비스', 1095, '입원 수속 및 초기 케어 도와주세요', 'MATCHED', NOW() - INTERVAL 30 DAY - INTERVAL 12 HOUR, NOW()),
(97, 300, 900, '응급 상황 대응', 1097, '응급 상황 시 연락 가능한 분 찾아요', 'MATCHED', NOW() - INTERVAL 30 DAY - INTERVAL 18 HOUR, NOW()),
(98, 300, 1000, '특별 식단 조리', 1098, '암 환자 특별 식단 조리 부탁해요', 'MATCHED', NOW() - INTERVAL 30 DAY - INTERVAL 20 HOUR, NOW()),
(99, 300, 700, '전문 간병 서비스', 1099, '중증 환자 전문 간병 필요합니다', 'MATCHED', NOW() - INTERVAL 30 DAY - INTERVAL 22 HOUR, NOW())

ON DUPLICATE KEY UPDATE
    member1_id = VALUES(member1_id),
    member2_id = VALUES(member2_id),
    title = VALUES(title),
    post_id = VALUES(post_id),
    last_message = VALUES(last_message),
    match_status = VALUES(match_status),
    updated_at = NOW();