package com.potato.oneall.ui.param;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class InternetGet {
    /**
     * 判断网络是否正常
     * @return
     */
    public static String isConnect(String ipPath){
        //定义其返回的状态，默认为false，网络不正常
        boolean connect = false;

        /**
         * 用Runtime.getRuntime().exec()来调用系统外部的某个程序，
         * 他会生成一个新的进程去运行调用的程序。
         * 此方法返回一个java.lang.Process对象，
         * 该对象可以得到之前开启的进程的运行结果，
         * 还可以操作进程的输入输出流。
         */
        String test = "";
        Runtime runtime = Runtime.getRuntime();
        Process process;
        try {
            process = runtime.exec("ping " + ipPath);
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is,"GBK");
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            StringBuffer sb = new StringBuffer();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            is.close();
            isr.close();
            br.close();
            test = sb.toString();
            if (!sb.toString().equals("")) {
                String logString = "";
                if (sb.toString().indexOf("TTL") > 0) {
                    // 网络畅通
                    connect = true;
                } else {
                    // 网络不畅通
                    connect = false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return test;
    }
}
