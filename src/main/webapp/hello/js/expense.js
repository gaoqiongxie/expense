var actionType;

$(function() {
	
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
//		singleSelect:true,
		url : '/expenses',
		queryParams : {
		},
		onBeforeLoad:function(){
        },
        onLoadSuccess:function(){
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
			 {field : 'updateTime',title : '更新时间',width : fillsize(0.20),align : 'center',
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
	
	
	$("#editButton").bind("click",function(){
		var row = getSingleSelectRow('expenseList','请选择一条记录！');
		if(!row) return;
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
				$('#expenseDlg').dialog('close');
			}
		}],
		onOpen:function(){
			
		},
		onClose:function(){
			clear();
			resetContent('expenseForm');
		}
	});
	
});

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


