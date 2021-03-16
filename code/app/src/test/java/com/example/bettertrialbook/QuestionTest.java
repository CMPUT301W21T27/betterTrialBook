package com.example.bettertrialbook;

import com.example.bettertrialbook.models.Question;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

public class QuestionTest{

    @Test
    public void testValidate() {
        Question q = new Question();
        assertFalse(q.validate());
        q.setTitle("Sample Title");
        assertFalse(q.validate());
        q.setPosterId("1234");
        assertFalse(q.validate());
        q.setText("Hello there");
        assertFalse(q.validate());
        q.setExperimentId("1234");
        // should only return true once all required fields have been set
        assertTrue(q.validate());
    }
}
