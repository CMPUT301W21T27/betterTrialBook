package com.example.bettertrialbook;

import com.example.bettertrialbook.models.User;

/**
 * You class - represents the current User
 * Allows access to user object from any activity without intents
 */
public class You {
    private static User you;

    /**
     * @param user - Sets the user object that represents 'You'
     */
    public static void setUser(User user){
        you=user;
    }

    /**
     * @return Returns the user object that represents 'You'
     */
    public static User getUser(){
        return you;
    }
}
