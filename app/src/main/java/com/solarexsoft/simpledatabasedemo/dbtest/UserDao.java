package com.solarexsoft.simpledatabasedemo.dbtest;

import com.solarexsoft.solarexdatabase.BaseDao;

/**
 * <pre>
 *    Author: houruhou
 *    Project: https://solarex.github.io/projects
 *    CreatAt: 08/06/2017
 *    Desc:
 * </pre>
 */

public class UserDao<User> extends BaseDao<User> {

    public UserDao(){}

    @Override
    public String createTable() {
        return "create table if not exists tb_user(user_name varchar(20), user_age int)";
    }
}
