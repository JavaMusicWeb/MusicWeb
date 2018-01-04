$("#login").click(function() {
    $.ajax({
        type: "POST",
        url: "/MusicWeb/api/user/login",
        data: { NumberId: $("#numberId").val(), Password: $("#password").val(), Validate: $("#validate").val() },
        async: false,
		dataType: "json",
        success: function(dat) {
			if(dat.status == 200)
        	{   
		    	alert("login successfully");
		    	setUserid(dat.data.NumberId);
                setNickName(dat.data.NikeName);
				$.cookie("userid", dat.data.NumberId, { expires: 7, path: '/' });
				$.cookie("nickname", JSON.stringify(dat.data).split("\"")[3], { expires: 7, path: '/' });
		    	self.location='index.html'; 
			}
			else if(dat.status == 2002){
				alert("login failed, validate is error");
			}
			else if(dat.status == 2003){
				alert("login failed, numberid or password is error");
			}
        }
    });
});
