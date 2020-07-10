package com.kitxiao.boot.jd_gpuprice;

public interface ProductList {  
    
    /** 
     * 爬取商品列表 
     * @return 
     */  
    public ElementLists getProductList()  throws Exception;  
    
    public void getImg(String areaurl) throws Exception;
  
}  
