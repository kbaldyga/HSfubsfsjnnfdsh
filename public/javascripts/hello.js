if (window.console) {
  console.log("Welcome to your Play application's JavaScript!");
}

var loadDropDowns = function() {
  $.get("/api/trades").success(function(data) {
    var html = ''; $(data).each(function (i) { html += '<option value="' + data[i].id + '">' + data[i].name + '</option>' });
    $('#trades').html(html);
  })
}

$(document).ready(function() {
  loadDropDowns();
})