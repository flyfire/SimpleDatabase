package com.solarexsoft.solarexdatabase;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;

/**
 * <pre>
 *    Author: houruhou
 *    Project: https://solarex.github.io/projects
 *    CreatAt: 07/06/2017
 *    Desc:
 * </pre>
 */

public class DaoManager {
    private String mDBPath;
    private SQLiteDatabase mSQLiteDatabase;

    private static volatile DaoManager sInstance;

    public DaoManager(String s) {
        mDBPath = s;
        mSQLiteDatabase = SQLiteDatabase.openOrCreateDatabase(mDBPath, null);
    }

    public static DaoManager getInstance() {
        if (sInstance == null) {
            synchronized (DaoManager.class) {
                if (sInstance == null) {
                    sInstance = new DaoManager(Environment.getExternalStorageDirectory()
                            .getAbsolutePath() + "/solarex.db");
                }
            }
        }
        return sInstance;
    }

    public static DaoManager getInstance(File file) {
        if (sInstance == null) {
            synchronized (DaoManager.class) {
                if (sInstance == null) {
                    sInstance = new DaoManager(file.getAbsolutePath());
                }
            }
        }
        return sInstance;
    }

    public synchronized <K extends BaseDao<V>, V> K getDao(Class<K> daoClz, Class<V> beanClz) {
        BaseDao baseDao = null;
        try {
            baseDao = daoClz.newInstance();
            baseDao.init(beanClz, mSQLiteDatabase);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (K) baseDao;
    }
}
