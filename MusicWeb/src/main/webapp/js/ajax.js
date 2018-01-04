window.onload = function() {
    var html = '';
    $.ajax({
        type: "GET",
        url: "/api/music/getAll?current=1",
        data: {},
        async: true,
        success: function(data) {
            for (var i = 0; i < data.length; i++) {
                html += "<div class='col-xs-6 col-sm-4 col-md-3'>" +
                    "<div class='item'>" +
                    "<div class='pos-rlt'>" +
                    "<div class='item-overlay opacity r r-2x bg-black'>" +
                    "<div class='center text-center m-t-n'>" +
                    "<a href='song-list.html?id= " + data[i].id + "' ><i class='fa fa-play-circle i-2x'></i></a>" +
                    "</div>" +
                    "</div>" +
                    "<a href='song-list.html?id= " + data[i].id + "' ><img src='" + data[i].pictureUrl + "' alt='' class='r r-2x img-full'></a>" +
                    "</div>" +
                    "<div class='padder-v'>" +
                    "<a href='song-list.html?id= " + data[i].id+ "' class='text-ellipsis'>" + data[i].name + "</a>" +
                    "<a href='song-list.html?id= " + data[i].id+ "' class='text-ellipsis text-xs text-muted'> "+ createTime +" </a>" +
                    "</div>" +
                    "</div>" +
                    "</div>";
            }
            $("musicLists").html(html);
        }
    });
}