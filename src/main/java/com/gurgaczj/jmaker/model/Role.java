package com.gurgaczj.jmaker.model;

public class Role {

    public static String getRole(int accountType){
        switch (accountType){
            case 2:
                return "ROLE_TUTOR";
            case 3:
                return "ROLE_SENIOR_TUTOR";
            case 4:
                return "ROLE_GAME_MASTER";
            case 5:
                return "ROLE_COMMUNITY_MANAGER";
            case 6:
                return "ROLE_ADMIN";
            default:
                return "ROLE_USER";
        }
    }
}
