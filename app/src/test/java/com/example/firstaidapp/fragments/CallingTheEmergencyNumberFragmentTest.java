package com.example.firstaidapp.fragments;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CallingTheEmergencyNumberFragmentTest {

    private CallingTheEmergencyNumberFragment callingTheEmergencyNumberFragment;
    private String text;

    @Before
    public void setUp(){
        callingTheEmergencyNumberFragment = new CallingTheEmergencyNumberFragment();
        text = "test";
        callingTheEmergencyNumberFragment.setText(text);
    }

    @Test
    public void getText() {
        assertEquals(text, callingTheEmergencyNumberFragment.getText());
    }

}