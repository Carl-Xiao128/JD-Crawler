package com.kitxiao.boot.jd_gpuprice;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Test {
    public static void main(String[] args) {   
    	 
        //创建固定大小的线程池
        ExecutorService executor=Executors.newFixedThreadPool(2);
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executor;
       
        for(int i = 1; i <= 5; i++) {
            Thread t = new MyThread(i);
            //将线程放到池中执行
            threadPoolExecutor.execute(t);
            System.out.println("线程池中现在的线程数目是："+threadPoolExecutor.getPoolSize()+",  队列中正在等待执行的任务数量为："+  
                    threadPoolExecutor.getQueue().size());
        }
        
        //关闭线程池
        threadPoolExecutor.shutdown();
    }
}


    class MyThread extends Thread {
    
    private Integer num; // 正在执行的任务数
    public MyThread(Integer num) {
        this.num = num;
    }
    
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName()+" 正在执行第 "+ num + "个任务");
        try {
            Thread.sleep(500);// 模拟执行任务需要耗时
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName()+" 执行完毕第 " + num + "个任务");
    } 

}
