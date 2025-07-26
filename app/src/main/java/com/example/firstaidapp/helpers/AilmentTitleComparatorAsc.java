package com.example.firstaidapp.helpers;

import com.example.firstaidapp.models.Ailment;

import java.util.Comparator;

public class AilmentTitleComparatorAsc implements Comparator<Ailment> {
    @Override
    public int compare(Ailment ailment1, Ailment ailment2) {
        return ailment1.getTitle().compareTo(ailment2.getTitle());
    }
}
