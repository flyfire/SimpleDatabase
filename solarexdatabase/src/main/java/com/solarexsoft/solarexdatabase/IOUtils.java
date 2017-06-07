package com.solarexsoft.solarexdatabase;

import java.io.Closeable;
import java.io.IOException;

/**
 * <pre>
 *    Author: houruhou
 *    Project: https://solarex.github.io/projects
 *    CreatAt: 07/06/2017
 *    Desc:
 * </pre>
 */

public class IOUtils {
    public static void close(Closeable... closeables){
        for (Closeable closeable : closeables) {
            try {
                closeable.close();
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
