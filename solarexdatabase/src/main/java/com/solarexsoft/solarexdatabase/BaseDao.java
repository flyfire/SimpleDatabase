package com.solarexsoft.solarexdatabase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.solarexsoft.solarexdatabase.interfaces.DBColumn;
import com.solarexsoft.solarexdatabase.interfaces.DBTable;
import com.solarexsoft.solarexdatabase.interfaces.IBaseDao;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <pre>
 *    Author: houruhou
 *    Project: https://solarex.github.io/projects
 *    CreatAt: 07/06/2017
 *    Desc:
 * </pre>
 */

public abstract class BaseDao<T> implements IBaseDao<T> {
    protected SQLiteDatabase mSQLiteDatabase;
    private volatile boolean isInit = false;
    private String mTableName;
    private Class<T> mEntityClass;
    private Map<String, Field> mCacheMap = new HashMap<>();


    public synchronized boolean init(Class<T> beanClz, SQLiteDatabase sqLiteDatabase) {
        if (!isInit) {
            this.mSQLiteDatabase = sqLiteDatabase;
            this.mTableName = beanClz.getAnnotation(DBTable.class).value();
            L.d("mTableName = " + mTableName);
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
            L.d("tableColumnName = " + tableColumnName);
            Field tableColumnField = null;
            for (Field beanClzField : beanClzFields) {
                String fieldName = beanClzField.getAnnotation(DBColumn.class).value();
                if (fieldName == null) {
                    fieldName = beanClzField.getName();
                }
                L.d("fieldName = " + fieldName);
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
        Map<String, String> map = getEntityValues(entity);
        ContentValues contentValues = getContentValues(map);
        Long result = this.mSQLiteDatabase.insert(this.mTableName, null, contentValues);
        return result;
    }

    private Map<String, String> getEntityValues(T entity) {
        HashMap<String, String> result = new HashMap<>();
        Set<Map.Entry<String, Field>> mapEntities = this.mCacheMap.entrySet();
        for (Map.Entry<String, Field> mapEntity : mapEntities) {
            String cacheKey = mapEntity.getKey();
            Field field = mapEntity.getValue();
            Object value = null;
            try {
                value = field.get(entity);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (value == null) {
                continue;
            }
            result.put(cacheKey, value.toString());
        }
        return result;
    }

    private ContentValues getContentValues(Map<String, String> map) {
        ContentValues contentValues = new ContentValues();
        Set<Map.Entry<String, String>> mapEntities = map.entrySet();
        for (Map.Entry<String, String> mapEntity : mapEntities) {
            String columnName = mapEntity.getKey();
            String columnValue = mapEntity.getValue();
            if (columnValue != null) {
                contentValues.put(columnName, columnValue);
            }
        }
        return contentValues;
    }

    @Override
    public int update(T entity, T where) {
        Map<String, String> mapEntities = getEntityValues(entity);
        ContentValues contentValues = getContentValues(mapEntities);
        Map<String, String> whereEntities = getEntityValues(where);
        Condition condition = new Condition(whereEntities);
        int rowsAffected = this.mSQLiteDatabase.update(this.mTableName, contentValues, condition
                        .getWhereClause(),
                condition.getWhereArgs());
        return rowsAffected;
    }

    @Override
    public List<T> query(T where) {
        return query(where, null, null, null, null);
    }

    @Override
    public List<T> query(T where, String[] columns, String orderBy, Integer startIndex, Integer
            limit) {
        Map<String, String> whereEntities = getEntityValues(where);
        Condition whereCondition = new Condition(whereEntities);
        String limitString = null;
        if (startIndex != null && limit != null) {
            limitString = startIndex + ", " + limit;
        }
        Cursor cursor = this.mSQLiteDatabase.query(this.mTableName, columns, whereCondition
                        .getWhereClause(),
                whereCondition.getWhereArgs(), null, null, orderBy, limitString);
        ArrayList<T> lists = new ArrayList<>();
        while (cursor.moveToNext()) {
            try {
                T item = (T) where.getClass().newInstance();
                boolean notNull = false;
                Set<Map.Entry<String, Field>> entrySet = this.mCacheMap.entrySet();
                for (Map.Entry<String, Field> entry : entrySet) {
                    String columnName = entry.getKey();
                    int columnIndex = cursor.getColumnIndex(columnName);
                    Field columnField = entry.getValue();
                    if (columnIndex != -1) {
                        Class columnClz = columnField.getType();
                        if (columnClz == String.class) {
                            columnField.set(item, cursor.getString(columnIndex));
                        } else if (columnClz == Float.class) {
                            columnField.set(item, cursor.getFloat(columnIndex));
                        } else if (columnClz == Short.class) {
                            columnField.set(item, cursor.getShort(columnIndex));
                        } else if (columnClz == Long.class) {
                            columnField.set(item, cursor.getLong(columnIndex));
                        } else if (columnClz == byte[].class) {
                            columnField.set(item, cursor.getBlob(columnIndex));
                        } else if (columnClz == Integer.class) {
                            columnField.set(item, cursor.getInt(columnIndex));
                        } else if (columnClz == Double.class) {
                            columnField.set(item, cursor.getDouble(columnIndex));
                        }
                        notNull = true;
                    }
                }
                if (notNull) {
                    lists.add(item);
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        IOUtils.close(cursor);
        return lists;
    }

    @Override
    public int delete(T where) {
        Map<String, String> whereEntities = getEntityValues(where);
        Condition whereCondition = new Condition(whereEntities);
        int rowsAffected = this.mSQLiteDatabase.delete(this.mTableName, whereCondition
                        .getWhereClause(),
                whereCondition.getWhereArgs());
        return rowsAffected;
    }

    @Override
    public void close() {
        this.mSQLiteDatabase.close();
    }

    public abstract String createTable();

    public abstract void rawQuery(String sql);

    public static class Condition {
        private String whereClause;
        private String[] whereArgs;

        public Condition(Map<String, String> mapEntities) {
            StringBuilder sb = new StringBuilder();
            ArrayList<String> args = new ArrayList<>();
            sb.append("1 = 1");
            Set<Map.Entry<String, String>> entities = mapEntities.entrySet();
            for (Map.Entry<String, String> entity : entities) {
                sb.append(" and ").append(entity.getKey()).append(" = ?");
                args.add(entity.getValue());
            }
            this.whereClause = sb.toString();
            this.whereArgs = args.toArray(new String[args.size()]);
            L.d("whereClause = " + whereClause);
            L.d("whereArgs = " + whereArgs);
        }

        public String getWhereClause() {
            return whereClause;
        }

        public String[] getWhereArgs() {
            return whereArgs;
        }
    }
}
