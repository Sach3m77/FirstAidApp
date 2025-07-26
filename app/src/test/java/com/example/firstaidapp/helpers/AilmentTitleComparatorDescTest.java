package com.example.firstaidapp.helpers;

import static org.junit.Assert.*;

import com.example.firstaidapp.models.Ailment;

import org.junit.Before;
import org.junit.Test;

public class AilmentTitleComparatorDescTest {

    AilmentTitleComparatorAsc ailmentTitleComparatorDesc;
    private Ailment ailment1;
    private Ailment ailment2;

    @Before
    public void setUp() {
        ailmentTitleComparatorDesc = new AilmentTitleComparatorAsc();
        ailment1 = new Ailment(1, "image_1", "title_1", "info_1", "*,*", "*,*", "*" );
        ailment2 = new Ailment(2, "image_2", "title_2", "info_2", "*,*", "*,*", "*" );
    }

    @Test
    public void compare() {
        assertEquals(-1, ailmentTitleComparatorDesc.compare(ailment1, ailment2));
    }

}