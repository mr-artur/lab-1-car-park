package ua.kpi.fict.carpark.entity.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_SUPERADMIN,
    ROLE_ADMIN,
    ROLE_DRIVER;

    private final static int FIRST_INDEX_OF_NAME = 5;

    @Override
    public String getAuthority() {
        return name();
    }

    public String getNameWithoutPrefix() {
        return name().substring(FIRST_INDEX_OF_NAME);
    }

    public String getMessageKey() {
        return getClass().getSimpleName().toLowerCase() + "." + getNameWithoutPrefix().toLowerCase();
    }
}
