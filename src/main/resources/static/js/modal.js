$(document).ready(function () {
    $("#add-connection").submit(function (event) {
        event.preventDefault();

        ajaxModal();
    });
});

function ajaxModal() {
    let email = $("#email").val();

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/modal",
        data: JSON.stringify(email),
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            $("#connectionModal").modal('hide');
            location.reload();

        },
        error: function (e) {
            let responseText = jQuery.parseJSON(e.responseText);
            let json = responseText.error;
            $('#errors').html(json);

            console.log("ERROR : ", e);
        }
    });

}
