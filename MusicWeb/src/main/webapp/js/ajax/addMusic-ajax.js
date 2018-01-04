var md5OfMusic="";
var data = new FormData;
var str = window.location.href;
var strs = str.split("?id=");
function uploadMusic(){
    data.append("MusicSheetId", strs[1]);
    data.append("MusicName", document.getElementById("inputSong").value);
    data.append("MusicSinger", document.getElementById("inputSinger").value);
    data.append("MusicContent", document.getElementById("file_upload").files[0]);
    $.ajax({
        url: "/MusicWeb/api/music/upload",
        type: "POST",
        dataType: "JSON",
        data: data,
        contentType: false,
        processData: false,
        success: function(rst) {
			if(rst.status==200){
            	alert("成功");
				self.location="add-music.html?id="+strs[1];
			}
			else{
				alert("失败");
				self.location="add-music.html?id="+strs[1];
			}
        }
    })
}
function transMusic(){
    $.ajax({
        url: "/MusicWeb/api/music/transmusic",
        type: "POST",
        dataType: "JSON",
        data: {Md5 : md5OfMusic, MusicSheetId : strs[1]},
        contentType: false,
        processData: false,
        success: function(rst) {
			if(rst.status==200){
            	alert("成功");
				self.location="add-music.html?id="+strs[1];
			}
			else{
				alert("失败");
				self.location="add-music.html?id="+strs[1];
			}
        }
    });
}
$("#btnUpload").on("click", function() {
	md5OfMusic = md5(document.getElementById("file_upload").files[0]);
    $.ajax({
        url: "/MusicWeb/api/music/thunder?md5="+md5OfMusic,
        type: "GET",
        dataType: "JSON",
        data: data,
        contentType: false,
        processData: false,
        success: function(rst) {
			if(rst.status==1){
				transMusic();
			}
			else{
				uploadMusic();
			}
        }
    });
})
window.onload = function() {
	basic();
}
