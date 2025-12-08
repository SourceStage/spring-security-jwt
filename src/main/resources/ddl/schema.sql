-- ===================== TABLE ROLES =====================
CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    role_name VARCHAR(50) UNIQUE NOT NULL,
    role_description VARCHAR(255),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- ===================== TABLE USERS =====================
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password TEXT NOT NULL,
    full_name VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- ===================== USER ↔ ROLE =====================
CREATE TABLE user_roles (
    user_id INT REFERENCES users(id) ON DELETE CASCADE,
    role_id INT REFERENCES roles(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

-- ===================== DEFAULT ROLES =====================
INSERT INTO roles (role_name, role_description)
VALUES 
    ('admin', 'Full system access'),
    ('user', 'Standard user'),
    ('guest', 'Guest user read-only');

    
    CREATE TABLE refresh_tokens (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),    -- cần extension pgcrypto
    user_id INT REFERENCES users(id) ON DELETE CASCADE,
    
    refresh_token TEXT NOT NULL,                       -- lưu dạng hashed để tránh lộ token
    issued_at TIMESTAMP NOT NULL DEFAULT NOW(),
    expires_at TIMESTAMP NOT NULL,                     -- hạn của refresh token
    revoked BOOLEAN DEFAULT FALSE,                     -- đánh dấu thu hồi
    revoked_at TIMESTAMP,                              -- thời điểm thu hồi
    
    ip_address VARCHAR(50),                            -- IP khi tạo token
    user_agent TEXT,                                   -- trình duyệt, device
    
    created_at TIMESTAMP DEFAULT NOW()
);
