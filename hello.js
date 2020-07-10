    var page = require('webpage').create();  
    page.onConsoleMessage = function (msg) {  
        console.log(msg);  
    }  
    system = require('system');  
    var url;  
    var path;  
    if (system.args.length == 1) {  
        phantom.exit();  
    }  
    else {  
        url = system.args[1];  
        if (system.args.length == 3) {  
            path = system.args[2];  
        }
    }  
    var width = 1349;  
    var height = 1883; 
    block_urls = ['baidu.com'];//为了提升速度，屏蔽一些需要时间长的。比如百度广告
    page.onResourceRequested = function(requestData, request){
        for(url in block_urls) {
            if(requestData.url.indexOf(block_urls[url]) !== -1) {
                request.abort();
                //console.log(requestData.url + " aborted");
                return;
            }
        }            
    }
    page.viewportSize = {width: width, height: height}; //浏览器大小，宽度根据网页情况自行设置，高度可以随意，因为后面会滚动到底部  
    page.settings.loadImages = false;  //为了提升加载速度，不加载图片
    page.settings.userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.9; rv:25.0) Gecko/20100101 Firefox/25.0 ";
    //page.customHeaders.User-Agent = "Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:50.0) Gecko/20100101 　　Firefox/50.0";
    page.open(url, function (status) {  
        if (status != "success") {  
            console.log('FAIL to load the address');  
            phantom.exit();  
        } 
        var sss = page.evaluate(function () { 
        	return document.getElementById("detail").getElementsByClassName("tab-main large")[0].getElementsByTagName("li")[1].getBoundingClientRect();
        	
        }); 
        //console.log(sss.left + Math.floor(sss.width / 2)+"          ");
        //console.log(sss.top+130+ Math.floor(sss.height / 2));
       
        //window.scrollTo(0, 1000);//滚动到底部  
        page.sendEvent('doubleclick', sss.left + Math.floor(sss.width / 2), sss.top+ Math.floor(sss.height / 2));
	        var length = page.evaluate(function () { 
	//        	 var as = document.getElementById("detail").getElementsByClassName("tab-main large")[0].getElementsByTagName("li")[1];
	//         	var es = document.createEvent('MouseEvents');
	//         	es.initMouseEvent('click', false, true);
	//         	as.onclick = function () {
	//         		console.log("hello");
	//         	}
	//         	as.dispatchEvent(es);
	            //此函数在目标页面执行的，上下文环境非本phantomjs，所以不能用到这个js中其他变量  
	        	var mycars = document.getElementById('detail').getElementsByClassName("tab-con")[0].getElementsByClassName("Ptable-item");
	        	for(j = 0,len=mycars.length; j < len; j++) {
	        		mycars[j].style.color="black";
	        		mycars[j].style.fontSize="17px";
	        		//mycars[j].style.fontWeight="bold";
	        	}
	           var div = document.getElementById('detail').getElementsByClassName("tab-con")[0]; //要截图的div 
	           return div.getBoundingClientRect();
	        });
	        page.clipRect = { //截图的偏移和宽高  
	            top: length.top+130,  
	            left: length.left,  
	            width: length.width,  
	            height: length.height  
	        };  
	        page.render(path);  
	        phantom.exit();  
    });  

