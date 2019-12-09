<%--
  Created by IntelliJ IDEA.
  User: maria
  Date: 10/15/2019
  Time: 5:44 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>Heroes 5.5 ladder</title>
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
    <p id="main>Btn" class="btn btn-success pull-left" onclick="controllerShow('informationDiv')" style="margin-left:20px">Main</p>
    <a id="gladiatorBtn" href="Gladiators.jsp" class="btn btn-success pull-left" style="margin-left:20px">Gladiator Arena</a>
    <p id="mapPoolBtn" class="btn btn-success pull-left" onclick="controllerShow('mapPoolDiv')" style="margin-left:20px">Map pool</p>
    <a id="loginButtn" href="login.jsp?returnPage=index.jsp" class="btn btn-success pull-right" style="float:right!important;display: none;">Login</a>
    <a id="signupButtn" href="signup.jsp" class="btn btn-success pull-right" style="float:right!important;margin-right: 20px;display: none;">Sign up</a>
    <a id="feedbackButton" href="feedback.jsp" class="btn btn-success pull-right" style="float:right!important;margin-right: 20px;display: block">Feedback</a>
    <p id="faqButton" class="btn btn-success pull-right" style="float:right!important;margin-right: 20px;display: block" onclick="controllerShow('faqDiv')">FAQ</p>
    <div id="welcomeDiv" style="float:right!important;margin-right: 20px;display: none;">
      <p id="logoutPar" class="small" style="float:right!important;cursor:pointer"><u>logout</u></p>
      <p id="welcomePar" style="float:right!important;margin-right: 10px;"></p>
    </div>
    <div class="container-fluid">
      <div class="row row-offcanvas row-offcanvas-left">
        <div class="col-xs-12 col-sm-12" data-spy="scroll" data-taget="#sidebar-nav">
          <div class="row">
            <div class="col-xs-12 col-sm-6 col-lg-3" style="padding-right:0px;">
              <div class="panel-default">
                <div class="panel-heading">
                  <h4 id="leftPannelTitle">Top Ranked Players</h4>
                </div>
                <div id="topPlayersDiv" class="panel-body latest-games">
                  <div class="list-group table-responsive">
                    <table class="table table-bordered" style="color:#f9f9f9">
                      <thead>
                        <td>Rank</td>
                        <td>Player</td>
                        <td>Points</td>
                      </thead>
                      <tbody id="PlayersTableBody">
                      </tbody>
                    </table>
                  </div>
                </div>
                <div id="matchHistoryDiv" class="panel-body latest-games">
                  <div class="list-group table-responsive">
                    <table class="table table-bordered" style="color:#f9f9f9">
                      <thead>
                        <td>Time reported</td>
                        <td>Match info</td>
                      </thead>
                        <tbody id="MatchIstoryTableBody">
                        </tbody>
                    </table>
                  </div>
                </div>
              </div>
              <div style="text-align: center">
                  <p class="btn btn-success" style="width:35%;margin-right: 20px;margin-left: 20px; font-size: 16px" onclick="leftControllerShow('topPlayersDiv')">Top players</p>
                  <p class="btn btn-success" style="width:35%;margin-right: 20px;margin-left: 20px; font-size: 16px" onclick="leftControllerShow('matchHistoryDiv')">Match history</p>
              </div>
              <img src="images/roman-guro-heroes-of-might-and-magic-v-game-poster2-art-fent.jpg" style="width:100%;margin-top:20px">
            </div>
            <div class="col-xs-12 col-sm-6 col-lg-5" style="padding-right:0px;">
              <div class="panel-default">
                <div class="panel-heading">
                  <h4 id="MidPanelTitle">Information</h4>
                </div>
                <div id="welcomeLadderDiv" style="display: none">
                    <p>In case you are looking for real games that will test your skills, and impact your ELO, press Ladder to find people who play at relatively times as you do.</p><br>
                    <p>If you want to test some strategies or may be play a less stressed game you can look for one on the right or press unranked to post your intention to play.</p> <br/>
                    <p>If you want an even more challenging way of playing you can try out the Gladiator Arena tournament by pressin the Tournament button.</p><br/>
                </div>
                <div id="informationDiv">
                  <!--<p>Welcome to the official ranked page of fan-made project Heroes 5.5.
                      Track your proggres in ladder, find whos the best player at the moment and feel
                      free to become the one.
                  </p>-->
                  <!--<img src="images/HeroesInfo.jpg" style="width: 50%; float:right; margin-top: 10px;"> -->
                  <p style="font-size:18px;margin:12px;">Welcome to the official Heroes 5.5 Ladder page!<br/>
                      Do you want to challenge yourself and put your favorite heroes to the test? <br/>
                      Ever wondered what is your skill level compared to others? <br/>
                      This is the place and time to find it out. Here you can:
                  </p>
                  <ul style="font-size:18px;margin:12px;">
                    <li style="margin:10px;">Find opponents around your skill level who play at similar hours to yours.</li>
                    <li style="margin:10px;">Track your imporvement using our ELO ranking system.</li>
                    <li style="margin:10px;">We also have an option to find people to play in a less stressed way where you can develop and test new strategies.</li>
                    <li style="margin:10px;">Find good maps for 1v1 games.</li>
                    <li style="margin:10px;">And if you are truely competitive we even have tournaments you can participate in.</li>
                  </ul>
                  <div style="text-align:center">
                      <a href="signup.jsp" class="btn btn-success">Click here to sign up</a>
                  </div>
                  <p style="margin-top:20px">If you want to sign up you must be a member of our discord server, to make sure you are, please click on the link below.</p>
                  <a href="https://discordapp.com/invite/G7jadyp" target="_blank">Click here to join our Heroes 5.5 discord server</a>
                </div>
                <div id="createChallengeDiv" style="display: none">
                  <form style="margin-top:30px">
                    <div class="form-group">
                      <label>Preffered map (leave empty if you don't care)</label>
                      <input type="text" class="form-control" id="inputMap" placeholder="map name"/>
                    </div>
                    <div class="form-group">
                      <label>Preferred difficulty settigs</label>
                      <div class="form-check">
                        <input class="form-check-input" type="checkbox" value="" id="difficultyNoramlInput"/>
                        <label class="form-check-label" for="difficultyNoramlInput">
                          Normal
                        </label>
                        <input class="form-check-input" type="checkbox" value="" id="difficultyHardInput"/>
                        <label class="form-check-label" for="difficultyHardInput">
                          Hard
                        </label>
                        <input class="form-check-input" type="checkbox" value="" id="difficultyHeroicInput"/>
                        <label class="form-check-label" for="difficultyHeroicInput">
                          Heroic
                        </label>
                        <input class="form-check-input" type="checkbox" value="" id="difficultyImpossibleInput"/>
                        <label class="form-check-label" for="difficultyImpossibleInput">
                          Impossible
                        </label>
                      </div>
                    </div>
                    <label for="startingHourInput">Starting hour: </label>
                    <p id="startingHourPar">12:00</p>
                    <input type="range" class="custom-range" min="0" max="24" id="startingHourInput" onchange="showStartingHour()"/>
                    <label for="endingHourInput">Ending hour: </label>
                    <p id="endingHourPar">12:00</p>
                    <input type="range" class="custom-range" min="0" max="24" id="endingHourInput" onchange="showEndingHour()"/>
                    <div class="text-center">
                      <button class="btn btn-primary" type="button" onclick="createChallenge()">Create challenge</button>
                    </div>
                  </form>
                  <div>
                    <p id="returnMsgCreateChallenge"></p>
                  </div>
                </div>
                <div id="ladderDiv" style="display: none">
                    <div style="margin-top:30px">
                        <div class="row">
                            <div class="col-md-12">
                                <label class="mdb-main-label">Choose maps to veto (for now, only a single map can be vetoed)</label>
                                <select id="selectVetoedMaps" multiple='multiple'>
                                    <option value="Lets Fight!">Lets Fight!</option>
                                    <option value="Deadly Environment">Deadly Environment</option>
                                    <option value="ARMG Jebus">ARMG Jebus</option>
                                </select>
                                <!--<button class="btn-save btn btn-primary btn-sm">Save selection</button>-->
                            </div>
                        </div>
                        <div>
                            <label class="mdb-main-label">What days and times can you play at?</label><br/>
                            <label><input id="mondayCheckbox" onchange="handleStarEndHoursDiv('Monday')" type="checkbox">Monday</label><br/>
                            <div id="startendHourMondayDiv" style="display: none">
                                <label for="startingHourMondayInput">Starting hour: </label>
                                <p id="startingHourMondayPar">12:00</p>
                                <input type="range" class="custom-range" min="0" max="24" id="startingHourMondayInput" onchange="showLadderStartingHour('Monday')"/>
                                <label for="endingHourMondayInput">Ending hour: </label>
                                <p id="endingHourMondayPar">12:00</p>
                                <input type="range" class="custom-range" min="0" max="24" id="endingHourMondayInput" onchange="showLadderEndingHour('Monday')"/>
                            </div>
                            <label><input id="tuesdayCheckbox" onchange="handleStarEndHoursDiv('Tuesday')" type="checkbox">Tuesday</label><br/>
                            <div id="startendHourTuesdayDiv" style="display: none">
                                <label for="startingHourTuesdayInput">Starting hour: </label>
                                <p id="startingHourTuesdayPar">12:00</p>
                                <input type="range" class="custom-range" min="0" max="24" id="startingHourTuesdayInput" onchange="showLadderStartingHour('Tuesday')"/>
                                <label for="endingHourTuesdayInput">Ending hour: </label>
                                <p id="endingHourTuesdayPar">12:00</p>
                                <input type="range" class="custom-range" min="0" max="24" id="endingHourTuesdayInput" onchange="showLadderEndingHour('Tuesday')"/>
                            </div>
                            <label><input id="wednesdayCheckbox" onchange="handleStarEndHoursDiv('Wednesday')" type="checkbox">Wednesday</label><br/>
                            <div id="startendHourWednesdayDiv" style="display: none">
                                <label for="startingHourWednesdayInput">Starting hour: </label>
                                <p id="startingHourWednesdayPar">12:00</p>
                                <input type="range" class="custom-range" min="0" max="24" id="startingHourWednesdayInput" onchange="showLadderStartingHour('Wednesday')"/>
                                <label for="endingHourWednesdayInput">Ending hour: </label>
                                <p id="endingHourWednesdayPar">12:00</p>
                                <input type="range" class="custom-range" min="0" max="24" id="endingHourWednesdayInput" onchange="showLadderEndingHour('Wednesday')"/>
                            </div>
                            <label><input id="thursdayCheckbox" onchange="handleStarEndHoursDiv('Thursday')" type="checkbox">Thursday</label><br/>
                            <div id="startendHourThursdayDiv" style="display: none">
                                <label for="startingHourThursdayInput">Starting hour: </label>
                                <p id="startingHourThursdayPar">12:00</p>
                                <input type="range" class="custom-range" min="0" max="24" id="startingHourThursdayInput" onchange="showLadderStartingHour('Thursday')"/>
                                <label for="endingHourThursdayInput">Ending hour: </label>
                                <p id="endingHourThursdayPar">12:00</p>
                                <input type="range" class="custom-range" min="0" max="24" id="endingHourThursdayInput" onchange="showLadderEndingHour('Thursday')"/>
                            </div>
                            <label><input id="fridayCheckbox" onchange="handleStarEndHoursDiv('Friday')" type="checkbox">Friday</label><br/>
                            <div id="startendHourFridayDiv" style="display: none">
                                <label for="startingHourFridayInput">Starting hour: </label>
                                <p id="startingHourFridayPar">12:00</p>
                                <input type="range" class="custom-range" min="0" max="24" id="startingHourFridayInput" onchange="showLadderStartingHour('Friday')"/>
                                <label for="endingHourFridayInput">Ending hour: </label>
                                <p id="endingHourFridayPar">12:00</p>
                                <input type="range" class="custom-range" min="0" max="24" id="endingHourFridayInput" onchange="showLadderEndingHour('Friday')"/>
                            </div>
                            <label><input id="saturdayCheckbox" onchange="handleStarEndHoursDiv('Saturday')" type="checkbox">Saturday</label><br/>
                            <div id="startendHourSaturdayDiv" style="display: none">
                                <label for="startingHourSaturdayInput">Starting hour: </label>
                                <p id="startingHourSaturdayPar">12:00</p>
                                <input type="range" class="custom-range" min="0" max="24" id="startingHourSaturdayInput" onchange="showLadderStartingHour('Saturday')"/>
                                <label for="endingHourSaturdayInput">Ending hour: </label>
                                <p id="endingHourSaturdayPar">12:00</p>
                                <input type="range" class="custom-range" min="0" max="24" id="endingHourSaturdayInput" onchange="showLadderEndingHour('Saturday')"/>
                            </div>
                            <label><input id="sundayCheckbox" onchange="handleStarEndHoursDiv('Sunday')" type="checkbox">Sunday</label><br/>
                            <div id="startendHourSundayDiv" style="display: none">
                                <label for="startingHourSundayInput">Starting hour: </label>
                                <p id="startingHourSundayPar">12:00</p>
                                <input type="range" class="custom-range" min="0" max="24" id="startingHourSundayInput" onchange="showLadderStartingHour('Sunday')"/>
                                <label for="endingHourSundayInput">Ending hour: </label>
                                <p id="endingHourSundayPar">12:00</p>
                                <input type="range" class="custom-range" min="0" max="24" id="endingHourSundayInput" onchange="showLadderEndingHour('Sunday')"/>
                            </div>
                        </div>
                    </div>
                    <div style="text-align: center; margin-top:15px">
                        <button id="finOpponentButton" class="btn btn-primary" type="button" onclick="submitLadder()">Find opponent</button>
                        <button id="clearPrioritiesButton" class="btn btn-primary" type="button" style="display: none" onclick="clearPriorities()">Don't look for opponent</button>
                    </div>
                    <div>
                        <p id="returnMsgLadder"></p>
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
                <div id="reportMatchDiv" style="display: none">
                    <div style="text-align: center">
                        <h4>Currently played ladder matches</h4>
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
                <!--<div id="reportMatchDiv" style="display: none">
                    <form style="margin-top:30px">
                        <div class="form-group">
                            <label>I played against:</label>
                            <input type="text" class="form-control" id="inputReportedMatchPlayer" placeholder="Player username"/>
                        </div>
                        <div>
                            <label>The match was:</label><br/>
                            <select id="matchReportScore" class="custom-select" style="color:#555">
                                <option selected>The match was</option>
                                <option value="loss">Lost</option>
                                <option value="won">Won</option>
                            </select>
                        </div>
                        <div>
                            <label>The difficulty setting was:</label><br/>
                            <select id="matchReportDifficulty" class="custom-select" style="color:#555">
                                <option selected>The difficulty setting was</option>
                                <option value="normal">Normal</option>
                                <option value="hard">Hard</option>
                                <option value="heroic">Heroic</option>
                                <option value="impossible">Impossible</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>We played on the map:</label>
                            <input type="text" class="form-control" id="inputReportedMatchMap" placeholder="Map name">
                        </div>
                        <div class="text-center">
                            <button class="btn btn-primary" type="button" onclick="reportMatch()">Send</button>
                        </div>
                    </form>
                    <div>
                        <p id="returnMsgReportMatch"></p>
                    </div>
                </div>-->
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
                      <p>Q: What is the difference between ladder and unranked challenges?<br/>
                          A: When you play ladder games you are forced to an extent to our rules of the game. Play a map from our map pool and we find an opponent for you. Those games also effect your ELO rating
                          and sometimes you'd rather play a chill game that doesn't effect your ELO. The unranked challenges allow you looking for opponents that will play the game you'd like to play by menas of
                          a map and difficulty setting. Unranked challenges can't be reported as those games don't effect your ELO, but once somebody agrees to play such a game you two are tagged in the
                          discord server and can start playing the game you wanted to play. If you'd like to play a game that won't effect your ELO perhaps you'd like to check the open unranked challenges on the fight.
                      </p>
                      <p>Q: How to ladder?<br/>
                        A: First, you must sign up, for that you must also join our discord server (see above).<br/>
                        - When signed up press on Ladder, and then you will be able to choose your priorities for playing regarding the hours of the week you can play at and maps you'd rather avoid.<br/>
                        - Press "Find opponent" at the bottom, and we'll start looking for an opponent for you, first of all based on the hours of the week you can play, and then we take ELO into account as well. <br/>
                        - Once we find an opponent you will be both tagged in our discord server so that you can find one another, and then you can start your game. <br/>
                        - When the game finishes come back here to report the match. It is better if the loser does that, but in case the losing player avoids that, the winning player can also report the match, but we'll confrim with the loser that it actually happened.
                    </p>
                    <p>Q: On what difficulty setting the ladder matches are played on?<br/>
                        A: Since the ladder is for the more competitive players we set the difficulty setting to impossible for those matches. In case both players agree the difficulty setting can be set to Heroic
                        but please avoid playing on Normal or Hard difficulty settings if you play a ranked ladder match. We have the unranked system for those who prefer more casual games.
                    </p>
                    <p>Q: What if I set up my ladder preferences but I won't be able to play soon, or I'd rather change those? <br/>
                        A: Once you set your ladder preferences you have the option to update them as well as cancel your search for an opponent.
                        If you already set your ladder preferences just check the ladder tab once you are logged in.
                    </p>
                    <p>Q: How many ladder games can I play at a time? <br/>
                        A: You can play as many ladder games as you want (as long as we can find enough opponents for you). Once an opponent is found, the ladder is no longer searching for a new opponent.
                        But in case you want 2 or more games at a time just go to the ladder tab and set your preferences again. From that point on you will be considered as one who looks for an opponent.
                        Once we find a second match for you, again you won't be treated as one who looks for a match, but you can change that. <br/>
                        (We might restrict the amount of games people can play at a time in the future, but for now you can play as many as you want)
                    </p>
                    <p>
                        Q: What if my opponent is in a different time zone then me? <br/>
                        A: We take care of the time zones. Each time you enter a time, or see a time, you actually see the time converted to the time zone your operation system is set to
                        (and we receive your submissions already converted into a generic time zone - UTC). <br/>
                        For example, if somebody in the CET time zone (west EU) enters that he can play starting from 19:00 till 23:00, then those who see the site in NA in the EST time zone
                        actually see from 13:00 till 17:00, as this is the actual time that user in Europe meant. Same goes for the ladder priorities when we are looking for an opponent for you.
                        We might find you an opponent in a different time zone, but we'll make sure that you have enough overlapping hours per week you can play at to finish a heroes game
                        in a week or two. When we find an opponent, or somebody accepts your challenge make sure to ask what time zone your opponent is, and by how much he is ahead / behind you.
                        You will find that you have enough hours, or the hours you mentioned in the challenge fit him well.
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
                  <div id="acceptChallengeDiv" style="display: none">
                    <p id="acceptChallengeInfo"></p>
                    <div id="getContacts" style="display: none">
                        <input type="text" class="form-control" id="getDiscordForChallenge" style="display: none" placeholder="Discord username"/>
                    </div>
                    <div id="shareContacts" style="display: none">
                        <p id="emailAddressOfChallengedPlayer" style="display: none"></p>
                        <br/>
                        <p id="discordUsernameOfChallengedPlayer" style="display: none"></p>
                    </div>
                    <p class="btn btn-success" id="completeChallengeAccept">Accept</p>
                    <div>
                        <p id="returnAcceptChallengePar"></p>
                    </div>
                </div>
                <div id="ladderControllerDiv" style="margin-bottom:30px;margin-top:10px;text-align:center;display:none">
                  <p id="playLadderButton" class="btn btn-success" style="width:30%" onclick="controllerShow('ladderDiv')">Play</p>
                  <p id="reportMatchButton" class="btn btn-success" style="width:30%" onclick="controllerShow('reportMatchDiv')">Report match</p>
                  <p id="mapPoolButton" class="btn btn-success" style="width:30%" onclick="controllerShow('mapPoolDiv')">Map pool</p>
                </div>
                <div id="controllerDiv" style="margin-top:10px;text-align:center;">
                  <p id="ladderButton" class="btn btn-success" style="width:30%;display: none" onclick="openLaddertab()">Ladder</p>
                  <p id="infoButton" class="btn btn-success" style="width:30%;display: none" onclick="controllerShow('informationDiv')">Information</p>
                  <p id="challengeButton" class="btn btn-success" style="width:30%;display: none" onclick="controllerShow('createChallengeDiv')">Unranked</p>
                  <a href="Gladiators.jsp" id="gaButton" class="btn btn-success" style="width:30%;display: none" >Tournament</a>
                </div>
              </div>
            </div>
            <div class="col-xs-12 col-sm-6 col-lg-4">
              <div class="panel-default">
                <div class="panel-heading">
                  <h4 id="rightPanelTitle">Open unranked challenges</h4>
                </div>
                <div id="unrankedChallengesDiv" class="panel-body latest-games">
                  <div class="list-group table-responsive">
                    <table class="table table-bordered" style="color:#f9f9f9">
                      <thead>
                      <td>Player</td>
                      <td>Map</td>
                      <td>Difficulty</td>
                      <td>Hours</td>
                      </thead>
                      <tbody id="ChallangessTableBody">
                      </tbody>
                    </table>
                  </div>
                </div>
                <div id="currentMatchesDiv" class="panel-body latest-games"  style="display: none">
                  <div class="list-group table-responsive">
                    <table class="table table-bordered" style="color:#f9f9f9">
                      <thead>
                      <td>Player1</td>
                      <td>Player2</td>
                      <td>Map</td>
                      <td>Started On</td>
                      </thead>
                      <tbody id="CurrentMatchesTableBody">
                      </tbody>
                    </table>
                  </div>
                </div>
              </div>
              <div style="text-align: center">
                  <p class="btn btn-success" style="width:35%;margin-right: 20px;margin-left: 20px; font-size: 16px" onclick="RightControllerShow('unrankedChallengesDiv')">Unranked</p>
                  <p class="btn btn-success" style="width:35%;margin-right: 20px;margin-left: 20px; font-size: 16px" onclick="RightControllerShow('currentMatchesDiv')">Current matches</p>
              </div>
              <img src="images/9391-Heroes_of_Might_And_Magic_5.2.jpg" style="width:100%;margin-top:20px">
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
<script type="text/javascript">
  $(document).ready( function() {
      $.post('LoginServelet', {"command": "checkLogin"}, showLoggedIfNecessary, 'json');
      $.get('GetInfoServlet', {"command" : "getTopRankedUsers"}, onPlayersResponse, 'json');
      $.get('GetInfoServlet', {"command" : "getFirstChallenges"}, onChallengesResponse, 'json');
      $.get('GetInfoServlet', {"command" : "getMatchHisotry"}, onMatchHistoryResponse, 'json');
      $.get('GetInfoServlet', {'command' : "getCurrentLadderMatches"}, onCurrentLadderMatchesResponse, 'json');
      $('#selectVetoedMaps').multiSelect();
    });

  var loggedUserId = -1;

  function toggleVisibility(id1, id2) {
      var selector = "#" + id1;
      $(selector).toggle();
      selector = "#" + id2;
      $(selector).toggle();
  }

  function handleStarEndHoursDiv(day) {
      var selector = "#startendHour" + day + "Div";
      $(selector).toggle();
  }

  function controllerShow(toShowDiv) {
    var divs = [{"div":"createChallengeDiv", "title":"Create unranked challenge"}, {"div":"informationDiv", "title":"Information"}, {"div":"reportMatchDiv", "title":"Report match"}, {"div":"acceptChallengeDiv", "title":"Accept challenge"}, {"div":"faqDiv", "title":"FAQ"}, {"div":"ladderDiv", "title":"Ladder"}, {"div":"mapPoolDiv", "title":"Ladder map pool"}, {"div":"welcomeLadderDiv", "title":"Welcome to the Ladder"}];

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

  function openLaddertab() {
      $("#ladderControllerDiv").toggle();
      var isVisible = $("#ladderControllerDiv").is(":visible");
      if(isVisible) {
          controllerShow("ladderDiv");
          $("#ladderButton").text("Close ladder");
          $("#infoButton").hide();
          $("#challengeButton").hide();
          $("#gaButton").hide();
      }
      else {
          controllerShow("welcomeLadderDiv");
          $("#ladderButton").text("Ladder")
          //$("#infoButton").show();
          $("#challengeButton").show();
          $("#gaButton").show();
      }
  }

  function leftControllerShow(toShowDiv) {
      var divs = [{"div":"topPlayersDiv", "title":"Top Ranked Players "}, {"div":"matchHistoryDiv", "title":"Match History"}];

      $.get('GetInfoServlet', {"command" : "getTopRankedUsers"}, onPlayersResponse, 'json');
      $.get('GetInfoServlet', {"command" : "getMatchHisotry"}, onMatchHistoryResponse, 'json');
      for(var i  in divs) {
          var div = divs[i];
          if(toShowDiv == div.div) {
              $("#" + div.div).show();
              $("#leftPannelTitle").text(div.title);
          }
          else
              $("#" + div.div).hide();
      }
  }

  function RightControllerShow(toShowDiv) {
      var divs = [{"div":"unrankedChallengesDiv", "title":"Open unranked challenges "}, {"div":"currentMatchesDiv", "title":"Current ongoing ladder matches"}];

      $.get('GetInfoServlet', {"command" : "getFirstChallenges"}, onChallengesResponse, 'json');
      $.get('GetInfoServlet', {"command" : "getCurrentLadderMatches"}, onCurrentLadderMatchesResponse, 'json');
      for(var i  in divs) {
          var div = divs[i];
          if(toShowDiv == div.div) {
              $("#" + div.div).show();
              $("#rightPanelTitle").text(div.title);
          }
          else
              $("#" + div.div).hide();
      }
  }

  function createChallenge() {
      $("#returnMsgCreateChallenge").text();

      var inputObject = {
        "command": "createChallenge",
        "mapName": $("#inputMap").val(),
        "normalDifficulty": $("#difficultyNoramlInput").is(':checked'),
        "hardDifficulty": $("#difficultyHardInput").is(':checked'),
        "heroicDifficulty": $("#difficultyHeroicInput").is(':checked'),
        "impossibleDifficulty": $("#difficultyImpossibleInput").is(':checked'),
        "startHour": getUtcTime($("#startingHourInput").val()),
        "endHour": getUtcTime($("#endingHourInput").val())
      };

      $.post('ChallengeServelet', JSON.stringify(inputObject), onChallengeSuccess, 'json');
  }

  function updatePriorities(priorities) {
      updateDayPriorities(priorities);
      updateStartingHourPriorities(priorities);
      updateEndingHourPriorities(priorities);
      $('#selectVetoedMaps').multiSelect('select', priorities.vetoedMaps);
  }

  function updateDayPriorities(priorities) {
      if(priorities.daysCanPlay[0]) {
          $("#mondayCheckbox").prop( "checked", true );
          $("#startendHourMondayDiv").show();
      }
      else {
          $("#mondayCheckbox").prop( "checked", false );
          $("#startendHourMondayDiv").hide();
      }

      if(priorities.daysCanPlay[1]) {
          $("#tuesdayCheckbox").prop( "checked", true );
          $("#startendHourTuesdayDiv").show();
      }
      else {
          $("#tuesdayCheckbox").prop( "checked", false );
          $("#startendHourTuesdayDiv").hide();
      }

      if(priorities.daysCanPlay[2]) {
          $("#wednesdayCheckbox").prop( "checked", true );
          $("#startendHourWednesdayDiv").show();
      }
      else {
          $("#wednesdayCheckbox").prop( "checked", false );
          $("#startendHourWednesdayDiv").hide();
      }

      if(priorities.daysCanPlay[3]) {
          $("#thursdayCheckbox").prop( "checked", true );
          $("#startendHourThursdayDiv").show();
      }
      else {
          $("#thursdayCheckbox").prop( "checked", false );
          $("#startendHourThursdayDiv").hide();
      }

      if(priorities.daysCanPlay[4]) {
          $("#fridayCheckbox").prop( "checked", true );
          $("#startendHourFridayDiv").show();
      }
      else {
          $("#fridayCheckbox").prop( "checked", false );
          $("#startendHourFridayDiv").hide();
      }

      if(priorities.daysCanPlay[5]) {
          $("#saturdayCheckbox").prop( "checked", true );
          $("#startendHourSaturdayDiv").show();
      }
      else {
          $("#saturdayCheckbox").prop( "checked", false );
          $("#startendHourSaturdayDiv").hide();
      }

      if(priorities.daysCanPlay[6]) {
          $("#sundayCheckbox").prop( "checked", true );
          $("#startendHourSundayDiv").show();
      }
      else {
          $("#sundayCheckbox").prop( "checked", false );
          $("#startendHourSundayDiv").hide();
      }
  }

  function updateStartingHourPriorities(priorities) {
      var tempDate = new Date(0);
      tempDate.setUTCHours(priorities.startHoursOfDay[0]);
      $("#startingHourMondayInput").val(tempDate.getHours());
      tempDate.setUTCHours(priorities.startHoursOfDay[1]);
      $("#startingHourTuesdayInput").val(tempDate.getHours());
      tempDate.setUTCHours(priorities.startHoursOfDay[2]);
      $("#startingHourWednesdayInput").val(tempDate.getHours());
      tempDate.setUTCHours(priorities.startHoursOfDay[3]);
      $("#startingHourThursdayInput").val(tempDate.getHours());
      tempDate.setUTCHours(priorities.startHoursOfDay[4]);
      $("#startingHourFridayInput").val(tempDate.getHours());
      tempDate.setUTCHours(priorities.startHoursOfDay[5]);
      $("#startingHourSaturdayInput").val(tempDate.getHours());
      tempDate.setUTCHours(priorities.startHoursOfDay[6]);
      $("#startingHourSundayInput").val(tempDate.getHours());

      showLadderStartingHour('Monday');
      showLadderStartingHour('Tuesday');
      showLadderStartingHour('Wednesday');
      showLadderStartingHour('Thursday');
      showLadderStartingHour('Friday');
      showLadderStartingHour('Saturday');
      showLadderStartingHour('Sunday');
  }

  function updateEndingHourPriorities(priorities) {
      var tempDate = new Date(0);
      tempDate.setUTCHours(priorities.endHoursOfDay[0]);
      $("#endingHourMondayInput").val(tempDate.getHours());
      tempDate.setUTCHours(priorities.endHoursOfDay[1]);
      $("#endingHourTuesdayInput").val(tempDate.getHours());
      tempDate.setUTCHours(priorities.endHoursOfDay[2]);
      $("#endingHourWednesdayInput").val(tempDate.getHours());
      tempDate.setUTCHours(priorities.endHoursOfDay[3]);
      $("#endingHourThursdayInput").val(tempDate.getHours());
      tempDate.setUTCHours(priorities.endHoursOfDay[4]);
      $("#endingHourFridayInput").val(tempDate.getHours());
      tempDate.setUTCHours(priorities.endHoursOfDay[5]);
      $("#endingHourSaturdayInput").val(tempDate.getHours());
      tempDate.setUTCHours(priorities.endHoursOfDay[6]);
      $("#endingHourSundayInput").val(tempDate.getHours());

      showLadderEndingHour('Monday');
      showLadderEndingHour('Tuesday');
      showLadderEndingHour('Wednesday');
      showLadderEndingHour('Thursday');
      showLadderEndingHour('Friday');
      showLadderEndingHour('Saturday');
      showLadderEndingHour('Sunday');
  }

  function clearPriorities() {
      $.post('LadderServlet', JSON.stringify({"command":"clearLadderPriorities"}), onClearPrioritiesSuccess, 'json');
  }

  function onClearPrioritiesSuccess(response) {
      if(response.success == false)
          $("#returnMsgLadder").text(response.errorMsg);
      else {
          $("#returnMsgLadder").text("You are no longer looking for an opponent");
          $("#finOpponentButton").text("Find opponent");
          $("#clearPrioritiesButton").hide();
      }
  }

  function submitLadder() {
      var inputObject = {
          "command":"submitLadderPriorities",
          "vetoedMaps":getVetoedMaps(),
          "playOnMonday":$("#mondayCheckbox").is(':checked'),
          "playOnTuesday":$("#tuesdayCheckbox").is(':checked'),
          "playOnWednesday":$("#wednesdayCheckbox").is(':checked'),
          "playOnThursday":$("#thursdayCheckbox").is(':checked'),
          "playOnFriday":$("#fridayCheckbox").is(':checked'),
          "playOnSaturday":$("#saturdayCheckbox").is(':checked'),
          "playOnSunday":$("#sundayCheckbox").is(':checked'),
          "monStartHour":getUtcTime($("#startingHourMondayInput").val()),
          "monEndHour":getUtcTime($("#endingHourMondayInput").val()),
          "tueStartHour":getUtcTime($("#startingHourTuesdayInput").val()),
          "tueEndHour":getUtcTime($("#endingHourTuesdayInput").val()),
          "wedStartHour":getUtcTime($("#startingHourWednesdayInput").val()),
          "wedEndHour":getUtcTime($("#endingHourWednesdayInput").val()),
          "thrStartHour":getUtcTime($("#startingHourThursdayInput").val()),
          "thrEndHour":getUtcTime($("#endingHourThursdayInput").val()),
          "friStartHour":getUtcTime($("#startingHourFridayInput").val()),
          "friEndHour":getUtcTime($("#endingHourFridayInput").val()),
          "satStartHour":getUtcTime($("#startingHourSaturdayInput").val()),
          "satEndHour":getUtcTime($("#endingHourSaturdayInput").val()),
          "sunStartHour":getUtcTime($("#startingHourSundayInput").val()),
          "sunEndHour":getUtcTime($("#endingHourSundayInput").val())
      };

      var numOfVetoedMaps = getNumOfVetoedMaps();
      var hoursPerWeek = calcHoursPerWeek(inputObject);

      if(numOfVetoedMaps > 1) {
          $("#returnMsgLadder").text("You are allowed to veto only a single map!");
          return;
      }
      if(hoursPerWeek < 8)
          $("#returnMsgLadder").text("To increase your chances of finding an opponent, please select at least 8 hours at which you are aviable.");
      else
          $.post('LadderServlet', JSON.stringify(inputObject), onLadderSuccess, 'json');
  }

  var mapNameMap = {"Lets Fight!":"Lets Fight!", "Deadly Environment":"Deadly Environment", "ARMG Jebus":"ARMG Jebus"};

  function calcHoursPerWeek(inputObject) {
      var startDay, endDay;
      var totalHours = 0;

      if(inputObject.playOnMonday)
          if(inputObject.monStartHour == inputObject.monEndHour)
              totalHours = totalHours + 24;
          else
              totalHours = totalHours + calcHoursOfDay(inputObject.monStartHour, inputObject.monEndHour);
      if(inputObject.playOnTuesday)
          if(inputObject.tueStartHour == inputObject.tueEndHour)
              totalHours = totalHours + 24;
          else
              totalHours = totalHours + calcHoursOfDay(inputObject.tueStartHour, inputObject.tueEndHour);
      if(inputObject.playOnWednesday)
          if(inputObject.wedStartHour == inputObject.wedEndHour)
              totalHours = totalHours + 24;
          else
              totalHours = totalHours + calcHoursOfDay(inputObject.wedStartHour, inputObject.wedEndHour);
      if(inputObject.playOnThursday)
          if(inputObject.thrStartHour == inputObject.thrEndHour)
              totalHours = totalHours + 24;
          else
              totalHours = totalHours + calcHoursOfDay(inputObject.thrStartHour, inputObject.thrEndHour);
      if(inputObject.playOnFriday)
          if(inputObject.friStartHour == inputObject.friEndHour)
              totalHours = totalHours + 24;
          else
              totalHours = totalHours + calcHoursOfDay(inputObject.friStartHour, inputObject.friEndHour);
      if(inputObject.playOnSaturday)
          if(inputObject.satStartHour == inputObject.satEndHour)
              totalHours = totalHours + 24;
          else
              totalHours = totalHours + calcHoursOfDay(inputObject.satStartHour, inputObject.satEndHour);
      if(inputObject.playOnSunday)
          if(inputObject.sunStartHour == inputObject.sunEndHour)
              totalHours = totalHours + 24;
          else
              totalHours = totalHours + calcHoursOfDay(inputObject.sunStartHour, inputObject.sunEndHour);

      return totalHours;
  }

  function calcHoursOfDay(startHour, endHour) {
      if(endHour < startHour)
          endHour = endHour + 24;

      return endHour - startHour;
  }

  function getVetoedMaps() {
      var mapIndexes = $("#selectVetoedMaps").val()
      var vetoedMaps = 0;
      var output = "";

      for(var i in mapIndexes) {
          if(output != "")
              output = output + ",";
          output = mapNameMap[mapIndexes[i]];
          vetoedMaps++;
      }

      return output;
  }

  function getNumOfVetoedMaps() {
      var mapIndexes = $("#selectVetoedMaps").val()
      var vetoedMaps = 0;

      for(var i in mapIndexes)
          vetoedMaps++;

      return vetoedMaps;
  }

  function onLadderSuccess(response) {
      if(response.success == false)
          $("#returnMsgLadder").text(response.errorMsg);
      else {
        if(response.opponentFound) {
          $("#returnMsgLadder").text("We found a match for you, check the discord server to see who do you play.");
          $("#finOpponentButton").text("Find opponent");
          $("#clearPrioritiesButton").hide();
        }
        else {
          $("#returnMsgLadder").text("You will be notified in the discord server as soon as we find a match for you.");
          $("#finOpponentButton").text("Update priorities");
          $("#clearPrioritiesButton").show();
        }
      }
  }

  function reportMatch() {
      var inputObject = {
          "command":"matchReport",
          "opponentUsername" : $("#inputReportedMatchPlayer").val(),
          "result": $("#matchReportScore").val(),
          "difficulty": $("#matchReportDifficulty").val(),
          "mapName": $("#inputReportedMatchMap").val()
      };

      $.post('ReportMatchServelet', JSON.stringify(inputObject), onReportMatchSuccess, 'json');
  }

  function getUtcTime(hours) {
    var d = new Date(0);
    d.setHours(hours);
    return d.getUTCHours();
  }

  function onChallengeSuccess(response) {
    if(response.success == false)
      $("#returnMsgCreateChallenge").text(response.errorMsg);
    else {
      $("#returnMsgCreateChallenge").text("Challenge created succesfully");
      $.get('GetInfoServlet', {"command" : "getFirstChallenges"}, onChallengesResponse, 'json');
    }
  }

  function onReportMatchSuccess(response) {
      if(response.success == false)
          $("#returnMsgReportMatch").text(response.errorMsg);
      else {
          $("#returnMsgReportMatch").text(response.successMsg);
          $.get('GetInfoServlet', {"command" : "getTopRankedUsers"}, onPlayersResponse, 'json');
      }
  }

  function showLadderStartingHour(day) {
      var startHourSelector = "#startingHour" + day + "Input";
      var startHour = $(startHourSelector).val();
      var paragraphSelector = "#startingHour" + day + "Par";
      $(paragraphSelector).text(startHour + ":00");
  }

  function showLadderEndingHour(day) {
      var startHourSelector = "#endingHour" + day + "Input";
      var endHour = $(startHourSelector).val();
      var paragraphSelector = "#endingHour" + day + "Par";
      $(paragraphSelector).text(endHour + ":00");
  }


  function showStartingHour() {
    var startHour = $("#startingHourInput").val();
    $("#startingHourPar").text(startHour + ":00");
  }

  function showEndingHour() {
    var endtHour = $("#endingHourInput").val();
    $("#endingHourPar").text(endtHour + ":00");
  }

  function showLoggedIfNecessary(response)
  {
    if(response.loggedIn)
    {
      loggedUserId = response.userId;
      $.post("LadderServlet", JSON.stringify({"command":"getUserOngoingMatches"}), onGetOngoingMatchesSuccess, 'json');
      $.post("LadderServlet", JSON.stringify({"command":"getLadderPriorities"}), onGetLadderPrioritiesSuccess, 'json');
      $("#loginButtn").hide();
      $("#signupButtn").hide();
      $("#welcomeDiv").show();
      $("#welcomePar").text("Welcome " + response.username);
      $("#logoutPar").click(logout);
      $("#informationDiv").hide();
      $("#controllerDiv").show();
      $("#challengeButton").show();
      $("#ladderButton").show();
      $("#MidPanelTitle").text("Choose");
      $("#matchHistoryDiv").hide();
      $("#welcomeLadderDiv").show();
      $("#gaButton").show();
    }
    else
    {
      loggedUserId = -1;
      $("#loginButtn").show();
      $("#signupButtn").show();
      $("#welcomeDiv").hide();
      $("#matchHistoryDiv").hide();
      $("#gaButton").hide();
    }
  }

  function logout() {
    $.post('LoginServelet', {"command": "logout"}, showLoggedIfNecessary);
  }

  function onPlayersResponse(response) {
    $("#PlayersTableBody").empty();
    var rank = 0;
    var players = response.names;
    for(var i in players) {
      rank = rank + 1;
      var htmlText = "<tr><td>" + rank + "</td><td>" + players[i].name + "</td><td>" + players[i].points + "</td></tr>";
      $("#PlayersTableBody").append(htmlText);
    }
  }

  function onMatchHistoryResponse(response) {
      $("#MatchIstoryTableBody").empty();
      var rank = 0;
      var matches = response.matches;
      for(var i in matches) {
          var tempDate = new Date(matches[i].date);
          var htmlText = "<tr><td>" + tempDate.toDateString() + "</td><td>" + matches[i].matchInfo + "</td></tr>";
          $("#MatchIstoryTableBody").append(htmlText);
      }
  }

  function onGetLadderPrioritiesSuccess(response) {
      if(response.success == false)
          return;

      if(response.foundPriorities == false) {
          $("#finOpponentButton").text("Find opponent");
          $("#clearPrioritiesButton").hide();
          return
      }

      $("#finOpponentButton").text("Update priorities");
      $("#clearPrioritiesButton").show();

      updatePriorities(response.priorities);
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
          $.get('GetInfoServlet', {"command" : "getTopRankedUsers"}, onPlayersResponse, 'json');
          $.get('GetInfoServlet', {"command" : "getMatchHisotry"}, onMatchHistoryResponse, 'json');
          $.post("LadderServlet", JSON.stringify({"command":"getUserOngoingMatches"}), onGetOngoingMatchesSuccess, 'json');
      }
  }

  function onCurrentLadderMatchesResponse(response) {
    $("#CurrentMatchesTableBody").empty();
    var matches = response.matches;

    for(var i in matches) {
        var startedOn = new Date(matches[i].startedOnDate);
        var dateText = startedOn.getDate() + '/' + (startedOn.getMonth() + 1) + '/' + startedOn.getFullYear();
        var htmlText = '<tr><td>' + matches[i].user1 + "</td><td>" + matches[i].user2 + "</td><td>" + matches[i].map + "</td><td>" + dateText + '</td></tr>';
        $("#CurrentMatchesTableBody").append(htmlText);
    }

  }

  var recievedChallenges;

  function onChallengesResponse(response) {
    $("#ChallangessTableBody").empty();
    var challenges = response.challenges;
    recievedChallenges = {};

    for(var i in challenges) {
      recievedChallenges[challenges[i].id] = challenges[i];
      var buttonHtml = '<button id="acceptChallenge' + challenges[i].id + '" class="btn btn-success" data-loading-text="please wait..." onclick="acceptChallenge(' + challenges[i].id + ')">Accept challenge</button>';
      if(loggedUserId == challenges[i].userId)
          buttonHtml = '<button id="acceptChallenge' + challenges[i].id + '" class="btn btn-success" data-loading-text="please wait..." onclick="removeChallenge(' + challenges[i].id + ')">Remove challenge</button>';
      var htmlText = '<tr id="rowOfChallenge' + challenges[i].id + '"><td>' + challenges[i].username + "</td><td>" + challenges[i].mapName + "</td><td>" +
              constructDifficultyText(challenges[i]) + "</td><td>" + constructHoursText(challenges[i]) + '</td><td>' + buttonHtml + '</td></tr>';
      $("#ChallangessTableBody").append(htmlText);
    }
  }

  function removeChallenge(id) {
      var selector = "#acceptChallenge" + id;
      $(selector).button('loading');

      var inputObject = {
          "command": "removeChallenge",
          "challengeId": id
      };

      $.post('ChallengeServelet', JSON.stringify(inputObject), function(response) { onRemoveChallenge(response, id);}, 'json');
  }

  function onRemoveChallenge(response, challengeId) {
      if(response.success == false) {
          $("#modalContent").text(response.errorMsg);
          var selector = "#acceptChallenge" + challengeId;
          $(selector).button('reset');
      } else {
          $("#modalContent").text("Your challenge was removed.");
          var selector = "#rowOfChallenge" + challengeId;
          $(selector).hide();
      }

      $("#exampleModal").modal("show");
  }

  function acceptChallenge(id) {
      var selector = "#acceptChallenge" + id;
      $(selector).button('loading');

      var inputObject = {
          "command": "acceptChallenge",
          "challengeId": id
      };

      $.post('ChallengeServelet', JSON.stringify(inputObject), function(response) { onAcceptChallenge(response, id);}, 'json');
  }

  function onAcceptChallenge(response, challengeId) {
      if(response.success == false) {
          $("#modalContent").text(response.errorMsg);
          var selector = "#acceptChallenge" + challengeId;
          $(selector).button('reset');
      } else {
          $("#modalContent").text("Everything is good, for now, check our Discord server to see who your opponent is and play your game.");
          var selector = "#rowOfChallenge" + challengeId;
          $(selector).hide();
      }

      $("#exampleModal").modal("show");
  }

  function constructDifficultyText(challenge) {
    var difficulties = [{"parameter":"isNomralDifficulty", "text":"Normal"}, {"parameter":"isHardDifficulty", "text":"Hard"},
      {"parameter":"isHeroicDifficulty", "text":"Heroic"}, {"parameter":"isImpossibleDifficulty", "text":"Impossible"}];

    var text = "";
    var isFirst = true;

    for(var i in difficulties)
      if(challenge[difficulties[i].parameter])
        if(isFirst) {
          isFirst = false;
          text = difficulties[i].text;
        }
        else
          text = text + "<br/>" + difficulties[i].text;

    return text;
  }

  function constructHoursText(challenge) {
    var tempDate = new Date(0);
    tempDate.setUTCHours(challenge.startHour);
    var startHour = tempDate.getHours();
    tempDate.setUTCHours(challenge.endHour);
    var endHour = tempDate.getHours();

    var text = startHour + ":00<br/>-<br/>" + endHour + ":00";

    return text;
  }

  function toggleDiscordUsernameInput() {
      var switchedOn = $("#getDiscordName").is(':checked');
      var giveDetailsSwitchedOn = $("#giveContactsInput").is(':checked');

      if(switchedOn && giveDetailsSwitchedOn)
          $("#discordUserName").show();
      else
          $("#discordUserName").hide();
  }
</script>
</html>
