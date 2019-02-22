var actionType;

$(function() {
	showMonthExpense();
	
	setExpenseTypeSlc('sType');
	setMemberSlc('sExpense');
	setMemberSlc('sPayer');
	
	$("#expenseList").datagrid({
		title : '家庭支出',
		width: $(window).width()*1,
		height:$(window).height()*0.8,
		iconCls : 'icon-save',
		fitColumns :true,
		striped : true,
		idField : 'recordId',
		singleSelect:true,
		url : '/expenses',
		queryParams : {
		},
		onBeforeLoad:function(){
        },
        onLoadSuccess:function(data){
        	compute(data);
        },
		loadMsg : '数据加载,中请稍后......',
		nowrap : false,
		pagination : true,
		rownumbers : true,
		pageSize:10,
		frozenColumns : [ [ 
		                   {field : 'ck',checkbox : true,align : 'center'} 
		               ] ],
        loadFilter: function(data){
        	return getData(data);
        },
		columns : [ [ 
			 {field : 'expenseName',title : '支出人',width : fillsize(0.20),align : 'center'},
			 {field : 'payer',title : '支付人',width : fillsize(0.20),align : 'center'},
			 {field : 'expense',title : '支出金额',width : fillsize(0.20),align : 'center'},
			 {field : 'typeName',title : '支出类型',width : fillsize(0.20),align : 'center'},
			 {field : 'expenseTime',title : '支出时间',width : fillsize(0.20),align : 'center',
				 formatter:function(value,row,index){
					 return formatterDateTime(value);
				 }
			 },
			 {field : 'expenseDesc',title : '备注',width : fillsize(0.20),align : 'center'},
			 {field : 'dataState',title : '状态',width : fillsize(0.20),align : 'center'},
			 {field : 'updateTime',title : '更新时间',width : fillsize(0.30),align : 'center',
				 formatter:function(value,row,index){
					 return value;
				 }	 
			 }
		] ],
		toolbar:'#tb'
	});
	
	$("#searchButton").bind('click',function(){
		var sExpense=$("#sExpense").combobox('getValue');
		var sPayer=$("#sPayer").combobox('getValue');
		var sType=$("#sType").combobox('getValue');
		
		var startTime = $('#startTime').datebox('getValue');	// 得到 datebox值
		var endTime = $('#endTime').datebox('getValue');	// 得到 datebox值
		
		var expenseDesc = $("#sexpenseDesc").val();
		
		var obj={};
		if(!isNullStr(sExpense)){
			obj.sExpense=sExpense;
		}
		if(!isNullStr(sPayer)){
			obj.sPayer=sPayer;
		}
		if(!isNullStr(sType)){
			obj.sType=sType;
		}
		if(!isNullStr(startTime)){
			obj.startTime=startTime;
		}
		if(!isNullStr(endTime)){
			obj.endTime=endTime;
		}
		if(!isNullStr(expenseDesc)){
			obj.expenseDesc=expenseDesc;
		}
		
		
		$('#expenseList').datagrid('load', obj);
	});
	
	$("#addButton").bind("click",function(){
		setMemberSlc('eExpense');
		setMemberSlc('ePayer');
		setExpenseTypeSlc('eType');
		actionType="add";
		$('#expenseDlg').dialog({title:'新增记录'});
		$('#expenseDlg').dialog('open');
	});
	
	$("#addMember").bind("click",function(){
		actionType="member";
		$(".add_type").html("输入角色名：");
		$('#addMemberOrType_Dlg').dialog({title:'新增角色'});
		$('#addMemberOrType_Dlg').dialog('open');
	});
	
	
	$("#addType").bind("click",function(){
		actionType="type";
		$(".add_type").html("输入新增消费类型：");
		$('#addMemberOrType_Dlg').dialog({title:'新增消费类型'});
		$('#addMemberOrType_Dlg').dialog('open');
	});
	
	
	$("#editButton").bind("click",function(){
		var row = getSingleSelectRow('expenseList','请选择一条记录！');
		if(!row||!row.expenseId) return;
		$.ajax({
			type:'get',
			url:'/expense/'+row.recordId,
			async: false,
			data:'',
			success:function(data){
//				var data = eval('(' + data + ')'); 
				data = data.data;
			    $('#expenseForm').form('load', data);
			    setMemberSlc('eExpense');
			    $("#eExpense").combobox('setValue', data.expenseId);
			    
			    setMemberSlc('ePayer');
			    $("#ePayer").combobox('setValue', data.payerId);
			     
			    setExpenseTypeSlc('eType');
			    $("#eType").combobox('setValue', data.typeId);
			}
		});
		
		actionType="edit";
		$('#expenseDlg').dialog({title:'修改记录'});
		$('#expenseDlg').dialog('open');
	});
	
	$("#delButton").bind("click",function(){
		var rows = $('#expenseList').datagrid('getSelections');
		var num = rows.length;
		var ids = null;
		if(num < 1){
			$.messager.alert('提示消息','请选择你要删除的记录!','warning');
		} else {
			$.messager.confirm('确认', "确定删除所选记录?", function(execut){
				if(execut){
					for(var i = 0; i < num; i++){
						if(null == ids || i == 0){
							ids = rows[i].recordId;
						} else {
							ids = ids + "," + rows[i].recordId;
						}
					}
					
					$.messager.progress({  
		                title:'请稍等',  
		                msg:'正在处理中...'  
		            });  
					$.ajax({
						type:'POST',
						url:'/expense/',
						async: false,
						data:'actionType=del&ids='+ids,
						success:function(data){
//							var data = eval('(' + data + ')'); 
							$.messager.progress('close');
							 if(data.status){
								 flashTable('expenseList');
						    	  $.messager.alert('提示',data.msg,'info');
						     } else{
						    	  $.messager.alert('提示',data.msg,'warning');
						     }
						}
					});
					
				}
			});
		}
	});
	
	
	$('#expenseDlg').dialog({
		 width:($(window).width()*0.5), 
		 height:($(window).height()*0.6), 
		 iconCls:'icon-save',
		 closed: true,  
	     modal:true,
	     resizable:true,
	     buttons:[{
			text:'提交',
			iconCls:'icon-ok',
			handler:function(){
				checkData();
			}
		},{ 
			text:'取消',
			iconCls:'icon-no',
			handler:function(){
				clear();
				showMonthExpense();
				$('#expenseDlg').dialog('close');
			}
		}],
		onOpen:function(){
			
		},
		onClose:function(){
			clear();
			showMonthExpense();
			resetContent('expenseForm');
		}
	});
	
	$('#addMemberOrType_Dlg').dialog({
		 width:200, 
		 height:150, 
		 iconCls:'icon-save',
		 closed: true,  
	     modal:true,
	     resizable:true,
	     buttons:[{
			text:'提交',
			iconCls:'icon-ok',
			handler:function(){
				addMember();
			}
		},{ 
			text:'取消',
			iconCls:'icon-no',
			handler:function(){
				clear();
				setExpenseTypeSlc('sType');
				setMemberSlc('sExpense');
				setMemberSlc('sPayer');
				$('#addMemberOrType_Dlg').dialog('close');
			}
		}],
		onOpen:function(){
			
		},
		onClose:function(){
			clear();
			setExpenseTypeSlc('sType');
			setMemberSlc('sExpense');
			setMemberSlc('sPayer');
		}
	});
	
	
	$("#expButton").bind('click',function(){
		var startTime = $('#startTime').datebox('getValue');	
		var endTime = $('#endTime').datebox('getValue');	
		var url = '/expenses/export?startTime='+startTime+'&endTime='+endTime;
		$('<form method="post" action="' + url + '"></form>').appendTo('body').submit().remove();
	});
	
//	
	$("#reportButton").click(function(){
		var sExpense=$("#sExpense").combobox('getValue');
		var sPayer=$("#sPayer").combobox('getValue');
		var sType=$("#sType").combobox('getValue');
		
		var startTime = $('#startTime').datebox('getValue');	// 得到 datebox值
		var endTime = $('#endTime').datebox('getValue');	// 得到 datebox值
		
		reportDlg(sExpense,sPayer,sType,startTime,endTime);
	});
	
	$('#report_Dlg').dialog({
		title:'家庭支出对比图',
		width: $(window).width()*1,
		height:$(window).height()*0.9,
		iconCls:'icon-save',
		closed: true,
		modal:true,
		resizable:true,
		buttons:[],
		onOpen:function(){
		},
		onClose:function(){
		}
	});
	
	$("#logOut").bind('click',function(){
		if(confirm("确认退出登录？")){
			$.ajax({
				type:'get',
				url:'/logout',
				success:function(data){
					//TODO 清空缓存
					window.location.href = "../../login.html";
				}
			});
			
		}
	});
});

function reportDlg(sExpense,sPayer,sType,startTime,endTime){
	$("#report_scroll_pie_iframe").attr("src","report_scroll_pie.html?sExpense="+sExpense+"&sPayer="+sPayer
			+"&sType="+sType+"&startTime="+startTime+"&endTime="+endTime);
	$("#report_iframe").attr("src","report_axis.html?sExpense="+sExpense+"&sPayer="+sPayer
			+"&sType="+sType+"&startTime="+startTime+"&endTime="+endTime);
	$('#report_Dlg').dialog('open');
}

function compute(data){
	var rows = data.rows//获取当前的数据行
    var expenseName = '',
    	payer = '',
    	expenseCount = 0,
    	typeName = '',
    	expenseTime = new Date(),
    	expenseDesc = '',
    	dataState = '',
    	updateTime = new Date()
    	;
	 var length = rows.length;
    for (var i = 0; i < length; i++) {
    	if (rows[i]['dataState']!='init') continue;
    	if(expenseName.indexOf(rows[i]['expenseName'])<0)
    		expenseName += rows[i]['expenseName']+"_";
    	if(payer.indexOf(rows[i]['payer'])<0)
    		payer += rows[i]['payer']+"_";
    	expenseCount += rows[i]['expense'];
    	if(typeName.indexOf(rows[i]['typeName'])<0)
    		typeName += rows[i]['typeName']+"_";
    	if(expenseDesc.indexOf(rows[i]['expenseDesc'])<0)
    		expenseDesc += rows[i]['expenseDesc']+"_";
    }

   $("#expenseList").datagrid('appendRow', { 
	   expenseName: expenseName,
	   payer: payer,
	   expense: expenseCount.toFixed(2),
	   typeName:typeName,
	   expenseTime:expenseTime,
	   expenseDesc:expenseDesc,
	   dataState:dataState,
	   updateTime:formatterDateTime(updateTime)
   	 });
}

function addMember(){
	var name = $("#name").val();
	if(!name) return;
	if("member"==actionType){
		$.post("/member",{ memberName:name},function(data,status){
			if(data.status==1){
	    	    closeDialog('addMemberOrType_Dlg');
	    	    $("#name").val('');
	    	    setExpenseTypeSlc('sType');
				setMemberSlc('sExpense');
				setMemberSlc('sPayer');
				$.messager.alert('提示',data.msg,'info');
	        } else{
	    	    $.messager.alert('提示',data.msg,'warning');
	        }
	  	});
	}else{
		$.post("/type",{ typeName:name},function(data,status){
			if(data.status==1){
	    	    closeDialog('addMemberOrType_Dlg');
	    	    $("#name").val('');
	    	    setExpenseTypeSlc('sType');
				setMemberSlc('sExpense');
				setMemberSlc('sPayer');
				$.messager.alert('提示',data.msg,'info');
	        } else{
	    	    $.messager.alert('提示',data.msg,'warning');
	        }
	  	});
	}
	
}

function showMonthExpense(){
	$.get("/monthInfo",function(data,status){
		$(".monthInfo").html("本月开销:"+data.data+"(元)");
	});
}

function checkData(){
	var memberId=$("#eExpense").combobox('getValues');
	if(isNullStr(memberId)){
		$.messager.alert('提示','请选择支出人','warning');
		return false;
	}
	var payerId=$("#ePayer").combobox('getValues');
	if(isNullStr(payerId)){
		$.messager.alert('提示','请选择支付','warning');
		return false;
	}
	var expenseType=$("#eType").combobox('getValues');
	if(isNullStr(expenseType)){
		$.messager.alert('提示','请选择支出类型','warning');
		return false;
	}
	
	$("#expenseId").val(memberId);
	$("#payerId").val(payerId);
	$("#expenseType").val(expenseType);
	
	dealDate();
}

function dealDate(){
	if($('#expenseForm').form('validate')){
		$('#expenseForm').form('submit',{  
		    url:'/expense?actionType='+actionType,  
		    onSubmit: function(){  
		    },  
		    success:function(data){ 
		    	var data = eval('(' + data + ')'); 
		        if(data.status){
//		    	    $.messager.alert('提示',data.msg,'info');
		    	    clear();
		    	    closeDialog('expenseDlg');
		    	    flashTable('expenseList');
		        } else{
		    	    $.messager.alert('提示',data.msg,'warning');
		        }
		    }  
		});
	}
}

function clear(){
	$('#eExpense').combobox('clear');  
	$('#ePayer').combobox('clear');  
	$('#eType').combobox('clear');  
	$('#expenseTime').combo('setText','');
	$('#expenseId').val('');
	$('#recordId').val('');
	$('#payerId').val('');
	$('#expenseType').val('');
	$('#expense').numberbox('setValue', '');
	$('#expenseDesc').val('');
}

function getData(data){
	if (data.data){
		return data.data;
	} else {
		return data;
	}
}

/** 设置消费类型*/
function setExpenseTypeSlc(id){
	$("#"+id).combobox({
		valueField:"typeId",
		textField:"typeName",
		loadFilter: function(data){
			return getData(data);
        },
		url:'/types'
	});
}

/** 设置主角*/
function setMemberSlc(id){
	$("#"+id).combobox({
		valueField:"memberId",
		textField:"name",
		loadFilter: function(data){
			return getData(data);
        },
		url:'/members'
	});
}


