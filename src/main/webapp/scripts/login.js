$(function(){  
	window.top.href='login.html';
	$("#submit").bind("click", function(){
		login();
	});
	
	$("#clear").bind("click", function(){
		$('#form').form('clear');
	});
	
	$(document).keydown(function(event){
		enterHandler(event);
	});

});

//按回车键时提交表单
function enterHandler(event)
{
	 event = event || window.event;
	 var keyCode = event.keyCode ? event.keyCode : event.which ? event.which : event.charCode;
	 
	 if (keyCode == 13) {
		 login();
	 } 
} 

function login(){
	if($('#form').form('validate')){
		$('#form').form('submit',{  
		    url:'/login',  
		    onSubmit: function(){ 
				var flag=$('#form').form('validate');
				if(flag){
					showProcess(true, '温馨提示', '正在登录，请稍后...');
				}
				return flag;
		    },  
		    success:function(data){ 
		    	showProcess(false);
		       var data = eval('(' + data + ')'); 
		       if(data.status=='1'){
		    	   document.cookie = data.data;
		    	   window.location.href="../fml/expense.html";
	    	   }
		    }  
		});
	}else{
		
	}
}
function showProcess(isShow, title, msg) {
	
	if (!isShow) {
	    $.messager.progress('close');
	    return;
	}
	var win = $.messager.progress({
		interval:100,
	     title: title,
	     msg: msg
	});
}