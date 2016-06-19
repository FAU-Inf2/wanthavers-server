package de.fau.cs.mad.wanthavers.server.sort;

import de.fau.cs.mad.wanthavers.common.Chat;

import java.util.Comparator;

/**
 * Created by Nico on 19.06.2016.
 */
public class ChatUpdatedAtComparator implements Comparator<Chat> {
    @Override
    public int compare(Chat o1, Chat o2) {
        return -o1.getUpdatedAt().compareTo(o2.getUpdatedAt());
    }
}
