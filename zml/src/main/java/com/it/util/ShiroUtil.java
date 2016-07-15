package com.it.util;

import com.it.pojo.User;
import org.apache.shiro.SecurityUtils;

public class ShiroUtil {
    public static User getCurrentUser(){
        return (User) SecurityUtils.getSubject().getPrincipal();
    }
    public static Integer getCurrentUserID(){
        return getCurrentUser().getId();
    }
    public static String getCurrentUserName(){
        return getCurrentUser().getUsername();
    }
    public static String getCurrentRealName(){
        return getCurrentUser().getRealname();
    }

}
