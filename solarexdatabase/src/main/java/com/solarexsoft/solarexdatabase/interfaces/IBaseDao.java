package com.solarexsoft.solarexdatabase.interfaces;

import java.util.List;

/**
 * <pre>
 *    Author: houruhou
 *    Project: https://solarex.github.io/projects
 *    CreatAt: 07/06/2017
 *    Desc:
 * </pre>
 */

public interface IBaseDao<T> {
    Long insert(T entity);
    int update(T entity, T where);
    List<T> query(T where);
    List<T> query(T where, String orderBy, Integer startIndex, Integer limit);
    int delete(T where);
}
