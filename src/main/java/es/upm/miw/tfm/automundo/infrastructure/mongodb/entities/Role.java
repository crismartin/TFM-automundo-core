package es.upm.miw.tfm.automundo.infrastructure.mongodb.entities;

public enum Role {
    ADMIN, AUTHENTICATED;

    public static final String PREFIX = "ROLE_";

    public static Role of(String withPrefix) {
        return Role.valueOf(withPrefix.replace(Role.PREFIX, ""));
    }

    public String withPrefix() {
        return PREFIX + this.toString();
    }
}
