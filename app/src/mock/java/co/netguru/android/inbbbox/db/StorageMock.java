package co.netguru.android.inbbbox.db;

public class StorageMock implements Storage {


    @Override
    public <EntityClass extends Serializable> EntityClass[] getArray(String s, Class<EntityClass> aClass) throws Exception {
        return null;
    }

    @Override
    public boolean exists(String key) throws Exception {
        return false;
    }

    @Override
    public <EntityClass> EntityClass getObject(String var1, Class<EntityClass> var2) throws Exception {
        return null;
    }

    @Override
    public void put(String key, Object[] objects) throws Exception {

    }

    @Override
    public void put(String key, Serializable serializable) throws Exception {

    }

    @Override
    public boolean getBoolean(String var1) throws Exception {
        return false;
    }

    @Override
    public void putBoolean(String key, boolean value) throws Exception {

    }

    @Override
    public void del(String key) throws Exception {

    }

    @Override
    public <EntityClass extends Serializable> EntityClass get(String key, Class<EntityClass> aClass) throws Exception {
        return null;
    }
}
