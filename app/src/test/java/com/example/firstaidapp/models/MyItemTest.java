package com.example.firstaidapp.models;

import static org.junit.Assert.*;

import android.graphics.Path;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

public class MyItemTest {

    private MyItem myItem;

    @Before
    public void setUp() {
        myItem = new MyItem(0.0, 0.0, "title", "snippet");
    }

    @Test
    public void getPosition() {
        assertEquals(new LatLng(0.0, 0.0), myItem.getPosition());
    }

    @Test
    public void getTitle() {
        assertEquals("title", myItem.getTitle());
    }

    @Test
    public void getSnippet() {
        assertEquals("snippet", myItem.getSnippet());
    }

    @Test
    public void getZIndex() {
        assertEquals(Optional.of(0f), Optional.ofNullable(myItem.getZIndex()));
    }
}