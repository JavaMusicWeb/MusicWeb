$("#btnUpload").on("click", function() {
    var data = new FormData;
    data.append("NumberId", $.cookie('userid'));
    data.append("SheetName", document.getElementById("listName").value);
    data.append("SheetPic", document.getElementById("upLoadIamge").files[0]);
    $.ajax({
        url: "/MusicWeb/api/musicsheet/create",
        type: "POST",
        dataType: "JSON",
        data: data,
        contentType: false,
        processData: false,
        success: function(rst) {
            alert("成功");
			self.location="create-list.html";
        }
    })
})
window.onload = function() {
	basic();
}
