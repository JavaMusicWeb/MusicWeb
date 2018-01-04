var audio = document.createElement("audio");
audio.src = "";
var indexOfMusic=-1;
var isPlaying=false;
function aPlay() {
	audio.play();
	isPlaying=true;
}
function aPause() {
	audio.pause();
	isPlaying=false;
}
function aStop() {
	audio.currentTime = 0;
	audio.pause();
	isPlaying=false;
}
function consolePlay(path,index){
	//change music
	if(indexOfMusic!=index){
		indexOfMusic = index;
		aStop();
		audio.src="/MusicWeb/upload/musics/"+path+".mp3";
		aPlay()
	}
	//don't change music, but have played it
	else if(audio.currentTime != 0){
		if(isPlaying != false)
			aPause();
		else
			aPlay();
	}
}
function removeMusic(musicid){
    var str = window.location.href;
    var strs = str.split("?id=");
	strs = strs[1].split("&name=");
    $.ajax({
        type: "GET",
        url: "/MusicWeb/api/music/remove?MusicsheetId=" + strs[0] + "&MusicId=" + musicid,
        data: {},
        async: true,
        success: function(datas) {
			if(datas.status==200){
				alert("移除成功");
				self.location="song-list.html";
			}
			else
				alert("移除失败");
		}
    });
}
$("#btnComment").on("click", function() {
    var str = window.location.href;
    var strs = str.split("?id=");
	strs = strs[1].split("&name=");
    $.ajax({
        url: "/MusicWeb/api/musicsheet/publishComment",
        type: "POST",
        dataType: "JSON",
        data: { MusicsheetId: strs[0], NumberId: $.cookie('userid'), Content: $("#commentContent").val() },
        success: function(rst) {
			if(rst.status==200){
            	alert("留言成功");
				self.location="song-list.html?"+str.split("?")[1];
			}
			else
				alert("留言失败");
        }
    });
})

window.onload = function() {

    var str = window.location.href;
    var strs = str.split("?id=");
	strs = strs[1].split("&name=");
	document.getElementById("listImage").src="/MusicWeb/upload/pictures/"+str.split("&photo=")[1];
    var flag = 0;
    $.ajax({
        type: "GET",
        url: "/MusicWeb/api/musicsheet/getInfo?userid=" + $.cookie('userid'),
        data: {},
        async: true,
        success: function(datas) {
			var count = 0;
            for (var i = 0; i < datas.data.length; i++) {
				if(datas.data[i].id==strs[0])
					count ++;
            }
			if(count != 0)
				flag = 1;
			else
				flag = 0;
        }
    });
	var musics="";
	
	$.ajax({
        type: "GET",
        url: "/MusicWeb/api/musicsheet/getSongs?musicsheetId=" + strs[0],
        data: {},
        async: true,
        success: function(datas) {
			if(datas.status==200){
				for(var i = 0; i < datas.data.length; i++) {
					if(flag!=0){
						musics+="<li class='list-group-item'>"+
						"<div class='pull-right m-l'><a onclick=\"removeMusic(\'"+datas.data[i].id+"\')\" href='javascript:void(0);'><i class='icon-close'></i></a></div>"+
						"<a onclick=\"consolePlay(\'"+datas.data[i].md5Value+"\',"+i+")\" href='javascript:void(0);' class='jp-play-me m-r-sm pull-left'><i class='icon-control-play text'></i>"+
						"<i class='icon-control-pause text-active'></i></a><div class='clear text-ellipsis'>"+
						"<span>"+datas.data[i].name+"</span><span class='text-muted'>---"+datas.data[i].songer+"</span></div></li>";
					}
					else{
						musics+="<li class='list-group-item'>"+
						"<div class='pull-right m-l'></div>"+
						"<a onclick=\"consolePlay(\'"+datas.data[i].md5Value+"\',"+i+")\" href='javascript:void(0);' class='jp-play-me m-r-sm pull-left'><i class='icon-control-play text'></i>"+
						"<i class='icon-control-pause text-active'></i></a><div class='clear text-ellipsis'>"+
						"<span>"+datas.data[i].name+"</span><span class='text-muted'>---"+datas.data[i].songer+"</span></div></li>";
					}
				}
			}
			else
				alert("获取失败");
			$("#musics").html(musics);
        }
    });

    $.ajax({
        type: "GET",
        url: "/MusicWeb/api/musicsheet/getAll?current=0",
        data: {},
        async: true,
        success: function(datas) {
			suggesstionLists ="";
			for (var i = 0; i < datas.data.length; i++){
				suggesstionLists += "<article class='media'>"+
				"<a href='song-list.html?id=" + datas.data[i].id + "&name=" + datas.data[i].name + "&photo="+datas.data[i].pictureUrl+"' class='pull-left thumb-md m-t-xs'>"+
				"<img src='/MusicWeb/upload/pictures/" + datas.data[i].pictureUrl + "'>"+
				"</a>"+
				"<div class='media-body'>"+
				"<a href='song-list.html?id=" + datas.data[i].id + "&name=" + datas.data[i].name + "&photo="+datas.data[i].pictureUrl+"' class='font-semibold'>" + datas.data[i].name + "</a>"+
				"<div class='text-xs block m-t-xs'>" + datas.data[i].createTime + "</div>"+
				"</div></article>";
				if(i>=6)
					break;
			}
			$("#suggesstionList").html(suggesstionLists);
        }
		
    });
	document.getElementById("numberOfComment").innerHTML= "0 "+"Comments";
    $.ajax({
        type: "GET",
        url: "/MusicWeb/api/musicsheet/getComments?MusicSheetId=" + strs[0],
        data: {},
        async: true,
        success: function(datas) {
            comments = "";
            for (var i = 0; i < datas.data.length; i++) {
                comments += "<article id='comment-id-1' class='comment-item'>"+
                "<a class='pull-left thumb-sm'>"+
                "<img src='images/a0.png' class='img-circle'>"+
                "</a>"+
                "<section class='comment-body m-b'>"+
                "<header>"+
                "<a href='#'><strong>"+datas.data[i].commentorNickName+"</strong></a>"+
                "<span class='text-muted text-xs block m-t-xs'>"+datas.data[i].time+"</span>"+
                "</header>"+
                "<div class='m-t-sm'>"+datas.data[i].content+"</div>"+
                "</section>"+
                "</article>";
            }
            $("#comment").html(comments);
			document.getElementById("numberOfComment").innerHTML= datas.data.length+" "+"Comments";
        }
    });
	listname=str.split("&name=")[1];
	document.getElementById("listName").innerHTML=listname.split("&photo=")[0];
	basic();
}
