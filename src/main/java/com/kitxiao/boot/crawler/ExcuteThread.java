package com.kitxiao.boot.jd_gpuprice;

import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcuteThread implements Runnable{
	private static final Logger LOGGER = LoggerFactory
			.getLogger(JDProductList.class);
	
	private final BlockingQueue<String> blockingQueue;
    private boolean flag;
    public ExcuteThread(BlockingQueue<String> blockingQueue,Boolean flag) {
        this.blockingQueue = blockingQueue;
        this.flag = flag;
    }
    
	@Override
	public void run() {
		try {
			String space= "";
            for(int i = 0;i<50;i++){
            	space+=" ";
            }
			LOGGER.info("============================>线程："+Thread.currentThread().getName()+
					"正在执行<============================="+space);
			String areaurl = blockingQueue.take();
			new JDProductList("", "",flag).getImg(areaurl);;
			LOGGER.info("============================>线程："+Thread.currentThread().getName()+
					"执行完毕<============================="+space);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
