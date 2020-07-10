package com.kitxiao.boot.crawler;

import jdk.internal.org.xml.sax.SAXException;

import java.io.*;

public class JSUtil {

    // 如果要更换运行环境，请注意exePath最后的phantom.exe需要更改。因为这个只能在window版本上运行。前面的路径名  
    // 也需要和exePath里面的保持一致。否则无法调用  
    private static String projectPath = System.getProperty("user.dir");
    private static String jsPath = projectPath + File.separator+ "hello.js";
    private static String exePath = projectPath +File.separator + "phantomjs.exe";

    public static void main(String[] args) throws IOException, SAXException {

        // 测试调用。传入url即可  
    	//String html = getParseredHtml("https://b2b.10086.cn/b2b/main/showBiao!showZhaobiaoResult.html");
        //String html = getParseredHtml2("http://huisheng99.b2b.hc360.com/");
        //System.out.println(html);
    	String out = getHtmlDivScreenShot("https://item.jd.com/3340788.html", "C:\\Users\\kitxiao\\Desktop\\STRIX-GTX1080-A8G-GAMING\\hello.png");
    	System.out.println(out);
    }

    public static String getHtmlDivScreenShot(String url,String imgpath) throws IOException {
        Runtime rt = Runtime.getRuntime();
        Process p = rt.exec(exePath + " " + jsPath+" "+url+" "+imgpath);
        InputStream is = p.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuffer sbf = new StringBuffer();
        String tmp = "";
        while ((tmp = br.readLine()) != null) {
            sbf.append(tmp);
        }
        return sbf.toString();
    }

    public static String getParseredHtml2(String url) throws IOException {
        Runtime rt = Runtime.getRuntime();

        Process p = rt.exec(exePath + " " + jsPath + " " + url);

        InputStream is = p.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuffer sbf = new StringBuffer();
        String tmp = "";
        while ((tmp = br.readLine()) != null) {
            sbf.append(tmp);
        }

        //        对数据进行处理，摘取自己需要的数据
        String[] result = sbf.toString().split("companyServiceMod");
        String result2 = "";
        if (result.length >= 2) {
            result2 = result[1];
            if (result2.length() > 200) {
                result2 = result2.substring(0, 200);
            }
        }
        return result2;

        //        return sbf.toString();
    }

}  
