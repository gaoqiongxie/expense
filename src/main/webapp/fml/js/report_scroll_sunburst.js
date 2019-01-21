$(function() {
	var dataOrderByExpens, dataOrderByPayer, dataOrderByType;
	$.get("/expenses/tree",function(data,status){
		dataOrderByExpense=data.data;
		var myChart = echarts.init(document.getElementById("bar1"));
		myChart.clear();
		option = getOptions(data)
		if (option && typeof option === "object") {
		    myChart.setOption(option, true);
		}
	});
	
});

function getOptions(dataSource) {
	option = {
		    visualMap: {
		        type: 'continuous',
		        min: 0,
		        max: 10,
		        inRange: {
		            color: ['#2D5F73', '#538EA6', '#F2D1B3', '#F2B8A2', '#F28C8C']
		        }
		    },
		    series: {
		        type: 'sunburst',
		        data: dataSource,
		        radius: [0, '90%'],
		        label: {
		            rotate: 'radial'
		        }
		    }
		};
	return option;
}