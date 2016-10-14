package co.netguru.android.inbbbox.db;

import android.content.Context;

import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import java.io.Serializable;

import javax.inject.Inject;

public class SynchronizedDb implements Storage{
    final Object lock = new Object();
    Context context;

    @Inject
    public SynchronizedDb(Context context) {
        this.context = context;
    }

    public <EntityClass extends Serializable> EntityClass[] getArray(String s, Class<EntityClass> aClass) throws SnappydbException {
        EntityClass[] result = null;
        SnappydbException ex = null;

        synchronized (lock) {
            DB db = DBFactory.open(context);
            try {
                result = db.getArray(s, aClass);
            } catch (SnappydbException e) {
                ex = e;
            } finally {
                if (db.isOpen()) {
                    db.close();
                }
            }
        }

        if (ex == null) {
            return result;
        } else {
            throw ex;
        }
    }

    public boolean exists(String key) throws SnappydbException {
        boolean result = false;
        SnappydbException ex = null;

        synchronized (lock) {
            DB db = DBFactory.open(context);
            try {
                result = db.exists(key);
            } catch (SnappydbException e) {
                ex = e;
            } finally {
                if (db.isOpen()) {
                    db.close();
                }
            }
        }

        if (ex == null) {
            return result;
        } else {
            throw ex;
        }
    }

    public <EntityClass> EntityClass getObject(String var1, Class<EntityClass> var2) throws SnappydbException {
        EntityClass result = null;
        SnappydbException ex = null;

        synchronized (lock) {
            DB db = DBFactory.open(context);
            try {
                result = db.getObject(var1, var2);
            } catch (SnappydbException e) {
                ex = e;
            } finally {
                if (db.isOpen()) {
                    db.close();
                }
            }
        }

        if (ex == null) {
            return result;
        } else {
            throw ex;
        }
    }

    public void put(String s, Object[] objects) throws SnappydbException {
        SnappydbException ex = null;

        synchronized (lock) {
            DB db = DBFactory.open(context);
            try {
                db.put(s, objects);
            } catch (SnappydbException e) {
                ex = e;
            } finally {
                if (db.isOpen()) {
                    db.close();
                }
            }
        }

        if (ex != null) {
            throw ex;
        }
    }

    public void put(String s, Serializable serializable) throws SnappydbException {
        SnappydbException ex = null;

        synchronized (lock) {
            DB db = DBFactory.open(context);
            try {
                db.put(s, serializable);
            } catch (SnappydbException e) {
                ex = e;
            } finally {
                if (db.isOpen()) {
                    db.close();
                }
            }
        }

        if (ex != null) {
            throw ex;
        }
    }

    public boolean getBoolean(String var1) throws SnappydbException {
        SnappydbException ex = null;
        boolean result = false;
        synchronized (lock) {
            DB db = DBFactory.open(context);
            try {
                result = db.getBoolean(var1);
            } catch (SnappydbException e) {
                ex = e;
            } finally {
                if (db.isOpen()) {
                    db.close();
                }
            }
        }

        if (ex == null) {
            return result;
        } else {
            throw ex;
        }
    }

    public void putBoolean(String s, boolean value) throws SnappydbException {
        SnappydbException ex = null;

        synchronized (lock) {
            DB db = DBFactory.open(context);
            try {
                db.putBoolean(s, value);
            } catch (SnappydbException e) {
                ex = e;
            } finally {
                if (db.isOpen()) {
                    db.close();
                }
            }
        }

        if (ex != null) {
            throw ex;
        }
    }

    public void del(String key) throws SnappydbException {
        SnappydbException ex = null;

        synchronized (lock) {
            DB db = DBFactory.open(context);
            try {
                db.del(key);
            } catch (SnappydbException e) {
                ex = e;
            } finally {
                if (db.isOpen()) {
                    db.close();
                }
            }
        }

        if (ex != null) {
            throw ex;
        }
    }

    public <EntityClass extends Serializable> EntityClass get(String s, Class<EntityClass> aClass) throws SnappydbException {
        EntityClass result = null;
        SnappydbException ex = null;

        synchronized (lock) {
            DB db = DBFactory.open(context);
            try {
                result = db.get(s, aClass);
            } catch (SnappydbException e) {
                ex = e;
            } finally {
                if (db.isOpen()) {
                    db.close();
                }
            }
        }

        if (ex == null) {
            return result;
        } else {
            throw ex;
        }
    }
}
