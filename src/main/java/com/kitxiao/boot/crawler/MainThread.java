package com.kitxiao.boot.crawler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainThread implements Runnable{
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(JDProductList.class);
	
	private Long st;
    public MainThread(Long st) {
        this.st = st;
    }
	
	@Override
	public void run() {
		int i=0;
		String space= "";
        for(int j = 0;j<50;j++){
        	space+=" ";
        }
		while(i != 4){
			i =Thread.activeCount();
			//System.out.println("当前还有线程正在运行！"+Thread.currentThread().getName()+"sleep，还有"+i+"个线程正在运行");
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		long et = System.currentTimeMillis();
		LOGGER.info("================================>获取相应商品图片结束<===================================="+space);
		LOGGER.info("===============================>获取图片总耗时："+(et-st)+"毫秒<================================"+space);
	}
}
