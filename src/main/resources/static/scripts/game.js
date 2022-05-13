var game = "lightsOff";
var username;
var isLogged;
var level;
var typeOfLevel;

$(document).ready(function () {
    refreshField("/lightsOff/field");
    getName();
    getScore();
    getMove();
    getTypeOfLevel().then(() => {
        getSaveButton();
        getLevel();
    });
    $.get('/lightsOff/isSolved', function (response) {
        if(response === "true" && level === "10") openWin();
    });
    getUsername().then(() => {
        if (isLogged === "false" || isLogged === "") {
            $('#logout').hide();
            $('#loginButton').show();
        } else {
            showLogout();
            $('#logout').show();
            $('#loginButton').hide();
        }
    });
});

async function getUsername(){
    username = await $.get('/lightsOff/name');
    isUserLogged();
}

function isUserLogged() {
    if(username === "" || username == null) {
        document.getElementById("is").value = "false";
    } else {
        document.getElementById("is").value = "true";
    }
    isLogged = document.getElementById('is').value;
}

async function logIn() {
    var name = document.getElementById('uname').value;
    var password = document.getElementById('psw').value;
    var state = await $.get('/api/user/' + game + '/' + name);
    if (password != state) {
        alert("Invalid username or password!");
        return;
    }
    username = name;
    setName();
    document.getElementById('is').value = "true";
    $("#loginButton").hide();
    showLogout();
    $('#logout').show();
    closeLogIn();
    setTimeout(function () {
        getName();
    }, 200);
}

function showLogout() {
    $('#logout').empty().append("<p>Username: " + username + "</p>\n<a onclick=\"logout()\">Log out</a>");
}

async function singUp() {
    var name = document.getElementById('username').value;
    var password = document.getElementById('password').value;
    if (name === "" || password === "") return;
    var response = await $.get('/api/user/' + game + '/' + name);
    if(response !== ""){
        alert("This name is already taken!");
        return;
    }
    var user = new Object();
    user.login = name;
    user.game = game;
    user.password = password;
    $.ajax({
        url: '/api/user',
        type: 'POST',
        data: JSON.stringify(user),
        contentType: "application/json"
    });
    openLogIn();
}

function logout() {
    username = "";
    setName();
    document.getElementById('is').value = "false";
    $('#logout').hide();
    $("#loginButton").show();
    setTimeout(function () {
        getName();
    }, 200);
}

function setName() {
    $.get('/lightsOff/setName?username=' + username);
}

function openLogIn(){
    var a = document.getElementById('logIn');
    a.style.visibility = 'visible';
    a.style.opacity = '1';
    a.style.transition = 'all 0.7s ease-out 0s';
    $("#logIn").show();
    $("#SignUp").hide();
}
function closeLogIn(){
    var a = document.getElementById('logIn');
    a.style.visibility = 'hidden';
    a.style.opacity = '0';
}

function openSignUp(){
    var a = document.getElementById('SignUp');
    a.style.visibility = 'visible';
    a.style.opacity = '1';
    a.style.transition = 'all 0.7s ease-out 0s';
    $("#logIn").hide();
    $("#SignUp").show();
}
function closeSignUp(){
    var a = document.getElementById('SignUp');
    a.style.visibility = 'hidden';
    a.style.opacity = '0';
}

function refreshField(url) {
    $.ajax({
        url: url,
    }).done(function (html) {
        $("#fieldFromService").html(html);
        $("#fieldFromService a").each(function () {
            const url = $(this).attr("href").replace("/lightsOff", "/lightsOff/field");
            $(this).removeAttr("href");
            $(this).click(function () {
                setTimeout(function () {
                    getName();
                    getScore();
                    getLevel();
                    getMove();
                }, 200);
                refreshField(url);
                setTimeout(function (){
                    isSolved();
                }, 200);
                $.get('/lightsOff/isSolved', function (response) {
                    if(response === "true" && level === "10") openWin();
                });
            });
        })
    });
}

function isSolved() {
    $.get('/lightsOff/isSolved', function (response) {
        if(response === "true"){
            if (username === "") return;
            var score = new Object();
            score.player = username;
            score.game = game;
            score.points = document.getElementById("score").textContent;
            score.playedOn = new Date();
            $.ajax({
                url: '/api/score',
                type: 'POST',
                data: JSON.stringify(score),
                contentType: "application/json"
            });
        }
    });
}

function openWin(){
    var a = document.getElementById('win');
    a.style.visibility = 'visible';
    a.style.opacity = '1';
    a.style.transition = 'all 0.7s ease-out 0s';
}
function closeWin(){
    var a = document.getElementById('win');
    a.style.visibility = 'hidden';
    a.style.opacity = '0';
}

function getName() {
    $.get('/lightsOff/name', function (response) {
        document.getElementById("name").textContent = response;
        if(response === "" || response == null){
            document.getElementById("name").textContent = "Guest";
        }
    });
}

function getScore() {
    $.get('/lightsOff/score', function (response) {
        document.getElementById("score").textContent = response;
    });
}

function getLevel() {
    if(typeOfLevel === "prepared") {
        $('#level').empty().append("<h1>Level: <span id=\"levelId\"></span></h1>");
        $.get('/lightsOff/level', function (response) {
            document.getElementById("levelId").textContent = response;
            level = response;
        });
    }
}

function getSaveButton() {
    if(typeOfLevel === "prepared") {
        $('#saveButton').append("<button class=\"button\" onclick='saveGame()'>Save</button>\n");
    }
}

function saveGame() {
    if (username === "") {
        alert("If you want to save your game you must to log in!");
        return;
    }
    $.get('/lightsOff/save');
}

async function getTypeOfLevel() {
    await $.get('/lightsOff/typeOfLevel', function (response) {
        typeOfLevel = response;
    });
}

function getMove() {
    $.get('/lightsOff/move', function (response) {
        document.getElementById("move").textContent = response;
    });
}