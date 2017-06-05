package Olive.Pets;

import java.util.Map;

public class Mediator
{
    static Map<String, Object> map;

    public static void set(String key, Object obj)
    {
        map.put(key, obj);
    }

    public static Object get(String key)
    {
        if(map.containsKey(key))
        {
            return map.get(key);
        }
        return null;
    }

}
