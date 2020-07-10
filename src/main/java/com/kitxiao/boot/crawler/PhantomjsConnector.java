package com.kitxiao.boot.crawler;

import java.io.*;

public class PhantomjsConnector {
    private String pid;        //进程PID
    private OutputStream out;
    private PrintWriter writer;
    private InputStream in;
    private InputStreamReader inReader;
    private BufferedReader reader;
    
    private static String projectPath = System.getProperty("user.dir");
    private static String jsPath = projectPath + File.separator+ "hello.js";
    private static String exePath = projectPath +File.separator + "phantomjs.exe";
    
    public PhantomjsConnector() {
        try {
            Process process = Runtime.getRuntime().exec("phantomjs D:/script.js");    //通过命令行启动phantomjs
            //初始化IO流
            in = process.getInputStream();
            inReader = new InputStreamReader(in, "utf-8");
            reader = new BufferedReader(inReader);
            pid = reader.readLine();        //从phantomjs脚本中获取本进程的PID
            out = process.getOutputStream();
            writer = new PrintWriter(out);
        } catch (Exception e) {
            close();
            e.printStackTrace();
        }
    }
    
    //结束当前维护的进程
    public void kill() {
        try {
            close();    //先关闭IO流
            Runtime.getRuntime().exec("taskkill /F /PID " + pid);    //Windows下清除进程的命令，Linux则为kill -9 pid
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //执行查询
    public String exec(String url) throws IOException {
        writer.println(url);        //把url输出到phantomjs
        writer.flush();                //立即输出
        return reader.readLine();    //读取phantomjs的输出
    }
    
    //关闭IO
    private void close() {
        try {
            if (in!=null) in.close();
            if (inReader!=null) inReader.close();
            if (reader!=null) reader.close();
            if (out!=null) out.close();
            if (writer!=null) writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
