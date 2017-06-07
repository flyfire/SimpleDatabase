package com.solarexsoft.simpledatabasedemo;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;

import com.solarexsoft.simpledatabasedemo.dbtest.User;
import com.solarexsoft.simpledatabasedemo.dbtest.UserDao;
import com.solarexsoft.solarexdatabase.DaoManager;
import com.solarexsoft.solarexdatabase.interfaces.IBaseDao;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    IBaseDao<User> mUserDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        File file = new File(Environment.getExternalStorageDirectory(), "solarex.db");
        mUserDao = DaoManager.getInstance(file).getDao(UserDao.class, User.class);
        mUserDao.insert(new User("Solarex", 18));
        User where = new User();
        where.setName("Solarex");
        User entity = new User("David", 100);
        mUserDao.update(entity, where);
        mUserDao.insert(new User("Solarex", 18));
        mUserDao.delete(entity);
    }
}
