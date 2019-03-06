var sysUrl = parent.baseJs;
// 全局监控所有的Ajax请求，对于没有权限或登录失效的ajax访问，踢回到登录页面
$(function() {

	$.ajaxSetup({
				headers : { // 默认添加请求头
					"Author" : "gaoqiongxie"
				},
				beforeSend : function(xhr) {
					debugger;
					if ($.cookie('tokenModel')) {
						var tokenModel = JSON.parse($.cookie('tokenModel'));
//						console.log("common refresh ajaxSetup. " + JSON.stringify(tokenModel));
						if (tokenModel) {
							var accessToken = tokenModel.userAuth.accessToken;
							xhr.setRequestHeader("accessToken", accessToken);
							var refreshToken = tokenModel.userAuth.refreshToken;
							xhr.setRequestHeader("refreshToken", refreshToken);
						}
					}

				},
				cache : false,
				global : true,
				complete : function(req, status) {
					try {
						var reqObj = eval('(' + req.responseText + ")");
						// console.log(reqObj);
						// 如果数据请求验证时，对应的请求资源(路径)没有权限(或者没有登录)
						if (reqObj && reqObj.errorCode) {
							var loginErrorCodeArr = [ '0004', '0005', '0006',
									'0007' ];
							if (loginErrorCodeArr.toString().indexOf(
									reqObj.errorCode) > -1) {
								$.messager.alert('提示', reqObj.msg, 'warning');
								return window.location.href = "../login.html";
							}
						}
					} catch (e) {
					}
				}
			});
	 
	window.setInterval(refreshTokens, 1000*60*25);

});

/**
 * 刷新token
 * 
 * @returns
 */
function refreshTokens() {
	console.log("refreshToken -- start time:"+ new Date());
	if ($.cookie('tokenModel')) {
		var tokenModel = JSON.parse($.cookie('tokenModel'));
		if (tokenModel) {
			var refreshToken = tokenModel.userAuth.refreshToken;
			$.ajax({
				type : 'get',
				url : '/refresh',
				success : function(data) {
					// TODO 清空缓存
//					var data = eval('(' + data + ')');
					if (data.status == '0') {
						$.messager.alert('提示', data.msg, 'warning');
					}
					if (data.status == '1') {
						tokenModel.userAuth = data.data;
						 $.cookie('tokenModel', JSON.stringify(data.data), { expires: 7, path: '/' });
					}
				}
			});

		}
	}
}

/**
 * 通用功能js 隐藏手机号码
 */
function hideUserPhone(phone) {
	if (!isNullStr(phone)) {
		return phone.substring(0, 3) + "****" + phone.substring(7);
	}
	return phone;
}
/**
 * 通用功能js Author xiechengcheng Date 2010-12-10
 */

function disableKeys(eve) {
	var ev = (document.all) ? window.event : eve;
	var evCode = (document.all) ? ev.keyCode : ev.which;
	var srcElement = (document.all) ? ev.srcElement : ev.target;
	// Enter键
	if (srcElement.type != "textarea") {
		if (evCode == 13) {
			return false;
		}
	}
}

function NewDate(str) {
	var strs = str.split(" ");
	str = strs[0].split('-');
	var date = new Date();
	date.setUTCFullYear(str[0], str[1] - 1, str[2]);
	date.setUTCHours(0, 0, 0, 0);
	return date;
}
/**
 * 获取 昨天 今天 明天的格式化字符串
 * 
 * @param AddDayCount
 * @returns {String}
 */
function GetDateStr(AddDayCount) {
	var dd = new Date();
	dd.setDate(dd.getDate() + AddDayCount);// 获取AddDayCount天后的日期
	var y = dd.getFullYear();
	var m = dd.getMonth() + 1;// 获取当前月份的日期
	if (m < 10) {
		m = "0" + m;
	}
	var d = dd.getDate();
	if (d < 10) {
		d = "0" + d;
	}
	return y + "-" + m + "-" + d;
}
/**
 * 获取日期时间差（已天为单位）
 * 
 * @param startDate
 * @param endDate
 * @returns
 */
function getDateDiff(startDate, endDate) {
	var date1 = new Date(startDate.replace(/\-/g, "\/"));
	var date2 = new Date(endDate.replace(/\-/g, "\/"));
	;
	var date3 = date2.getTime() - date1.getTime();
	return Math.floor(date3 / (24 * 3600 * 1000));
}

function jsonObj2Str(json) {
	var str = "{";
	for (prop in json) {
		str += prop + ":" + json[prop] + ",";
	}
	str = str.substr(0, str.length - 1);
	str += "}";
	return str;
}

(document.all) ? (document.onkeydown = disableKeys)
		: (document.onkeypress = disableKeys);

/**
 * 获取字符串的真实长度(一个中文占两个长度，英文或者数字占一个长度)
 * 
 * @param str
 *            获取实际长度的目标字符串
 * 
 * @return 目标字符串的实际长度
 */
function getTrueLenth(str) {
	return str.replace(/[^\x00-\xff]/g, "xx").length;
}

function isNullStr(str) {
	if (str == null || typeof (str) == undefined) {
		return true;
	}
	if ($.trim(str + "").length <= 0) {
		return true;
	}

	return false;
}

/**
 * @param num
 *            控制的字符长度
 * @param str
 *            输入的内容
 * @param idname
 *            显示剩余的控件ID
 */
function getRestLength(num, str, idname) {
	var restlegth = (num - getTrueLenth(str)) / 2;
	if (restlegth > 0) {
		$("#" + idname).html("<font color='red'>还有" + restlegth + "字</font>");
	} else {
		$("#" + idname).html("<font color='red'>已经到达字数最大上限！</font>");
	}
}

/**
 * 清空指定表单中的内容,参数为目标form的id
 * 
 * @param formId
 *            将要清空内容的表单的id
 */
function resetContent(formId) {
	var clearForm = document.getElementById(formId);
	if (null != clearForm) {
		clearForm.reset();
		// $('#'+formId).form('clear');
	}

}

/**
 * 刷新列表(适用于Jquery Easy Ui中的dataGrid)
 * 
 * @param dataTableId
 *            将要刷新数据的table列表id
 * @param type
 *            如果为selectAll，则表示查询所有，此时要清除列表数据请求参数中的查询条件condition，
 *            （当执行查询操作时，列表的condition参数值将不为空）
 */
function flashTable(dataTableId, type) {
	if ("selectAll" == type) {
		var queryParams = $('#' + dataTableId).datagrid('options').queryParams;
		// 获取搜索字
		queryParams.condition = "1 = 1";
		$('#' + dataTableId).datagrid('options').queryParams = queryParams;
	}
	$('#' + dataTableId).datagrid('reload');
}
/**
 * 创建默认kindEditor
 */
function createDefEditor(name) {
	KindEditor.basePath = '../../resources/js/kindeditor-4.1.6/';
	var editor = KindEditor.create('textarea[name="' + name + '"]', {// 打开Dialog后创建编辑器
		uploadJson : sysUrl + '/kindEditor.do?method=uploadJson',
		fileManagerJson : sysUrl + '/kindEditor.do?method=fileManagerJson',
		allowFileManager : true,
		allowFlashUpload : true,
		allowMediaUpload : true,
		newlineTag : 'p',
		resizeType : 1,
		filterMode : false
	});
	return editor;
}

function convertToChinese(nub) {
	var STR_NUMBER = [ "零", "一", "二", "三", "四", "五", "六", "七", "八", "九", "十" ];
	if (nub > 0 && nub <= 10) {
		return STR_NUMBER[nub];
	} else if (nub > 10 && nub < 20) {
		return STR_NUMBER[10] + STR_NUMBER[nub - 10];
	} else if (nub > 20 && nub < 30) {
		return "二十" + STR_NUMBER[nub - 20];
	} else {
		return nub + "";
	}
}

function createSimpleEditor(name) {
	KindEditor.basePath = '../../resources/js/kindeditor-4.1.6/';
	var editor = KindEditor.create('textarea[name="' + name + '"]', {
		resizeType : 1,
		allowPreviewEmoticons : false,
		allowImageUpload : false,
		items : [ 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor',
				'bold', 'italic', 'underline', 'removeformat', '|',
				'justifyleft', 'justifycenter', 'justifyright',
				'insertorderedlist', 'insertunorderedlist', '|', 'emoticons',
				'image', 'link' ]
	});

	return editor;
}

function removeEditor(name) {
	KindEditor.remove('textarea[name="' + name + '"]');
	return null;
}

/**
 * 取消选择(适用于Jquery Easy Ui中的dataGrid)
 * 
 * @param dataTableId
 *            将要取消所选数据记录的目标table列表id
 */
function clearSelect(dataTableId) {
	$('#' + dataTableId).datagrid('clearSelections');
	$("input[type='checkbox']").eq(0).attr("checked", false);
}

/**
 * 取消选择,主要用于系统中主明细结构的明细表中(为明细表定制的)(适用于Jquery Easy Ui中的dataGrid)
 * 
 * @param mainTableId
 *            将要取消所选数据记录的目标主表id
 * @param MxTableId
 *            将要取消所选数据记录的目标明细表id
 */
function clearMxSelect(mainTableId, MxTableId) {
	$('#' + MxTableId).datagrid('clearSelections');
	var rows = $("#" + mainTableId).datagrid('getRows');
	if (null != rows) {
		$("input[type='checkbox']").eq(rows.length + 1).attr("checked", false);
	}
}

/**
 * 关闭窗口(适用于Jquery Easy Ui中的dataGrid)
 * 
 * @param dialogId
 *            将要关闭窗口的id
 */
function closeDialog(dialogId) {
	$('#' + dialogId).dialog('close');
}

/**
 * 自适应表格的宽度处理(适用于Jquery Easy Ui中的dataGrid), 可以实现列表的各列宽度跟着浏览宽度的变化而变化。
 * 
 * @param percent
 *            当前列的列宽所占整个窗口宽度的百分比(以小数形式出现，如0.3代表30%)
 * 
 * @return 通过当前窗口和对应的百分比计算出来的具体宽度
 */
function fillsize(percent) {
	var bodyWidth = document.body.clientWidth;
	return (bodyWidth - 90) * percent;
}

/**
 * 获取所选记录的id
 * 
 * @param dataTableId
 *            目标记录所在的列表table的id
 * @param errorMessage
 *            错误的提示信息
 * 
 * @return 所选记录js对象
 */
function getSingleSelectRow(dataTableId, errorMessage) {
	var rows = $('#' + dataTableId).datagrid('getSelections');
	var num = rows.length;
	var ids = null;
	if (num == 1) {
		return rows[0];
	} else {
		$.messager.alert('提示消息', errorMessage, 'info');
		return null;
	}
}

/**
 * 获取所选记录的id,多个id用逗号分隔
 * 
 * @param dataTableId
 *            目标记录所在的列表table的id
 * 
 * @return 所选记录的id字符串(多个id用逗号隔开)
 */
function getSelectIds(dataTableId, noOneSelectMessage) {
	var rows = $('#' + dataTableId).datagrid('getSelections');
	var num = rows.length;
	var ids = null;
	if (num < 1) {
		if (null != noOneSelectMessage)
			$.messager.alert('提示消息', noOneSelectMessage, 'info');
		return null;
	} else {
		for (var i = 0; i < num; i++) {
			if (null == ids || i == 0) {
				ids = rows[i].id;
			} else {
				ids = ids + "," + rows[i].id;
			}
		}
		return ids;
	}
}

/**
 * 删除所选记录(适用于Jquery Easy Ui中的dataGrid)(删除的依据字段是id)
 * 
 * @param dataTableId
 *            将要删除记录所在的列表table的id
 * @param requestURL
 *            与后台服务器进行交互，进行具体删除操作的请求路径
 * @param confirmMessage
 *            删除确认信息
 * @param mxDataTableId：明细表datagrid的id(实用于主明细表结构，删除主表记录操作)
 * @param mainId
 *            在主从表结构的从表全删除操作中，该id指向主表中的记录id
 */
function deleteNoteById(dataTableId, requestURL, confirmMessage, mxDataTableId,
		mainId) {
	if (null == confirmMessage || typeof (confirmMessage) == "undefined"
			|| "" == confirmMessage) {
		confirmMessage = "确定删除所选记录?";
	}
	var rows = $('#' + dataTableId).datagrid('getSelections');
	var num = rows.length;
	var ids = null;
	if (num < 1) {
		$.messager.alert('提示消息', '请选择你要删除的记录!', 'info');
	} else {
		$.messager.confirm('确认', confirmMessage, function(r) {
			if (r) {
				for (var i = 0; i < num; i++) {
					if (null == ids || i == 0) {
						ids = rows[i].id;
					} else {
						ids = ids + "," + rows[i].id;
					}
				}
				var condition = "a.id in (" + ids + ")";
				openProcessingDialog();
				$.getJSON(requestURL, {
					"condition" : condition,
					"mainId" : mainId
				}, function(data) {
					closeProcessingDialog();
					if (null != data && null != data.message
							&& "" != data.message) {
						$.messager.alert('提示消息', data.message, 'info');
						flashTable(dataTableId);
					} else {
						$.messager.alert('提示消息', '删除失败！', 'warning');
					}
					clearSelect(dataTableId);
					if (null != mxDataTableId
							|| typeof (mxDataTableId) != "undefined"
							|| "" != mxDataTableId) {
						flashTable(mxDataTableId);
					}
				});
			}
		});
	}
}

/**
 * 删除所选记录(适用于Jquery Easy Ui中的dataGrid)，删除的依据字段是mxid
 * 
 * @param dataTableId
 *            将要删除记录所在的列表table的id
 * @param requestURL
 *            与后台服务器进行交互，进行具体删除操作的请求路径
 * @param mainId
 *            在主从表结构的从表全删除操作中，该id指向主表中的记录id
 */
function deleteNoteByMxIds(dataTableId, requestURL, mxid, mainId) {
	var rows = $('#' + dataTableId).datagrid('getSelections');
	var num = rows.length;
	var mxids = null;
	if (num < 1) {
		$.messager.alert('提示消息', '请选择你要删除的记录!', 'info');
	} else {
		$.messager.confirm('确认', '确定删除所选记录?', function(r) {
			if (r) {
				for (var i = 0; i < num; i++) {
					if (null == mxids || i == 0) {
						// mxids = rows[i].mxid;
						mxids = rows[i][mxid];
					} else {
						mxids = mxids + "," + rows[i][mxid];
					}
				}
				// var condition = "mxid in ("+mxids+")"; 注释 yh
				var condition = mxids;
				openProcessingDialog();
				$.getJSON(requestURL, {
					"condition" : condition,
					"mainId" : mainId
				}, function(data) {
					closeProcessingDialog();
					if (null != data && null != data.msg && "" != data.msg) {
						$.messager.alert('提示消息', data.msg, 'info');
					} else {
						$.messager.alert('提示消息', '删除失败！', 'warning');
					}
					flashTable(dataTableId);
					clearSelect(dataTableId);
				});
			}
		});
	}
}

/**
 * 删除所有记录
 * 
 * @param dataTableId
 *            将要删除记录所在的列表table的id
 * @param requestURL
 *            请求服务器进行删除操作的请求路径
 * @param mainId
 *            在主从表结构的从表全删除操作中，该id指向主表中的记录id
 * @param mxDataTableId：明细表datagrid的id(实用于主明细表结构，删除明细表记录操作)
 * @param confirmMessage
 *            删除确认信息
 */
function deleteAll(dataTableId, requestURL, mainId, mxDataTableId,
		confirmMessage) {
	var tip = $('#' + dataTableId).datagrid('options').queryParams.tip;
	var pager = $('#' + dataTableId).datagrid('getPager');
	var total = $(pager).pagination('options').total;
	var pageSize = $(pager).pagination('options').pageSize;
	var totlePage = parseInt(total / pageSize) + (total % pageSize > 0 ? 1 : 0);
	if (null == confirmMessage || typeof (confirmMessage) == "undefined"
			|| "" == confirmMessage) {
		if (null == tip || "" == tip) {
			confirmMessage = '本全删将删除<font color="red">所有记录</font>，'
					+ '共计<font color="red">' + totlePage
					+ '</font>页<font color="red">' + total
					+ '</font>条记录，删除后数据将不能恢复，' + '确定删除?';
		} else {
			confirmMessage = '本全删操作将删除<font color="red">当前查询出的所有记录</font>，'
					+ '查询条件为<font color="red">[' + tip + ']</font>，'
					+ '共计<font color="red">' + totlePage
					+ '</font>页<font color="red">' + total
					+ '</font>条记录，删除后数据将不能恢复，' + '确定删除?';
		}

	}
	if (total <= 0) {
		$.messager.alert('提示消息', '没有数据可以删除！', 'info');
	} else {
		$.messager
				.confirm(
						'确认',
						confirmMessage,
						function(r) {
							if (r) {
								var condition = $('#' + dataTableId).datagrid(
										'options').queryParams.condition;
								openProcessingDialog();
								$
										.post(
												requestURL,
												{
													"condition" : condition,
													"mainId" : mainId
												},
												function(data) {
													closeProcessingDialog();
													if (null != data
															&& "" != data) {
														data = eval('(' + data
																+ ')');
														if (null != data.message
																&& "" != data.message) {
															$.messager
																	.alert(
																			'提示消息',
																			data.message,
																			'info');
															flashTable(dataTableId);
														} else {
															$.messager.alert(
																	'提示消息',
																	'删除失败！',
																	'warning');
														}
													}
													clearSelect(dataTableId);
													// 如果有明细表则在执行删除操作完成后执行明细表刷新操作
													if (null != mxDataTableId
															|| typeof (mxDataTableId) != "undefined"
															|| "" != mxDataTableId) {
														flashTable(mxDataTableId);
													}
												});
							}
						});
	}
}

function openProcessingDialog() {
	$.messager.progress();
}
function closeProcessingDialog() {
	$.messager.progress('close');
}

/**
 * 自动调整弹出窗口的长和宽,以适应不同分辨率浏览器下的显示
 * 
 * @param dialogId
 *            弹出窗口的id
 * @param widthRate
 *            弹出窗口的宽度与浏览器所能提供的宽度(通常为弹出窗口所子页面对应的Iframe的宽度)的比例,在不同分辨率下使用该比率自动调整
 * @param maxHeight
 *            弹出窗口的最大高度
 * @param maxWidth
 *            弹出窗口的最大宽度
 */
function fillDialogWidthAndHeight(dialogId, widthRate, maxHeight, maxWidth) {
	var currentBodyHeight = $(window).height();
	var currentBodyWidth = $(window).width();
	// 当前iframe窗口的高宽比
	var heightToWidthRate = currentBodyHeight / currentBodyWidth;
	var fillWidth = currentBodyWidth * widthRate;
	var fillHeight = fillWidth * heightToWidthRate;
	// 如果当前iframe窗口按百分比计算出的宽度大于实际设置的最大值，则以最大值为准
	if (fillWidth >= maxWidth) {
		fillWidth = maxWidth;
		// 如果计算出的宽度小于最大值则进一步调整
	} else {
		// 如果当前窗口的宽度的98%大于设置的最大值，则自动调整到最大值，否则就取当前窗口的98%作为宽度
		if ((currentBodyWidth * (95 / 100)) > maxWidth) {
			fillWidth = maxWidth;
		} else {
			fillWidth = currentBodyWidth * (95 / 100);
		}
	}
	// 如果当前iframe窗口按百分比计算出的高度值大于实际设置的最大值，则以最大值为准，否则进一步调整的方法与以上的宽度调整相同
	if (fillHeight >= maxHeight) {
		fillHeight = maxHeight;
	}
	// 计算窗口左上角的坐标，使窗口居中
	var leftPos = (currentBodyWidth - fillWidth) / 2;
	var topPos = (currentBodyHeight - fillHeight) / 2;
	// alert("currentBodyHeight = " + currentBodyHeight + ", currentBodyWidth =
	// " + currentBodyWidth + ", fillHeight = " + fillHeight + ", fillWidth = "
	// + fillWidth);
	$('#' + dialogId).dialog("resize", {
		width : fillWidth,
		height : fillHeight,
		left : leftPos,
		top : topPos
	});
}

/**
 * 自动调整弹出窗口的长和宽,以适应不同分辨率浏览器下的显示
 * 
 * @param dialogId
 *            弹出窗口的id
 * @param suitWidth
 *            当前弹出窗口的最合适宽度
 * @param widthRate
 *            弹出窗口的宽度与浏览器所能提供的宽度(通常为弹出窗口所子页面对应的Iframe的宽度)的比例,在不同分辨率下使用该比率自动调整
 */
function fillDialogToSutiWidth(dialogId, suitWidth, widthRate) {
	var currentBodyHeight = $(window).height();
	var currentBodyWidth = $(window).width();
	// 当前父容器窗口(iframe窗口)的高宽比
	var heightToWidthRate = currentBodyHeight / currentBodyWidth;
	var fillWidth = 0;
	// 如果提供了当前窗口和当前父容器(通常为当前页面所在的iframe)的宽比，则使用该比值进行计算，否则使用当前父容器窗口宽度的95%
	if (null != widthRate && "" != widthRate
			&& typeof (widthRate) != "undefined") {
		fillWidth = currentBodyWidth * widthRate;
	} else {
		fillWidth = currentBodyWidth * (95 / 100);
	}
	if (fillWidth > suitWidth) {
		fillWidth = suitWidth;
	}
	// 高度按当前父容器的高宽比在刚计算出的当前弹出窗口宽度的基础之上进行计算
	var fillHeight = fillWidth * heightToWidthRate;
	// 计算窗口左上角的坐标，使窗口居中
	var leftPos = (currentBodyWidth - fillWidth) / 2;
	var topPos = (currentBodyHeight - fillHeight) / 2;
	$('#' + dialogId).dialog("resize", {
		width : fillWidth,
		height : fillHeight,
		left : leftPos,
		top : topPos
	});
}

function proxyDeal() {
	var asknum_span = window.parent.document.getElementById("asknum_span");
	var askHtml = asknum_span.innerHTML;
	if (!isNullStr(askHtml)) {
		var count = askHtml.replace("（", "").replace("）", "");
		count = parseInt(count);
		if (count == 1) {
			asknum_span.innerHTML = "";
		} else if (count > 1) {
			asknum_span.innerHTML = "（" + (count - 1) + "）";
		}
	}

	// var wait_span=window.parent.document.getElementById("waitnum_span");
	// var waitHtml=wait_span.innerHTML;
	// if(!isNullStr(waitHtml)){
	// var count=waitHtml.replace("（","").replace("）","");
	// count=parseInt(count);
	// wait_span.innerHTML="（"+(count+1)+"）";
	// }else{
	// wait_span.innerHTML="（1）";
	// }
}

/**
 * 获取url参数
 * 
 * @param name
 * @return
 */
function getQueryString(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
	var r = window.location.search.substr(1).match(reg);
	if (r != null)
		return unescape(r[2]);
	return null;
}

function delHtmlTag(str) {
	return str.replace(/<[^>]+>/g, "");// 去掉所有的html标记
}

function addError(tr, html) {
	tr.addClass("error");
	tr.find(".msg").html(html);
}

function removeError(tr) {
	tr.removeClass("error");
	tr.find(".msg").html("");
}

/**
 * linkbutton方法扩展
 * 
 * @param {Object}
 *            jq
 */
$
		.extend(
				$.fn.linkbutton.methods,
				{
					/**
					 * 激活选项（覆盖重写）
					 * 
					 * @param {Object}
					 *            jq
					 */
					enable : function(jq) {
						return jq
								.each(function() {
									var state = $.data(this, 'linkbutton');
									if ($(this).hasClass('l-btn-disabled')) {
										var itemData = state._eventsStore;
										// 恢复超链接
										if (itemData.href) {
											$(this).attr("href", itemData.href);
										}
										// 回复点击事件
										if (itemData.onclicks) {
											for (var j = 0; j < itemData.onclicks.length; j++) {
												$(this).bind('click',
														itemData.onclicks[j]);
											}
										}
										// 设置target为null，清空存储的事件处理程序
										itemData.target = null;
										itemData.onclicks = [];
										$(this).removeClass('l-btn-disabled');
									}
								});
					},
					/**
					 * 禁用选项（覆盖重写）
					 * 
					 * @param {Object}
					 *            jq
					 */
					disable : function(jq) {
						return jq
								.each(function() {
									var state = $.data(this, 'linkbutton');
									if (!state._eventsStore)
										state._eventsStore = {};
									if (!$(this).hasClass('l-btn-disabled')) {
										var eventsStore = {};
										eventsStore.target = this;
										eventsStore.onclicks = [];
										// 处理超链接
										var strHref = $(this).attr("href");
										if (strHref) {
											eventsStore.href = strHref;
											$(this).attr("href",
													"javascript:void(0)");
										}
										// 处理直接耦合绑定到onclick属性上的事件
										var onclickStr = $(this)
												.attr("onclick");
										if (onclickStr && onclickStr != "") {
											eventsStore.onclicks[eventsStore.onclicks.length] = new Function(
													onclickStr);
											$(this).attr("onclick", "");
										}
										// 处理使用jquery绑定的事件
										var eventDatas = $(this).data("events")
												|| $._data(this, 'events');
										if (eventDatas["click"]) {
											var eventData = eventDatas["click"];
											for (var i = 0; i < eventData.length; i++) {
												if (eventData[i].namespace != "menu") {
													eventsStore.onclicks[eventsStore.onclicks.length] = eventData[i]["handler"];
													$(this)
															.unbind(
																	'click',
																	eventData[i]["handler"]);
													i--;
												}
											}
										}
										state._eventsStore = eventsStore;
										$(this).addClass('l-btn-disabled');
									}
								});
					}
				});
$.fn.serializeObject = function() {
	var o = {};
	var a = this.serializeArray();
	$.each(a, function() {
		if (o[this.name]) {
			if (!o[this.name].push) {
				o[this.name] = [ o[this.name] ];
			}
			o[this.name].push(this.value || '');
		} else {
			o[this.name] = this.value || '';
		}
	});
	return o;
};
function getDefaultDate() {
	$today = new Date();
	$yesterday = new Date($today);
	$yesterday.setDate($today.getDate() - 7);
	var $dd = $yesterday.getDate();
	var $mm = $yesterday.getMonth() + 1;
	var $yyyy = $yesterday.getFullYear();
	if ($dd < 10) {
		$dd = '0' + $dd
	}
	if ($mm < 10) {
		$mm = '0' + $mm
	}
	$yesterday = $yyyy + '-' + $mm + '-' + $dd;
	return $yesterday;
}
function validatePhone(_this) {
	var pattern = /^1[3|4|5|7|8][0-9]{9}$/;
	var val = _this.val();
	if (!val) {
		$.messager.alert('提示', '手机号不能为空', 'warning');
		return;
	} else if (!pattern.test(val)) {
		$.messager.alert('提示', '手机号不正确', 'warning');
		return;
	}
}

function formatterDateTime(time) {
	var date = new Date(time.toString().replace(/\-/g, "/"));// ios系统下的浏览器年月则为Nan
	var datetime = date.getFullYear()
			+ "-"// "年"
			+ ((date.getMonth() + 1) > 10 ? (date.getMonth() + 1) : "0"
					+ (date.getMonth() + 1)) + "-"// "月"
			+ (date.getDate() < 10 ? "0" + date.getDate() : date.getDate());
	return datetime;
}

function getColor(key) {
	switch (key) {
	case "expenseId":
		return [ '#FFCC33', '#333399', '#CC0033', '#FF0033', '#CCCC00',
				'#006699', '#336666', '##003399', '#FFFF00', '#FF6600',
				'#663399' ]
	case "payerId":
		return [ '#FF6666', '#FF9900', '#FF6600', '#FF0033', '#CCCCFF',
				'#66CCFF', '#FFFFFF', '#336699', '#CCCCCC', '#003366',
				'#99CCFF' ]
	case "typeId":
		return [ '#FF99CC', '#993366', '#CC3333', '#6666FF', '#FF9999',
				'#FFCC00', '#66CCCC', '#999900', '#CC99CC', '#FF9933',
				'#66CCFF' ]
	default:
		return [ '#c23531', '#2f4554', '#61a0a8', '#d48265', '#91c7ae',
				'#749f83', '#ca8622', '#bda29a', '#6e7074', '#546570',
				'#c4ccd3' ]
	}
}
