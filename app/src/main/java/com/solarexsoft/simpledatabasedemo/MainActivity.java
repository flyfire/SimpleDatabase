package com.solarexsoft.simpledatabasedemo;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.solarexsoft.simpledatabasedemo.dbtest.User;
import com.solarexsoft.simpledatabasedemo.dbtest.UserDao;
import com.solarexsoft.solarexdatabase.DaoManager;
import com.solarexsoft.solarexdatabase.L;
import com.solarexsoft.solarexdatabase.interfaces.IBaseDao;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    IBaseDao<User> mUserDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        File file = new File(Environment.getExternalStorageDirectory(), "solarex.db");
        mUserDao = DaoManager.getInstance(file).getDao(UserDao.class, User.class);
        //mUserDao.insert(new User("Solarex", 18));
//        User where = new User();
//        where.setName("Solarex");
//        User entity = new User("David", 100);
//        mUserDao.update(entity, where);
//        mUserDao.insert(new User("Solarex", 18));
//        //mUserDao.delete(entity);
//        List<User> users = mUserDao.query(where);
//        L.d(users.toString());
    }


    public void sInsert(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                long before = System.currentTimeMillis();
                User user = new User("Solarex", 18);
                for (int i = 0; i < 1000; i++) {
                    mUserDao.insert(user);
                }
                long after = System.currentTimeMillis();
                L.d("Solarex_Insert: " + (after - before));
            }
        }).start();
    }

    public void gInsert(View view) {
    }

    public void sUpdate(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                long before = System.currentTimeMillis();
                User where = new User();
                where.setAge(18);
                User entity = new User("David", 100);
                int result = mUserDao.update(entity, where);
                long after = System.currentTimeMillis();
                L.d("Solarex_Update: " + result + ", spend " + (after - before));
            }
        }).start();
    }

    public void gUpdate(View view) {

    }

    public void sDelete(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                long before = System.currentTimeMillis();
                User where = new User();
                where.setName("Solarex");
                int result = mUserDao.delete(where);
                long after = System.currentTimeMillis();
                L.d("Solarex_Delete: " + result + ", spend " + (after - before));
            }
        }).start();
    }

    public void gDelete(View view) {
    }

    public void sQuery(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                long before = System.currentTimeMillis();
                User where = new User();
                where.setName("Solarex");
                List<User> users = mUserDao.query(where);
                long after = System.currentTimeMillis();
                L.d("Solarex_Query: " + users.size() + ", spend " + (after - before));
            }
        }).start();
    }

    public void gQuery(View view) {
    }


}
