musicListTotal = 0;



var nowPageNum=1;
var pageCount=1;
function jump(index){
	if(index <= pageCount && index > 0)
		jumpPage(index-1);
}
function jumpPage(indexes) {
    $.ajax({
        type: "GET",
        url: "/MusicWeb/api/musicsheet/getAll?current=" + indexes,
        data: {},
        async: true,
        success: function(datas) {
			var page = "";
			var shareMusicLists = '';
            for (var i = 0; i < datas.data.length; i++) {
                shareMusicLists += "<div class='col-xs-6 col-sm-4 col-md-3'>" +
                    "<div class='item'>" +
                    "<div class='pos-rlt'>" +
                    "<div class='item-overlay opacity r r-2x bg-black'>" +
                    "<div class='center text-center m-t-n'>" +
                    "<a href='song-list.html?id=" + datas.data[i].id + "&name=" + datas.data[i].name + "&photo="+datas.data[i].pictureUrl+"' ><i class='fa fa-play-circle i-2x'></i></a>" +
                    "</div>" +
                    "</div>" +
                    "<a href='song-list.html?id=" + datas.data[i].id + "&name=" + datas.data[i].name + "&photo="+datas.data[i].pictureUrl+"' ><img src='/MusicWeb/upload/pictures/" + datas.data[i].pictureUrl + "' alt='' class='r r-2x img-full'></a>" +
                    "</div>" +
                    "<div class='padder-v'>" +
                    "<a href='song-list.html?id=" + datas.data[i].id + "&name=" + datas.data[i].name + "&photo="+datas.data[i].pictureUrl+"' class='text-ellipsis'>" + datas.data[i].name + "</a>" +
                    "<a href='song-list.html?id=" + datas.data[i].id + "&name=" + datas.data[i].name + "&photo="+datas.data[i].pictureUrl+"' class='text-ellipsis text-xs text-muted'> " + datas.data[i].createTime + " </a>" +
                    "</div>" +
                    "</div>" +
                    "</div>";
            }
            musicListTotal = datas.total;
            pageCount = Math.ceil(musicListTotal / 12);
			nowPageNum=indexes+1;
            page += "<li><a onclick=\"jump("+Math.ceil(nowPageNum-1)+")\" href='javascript:void(0);'><i class='fa fa-chevron-left'></i></a></li>";
            for (var i = 1; i <= pageCount; i++) {
                if(i == nowPageNum){
                    page += "<li><a onclick=\"jumpPage("+Math.ceil(i-1)+")\" href='javascript:void(0);'>" + i.toString() + "</a></li>";
                }
                else{
                    page += "<li><a onclick=\"jumpPage("+Math.ceil(i-1)+")\" href='javascript:void(0);'>" + i.toString() + "</a></li>";
                }
            }
            page += "<li><a onclick=\"jump("+Math.ceil(nowPageNum+1)+")\" href='javascript:void(0);'><i class='fa fa-chevron-right'></i></a></li>";

			$("#pageCount").html(page);
            $("#musicLists").html(shareMusicLists);
        }
    });
}
window.onload = function() {

    
    basic();
    jumpPage(0);

}
