package co.netguru.android.inbbbox.db;

import java.io.Serializable;

public interface Storage {
    <EntityClass extends Serializable> EntityClass[] getArray(String s, Class<EntityClass> aClass)
            throws Exception;

    boolean exists(String key) throws Exception;

    <EntityClass> EntityClass getObject(String var1, Class<EntityClass> var2) throws Exception;

    void put(String key, Object[] objects) throws Exception;

    void put(String key, Serializable serializable) throws Exception;

    boolean getBoolean(String var1) throws Exception;

    void putBoolean(String key, boolean value) throws Exception;

    void del(String key) throws Exception;

    <EntityClass extends Serializable> EntityClass get(String key, Class<EntityClass> aClass)
            throws Exception;
}