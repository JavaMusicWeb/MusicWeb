$("#signup").click(function() {
    $.ajax({
        type: "POST",
        url: "/MusicWeb/api/user/register",
        data: { NikeName: $("#nickName").val(), NumberId: $("#numberId").val(), Password: $("#password").val() },
        async: false,
        success: function(dat) {
            // 处理注册
            if(dat.status == 200){
                alert("注册成功！");
		self.location='signin.htm'; 
            }
            else{
                alert("注册失败！");
            }
        }
    });
});
