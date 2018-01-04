
$("#btnUpdate").click(function() {
    $.ajax({
        type: "POST",
        url: "/MusicWeb/api/user/editInfo",
        data: { NikeName: $("#inputNickName").val(), Password: $("#inputPassword").val(), NubmerId: $.cookie('userid') },
        async: false,
		dataType: "json",
        success: function(dat) {
            // 处理注册
	    if(dat.status==200){
			alert("成功");
			$.cookie("nickname", $("#inputNickName").val(), { expires: 7, path: '/' });
			self.location="setting.html";
		}
	    else
			alert(dat.status+"失败");
        }
    });
});
window.onload = function() {
	basic();
}
