package com.solarexsoft.simpledatabasedemo.dbtest;

import com.solarexsoft.solarexdatabase.interfaces.DBColumn;
import com.solarexsoft.solarexdatabase.interfaces.DBTable;

/**
 * <pre>
 *    Author: houruhou
 *    Project: https://solarex.github.io/projects
 *    CreatAt: 08/06/2017
 *    Desc:
 * </pre>
 */
@DBTable("tb_user")
public class User {
    @DBColumn("user_name")
    private String name;
    @DBColumn("user_age")
    private Integer age;

    public User() {
    }

    public User(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User:(" + name + "," + age + ")";
    }
}
