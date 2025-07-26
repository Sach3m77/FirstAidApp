package com.example.firstaidapp.helpers;

import static org.junit.Assert.*;

import com.example.firstaidapp.models.Ailment;

import org.junit.Before;
import org.junit.Test;

public class AilmentTitleComparatorAscTest {
    AilmentTitleComparatorAsc ailmentTitleComparatorAsc;
    private Ailment ailment1;
    private Ailment ailment2;

    @Before
    public void setUp() {
        ailmentTitleComparatorAsc = new AilmentTitleComparatorAsc();
        ailment1 = new Ailment(1, "*", "title_1", "*", "*,*", "*,*", "*");
        ailment2 = new Ailment(2, "*", "title_2", "*", "*,*", "*,*", "*");
    }

    @Test
    public void compare() {
        assertEquals(-1, ailmentTitleComparatorAsc.compare(ailment1, ailment2));
    }
}