var newContractorId = 0;

var loadStep1 = function () {
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
                console.log("step1 response: " + JSON.stringify(response))
                newContractorId = response.id

                $('#step1').hide();
                $('#step2').show();
            },
            dataType: "json",
            contentType: "application/json; charset=utf-8",
        });

        e.preventDefault(); // prevents default
        return false;
    });
}

var loadStep2 = function() {
    $('#step2Next').click(function(e) {
        var formArray = $('#step2form').serializeArray(),
            formLen = formArray.length
            formObj = { };
        for(i=0;i<formLen;i++) {
            formObj[formArray[i].name]=formArray[i].value
        }

        $.ajax({
            type: "PUT",
            url: "/api/contractors/" + newContractorId,
            data: JSON.stringify(formObj),
            success: function(response) {
                console.log("step2 response: " + response)

                $('#step2').hide();
                $('#step3').show();
            },
            dataType: "json",
            contentType: "application/json; charset=utf-8",
        });

        e.preventDefault(); // prevents default
        return false;
    });
}

var loadStep3 = function() {
    var getRow = function(id, name) {
        return "<div class=\"checkbox\"><label>" +
        "<input type=\"checkbox\" id=\"trade" + id + "\" value=\"" + id + "\">" + name +
        "</label></div>";
    }

    $.ajax({
        type: "GET",
        url: "/api/trades",
        success: function(response) {
            var trades = "";
            for(i=0;i<response.length;i++) {
                trades += getRow(response[i].id, response[i].name)
            }
            $("#trades-container").html(trades)
        }
    });

    $("#step3Next").click(function(e) {
        var selected = [];
        $('#trades-container input:checked').each(function() {
            selected.push($(this).attr('value'));
        });

        $.ajax({
            type: "PUT",
            url: "/api/contractors/" + newContractorId + "/trades",
            data: JSON.stringify(selected),
            success: function(response) {
                console.log("step3 response: " + response)
            },
            dataType: "json",
            contentType: "application/json; charset=utf-8",
        });

        e.preventDefault(); // prevents default
        return false;
    });
}

$(document).ready(function() {
  loadStep1();
  loadStep2();
  loadStep3();
})