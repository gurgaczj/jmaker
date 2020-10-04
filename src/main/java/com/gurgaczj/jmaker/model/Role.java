package com.gurgaczj.jmaker.model;

public enum Role {

    ROLE_USER, ROLE_TUTOR, ROLE_SENIOR_TUTOR, ROLE_GAME_MASTER, ROLE_COMMUNITY_MANAGER, ROLE_ADMIN;

    public static Role getRole(int accountType) {
        switch (accountType) {
            case 2:
                return ROLE_TUTOR;
            case 3:
                return ROLE_SENIOR_TUTOR;
            case 4:
                return ROLE_GAME_MASTER;
            case 5:
                return ROLE_COMMUNITY_MANAGER;
            case 6:
                return ROLE_ADMIN;
            default:
                return ROLE_USER;
        }
    }
}
