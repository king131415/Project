
//页面加载完后的触发事件;
$(function () {
    $("#topBtn").click(setTop);
    $("#wonderfulBtn").click(setWonderful);
    $("#deleteBtn").click(setDelete);

});

//置顶
function setTop() {
   $.post(
       CONTECT_PATH+"/top",
       {"id":$("#postId").val()},
       function (data) {
             data=$.parseJSON(data);
             if(data.code==0){
                 //disabled ==>是标签属性
                $("#topBtn").attr("disabled","disabled");
             }else {
                 alert(data.msg);
             }
       }
   );
}

//加精
function setWonderful() {
    $.post(
        CONTECT_PATH+"/wonderful",
        {"id":$("#postId").val()},
        function (data) {
            data=$.parseJSON(data);
            if(data.code==0){
                $("#wonderfulBtn").attr("disabled","disabled");
            }else {
                alert(data.msg);
            }
        }
    );
}


//删除
function setDelete() {
    $.post(
        CONTECT_PATH+"/delete",
        {"id":$("#postId").val()},
        function (data) {
            data=$.parseJSON(data);
            if(data.code==0){
                //删除后去首页
               location.href=CONTECT_PATH +"/index";
            }else {
                alert(data.msg);
            }
        }
    );
}







//this 传进来后 定义为 btn
function like(btn,entityType,entityId,entityUserId,postId) {
    $.post(
        CONTECT_PATH +"/like",
        {"entityType":entityType,"entityId":entityId,"entityUserId":entityUserId,"postId":postId},
        function (data) {
            data = $.parseJSON(data);
            if(data.code==0){
                //通过Jqury语法修改子节点的值
                $(btn).children("i").text(data.likeCount);
                $(btn).children("b").text(data.likeStatus==1?'已赞':'赞');
            }else {
                    alert(data.msg);
            }
        }
    );

}