package com.wmm.shirodemo.util;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * Created by wmm on 2019/4/15.
 */
public class PasswordUtil {

    public static final String encryptPassword(String password, String salt){
        try {
            String hashAlgorithmName = "md5";//加密方式
            int hashIterations = 2;//加密次数
            //盐：为了即使相同的密码不同的盐加密后的结果也不同
            ByteSource byteSalt = ByteSource.Util.bytes(salt);
            //密码
            Object source = password;
            SimpleHash result = new SimpleHash(hashAlgorithmName, source, byteSalt, hashIterations);
            return result.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }
}
