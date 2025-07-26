package com.example.firstaidapp.models;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class AilmentTest {

    private Ailment fakeAilment;
    private String[] fakeSymptomsArray;
    private String[] rescueActivitiesArray;

    @Before
    public void setUp() {
        fakeAilment = new Ailment(0, "image_0", "title_0", "info_0",
                "symptom1;symptom2", "rescueActivities1;rescueActivities2", "rescueActivitiesImage0" );
        fakeSymptomsArray = new String[]{"symptom1", "symptom2"};
        rescueActivitiesArray = new String[]{"rescueActivities1", "rescueActivities2"};
    }

    @Test
    public void getImage() {
        assertEquals("image_0", fakeAilment.getImage());
    }

    @Test
    public void getTitle() {
        assertEquals("title_0", fakeAilment.getTitle());
    }

    @Test
    public void getInfo() {
        assertEquals("info_0", fakeAilment.getInfo());
    }

    @Test
    public void getRescueActivitiesImage() {
        assertEquals("rescueActivitiesImage0", fakeAilment.getRescueActivitiesImage());
    }

    @Test
    public void getSymptomsArray() {

        assertArrayEquals(fakeSymptomsArray, fakeAilment.getSymptomsArray());
    }

    @Test
    public void getRescueActivitiesArray() {
        assertArrayEquals(rescueActivitiesArray, fakeAilment.getRescueActivitiesArray());
    }
}