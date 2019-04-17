package com.oyf.common;

import java.util.UUID;

/**
 * Create Time: 2019年04月17日 14:42
 * Create Author: 欧阳飞
 **/

public class IDUtils {

    public static String createIdbyUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}




