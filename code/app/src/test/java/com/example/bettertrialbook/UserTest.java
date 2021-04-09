package com.example.bettertrialbook;

import com.example.bettertrialbook.models.User;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit testing for User functionality
 * If parcelable causes problems: https://stackoverflow.com/a/54873453
 */
public class UserTest {

    User test;

    private User mockUser(){
        return new User("1234");
    }

    @Before
    public void setUp(){
        test = mockUser();
    }

    @Test
    public void createUserTest(){
        User compTest = new User("1234");
        assertEquals(test,compTest);
    }

    @Test
    public void testID(){
        assertEquals(test.getID(),"1234");
    }

    @Test
    public void updateUsername(){
        test.setUsername("UsernameTest01");
        assertEquals(test.getUsername(),"UsernameTest01");
    }

    @Test
    public void updateEmail(){
        test.getContact().setEmail("test@gmail.com");
        assertEquals(test.getContact().getEmail(),"test@gmail.com");

        test.setContact("test2@gmail.com","");
        assertEquals(test.getContact().getEmail(),"test2@gmail.com");
    }

    @Test
    public void updatePhone(){
        test.getContact().setPhone("123-56789");
        assertEquals(test.getContact().getPhone(),"123-56789");

        test.setContact("","456-7890");
        assertEquals(test.getContact().getPhone(),"456-7890");
    }

}


