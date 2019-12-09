<%--
  Created by IntelliJ IDEA.
  User: Bulya
  Date: 05/11/2019
  Time: 20:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Gladiator Heroes 5.5 tournament</title>
        <script src="https://code.jquery.com/jquery-1.10.2.js" type="text/javascript"></script>
        <link rel="stylesheet" href="bootstrap/css/bootstrap.min.css">
        <link rel="stylesheet" type="text/css" href="css/multi-select.css">
        <script src="bootstrap/js/bootstrap.min.js"></script>
        <script src="js/jquery.multi-select.js"></script>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" href="style.css">
        <link rel="stylesheet" href="utilities.css">
        <link rel="stylesheet" href="Colors.css">
        <scipt src="js/utilities.js"></scipt>
    </head>
    <body>
    <a id="loginButtn" href="login.jsp?returnPage=Gladiators.jsp" class="btn btn-success pull-right" style="float:right!important;display: none;">Login</a>
    <a id="signupButtn" href="signup.jsp" class="btn btn-success pull-right" style="float:right!important;margin-right: 20px;display: none;">Sign up</a>
    <a id="feedbackButton" href="feedback.jsp" class="btn btn-success pull-right" style="float:right!important;margin-right: 20px;display: block">Feedback</a>
    <p id="faqButton" class="btn btn-success pull-right" style="float:right!important;margin-right: 20px;display: block" onclick="controllerShow('faqDiv')">FAQ</p>
    <div id="upperWelcomeDiv" style="float:right!important;margin-right: 20px;display: none;">
        <p id="logoutPar" class="small" style="float:right!important;cursor:pointer"><u>logout</u></p>
        <p id="welcomePar" style="float:right!important;margin-right: 10px;"></p>
    </div>
    <div class="container-fluid">
        <div class="row row-offcanvas row-offcanvas-left">
            <div class="row">
                <div class="col-xs-12 col-sm-6 col-lg-3" style="padding-right:0px;">
                    <div class="panel-default">
                        <div class="panel-heading">
                            <h4 id="leftPannelTitle">Current streaks</h4>
                        </div>
                        <div id="currentStreakDiv" class="panel-body latest-games">
                            <div class="list-group table-responsive">
                                <table class="table table-bordered" style="color:#f9f9f9">
                                    <thead>
                                    <td>Rank</td>
                                    <td>Player</td>
                                    <td>Streak</td>
                                    </thead>
                                    <tbody id="streakTableBody">
                                    </tbody>
                                </table>
                            </div>
                            <div id="usedHeroesLeftPannelDiv" style="text-align:center; display: none">
                                <h5 id="usedHeroesHeader"></h5>
                                <div id="usedHeroesPar"></div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-xs-12 col-sm-6 col-lg-5" style="padding-right:0px;">
                    <div class="panel-default">
                        <div class="panel-heading">
                            <h4 id="MidPanelTitle">Gladiator Arena</h4>
                        </div>
                        <div id="welcomeDiv" style="font-size:18px;">
                            You ve slayed Dragons and made Titans fall to their knees?<br/>
                            Armies all across Ashan bend to your will?<br/>
                            Then you've come to the right place!<br/>
                            Test your steal in the Gladiator Arena and fight your way into the Hall of Fame!<br/>
                            Click a player to check out the heroes he used in his winning-streak<br/>
                            or challenge a player in just 2 clicks!<br/><br/>

                            To join the arena you must sign up to our ladder first and then you can join the arena in a click.<br/>
                            Click here to sign up to the arena:<br/>
                            <div style="display: flex;justify-content: center;">
                            <a href="signup.jsp" class="btn btn-success">Sign up</a>
                            </div>
                            Or in case you are part of our ladder, then click here to login:
                            <div style="display: flex;justify-content: center;">
                                <a href="login.jsp?returnPage=Gladiators.jsp" class="btn btn-success">Login</a>
                            </div>
                            <img src="images/GladiatorArena.jpg" style="width: 100%; margin-top: 10px;">
                        </div>
                        <div id="welcomeForLadderDiv" style="font-size:18px;display:none;">
                            You ve slayed Dragons and made Titans fall to their knees?<br/>
                            Armies all across Ashan bend to your will?<br/>
                            Then you've come to the right place!<br/>
                            Test your steal in the Gladiator Arena and fight your way into the Hall of Fame!<br/>
                            Click a player to check out the heroes he used in his winning-streak<br/>
                            or challenge a player in just 2 clicks!<br/><br/>

                            Join now to become a gladiator in the arena!<br/>
                            <div style="display: flex;justify-content: center;">
                                <button id="joinArenaBtn" class="btn btn-success" onclick="joinArena()" data-loading-text="please wait...">Join the arena</button>
                            </div>
                            <p id="returnMessageOnJoinArena"></p>
                            <img src="images/GladiatorArena.jpg" style="width: 100%; margin-top: 10px;">
                        </div>
                        <div id="welcomeForGladiatorsDiv" style="display: none">
                            <img src="images/GladiatorArena.jpg" style="width: 100%; margin-top: 10px;">
                        </div>
                        <div id="challengeDiv" style="display: none">
                            <div style="text-align: center">
                                <h4>Gladiators you can challenge</h4>
                                <div class="list-group table-responsive" style="margin-top: 30px">
                                    <table class="table table-bordered" style="color:#f9f9f9">
                                        <thead>
                                        <td>Gladiator</td>
                                        <td>Streak</td>
                                        <td>Used heroes</td>
                                        <td>Challenge</td>
                                        </thead>
                                        <tbody id="ChallengeTableBody">
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                            <div id="mapVetoDiv" style="display: none">
                                <p>What map would you rather play on. The rules say you can veto a map so that the match won't be played on that map.
                                    If your opponent accepts the challenge you will be notifed what map will you play on.</p>
                                <div style="text-align: center;display: grid;grid-template-columns: 1fr 1fr">
                                    <label>Map veto: </label>
                                    <select id="mapVeto" class="form-control" style="width:60%">
                                        <option value="Lets Fight!">Lets Fight!</option>
                                        <option value="Deadly Environment">Deadly Environment</option>
                                        <option value="ARMG Jebus">ARMG Jebus</option>
                                    </select>
                                </div>
                            </div>
                            <div id="mapPrioritiesDiv" style="display: none">
                                <p>Since you are on a lower streak you get an advantage with the map pick. Pick your priorities for the map.
                                    The map will be one of those two, with the first priority being the likely one, unless the player you challenge vetoes it:</p>
                                <div style="text-align: center; margin-top: 5px;display: grid;grid-template-columns: 1fr 1fr">
                                    <label>First priority: </label>
                                    <select id="firstPriority" class="form-control" style="width:60%">
                                        <option value="Lets Fight!">Lets Fight!</option>
                                        <option value="Deadly Environment">Deadly Environment</option>
                                        <option value="ARMG Jebus">ARMG Jebus</option>

                                    </select>
                                </div>
                                <div style="text-align: center; margin-top: 5px; display: grid;grid-template-columns: 1fr 1fr">
                                    <label>Second priority: </label>
                                    <select id="secondPriority" class="form-control" style="width:60%">
                                        <option value="Lets Fight!">Lets Fight!</option>
                                        <option value="Deadly Environment">Deadly Environment</option>
                                        <option value="ARMG Jebus">ARMG Jebus</option>
                                    </select>
                                </div>
                            </div>
                            <div id="challengeGladiatorDiv" style="text-align: center;margin-top:20px; display: none">
                                <button id="confirmChallengeBtn" class="btn btn-primary" data-loading-text="please wait...">Confirm challenge</button>
                                <p id="returnMessageOnChallenge"></p>
                            </div>
                        </div>
                        <div id="reportMatchDiv" style="display: none">
                            <div style="text-align: center">
                                <h4>Currently played matches in the Gladiator Arena</h4>
                                <div class="list-group table-responsive" style="margin-top: 30px">
                                    <table class="table table-bordered" style="color:#f9f9f9">
                                        <thead>
                                        <td>Against</td>
                                        <td>Map</td>
                                        <td>Started on</td>
                                        <td>Report</td>
                                        </thead>
                                        <tbody id="MatchesToReportTableBody">
                                        </tbody>
                                    </table>
                                    <div style="margin-top: 30px;font-size:18px">
                                        <label>The match was:</label><br/>
                                        <select id="matchReportScore" class="custom-select" style="color:#555">
                                            <option selected>The match was</option>
                                            <option value="loss">Lost</option>
                                            <option value="won">Won</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div>
                                <p id="returnMsgReportLadderMatch"></p>
                            </div>
                        </div>
                        <div id="mapPoolDiv" style="display: none">
                            <p>These are the maps we play now in our ranked ladder games. For now we have 3 maps, but we intend to bring more map, and perhaps later rotate maps in and out of the pool.
                                But we intend to keep the map pool at 5 maps.</p>
                            <p style="margin-top: 15px"><u>Lets Fight!</u> (click on the map image to see the underground)</p>
                            <p style="display: flex">
                                <img id="LF_groundImg" src="images/LF_ground.png" style="width:30%;float:left; margin-right:10px;" onclick="toggleVisibility('LF_groundImg','LF_undergroundImg')">
                                <img id="LF_undergroundImg" src="images/LF_underground.png" style="width:30%;float:left;display: none; margin-right:10px;" onclick="toggleVisibility('LF_groundImg','LF_undergroundImg')">
                                A true classic of HoMM5. Both players start in similar positions.
                                Attracted by the treasures of the center island,
                                players try their best to grab what they can before the enemy does.
                                There is not much room to fall back - so better be ready to fight!
                                expected game duration: short</p>
                            <a href="Maps/Lets_FightTotE.h5m">Download</a> <br/>
                            <p style="margin-top: 15px"><u>Deadly Environement</u> (click on the map image to see the underground)</p>
                            <p style="display: flex">
                                <img id="DE_groundImg" src="images/DE_ground.png" style="width:30%;float:left;margin-right:10px;" onclick="toggleVisibility('DE_groundImg','DE_undergroundImg')">
                                <img id="DE_undergroundImg" src="images/DE_underground.png" style="width:30%;float:left;display: none;margin-right:10px;" onclick="toggleVisibility('DE_groundImg','DE_undergroundImg')"> This map was designed specifically for MMH5.5.
                                The starting positions are perfectly mirrored.
                                On this map, you better think before you blindly run into a creep,
                                as the monsters aren't to be underestimated!
                                Grow your hero and army as you fight your way through these deadly lands
                                and get ready for a big final fight!
                                expected game duration: medium</p>
                            <a href="Maps/H5.5DeadlyEnvironment.h5m">Download</a>
                            <p style="margin-top: 15px"><u>ARMG jebus template</u> - A map generated by the new ARMG using the jebus template.
                                Starting positions can differ a lot!
                                Your goal is to adjust to what you are given,
                                take your towns and conquer the the center area.
                                Mighty artifacts can only be found in the center,
                                so better dont be late!
                                expected game length: short<br/>
                                In case you don't know how to make an ARMG map you can check our FAQ section as we answer it there. You can also veto this option so that you don't have to deal with it,
                                but since ARMG maps were one of the new features the Heroes 5.5 mod extended lately we suggest at least giving it a try.
                            </p>
                        </div>
                        <div id="faqDiv" style="display: none">
                            <p>
                                Q: What is Heroes 5.5?<br/>
                                A: A fan-made Heroes 5 project which greatly improves on the base game in a lot of ways!
                                There are many new features and an active community waiting for you!
                                For further information visit: <br/>
                                <a href="http://heroescommunity.com/viewthread.php3?TID=41303">Heroes community</a>
                            </p>
                            <br/>
                            <p>Q: How do I install Heroes 5.5 and how do i play in multiplayer?<br/>
                                A: You can find answers to all these questions and more on our discord server:<br/>
                                <a href="https://discordapp.com/invite/G7jadyp">Click here to join the Heroes 5.5 discord server</a>
                            </p>
                            <p>
                                Q: How do i create an ARMG map? <br/>
                                A: In your Heroes folder you will find a file called "ARMG Manual V2.1".
                                This manual will give you a detailed explanation of everything there is to know about the ARMG.<br/>
                                In case you only want to create the ARMG jebus template for a ladder game, follow these steps:
                                -Open MMH55_Editor_64<br/>
                                -Go Tools -> Advanced RMG<br/>
                                -Use the following settings:<br/>
                                Map Name: PlayerA_PlayerB<br/>
                                Size: Large<br/>
                                Template: 2P-Jebus-L<br/>
                                Start Seed: 0<br/>
                                Monster Strength: Very Strong<br/>
                                Players: 2<br/>
                                Ressources: Little<br/>
                                Experience: 4<br/>
                                check "Random towns only"<br/>
                                dont check "Underground" or "Water"<br/>
                                -click "ok".<br/>
                                In the next step, you will need to activate Scripts to make the map playable in multiplayer:<br/>
                                1. Click "Map Properties Tree" in the top panel<br/>
                                2. Search for "MapScript" on the left panel<br/>
                                3. Click "New" button next to "MapScript" (if you cant see it, widen the panel)<br/>
                                4. Type something to the "Name" (for example: MapScript)<br/>
                                5. Click "OK"<br/>
                                6. Save map ("File" -> "Save")<br/>
                                7. Close Map Editor<br/>

                                And finally: Send the map to your opponent, so you both have the same map.
                                If you have any trouble, feel free to ask us on the discord server.
                                It's easier than it looks like, at least the 2nd time you do it.
                            </p>
                        </div>
                        <div id="tournamentControllerDiv" style="margin-bottom:30px;margin-top:10px;text-align:center;display: none">
                            <p id="playButton" class="btn btn-success" style="width:30%" onclick="controllerShow('challengeDiv')">Play</p>
                            <p id="reportMatchButton" class="btn btn-success" style="width:30%" onclick="controllerShow('reportMatchDiv')">Report match</p>
                            <p id="mapPoolButton" class="btn btn-success" style="width:30%" onclick="controllerShow('mapPoolDiv')">Map pool</p>
                        </div>
                        <div id="controllerDiv" style="margin-top:10px;text-align:center;">
                            <a href="index.jsp" id="ladderButton" class="btn btn-success" style="width:30%">Back to ladder</a>
                        </div>
                    </div>
                </div>
                <div class="col-xs-12 col-sm-6 col-lg-4">
                    <div class="panel-default">
                        <div class="panel-heading">
                            <h4 id="rightPanelTitle">Hall of fame</h4>
                        </div>
                        <div id="hallOfFameDiv" class="panel-body latest-games">
                            <div class="list-group table-responsive">
                                <table class="table table-bordered" style="color:#f9f9f9">
                                    <thead>
                                    <td>Rank</td>
                                    <td>Player</td>
                                    <td>Streak</td>
                                    </thead>
                                    <tbody id="hallOfFameTableBody">
                                    </tbody>
                                </table>
                            </div>
                            <div id="usedHeroesRightPannelDiv" style="text-align:center; display: none">
                                <h5 id="usedFallOfFameHeroesHeader"></h5>
                                <div id="usedHallOfFameHeroesPar"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-header">
                <div id="modalContent" class="modal-body"></div>
            </div>
            <div class="modal-content" style="text-align: center">
                <button type="button" style="color: #555" class="btn btn-secondary" data-dismiss="modal">Ok</button>
            </div>
        </div>
    </div>
    </body>
<script>
    $(document).ready( function() {
        $.post('LoginServelet', {"command": "checkLogin"}, showLoggedIfNecessary, 'json');
        $.get('GetInfoServlet', {"command" : "getCurrentStreaks"}, onStreaksResponse, 'json');
        $.get('GetInfoServlet', {"command" : "getHallOfFame"}, onHallOfFameResponse, 'json');
    });

    function showLoggedIfNecessary(response)
    {
        if(response.loggedIn) {
            $("#tournamentControllerDiv").show();
            $("#welcomeDiv").hide();
            if(response.isGladiator)
                $("#welcomeForGladiatorsDiv").show();
            else
                $("#welcomeForLadderDiv").show();
            $.post('GladiatorServlet', JSON.stringify({"command":"getGladiatorsCanChallenge"}), onGladiatorsCanChallengeResponse, 'json');
            $.post("GladiatorServlet", JSON.stringify({"command":"getUserOngoingMatches"}), onGetOngoingMatchesSuccess, 'json');
        }
        else {
            $("#welcomeDiv").show();
            $("#loginButtn").show();
            $("#signupButtn").show();
        }
    }

    function onGetOngoingMatchesSuccess(response) {
        if(response.success == false)
            return;
        $("#MatchesToReportTableBody").empty();
        var matches = response.matches;

        for(var i in matches) {
            var startedOn = new Date(matches[i].startedOn);
            var dateText = startedOn.getDate() + '/' + (startedOn.getMonth() + 1) + '/' + startedOn.getFullYear();
            var buttonHtml = '<button id="reportMatch' + matches[i].id + '" class="btn btn-success" data-loading-text="please wait..." onclick="reportLadderMatch(' + matches[i].id + ')">Report match</button>';
            var htmlText = '<tr id="rowOfLadderMatch' + matches[i].id + '"><td>' + matches[i].opponentUsername + "</td><td>" + matches[i].mapPlayed + "</td><td>" +
                dateText + "</td><td>" + buttonHtml + '</td></tr>';
            $("#MatchesToReportTableBody").append(htmlText);
        }
    }

    function reportLadderMatch(id) {
        var scoreReported = $("#matchReportScore").val();
        if(scoreReported != 'won' && scoreReported != 'loss') {
            $("#returnMsgReportLadderMatch").text("Please select whether the match was won or lost");
            return;
        }
        var inputObject = {
            "command":"reportMatch",
            "matchId" : id,
            "result": $("#matchReportScore").val()
        };

        $.post('LadderServlet', JSON.stringify(inputObject), onReportLadderMatchSuccess, 'json');
        $("#returnMsgReportLadderMatch").text('');
    }

    function onReportLadderMatchSuccess(response) {
        if(response.success == false) {
            $("#returnMsgReportLadderMatch").text(response.errorMsg);
            return;
        }
        else {
            $("#returnMsgReportLadderMatch").text(response.successMsg);
            $.get('GetInfoServlet', {"command" : "getCurrentStreaks"}, onStreaksResponse, 'json');
            $.get('GetInfoServlet', {"command" : "getHallOfFame"}, onHallOfFameResponse, 'json');
            $.post("GladiatorServlet", JSON.stringify({"command":"getUserOngoingMatches"}), onGetOngoingMatchesSuccess, 'json');
        }
    }

    var userRank;
    var gladiators;

    function onGladiatorsCanChallengeResponse(response) {
        $("#ChallengeTableBody").empty();
        gladiators = response.gladiators;
        userRank = response.userRank;
        for(var i in gladiators) {
            var buttonHtml = '<button id="challenge' + gladiators[i].userId + '" class="btn btn-success" data-loading-text="please wait..." onclick="challengeGladiator(' + i + ')">Challenge</button>';
            var htmlText = "<tr><td>" + gladiators[i].username + "</td><td>" + gladiators[i].streak + "</td><td>" + gladiators[i].usedHeroes + "</td><td>" + buttonHtml + "</td></tr>";
            $("#ChallengeTableBody").append(htmlText);
        }
    }

    function challengeGladiator(challengedUser) {
        var lowerRanked = false;
        if(gladiators[challengedUser].rank < userRank) {
            lowerRanked = true;
            $("#mapPrioritiesDiv").show();
            $("#mapVetoDiv").hide();
            $("#challengeGladiatorDiv").show();
        } else {
            $("#mapPrioritiesDiv").hide();
            $("#mapVetoDiv").show();
            $("#challengeGladiatorDiv").show();
        }

        $("#confirmChallengeBtn").click(function () {
            confirmChallenge(challengedUser, lowerRanked);
        })
    }

    function confirmChallenge(challengedUser, lowerRanked) {
        if(lowerRanked) {
            var priority1 = $("#firstPriority").val();
            var priority2 = $("#secondPriority").val();
            if(priority1 == priority2) {
                $("#modalContent").text("The two map vetoes must differ!");
                $("#exampleModal").modal("show");
                return;
            }
            $("#confirmChallengeBtn").unbind('click');
            $("#returnMessageOnChallenge").empty();
            $.post('GladiatorServlet', JSON.stringify({"command":"challengeGaldiator", "userToChallenge":gladiators[challengedUser].userId, "mapPriorities":[priority1, priority2]}), onChallengeGladiatorResponse, 'json');
        } else {
            var mapVeto = $("#mapVeto").val();
            $("#confirmChallengeBtn").unbind('click');
            $("#returnMessageOnChallenge").empty();
            $.post('GladiatorServlet', JSON.stringify({"command":"challengeGaldiator", "userToChallenge":gladiators[challengedUser].userId, "mapVeto":mapVeto}), onChallengeGladiatorResponse, 'json');
        }
    }

    function onChallengeGladiatorResponse(response) {
        if(response.success)
            $("#returnMessageOnChallenge").text("A message was sent to the person you challenged. Please wait to see if he accepts or declines that challenge, " +
                "and if he does the map will be revealed to you and you will be able to pick a hero by the rules of the tournament.")
        else
            $("#returnMessageOnChallenge").text(response.errorMsg);
    }

    function joinArena() {
        $.post('GladiatorServlet', JSON.stringify({"command":"joinArena"}), onJoinArenaResponse, 'json');
    }

    function onJoinArenaResponse(response) {
        $("#returnMessageOnJoinArena").text(response.errorMsg);

        if(response.success)
            location.reload();
    }

    var streakResponse = null;

    function onStreaksResponse(response) {
        streakResponse = response;
        $("#streakTableBody").empty();
        $("usedHeroesLeftPannelDiv").hide();
        var rank, rowNum = 0;
        var previousStreak = 1000;
        var streaks = response.streaks;
        for(var i in streaks) {
            rowNum = rowNum + 1;
            if(streaks[i].streak < previousStreak) {
                previousStreak = streaks[i].streak;
                rank = rowNum;
            }
            var id = 'streakTd' + i;
            var htmlText = "<tr><td>" + rank + '</td><td id="' + id + '" style="cursor: pointer;">' + streaks[i].username + "</td><td>" + streaks[i].streak + "</td></tr>";
            $("#streakTableBody").append(htmlText);
            addShowUsedHeroesOnClick(id, i);
        }
    }

    function addShowUsedHeroesOnClick(tdId, index) {
        var selector = "#" + tdId;
        $(selector).click(function() { showUsedHeroes(index); });
    }

    function showUsedHeroes(index) {
        $("#usedHeroesLeftPannelDiv").show();
        $("#usedHeroesPar").empty();
        $("#usedHeroesHeader").text("The heroes used by " + streakResponse.streaks[index].username);
        for(var i in streakResponse.streaks[index].usedHeroes) {
            var imgHtml = '<img src="images/heroes/' + streakResponse.streaks[index].usedHeroes[i] + '.png" style="margin:10px" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" title="' + streakResponse.streaks[index].usedHeroes[i] + '">'
            $("#usedHeroesPar").append(imgHtml);
        }
        $('[data-toggle="tooltip"]').tooltip();
    }

    var hallfOfFame;

    function onHallOfFameResponse(response) {
        $("#hallOfFameTableBody").empty();
        var rank, rowNum = 0;
        var previousStreak = 1000
        hallfOfFame = response;
        var streaks = response.streaks;
        for(var i in streaks) {
            rowNum = rowNum + 1;
            if(streaks[i].streak < previousStreak) {
                previousStreak = streaks[i].streak;
                rank = rowNum;
            }
            var id = 'HallOfFameRow' + i;
            var htmlText = "<tr><td>" + rank + '</td><td id="' + id + '" style="cursor: pointer;">' + streaks[i].username + "</td><td>" + streaks[i].streak + "</td></tr>";
            $("#hallOfFameTableBody").append(htmlText);
            addShowHallOfFameUsedHeroesOnClick(id, i);
        }
    }

    function addShowHallOfFameUsedHeroesOnClick(tdId, index) {
        var selector = "#" + tdId;
        $(selector).click(function() { showHallfOfFameUsedHeroes(index); });
    }

    function showHallfOfFameUsedHeroes(index) {
        $("#usedHeroesRightPannelDiv").show();
        $("#usedHallOfFameHeroesPar").empty();
        $("#usedFallOfFameHeroesHeader").text("The heroes used by " + hallfOfFame.streaks[index].username);
        for(var i in hallfOfFame.streaks[index].usedHeroes) {
            var imgHtml = '<img src="images/heroes/' + hallfOfFame.streaks[index].usedHeroes[i] + '.png" style="margin:10px" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" title="' + hallfOfFame.streaks[index].usedHeroes[i] + '">'
            $("#usedHallOfFameHeroesPar").append(imgHtml);
        }
        $('[data-toggle="tooltip"]').tooltip();
    }

    function controllerShow(toShowDiv) {
        var divs = [{"div":"welcomeDiv", "title":"Gladiator Arena"},{"div":"challengeDiv", "title":"Challenge a gladiator"}, {"div":"reportMatchDiv", "title":"Report match"}, {"div":"mapPoolDiv", "title":"Tournament map pool"}, {"div":"faqDiv", "title":"FAQ"}];

        for(var i  in divs) {
            var div = divs[i];
            if(toShowDiv == div.div) {
                $("#" + div.div).show();
                $("#MidPanelTitle").text(div.title);
            }
            else
                $("#" + div.div).hide();
        }
    }

    function toggleVisibility(id1, id2) {
        var selector = "#" + id1;
        $(selector).toggle();
        selector = "#" + id2;
        $(selector).toggle();
    }
</script>
</html>
