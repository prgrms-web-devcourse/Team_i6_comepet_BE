INSERT INTO animal(created_at, updated_at, code, name)
VALUES (NOW(), NOW(), '417000', '개'),
       (NOW(), NOW(), '422400', '고양이'),
       (NOW(), NOW(), '429900', '기타')
;

INSERT INTO town (created_at, updated_at, code, name, city_id)
SELECT NOW(), NOW(), '0000000', '전체', id
FROM city
WHERE code = '5690000';
