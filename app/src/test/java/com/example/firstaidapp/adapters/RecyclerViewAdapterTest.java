package com.example.firstaidapp.adapters;

import static org.junit.Assert.*;

import com.example.firstaidapp.models.Ailment;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapterTest {

    private List<Ailment> fakeAilmentsList;
    private RecyclerViewAdapter recyclerViewAdapter;


    @Before
    public void setUp() {
        fakeAilmentsList = new ArrayList<>();
        fakeAilmentsList.add(new Ailment(0, "image_0", "title_0", "info_0", "*;*", "*;*", "*"));
        fakeAilmentsList.add(new Ailment(1, "image_1", "title_1", "info_1", "*;*", "*;*", "*"));
        fakeAilmentsList.add(new Ailment(2, "image_2", "title_2", "info_2", "*;*", "*;*", "*"));
        fakeAilmentsList.add(new Ailment(3, "image_3", "title_3", "info_3", "*;*", "*;*", "*"));
        fakeAilmentsList.add(new Ailment(4, "image_4", "title_4", "info_4", "*;*", "*;*", "*"));
        recyclerViewAdapter = new RecyclerViewAdapter(fakeAilmentsList, null);
    }

    @Test
    public void getItemCount() {
        assertEquals(fakeAilmentsList.size(), recyclerViewAdapter.getItemCount());
    }

}