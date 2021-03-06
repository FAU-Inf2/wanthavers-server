package de.fau.cs.mad.wanthavers.server.sort;

import de.fau.cs.mad.wanthavers.common.Category;
import de.fau.cs.mad.wanthavers.common.Chat;

import java.util.Comparator;

public class CategoryNameComperator implements Comparator<Category> {
    @Override
    public int compare(Category o1, Category o2) {
        if(o1.getName() == null)
            return -1;

        if(o2.getName() == null)
            return 1;

        return o1.getName().compareTo(o2.getName());
    }
}
