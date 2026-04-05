-- 관리자 계정
-- 비밀번호: admin1234
INSERT INTO members (id, email, name, pwd, role, created_at, update_at)
VALUES (
    UUID_TO_BIN('019d5bc7-dc99-7e1c-acc0-2f081c57496e'),
    'admin@admin.com',
    '관리자',
    '{bcrypt}$2a$10$eF/L1E4PTqvgrOuUcRKqu..C7PolNcP9gtCs8nl4MnG2JT4ARfVXq',
    'ROLE_ADMIN',
    NOW(),
    NOW()
)
ON DUPLICATE KEY UPDATE
    name  = VALUES(name),
    role  = VALUES(role);
