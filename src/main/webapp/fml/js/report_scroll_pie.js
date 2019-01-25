$(function() {
	var dataOrderByExpens, dataOrderByPayer, dataOrderByType;
	$.get("/expenses/group?groupBy=expenseId&groupByMonth=false",function(data,status){
		dataOrderByExpense=data.data;
		var myChart = echarts.init(document.getElementById("bar2"),'light');
		myChart.clear();
		option = getOptions(dataOrderByExpense, "expenseId")
		if (option && typeof option === "object") {
		    myChart.setOption(option, true);
		}
	});
	
	$.get("/expenses/group?groupBy=payerId&groupByMonth=false",function(data,status){
		dataOrderByPayerId=data.data;
		var myChart = echarts.init(document.getElementById("bar1"));
		myChart.clear();
		option = getOptions(dataOrderByPayerId, "payerId")
		if (option && typeof option === "object") {
		    myChart.setOption(option, true);
		}
	});
	
	$.get("/expenses/group?groupBy=typeId&groupByMonth=false",function(data,status){
		dataOrderByTypeId=data.data;
		var myChart = echarts.init(document.getElementById("bar3"));
		myChart.clear();
		option = getOptions(dataOrderByTypeId, "typeId")
		if (option && typeof option === "object") {
		    myChart.setOption(option, true);
		}
	});
	
});

function getOptions(dataSource, type) {
	var legendData = getLegendData(type);
	var data = genData(dataSource);
	option = null;
	option = {
	    title : {
	        text: '2019年1月起家庭总支出',
	        subtext: '依据'+getType(dataSource[0].groupKey)+'统计',
	        x:'center'
	    },
	    tooltip : {
	    	trigger: 'item',
	        formatter: "{a} <br/>{b} : {c} ({d}%)"
	    },
	    legend: {
	    	type: 'scroll',
	        orient: 'vertical',
	        right: 10,
	        top: 20,
	        bottom: 20,
	        data: legendData,
	        selected: data.selected
	    },
	    series : [
	        {
	            name: getType(dataSource[0].groupKey),
	            type: 'pie',
	            radius : '55%',
	            center: ['40%', '50%'],
	            data: data.seriesData,
	            itemStyle: {
	                emphasis: {
	                    shadowBlur: 10,
	                    shadowOffsetX: 0,
	                    shadowColor: 'rgba(0, 0, 0, 0.5)'
	                }
	            }
	        }
	    ]/*,
	    color:getColor(dataSource[0].groupKey)*/
	};
	
	return option;
}

function genData(dataSource){
//	debugger;
	var seriesData = [];
	var nameExpenses = dataSource[0].groupValue;
//	console.log(dataSource[0].groupValue);
	var selected = {};
	for (var i = 0; i < nameExpenses.length; i++) {
	    seriesData.push({
	        name: nameExpenses[i].name,
	        value: nameExpenses[i].expenseSum
	    });
	    selected[nameExpenses[i].name] = i < 6;
	}
	
	return {
	    seriesData: seriesData,
	    selected: selected
	};
}


function getType(key){
	switch (key) {
	case "expenseId":
		return "支出人"
	case "payerId":
		return "支付人"
	default:
		return "花销类型"
	}
}

/**
 * 图例
 * @param dataSource
 * @returns
 */
function getLegendData(type){
	var legendArr = new Array();
	
	if("typeId"==type){
		$.get("/types",function(data,status){
			$.each(data.data,function(n,v) {   
				legendArr.push(v.typeName);
			});
			
			return legendArr;
		});
	}else{
		$.get("/members",function(data,status){
			$.each(data.data,function(n,v) {   
				legendArr.push(v.name);
			});
			return legendArr;
		});
	}
	
}

