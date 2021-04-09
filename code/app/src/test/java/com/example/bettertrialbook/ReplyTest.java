package com.example.bettertrialbook;

import com.example.bettertrialbook.models.Reply;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit testing for posting an answer
 */
public class ReplyTest {

    @Test
    public void testValidate() {
        Reply reply = new Reply();
        reply.setQuestionId("1234");
        assertFalse(reply.validate());
        reply.setPosterId("1234");
        assertFalse(reply.validate());
        reply.setExperimentId("1234");
        reply.setText("Reply text");
        assertTrue(reply.validate());
    }
}
