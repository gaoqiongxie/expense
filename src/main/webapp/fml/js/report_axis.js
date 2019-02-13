$(function() {
	var dataOrderByExpens, dataOrderByPayer, dataOrderByType;
	$.get("/expenses/group?groupBy=expenseId",function(data,status){
		dataOrderByExpense=data.data;
		var myChart = echarts.init(document.getElementById("bar2"),'light');
		myChart.clear();
		option = getOptions(dataOrderByExpense, "expenseId")
		if (option && typeof option === "object") {
		    myChart.setOption(option, true);
		}
	});
	
	$.get("/expenses/group?groupBy=payerId",function(data,status){
		dataOrderByPayerId=data.data;
		var myChart = echarts.init(document.getElementById("bar1"));
		myChart.clear();
		option = getOptions(dataOrderByPayerId, "payerId")
		if (option && typeof option === "object") {
		    myChart.setOption(option, true);
		}
	});
	
	$.get("/expenses/group?groupBy=typeId",function(data,status){
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
	var xAxisData = getXAxisData(dataSource);
	var seriesData = getSeriesData(dataSource);
	option = null;
	option = {
	    title : {
	        text: '2019年家庭总支出',
	        subtext: '依据'+getType(dataSource[0].groupKey)+'统计'
	    },
	    tooltip : {
	        trigger: 'axis'
//	    	trigger:'item'//只显示选中
	    	
	    },
	    legend: {
	        data:legendData
	    },
	    toolbox: {
	        show : true,
	        feature : {
	            dataView : {show: true, readOnly: false},
	            magicType : {show: true, type: ['line', 'bar']},
	            restore : {show: true},
	            saveAsImage : {show: true}
	        }
	    },
	    calculable : true,
	    xAxis : [
	        {
	            type : 'category',
	            data : xAxisData
	        }
	    ],
	    yAxis : [
	        {
	            type : 'value'
	        }
	    ],
	    series : seriesData/*,
	    color:getColor(dataSource[0].groupKey)*/
	};
	
	return option;
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

/**
 * x-轴坐标
 * @param dataSource
 * @returns
 */
function  getXAxisData(dataSource){
	var xAxisArr = new Array();
	$.each(dataSource,function(n,v) {   
		xAxisArr.push(v.month);
	});
	return xAxisArr;
}

function getSeriesData(dataSource){
	var map = {}, dest = [];
	var seriesArr = new Array();
	var serie;
	$.each(dataSource,function(n,v) {   
		var arr =  v.groupValue;
		for(var i = 0; i < arr.length; i++){
		    var ai = arr[i];
		    if(!map[ai.name]){
		        dest.push({
		            name: ai.name,
		      
		            data: [ai.expenseSum]
		        });
		        map[ai.name] = ai;
		    }else{
		        for(var j = 0; j < dest.length; j++){
		            var dj = dest[j];
		            if(dj.name == ai.name){
		                dj.data.push(ai.expenseSum);
		                break;
		            }
		        }
		    }
		}
	});
	$.each(dest,function(n,v) {   
		var serie = seriesInit(v.name, v.data);
		seriesArr.push(serie);
	})
	return seriesArr;
}

function seriesInit(name, data){
	var serie = new Object();
	serie.name=name;
	serie.type='bar';
	serie.data=data;
//	serie.markPoint={
//            data : [
//                {type : 'max', name: '最大值'},
//                {type : 'min', name: '最小值'}
//            ]
//        };
	serie.label={
			normal:{
				show:true,
				position:'top'
			}
	};
	serie.markLine={
            data : [
                {type : 'average', name: '平均值'}
            ]
        };
	return serie;
}
