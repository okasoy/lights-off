var scoresTable;
var ratingTable;
var commentsTable;
var game = "lightsOff";
var username;
var isLogged;

$(document).ready(function () {
    getName().then(() => {
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

async function getName(){
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
}

function showLogout() {
    $('#logout').empty().append("<p>Username: " + username + "</p>\n<a onclick=\"logout()\">Log out</a>");
}

async function singUp() {
    var name = document.getElementById('name').value;
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
}

function setName() {
    $.get('lightsOff/setName?username=' + username);
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

function openRules(){
    var a = document.getElementById('rules');
    a.style.visibility = 'visible';
    a.style.opacity = '1';
    a.style.transition = 'all 0.7s ease-out 0s';
}
function closeRules(){
    var a = document.getElementById('rules');
    a.style.visibility = 'hidden';
    a.style.opacity = '0';
}

function openScores(){
    var b = document.getElementById('score');
    b.style.visibility = 'visible';
    b.style.opacity = '1';
    b.style.transition = 'all 0.7s ease-out 0s';
    topScores();
}
function closeScores(){
    var b = document.getElementById('score');
    b.style.visibility = 'hidden';
    b.style.opacity = '0';
    scoresTable.destroy();
}

function openComments(){
    var a = document.getElementById('comment');
    a.style.visibility = 'visible';
    a.style.opacity = '1';
    a.style.transition = 'all 0.7s ease-out 0s';
    comments();
}
function closeComments(){
    var a = document.getElementById('comment');
    a.style.visibility = 'hidden';
    a.style.opacity = '0';
    commentsTable.destroy();
}

function openRating(){
    var a = document.getElementById('rating');
    a.style.visibility = 'visible';
    a.style.opacity = '1';
    a.style.transition = 'all 0.7s ease-out 0s';
    rating();
}
function closeRating(){
    var a = document.getElementById('rating');
    a.style.visibility = 'hidden';
    a.style.opacity = '0';
    ratingTable.destroy();
}

function topScores() {
    scoresTable = $('#clientSideScoreTable').DataTable({
        columns: [
            {data: "№",
                render: function (data, type, row, meta) {
                    return meta.row + meta.settings._iDisplayStart + 1;
                }},
            {data: "player"},
            {data: "points"},
            {data: "playedOn"}
        ],
        dom: 't',
        retrieve: true,
        ajax: {url: "/api/score/lightsOff", dataSrc: ''}
    });
    scoresTable.order([2, 'desc']).draw();
    setTimeout(function (){
        dateFormat('#clientSideScoreTable tr', 3);
    }, 400);
}

function comments() {
    commentsTable = $('#clientSideCommentsTable').DataTable({
        columns: [
            {data: "player"},
            {data: "comment"},
            {data: "commentedOn"}
        ],
        dom: 't',
        retrieve: true,
        ajax: {url: "/api/comment/lightsOff", dataSrc: ''}
    });
    commentsTable.order([2, 'desc']).draw();
    setTimeout(function (){
        dateFormat('#clientSideCommentsTable tr', 2);
    }, 300);
}

function addComment() {
    if (username == null) return;
    if (document.getElementById("commentInput").value == "") {
        document.getElementById("commentInput").style.borderColor = "red";
        return;
    } else document.getElementById("commentInput").style.border = "3px solid #ccc";
    var comment = new Object();
    comment.player = username;
    comment.game = game;
    comment.comment = document.getElementById("commentInput").value;
    comment.commentedOn = new Date();
    $.ajax({
        url: '/api/comment',
        type: 'POST',
        data: JSON.stringify(comment),
        contentType: "application/json"
    });
    setTimeout(function (){
        commentsTable.destroy();
        comments();
    }, 400);
}

function rating(){
    ratingTable = $('#clientSideRatingTable').DataTable({
        columns: [
            {data: "player"},
            {data: "rating"},
            {data: "ratedOn"}
        ],
        dom: 't',
        retrieve: true,
        ajax: {url: "/api/rating/lightsOff", dataSrc: ''}
    });
    ratingTable.order([2, 'desc']).draw();
    setTimeout(function (){
        dateFormat('#clientSideRatingTable tr', 2);
    }, 300);
}

function dateFormat(name, i){
    $(name).each(function (){
        var tmp = $(this).find("td").eq(i).html();
        if(tmp == null) return;

        var date = new Date(tmp);

        var hour = date.getHours();
        var minutes = date.getMinutes();
        if(minutes < 10){
            minutes = '0' + minutes;
        }
        var day = date.getDate();
        if(day < 10){
            day = '0' + day;
        }
        var month = date.getMonth() + 1;
        if(month < 10){
            month = '0' + month;
        }
        var year = date.getFullYear();

        $(this).find("td").eq(i).text(day + '.' + month + '.' + year + ' ' + hour + ':' + minutes);
    })
}