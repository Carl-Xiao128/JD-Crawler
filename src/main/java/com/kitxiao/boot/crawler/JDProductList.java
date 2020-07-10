package com.kitxiao.boot.jd_gpuprice;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;  
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import javax.swing.filechooser.FileSystemView;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.ss.usermodel.Workbook;
import org.jsoup.Jsoup;  
import org.jsoup.nodes.Document;  
import org.jsoup.nodes.Element;  
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JDProductList implements ProductList{  
    
	private String jdUrl;  
      
    private String productName;  
    
    private Boolean flag;  
    
    private static final Logger LOGGER = LoggerFactory
			.getLogger(JDProductList.class);
      
    private static final String s54 = "s54x54";
    private static final String s450 = "s450x450";
    private static final String s546 = "s546x546";
    private static final String s800 = "s800x800";
    private static final String X54 = "X54";
    private static final String X450 = "X450";
    private static final String X546 = "X546";
    private static final String X800 = "X800";
    private static final String PROTOCAL = "HTTPS:";
    private static final String IMGNAME = "pic";
    private static final String REGX = "(?<=[0-9]{1})/(.*?)jfs/";
    private static final String SHOP = "自营";
    
    //获取当前计算机桌面路径
    private static	File desktopDir = FileSystemView.getFileSystemView() .getHomeDirectory();
    private static	String desktopPath = desktopDir.getAbsolutePath();
    
    private static PriceCheckUtil pcu = PriceCheckUtil.getInstance();  
    private static JSUtil jsu = new JSUtil();  
    
    private static HttpUtil3 hu = new HttpUtil3();
      
    public JDProductList(String jdUrl, String productName,Boolean flag){  
        this.jdUrl = jdUrl;  
        this.productName = productName;  
        this.flag = flag;  
    }  
  
	@Override  
    public ElementLists getProductList() throws Exception {  
    	ElementLists el = new ElementLists();
    	List<String> strs = new ArrayList<>();
        List<ProductInfo> jdProductList = new ArrayList<ProductInfo>();  
        ProductInfo productInfo = null;  
        String url = "";  
        for(int i = 0; i < 1; i++){  
                //LOGGER.info("JD Product 第[" + (i + 1) + "]页");  
                if(i == 0) {  
                    url = jdUrl;  
                }else{  
                    url = Constants.JDURL + pcu.getGbk(productName) + Constants.JDENC + Constants.JDPAGE + (i + 1);  
                }  
                //LOGGER.info(url);  
                Document document = Jsoup.connect(url).timeout(5000).get();  
                Elements uls = document.select("ul[class=gl-warp clearfix]");  
                Iterator<Element> ulIter = uls.iterator();  
                Element ul = ulIter.next();  
                Elements lis = ul.select("li[data-sku]");  
                Iterator<Element> liIter = lis.iterator();  
                while(liIter.hasNext()){
	                Element li = liIter.next();  
	                Element div = li.select("div[class=gl-i-wrap]").first();  
	                
	                Elements shop = div.select("div[class=p-shop]>span>a");  
	                String shopName = shop.attr("title"); //得到商品店铺名
	                //非自营店跳出当前循环
	                if(!shopName.contains(SHOP)){
	                	continue;
                }else{
	                	Elements title = div.select("div[class=p-name p-name-type-2]>a>em");  
	                	String productName = title.text(); //得到商品名称  
	                	//计算商品名称和关键词的相似度
	                	//double similar = pcu.sim(this.productName, productName);
	                	
	                	Elements price = div.select("div[class=p-price]>strong");  
	                	String productPrice =price.text(); //得到商品价格  
	                	
	                	Elements imga = div.select("div[class=p-img]>a");  
	                	String areaurl = imga.attr("href");
	                	Document imgdocument = Jsoup.connect(PROTOCAL+areaurl).timeout(5000).get(); 
	                	strs.add(areaurl);
	                	//获取商品精确品牌型号
	                	Elements detaildiv = imgdocument.select("div[id=detail]").select("div[class=tab-con]>div");
	                	Elements detailParams = detaildiv.get(1).select("div[class=Ptable]")
	                			.select("div[class=Ptable-item]").first().select("dl");
	                	String brand = detailParams.select("dd").first().text();
	                	String modal = detailParams.select("dd").get(1).text();
	                	productName = brand+modal;
	                	
	                	productInfo = new ProductInfo();  
	                	
	                	productInfo.setProductName(productName);  
	                	productInfo.setProductPrice(productPrice); 
	                	productInfo.setShopName(shopName);
	                	jdProductList.add(productInfo);  
	                	el.setPlists(jdProductList);
	                	el.setAreaurls(strs);
	                	break;
	                }
       	}
        }  
        return el;  
    }  
    

	@SuppressWarnings("static-access")
	@Override
	public void getImg(String areaurl) throws Exception {
			//获取该商品的图片（54x54,450x450,546x546,800x800）
    		int count = 1;
    		String space= "";
            for(int a = 0;a<50;a++){
            	space+=" ";
            }
            Document imgdocument = Jsoup.connect(PROTOCAL+areaurl).timeout(5000).get(); 
            //获取商品精确品牌型号
        	Elements detaildiv = imgdocument.select("div[id=detail]").select("div[class=tab-con]>div");
        	Elements detailParams = detaildiv.get(1).select("div[class=Ptable]")
        			.select("div[class=Ptable-item]").first().select("dl");
        	String brand = detailParams.select("dd").first().text();
        	String modal = detailParams.select("dd").get(1).text();
        	String productName = brand+modal;
    		//获取GPU商品详细参数并绘制为图片
        	productName=productName.replaceAll("\\s+", "");
        	String sss=jsu.getHtmlDivScreenShot(PROTOCAL+areaurl, desktopPath+"\\GPUParams\\"+productName+".png");
        	LOGGER.info("GPU商品详细参数图片路径:"+desktopPath+"\\GPUParams\\"+productName+".png"+space);
        	//System.out.println(sss);
    		Elements imgdiv = imgdocument.select("div[id=spec-list]");  
    		Elements imgul = imgdiv.select("ul[class=lh]");
    		Elements imglis = imgul.select("li");
    		Iterator<Element> imgliter = imglis.iterator();
    		productName = productName.replaceAll("\\\\", "").replaceAll("/", "");
    		
    		LOGGER.info("保存图片："+productName+space);
    		while(imgliter.hasNext()){
    			Element item = imgliter.next();
    			Elements img = item.select("img");
    			String imgurl = img.attr("src");
    			String img54url = imgurl.replaceAll(REGX, "/"+s54+"_jfs/");
    			//System.out.println(img54url);
    			String img546url = imgurl.replaceAll(REGX, "/"+s546+"_jfs/");
    			String img450url = imgurl.replaceAll(REGX, "/"+s450+"_jfs/");
    			String img800url = imgurl.replaceAll(REGX, "/"+s800+"_jfs/");
    			byte[] in54 = hu.getPicutre(PROTOCAL+img54url);
    			pcu.saveByteImage(in54, X54,productName,IMGNAME+X54+count+".jpg");
    			byte[] in546 = hu.getPicutre(PROTOCAL+img546url);
    			pcu.saveByteImage(in546, X546,productName,IMGNAME+X546+count+".jpg");
    			byte[] in450 = hu.getPicutre(PROTOCAL+img450url);
    			pcu.saveByteImage(in450, X450,productName,IMGNAME+X450+count+".jpg");
    			byte[] in800 = hu.getPicutre(PROTOCAL+img800url);
    			pcu.saveByteImage(in800, X800,productName,IMGNAME+X800+count+".jpg");
    			count++;
    		}
		
	}  
  
      
    @SuppressWarnings("static-access")
	public static void main(String[] args) {  
        try {  
        	//////////////////////////////////
        	//获取显卡价格excel
        	//////////////////////////////////
        	long kst = System.currentTimeMillis();
        	PropertyConfigurator.configure("log4j.properties");
        	List<ProductInfo> alllist = new ArrayList<ProductInfo>();
        	List<String> strs = new ArrayList<String>();
        	 String space= "";
             for(int i = 0;i<50;i++){
             	space+=" ";
             }
        	Boolean flag = false;
            String keyword = pcu.hasProperties().getProperty("KEYWORD");
            Scanner scanner = new Scanner(System.in);  
            while(true){
            	System.out.println("Do you want to export pictures of related goods?:Y/N");  
            	String ifExportImg = scanner.next();  
            	//scanner.close();
            	if("y".equalsIgnoreCase(ifExportImg)){
            		flag = true;
            		break;
            	}else if("n".equalsIgnoreCase(ifExportImg)){
            		break;
            	}
            }
            scanner.close();
            if(StringUtils.isEmpty(keyword)){
            	throw new Exception("关键字不能为空！");
            }
            List<String> productNames = Arrays.asList(keyword.split(","));
            for(int i = 0;i < productNames.size(); i++){
            	String jdUrl = Constants.JDURL + productNames.get(i)  + Constants.JDENC;  
            	//LOGGER.info(jdUrl);
            	ElementLists els = new JDProductList(jdUrl, productNames.get(i),flag).getProductList(); 
            	alllist.addAll(els.getPlists());
            	strs.addAll(els.getAreaurls());
            }
            LOGGER.info("================================>获取当日显卡价格表开始<=================================="+space);
           
            for(ProductInfo pi : alllist){  
            	//java.text.DecimalFormat   df   =new   java.text.DecimalFormat("0.00"); 
            	//LOGGER.info("商品名称："+pi.getProductName() + "===价格：" + pi.getProductPrice()+"===相似度："+df.format(pi.getSimilar()));
            	String equals = "";
            	if(pi.getProductName().length() <=50){
            		int leftnum = 50-pi.getProductName().length();
            		for(int i = 0;i<leftnum;i++){
            			equals+="=";
            		}
            	}
            	LOGGER.info("商品名称："+pi.getProductName() + equals+"===价格：" + pi.getProductPrice()+space);  
            }  
            // 根据查询提现列表数据，生成Excel文件
 			// excel目录
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
 			String[] title = { "商品编号", "商品名称", "商品价格"};
 			List<String[]> titles = new ArrayList<String[]>();
 			titles.add(title);
 			// excel每行数据
 			List<String[]> data = new ArrayList<String[]>();
 			if (alllist != null && alllist.size() > 0) {
 				// 遍历，并给每行赋值
 				for (int i = 0; i < alllist.size(); i++) {
 					ProductInfo cg = alllist.get(i);
 					String[] tempData = new String[3];
 					tempData[0] = ""+(i+1);
 					tempData[1] = cg.getProductName();
 					tempData[2] = cg.getProductPrice();
 					data.add(tempData);
 				}
 			}else{
 				throw new Exception("关键字没有查询到相关数据！");
 			}
 			List<List<String[]>> datas = new ArrayList<List<String[]>>();
 			datas.add(data);
 			String[] sheetName = { "当日显卡价格表" };
 			String filename = sdf.format(new Date())+"显卡价格表.xlsx";
 			LOGGER.info("文件保存路径："+desktopPath+"\\"+filename+space);
 			OutputStream out = new FileOutputStream(new File(desktopPath+"\\"+filename));
 			// 创建并获取工作簿对象
 			Workbook wb = new ExcelexportUtils()
 					.getAutoWorkBook(sheetName, titles, datas);
 			wb.write(out);
 			long ket = System.currentTimeMillis();
 			LOGGER.info("================================>获取当日显卡价格表结束<=================================="+space);
 			LOGGER.info("============================>获取当日显卡价格表总耗时："+(ket-kst)+"毫秒<============================="+space);
 			
			//////////////////////////////////
			//获取相应的图片（线程池，多线程，阻塞队列）
 			//////////////////////////////////
 			if(flag){
 				LOGGER.info("###################################################################################"+space);
 				LOGGER.info("###################################################################################"+space);
 				long st = System.currentTimeMillis();
 				LOGGER.info("================================>获取相应商品图片开始<===================================="+space);
 				BlockingQueue<String> blockingQueue = new LinkedBlockingQueue<String>(strs.size());
 				for(int i = 0;i < strs.size(); i++){
 					blockingQueue.put(strs.get(i));
 				}
 				ExcuteThread a = new ExcuteThread(blockingQueue, flag);
 				
 				//创建固定大小的线程池
 		        ExecutorService executor=Executors.newFixedThreadPool(10);
 		        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executor;
 		       
 		        for(int i = 1; i<strs.size(); i++) {
 		        	//创建相应的线程
 		        	Thread t = new Thread(a,"Thread"+i);
 		            //将线程放到池中执行
 		            threadPoolExecutor.execute(t);
 		           LOGGER.info("=================>线程池中现在的线程数目是："+threadPoolExecutor.getPoolSize()+",  队列中正在等待执行的任务数量为："+  
 		                    threadPoolExecutor.getQueue().size()+"<================"+space);
 		        }
 		        
 		        //关闭线程池
 		        threadPoolExecutor.shutdown();
 		        
 		        
 				MainThread ss= new MainThread(st);
 				new Thread(ss).start();
 				
 			}
            
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }

}  