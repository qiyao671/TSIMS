package com.example.slmgroup.tsims.model;

/**
 * Created by lvqiyao (amorfatilay@163.com).
 * 2016/9/20 12:55.
 * 类描述：
 */
public class UserData {
    private static String userNum;   //学号
    private static String studentStart; //订书开始时间
    private static String studentEnd;   //订书截止时间

    public UserData() {
    }

    public static String getUserNum() {
        return userNum;
    }

    public static void setUserNum(String userNum) {
        UserData.userNum = userNum;
    }

    public static String getStudentEnd() {
        return studentEnd;
    }

    public static void setStudentEnd(String studentEnd) {
        UserData.studentEnd = studentEnd;
    }

    public static String getStudentStart() {
        return studentStart;
    }

    public static void setStudentStart(String studentStart) {
        UserData.studentStart = studentStart;
    }



}
