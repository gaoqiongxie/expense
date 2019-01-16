$(function() {
	var dataOrderByExpens, dataOrderByPayer, dataOrderByType;
	$.get("/expenses/group?groupBy=expenseId",function(data,status){
		dataOrderByExpense=data.data;
		var myChart = echarts.init(document.getElementById("bar3"));
		myChart.clear();
		option = getOptions(dataOrderByExpense, "expenseId")
		if (option && typeof option === "object") {
		    myChart.setOption(option, true);
		}
	});
	
	$.get("/expenses/group?groupBy=payerId",function(data,status){
		dataOrderByPayerId=data.data;
		var myChart = echarts.init(document.getElementById("bar2"));
		myChart.clear();
		option = getOptions(dataOrderByPayerId, "payerId")
		if (option && typeof option === "object") {
		    myChart.setOption(option, true);
		}
	});
	
	$.get("/expenses/group?groupBy=typeId",function(data,status){
		dataOrderByTypeId=data.data;
		var myChart = echarts.init(document.getElementById("bar1"));
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
	var seriesData = getSeriesData(dataSource, legendData);
	option = null;
	option = {
	    title : {
	        text: '家庭支出',
	        subtext: '依据'+dataSource[0].groupKey+'统计'
	    },
	    tooltip : {
	        trigger: 'axis'
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
	    series : seriesData,
	    color:getColor(dataSource[0].groupKey)
	};
	
	return option;
}

function getColor(key){
	switch (key) {
	case "expenseId":
		return ['#c23531','#2f4554', '#61a0a8', '#d48265', '#91c7ae','#749f83',  '#ca8622', '#bda29a','#6e7074', '#546570', '#c4ccd3']
	case "payerId":
		return ['#FF4500', '#FF7F50', '#FA8072', '#F4A460', '#48D1CC', '#AFEEEE', '#00FFFF', '#00FA9A', '#7FFFD4', '#228B22', '#EEE8AA']
	default:
		return['#c23531','#2f4554', '#61a0a8', '#d48265', '#91c7ae','#749f83',  '#ca8622', '#bda29a','#6e7074', '#546570', '#c4ccd3']
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
				console.log(v.name);
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

function getSeriesData(dataSource, legendData){
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
	serie.markPoint={
            data : [
                {type : 'max', name: '最大值'},
                {type : 'min', name: '最小值'}
            ]
        };
	serie.markLine={
            data : [
                {type : 'average', name: '平均值'}
            ]
        };
	return serie;
}
