# SimpleDatabase
面向对象的数据库存储框架

[ ![Download](https://api.bintray.com/packages/solarexsoft/maven/SolarexDatabase/images/download.svg) ](https://bintray.com/solarexsoft/maven/SolarexDatabase/_latestVersion)

```
        File file = new File(Environment.getExternalStorageDirectory(), "solarex.db");
        mUserDao = DaoManager.getInstance(file).getDao(UserDao.class, User.class);
        mUserDao.insert(new User("Solarex", 18));
        User where = new User();
        where.setName("Solarex");
        User entity = new User("David", 100);
        mUserDao.update(entity, where);
        mUserDao.insert(new User("Solarex", 18));
        //mUserDao.delete(entity);
        List<User> users = mUserDao.query(where);
        L.d(users.toString());
```
