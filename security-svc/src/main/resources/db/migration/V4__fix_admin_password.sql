UPDATE security_users
    SET password = '${DEF_ADMIN_PASSWORD_HASH}'
    WHERE email = '${DEF_ADMIN_USER}'