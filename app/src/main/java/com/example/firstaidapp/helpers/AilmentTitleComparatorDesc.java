package com.example.firstaidapp.helpers;

import com.example.firstaidapp.models.Ailment;

import java.util.Comparator;

public class AilmentTitleComparatorDesc implements Comparator<Ailment> {
    @Override
    public int compare(Ailment ailment1, Ailment ailment2) {
        return ailment2.getTitle().compareTo(ailment1.getTitle());
    }
}
