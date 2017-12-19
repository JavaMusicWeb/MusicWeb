window.onload = function() {
    var html = '';

    for (var i = 0; i < 12; i++) {
        html += "<div class='col-xs-6 col-sm-4 col-md-3'>" +
            "<div class='item'>" +
            "<div class='pos-rlt'>" +
            "<div class='item-overlay opacity r r-2x bg-black'>" +
            "<div class='center text-center m-t-n'>" +
            "<a href='song-list.html'><i class='fa fa-play-circle i-2x'></i></a>" +
            "</div>" +
            "</div>" +
            "<a href='song-list.html'><img src='images/m40.jpg' alt='' class='r r-2x img-full'></a>" +
            "</div>" +
            "<div class='padder-v'>" +
            "<a href='song-list.html' class='text-ellipsis'>Tempered Song</a>" +
            "<a href='song-list.html' class='text-ellipsis text-xs text-muted'>Miaow</a>" +
            "</div>" +
            "</div>" +
            "</div>";
    }
    $("#musicLists").html(html);
}

// window.onload = function() {
//     var html = '';
//     $.ajax({
//         type: "GET",
//         url: "http://127.0.0.1:8080/musicWeb//api/music/getAll?current=1",
//         data: {},
//         async: true,
//         success: function(data) {
//             for (var i = 0; i < data.length; i++) {
//                 html += "<div class='col-xs-6 col-sm-4 col-md-3'>" +
//                     "<div class='item'>" +
//                     "<div class='pos-rlt'>" +
//                     "<div class='item-overlay opacity r r-2x bg-black'>" +
//                     "<div class='center text-center m-t-n'>" +
//                     "<a href='song-list.html'><i class='fa fa-play-circle i-2x'></i></a>" +
//                     "</div>" +
//                     "</div>" +
//                     "<a href='song-list.html'><img src='images/m40.jpg' alt='' class='r r-2x img-full'></a>" +
//                     "</div>" +
//                     "<div class='padder-v'>" +
//                     "<a href='song-list.html' class='text-ellipsis'>Tempered Song</a>" +
//                     "<a href='song-list.html' class='text-ellipsis text-xs text-muted'>Miaow</a>" +
//                     "</div>" +
//                     "</div>" +
//                     "</div>";
//             }
//             $("musicLists").html(html);
//         }
//     });