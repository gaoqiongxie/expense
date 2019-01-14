var actionType;

$(function() {
//	$.get("/expense",function(data,status){
//		alert("Data: " + data + "\nStatus: " + status);
//	});
	
	setExpenseTypeSlc('sType');
	setMemberSlc('sExpense');
	setMemberSlc('sPayer');
	
	
	
	$("#expenseList").datagrid({
		title : '家庭支出',
		width: $(window).width()*0.8,
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
        onLoadSuccess:function(){
        },
		loadMsg : '数据加载,中请稍后......',
		nowrap : false,
		pagination : true,
		rownumbers : true,
		pageSize:30,
		frozenColumns : [ [ 
		                   {field : 'ck',checkbox : true,align : 'center'} 
		               ] ],
        loadFilter: function(data){
        	console.log(data)
        	if (data.data){
        		return data.data;
        	} else {
        		return data;
        	}
        },
		columns : [ [ 
			 {field : 'expenseName',title : '所属',width : fillsize(0.20),align : 'center'},
			 {field : 'payer',title : '付款人',width : fillsize(0.20),align : 'center'},
			 {field : 'expense',title : '支出',width : fillsize(0.20),align : 'center'},
			 {field : 'expenseDesc',title : '备注',width : fillsize(0.20),align : 'center'},
			 {field : 'expenseTime',title : '支出时间',width : fillsize(0.20),align : 'center',
				 formatter:function(value,row,index){
					 return value;
				 }
			 },
			 {field : 'updateTime',title : '更新时间',width : fillsize(0.20),align : 'center',
				 formatter:function(value,row,index){
					 return value;
				 }	 
			 }
		] ],
		toolbar:'#tb'
	});
	
	
	$("#editButton").bind("click",function(){
		var row = getSingleSelectRow('expenseList','请选择一条记录！');
		$.ajax({
			type:'get',
			url:'/getDtoRecordById/'+row.recordId,
			async: false,
			data:'',
			success:function(data){
				var data = eval('(' + data + ')'); 
//				data.expenseTime = formatterDateTime(data.expenseTime);
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
	
	$('#expenseDlg').dialog({
		 width:($(window).width()*0.7), 
		 height:($(window).height()*0.7), 
		 iconCls:'icon-save',
		 closed: true,  
	     modal:true,
	     resizable:true,
	     buttons:[{
			text:'提交',
			iconCls:'icon-ok',
			handler:function(){
				CheckData();
			}
		},{ 
			text:'取消',
			iconCls:'icon-no',
			handler:function(){
				$('#expenseDlg').dialog('close');
			}
		}],
		onOpen:function(){
			
		},
		onClose:function(){
			resetContent('expenseForm');
		}
	});
	
	$('#expenseForm').form({
		onLoadSuccess:function(){
			
		}
	});
	
});


/** 设置消费类型*/
function setExpenseTypeSlc(id){
	$("#"+id).combobox({
		valueField:"typeId",
		textField:"typeName",
		url:'/types'
	});
}

/** 设置主角*/
function setMemberSlc(id){
	$("#"+id).combobox({
		valueField:"memberId",
		textField:"name",
		url:'/members'
	});
}


