package com.potato.oneall.colleges.base;

import android.graphics.Bitmap;

import com.potato.oneall.bean.Course;

import java.util.List;

public interface College {
    /**
     * 获取学校名字
     *
     * @return
     */
    String getCollegeName();

    /**
     * 登录
     *
     * @param account
     * @param pw
     * @return 返回是否登录成功
     */
    boolean login(String account, String pw);

    /**
     * 获取课程
     *
     * @param term 课程学期
     * @return
     */
    List<Course> getCourses(String term);

    /**
     * 获取验证码的BitMap
     *
     * @param dirPath 验证码图片保存路径
     * @return
     */
    Bitmap getRandomCodeImg(String dirPath);

    /**
     * 获取课程学期选项
     *
     * @return
     */
    String[] getTermOptions();

    /**
     * 判断是否已经登陆
     * @return
     */
    boolean isLogin();

    /**
     * 配置okhttp是否自动重定向
     * @return
     */
    boolean getFollowRedirects();

    /**
     *
     * @return 验证码长度
     */
    int getRandomCodeMaxLength();
}
