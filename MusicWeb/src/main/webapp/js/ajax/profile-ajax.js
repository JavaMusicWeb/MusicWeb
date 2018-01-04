localMusicLists = "";
localMusicLists2 = "";

function removeMusicList(thisId) {
    $.ajax({
        type: "GET",
        url: "/MusicWeb/api/musicsheet/delete?id=" + thisId,
        data: {},
        async: true,
        success: function(datas) {
            alert("删除成功");
			self.location="profile.html";
        }
    })
}
window.onload = function() {
    $.ajax({
        type: "GET",
        url: "/MusicWeb/api/musicsheet/getInfo?userid=" + $.cookie('userid'),
        data: {},
        async: true,
        success: function(datas) {
            localMusicLists += "<li class='hidden-nav-xs padder m-t m-b-sm text-xs text-muted'>" +
                "<span class='pull-right'><a href='create-list.html'><i class='icon-plus i-lg'></i></a></span>" +
                "Playlist" +
                "</li>";
            for (var i = 0; i < datas.data.length; i++) {
                localMusicLists2 += "<li class='list-group-item bg-info'>" +
                    "<span class='pull-right' >" +
                    "<a href='add-music.html?id=" + datas.data[i].id + "'><i class='fa fa-plus fa-fw m-r-xs'></i></a>" +
                    "<a onclick=\"removeMusicList(\'" + datas.data[i].id.toString() + "\')\" href='javascript:void(0)'><i class='fa fa-times fa-fw'></i></a>" +
                    "</span>" +
                    "<span class='pull-left media-xs'><i class='fa fa-sort text-muted fa m-r-sm'></i>" + i.toString() + "</span>" +
                    "<div class='clear text-white'>" + datas.data[i].name +
                    "</div>" +
                    "</li>";
                if (datas.data[i].totalSongs == 0) {
                    localMusicLists += "<li>" +
                        "<a href='song-list.html?id=" + datas.data[i].id + "&name=" + datas.data[i].name + "&photo="+datas.data[i].pictureUrl+"'>" +
                        "<i class='icon-music-tone icon'></i>" +
                        "<span>" + datas.data[i].name + "</span>" +
                        "</a>" +
                        "</li>";
                } else {
                    localMusicLists += "<li>" +
                        "<a href='song-list.html?id=" + datas.data[i].id + "&name=" + datas.data[i].name + "&photo="+datas.data[i].pictureUrl+"'>" +
                        "<i class='icon-playlist icon text-success-lter'></i>" +
                        "<b class='badge bg-success dker pull-right'>" + datas.data[i].totalSongs + "</b>" +
                        "<span>" + datas.data[i].name + "</span>" +
                        "</a>" +
                        "</li>";
                }
            }
            $("#localMusicLists").html(localMusicLists);
            $("#localMusicLists2").html(localMusicLists2);
        }
    });
	nickname = $.cookie('nickname');
	dorpdown="<a href='#' class='dropdown-toggle bg clear' data-toggle='dropdown'>"+
	"<span class='thumb-sm avatar pull-right m-t-n-sm m-b-n-sm m-l-sm'>"+
	"<img src='images/a0.png' alt='...'></span>" + nickname + "<b class='caret'></b></a>"+
	"<ul class='dropdown-menu animated fadeInRight'><li><span class='arrow top'></span><a href='setting.html'>Settings</a>"+
	"</li><li><a href='profile.html'>Profile</a></li><li class='divider'></li><li><a onclick='signout()' href='signin.html' data-toggle='' >Logout</a>"+
	"</li></ul>";
    if ($.cookie('userid') == "" || $.cookie('userid') == null) {
        $("li.dropdown").hide();
        $("footer").hide();
    } else {
        $("li.dropdown").html(dorpdown);
		document.getElementById("nickName").innerHTML=nickname;
        $("footer").show();
    }
}
