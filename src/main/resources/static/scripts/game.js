var game = "lightsOff";
var username;
var isLogged;

$(document).ready(function () {
    refreshField("/lightsOff/field");
    getName();
    getScore();
    getLevel();
    getMove();
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
        alert("Invalid password!");
        return;
    }
    username = name;
    setName();
    document.getElementById('is').value = "true";
    $("#loginButton").hide();
    showLogout();
    $('#logout').show();
    closeLogIn();
    getName();
}

function showLogout() {
    $('#logout').empty().append("<p>Username: " + username + "</p>\n<a onclick=\"logout()\">Log out</a>");
}

async function singUp() {
    var name = document.getElementById('username').value;
    var password = document.getElementById('password').value;
    if (name == null || password == null) return;
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
    getName();
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
                getName();
                getScore();
                getLevel();
                getMove();
                refreshField(url);
                getName();
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
    $.get('/lightsOff/level', function (response) {
        document.getElementById("level").textContent = response;
    });
}

function getMove() {
    $.get('/lightsOff/move', function (response) {
        document.getElementById("move").textContent = response;
    });
}