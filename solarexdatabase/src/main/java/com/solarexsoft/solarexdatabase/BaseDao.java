package com.solarexsoft.solarexdatabase;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.solarexsoft.solarexdatabase.interfaces.DBColumn;
import com.solarexsoft.solarexdatabase.interfaces.DBTable;
import com.solarexsoft.solarexdatabase.interfaces.IBaseDao;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 *    Author: houruhou
 *    Project: https://solarex.github.io/projects
 *    CreatAt: 07/06/2017
 *    Desc:
 * </pre>
 */

public abstract class BaseDao<T> implements IBaseDao<T> {
    private SQLiteDatabase mSQLiteDatabase;
    private volatile boolean isInit = false;
    private String mTableName;
    private Class<T> mEntityClass;
    private Map<String, Field> mCacheMap = new HashMap<>();


    public synchronized boolean init(Class<T> beanClz, SQLiteDatabase sqLiteDatabase) {
        if (!isInit) {
            this.mSQLiteDatabase = sqLiteDatabase;
            this.mTableName = beanClz.getAnnotation(DBTable.class).value();
            this.mEntityClass = beanClz;
            if (!sqLiteDatabase.isOpen()) {
                return false;
            }

            if (!TextUtils.isEmpty(createTable())) {
                mSQLiteDatabase.execSQL(createTable());
            }

            initCacheMap();
            isInit = true;
        }
        return true;
    }

    private void initCacheMap() {
        String getColumnSql = "select * from " + this.mTableName + " limit 1, 0";
        Cursor cursor = null;
        cursor = this.mSQLiteDatabase.rawQuery(getColumnSql, null);
        String[] tableColumnNames = cursor.getColumnNames();
        Field[] beanClzFields = this.mEntityClass.getDeclaredFields();
        for (Field beanClzField : beanClzFields) {
            beanClzField.setAccessible(true);
        }
        for (String tableColumnName : tableColumnNames) {
            Field tableColumnField = null;
            for (Field beanClzField : beanClzFields) {
                String fieldName = beanClzField.getAnnotation(DBColumn.class).value();
                if (fieldName == null) {
                    fieldName = beanClzField.getName();
                }
                if (tableColumnName.equals(fieldName)) {
                    tableColumnField = beanClzField;
                    break;
                }
            }
            if (tableColumnField != null) {
                this.mCacheMap.put(tableColumnName, tableColumnField);
            }
        }
        IOUtils.close(cursor);

    }

    @Override
    public Long insert(T entity) {
        return null;
    }

    @Override
    public int update(T entity, T where) {
        return 0;
    }

    @Override
    public List<T> query(T where) {
        return null;
    }

    @Override
    public List<T> query(T where, String orderBy, Integer startIndex, Integer limit) {
        return null;
    }

    @Override
    public int delete(T where) {
        return 0;
    }

    abstract String createTable();
}
