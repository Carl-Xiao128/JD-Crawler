
var system = require('system');  
var address = '';  
  
if (system.args.length != 2) {  
    console.log('Try to pass two args when invoking this script!');  
    phantom.exit();  
} else {  
    address = system.args[1];  
}  
  
var page = require('webpage').create();  
var url  = address;  
phantom.outputEncoding = 'GBK';  
page.open(url, function (status) {  
    if (status !== 'success') {  
        console.log('Failed to get the page!');  
    } else {  
        console.log(page.content);  
    }  
    phantom.exit();  
});  