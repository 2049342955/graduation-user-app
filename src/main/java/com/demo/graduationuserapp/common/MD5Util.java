package com.demo.graduationuserapp.common;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * author : Sdniu
 * date   : 2017/9/26.
 * description :
 */
public class MD5Util {
    private static final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static String MD5(String s) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            md.update(s.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return toHexString(md.digest()).toUpperCase();
    }

    public static String MD5(String s, String charsetName) throws UnsupportedEncodingException {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(s.getBytes(charsetName));
        return toHexString(md.digest()).toUpperCase();
    }


    private static String toHexString(byte b[]) {
        int i;
        StringBuffer buf = new StringBuffer(200);
        for (int offset = 0; offset < b.length; offset++) {
            i = b[offset] & 0xff;
            if (i < 16) {
                buf.append("0");
            }
            buf.append(Integer.toHexString(i));
        }
        return buf.toString();
    }
}
