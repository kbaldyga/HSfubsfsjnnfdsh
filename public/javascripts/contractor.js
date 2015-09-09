loadButtons = function () {
    $('#step1Next').click(function(e) {
        var formArray = $('#step1form').serializeArray(),
            formLen = formArray.length
            formObj = { };
        for(i=0;i<formLen;i++) {
            formObj[formArray[i].name]=formArray[i].value
        }

        $.ajax({
            type: "POST",
            url: "/api/contractors",
            data: JSON.stringify(formObj),
            success: function(response) {
                //alert(response);
            },
            dataType: "json",
            contentType: "application/json; charset=utf-8",
        });

        e.preventDefault(); // prevents default
        return false;
    });
}

$(document).ready(function() {
  loadButtons();
})