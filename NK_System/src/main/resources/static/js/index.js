
//jqury语法，在页面加载完毕后,通过id选择器获取到这个发布按钮
//然后给这个按钮定义了一个单击事件(鼠标单击）
//单击后调用publish方法
$(function(){
	$("#publishBtn").click(publish);
});

function publish() {
	//首先，把填数据的框给隐藏了(id选择器选中,然后隐藏掉)
	$("#publishModal").modal("hide");

	//获取发布的内容,id选择器选中,.val是获取内容
	var title=$("#recipient-name").val();
	var content=$("#message-text").val();
	//发送异步请求
	$.post(
		CONTECT_PATH +"/discuss/add",   //请求路径
		{"title":title,"content":content},
		function (data) {     //回调函数获取回来的数据
			data=$.parseJSON(data);  //转成JSON对象，Jqury语法

			//修改文本框(id选择器选中后,更改文本框中的消息)
			$("#hintBody").text(data.msg);

           //显示提示框
			$("#hintModal").modal("show");
			setTimeout(function(){
				//2S后自动关闭隐藏提示框
				$("#hintModal").modal("hide");
				//刷新页面
				if(data.code==0){
					//发布成功,重新加载页面
					window.location.reload(); //重新加载
				}

			}, 2000);

		}
	)
}