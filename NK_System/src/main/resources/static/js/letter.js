//页面加载事件，点击发私信，会弹出发私信的框，点击发送按钮就会触发发送事件
$(function(){
	$("#sendBtn").click(send_letter);
	//私信详情的删除方法 通过.class类选择器选中
	$(".close").click(delete_msg);
});

function send_letter() {
	$("#sendModal").modal("hide");
	let toName=$("#recipient-name").val();
	let content=$("#message-text").val();

	$.post(
		CONTECT_PATH +"/letter/send",
		{"toName":toName,"content":content},
		function (data) {
			//将JSON对象转换成JS对象
			data=$.parseJSON(data);
			if(data.code==0){
				$("#hintBody").text(data.msg);
			}else {
				$("#hintBody").text(data.msg);
			}

			$("#hintModal").modal("show");
			setTimeout(function(){
				$("#hintModal").modal("hide");
				location.reload();
			}, 2000);
		}
	);
}

function delete_msg() {
	// TODO 删除数据，通过类选择器选中当前元素父节点的某个父节(.media)节点移除 ==》<li>标签中有很多列
	$(this).parents(".media").remove();
}