$(document).ready(function () {
    refreshField("/lightsOff/field");
    getName();
    getScore();
    getLevel();
    getMove();
});

function refreshField(url) {
    $.ajax({
        url: url,
    }).done(function (html) {
        $("#fieldFromService").html(html);
        $("#fieldFromService a").each(function () {
            const url = $(this).attr("href").replace("/lightsOff", "/lightsOff/field");
            $(this).removeAttr("href");
            $(this).click(function () {
                getScore();
                getLevel();
                getMove();
                refreshField(url);
                getScore();
                getLevel();
                getMove();
            });
        })
    });
}

function getName() {
    $.get('/lightsOff/name', function (response) {
        document.getElementById("name").textContent = response;
    });
}

function getScore() {
    $.get('/lightsOff/score', function (response) {
        document.getElementById("score").textContent = response;
    });
}

function getLevel() {
    $.get('/lightsOff/level', function (response) {
        document.getElementById("level").textContent = response;
    });
}

function getMove() {
    $.get('/lightsOff/move', function (response) {
        document.getElementById("move").textContent = response;
    });
}