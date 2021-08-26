//页面加载完后的触发事件;
$(function(){
	$(".follow-btn").click(follow);
});

function follow() {
	var btn = this;
	//利用Jqury选中元素后判断，都否关注
	if($(btn).hasClass("btn-info")) {
		// 关注TA
		$.post(
			CONTECT_PATH +"/follow",
			{"entityType":3,"entityId":$(btn).prev().val()},
			function (data) {
				data=$.parseJSON(data);
				if(data.code==0){
					window.location.reload();

				}else {
					alert(data.msg);
				}

			}
		);
		// $(btn).text("已关注").removeClass("btn-info").addClass("btn-secondary");
	} else {
		// 取消关注
		// $(btn).text("关注TA").removeClass("btn-secondary").addClass("btn-info");

		$.post(
			CONTECT_PATH +"/unfollow",
			//$(btn).prev().val() ==>找到当前元素节点的前一个节点的值
			{"entityType":3,"entityId":$(btn).prev().val()},
			function (data) {
				data=$.parseJSON(data);
				if(data.code==0){
					window.location.reload();

				}else {
					alert(data.msg);
				}

			}
		);
	}
}