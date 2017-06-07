package com.solarexsoft.simpledatabasedemo;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;

import com.solarexsoft.simpledatabasedemo.dbtest.User;
import com.solarexsoft.simpledatabasedemo.dbtest.UserDao;
import com.solarexsoft.solarexdatabase.DaoManager;
import com.solarexsoft.solarexdatabase.L;
import com.solarexsoft.solarexdatabase.interfaces.IBaseDao;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    IBaseDao<User> mUserIBaseDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        File file = new File(Environment.getExternalStorageDirectory(), "solarex.db");
        mUserIBaseDao = DaoManager.getInstance(file).getDao(UserDao.class, User.class);
        mUserIBaseDao.insert(new User("Solarex", 18));
    }
}
