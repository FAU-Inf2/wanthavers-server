package de.fau.cs.mad.wanthavers.server;

import java.util.HashMap;

/**
 * Created by philipp on 08.06.16.
 */
public class SingletonManager {

    private static HashMap<Class, Object> map = new HashMap<>();

    public static void add(Object o){
        map.put(o.getClass(), o);
    }

    public static Object get(Class c){
        return map.get(c);
    }

}
