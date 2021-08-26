$(function(){
	//表单提交时候要检查的事件
	$("form").submit(check_data);
	$("input").focus(clear_error);
});

function check_data() {
	var pwd1 = $("#password").val();
	var pwd2 = $("#confirm-password").val();
	if(pwd1 != pwd2) {
		$("#confirm-password").addClass("is-invalid");
		return false;
	}
	return true;
}

function clear_error() {
	//移除is-invalid这个属性，因为后端返回错误数据后，在HTML上自己判断是否加上is-invalid
	$(this).removeClass("is-invalid");
}