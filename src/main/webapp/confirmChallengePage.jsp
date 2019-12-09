<%--
  Created by IntelliJ IDEA.
  User: Bulya
  Date: 07/11/2019
  Time: 09:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Confirm Gladiator arena challenge</title>
    <script src="https://code.jquery.com/jquery-1.10.2.js"
            type="text/javascript"></script>
    <link rel="stylesheet" href="bootstrap/css/bootstrap.min.css">
    <script src="bootstrap/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="style.css">
    <link rel="stylesheet" href="utilities.css">
    <link rel="stylesheet" href="Colors.css">
    <scipt src="js/utilities.js"></scipt>
</head>
<body>
<div class="container-fluid">
    <a href="" class="btn btn-success pull-right" style="float:right!important;visibility: hidden">Login</a>
    <div class="row row-offcanvas row-offcanvas-left">
        <div class="col-xs-12 col-sm-12" data-spy="scroll" data-taget="#sidebar-nav">
            <div class="row">
                <div class="col-xs-12 col-sm-6 col-lg-3">
                </div>
                <div class="col-xs-12 col-sm-6 col-lg-5">
                    <div id="acceptChallengeDiv" style="display: none">
                        <div class="panel-default">
                            <div class="panel-heading">
                                <h4>Do you want to accept the challenge match</h4>
                            </div>
                            <div style="text-align: center;margin-top:20px;">
                                <p id="challengeInfoPar"></p>
                                <div id="acceptOrDeclineDiv">
                                    <p class="btn btn-success" style="width:40%" onclick="acceptChallenge()">Accept</p>
                                    <p class="btn btn-success" style="width:40%" onclick="declineChallenge()">Decline</p>
                                </div>
                                <div id="mapPickDiv" style="display: none;text-align: center">
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
                                    <button id="submitMapPickBtn" style="margin:20px;" class="btn btn-primary" data-loading-text="please wait..." onclick="sendMapPick()">Submit map pick</button>
                                </div>
                                <div id="heroPickDiv" style="text-align: center">
                                    <h4>Now you get to pick your hero.</h4>
                                    <p id="mapToBePlayed"></p>
                                    <p>Pick your faction first, click on the faction you'd like to play with.</p>
                                    <div style="align-content: center; display: grid; grid-template-columns: auto auto auto auto;">
                                        <img src="images/factions/TOWN_ACADEMY.png" style="justify-self: center;margin:10px;cursor: pointer;" data-toggle="tooltip" data-placement="top" title="Academy" onclick="ShowHeroes('academy')"/>
                                        <img src="images/factions/TOWN_DUNGEON.png" style="justify-self: center;margin:10px;cursor: pointer;" data-toggle="tooltip" data-placement="top" title="Dungeon" onclick="ShowHeroes('dungeon')"/>
                                        <img src="images/factions/TOWN_FORTRESS.png" style="justify-self: center;margin:10px;cursor: pointer;" data-toggle="tooltip" data-placement="top" title="Fortress" onclick="ShowHeroes('fortress')"/>
                                        <img src="images/factions/TOWN_HEAVEN.png" style="justify-self: center;margin:10px;cursor: pointer;" data-toggle="tooltip" data-placement="top" title="Haven" onclick="ShowHeroes('haven')"/>
                                        <img src="images/factions/TOWN_INFERNO.png" style="justify-self: center;margin:10px;cursor: pointer;" data-toggle="tooltip" data-placement="top" title="Inferno" onclick="ShowHeroes('inferno')"/>
                                        <img src="images/factions/TOWN_NECROMANCY.png" style="justify-self: center;margin:10px;cursor: pointer;" data-toggle="tooltip" data-placement="top" title="Necromancy" onclick="ShowHeroes('necropolis')"/>
                                        <img src="images/factions/TOWN_PRESERVE.png" style="justify-self: center;margin:10px;cursor: pointer;" data-toggle="tooltip" data-placement="top" title="Sylvan" onclick="ShowHeroes('sylvan')"/>
                                        <img src="images/factions/TOWN_STRONGHOLD.png" style="justify-self: center;margin:10px;cursor: pointer;" data-toggle="tooltip" data-placement="top" title="Stronghold" onclick="ShowHeroes('stronghold')"/>
                                    </div>
                                    <div id="heroImagesDiv" style="margin-top:20px;display: none">
                                        <p>Pick your hero:</p>
                                        <div id="havenHeroesDiv" style="display: none">
                                            <img src="images/heroes/Duncan.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Duncan')" title="Duncan"/>
                                            <img src="images/heroes/Dougal.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Dougal')" title="Dougal"/>
                                            <img src="images/heroes/Klaus.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Klaus')" title="Klaus"/>
                                            <img src="images/heroes/Irina.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Irina')" title="Irina"/>
                                            <img src="images/heroes/Isabel.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Isabel')" title="Isabel"/>
                                            <img src="images/heroes/Laszlo.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Laszlo')" title="Laszlo"/>
                                            <img src="images/heroes/Andreas.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Andreas')" title="Andreas"/>
                                            <img src="images/heroes/Bertrand.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Bertrand')" title="Bertrand"/>
                                            <img src="images/heroes/Nicolai.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Nicolai')" title="Nicolai"/>
                                            <img src="images/heroes/Godric.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Godric')" title="Godric"/>
                                            <img src="images/heroes/Freyda.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Freyda')" title="Freyda"/>
                                            <img src="images/heroes/Rutger.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Rutger')" title="Rutger"/>
                                            <img src="images/heroes/Maeve.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Maeve')" title="Maeve"/>
                                            <img src="images/heroes/Ellaine.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Ellaine')" title="Ellaine"/>
                                            <img src="images/heroes/Jeddite.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Jeddite')" title="Jeddite"/>
                                            <img src="images/heroes/Alaric.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Alaric')" title="Alaric"/>
                                            <img src="images/heroes/Valeria.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Valeria')" title="Valeria"/>
                                            <img src="images/heroes/Gabrielle.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Gabrielle')" title="Gabrielle"/>
                                            <img src="images/heroes/Orlando.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Orlando')" title="Orlando"/>
                                            <img src="images/heroes/Markal.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Markal')" title="Markal"/>
                                        </div>
                                        <div id="sylvanHeroesDiv" style="display: none">
                                            <img src="images/heroes/Findan.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Findan')" title="Findan"/>
                                            <img src="images/heroes/Jenova.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Jenova')" title="Jenova"/>
                                            <img src="images/heroes/Wyngaal.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Wyngaal')" title="Wyngaal"/>
                                            <img src="images/heroes/Anwen.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Anwen')" title="Anwen"/>
                                            <img src="images/heroes/Talanar.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Talanar')" title="Talanar"/>
                                            <img src="images/heroes/Ossir.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Ossir')" title="Ossir"/>
                                            <img src="images/heroes/Ivor.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Ivor')" title="Ivor"/>
                                            <img src="images/heroes/Gilraen.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Gilraen')" title="Gilraen"/>
                                            <img src="images/heroes/Alaron.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Alaron')" title="Alaron"/>
                                            <img src="images/heroes/Kyrre.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Kyrre')" title="Kyrre"/>
                                            <img src="images/heroes/Melodia.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Melodia')" title="Melodia"/>
                                            <img src="images/heroes/Mephala.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Mephala')" title="Mephala"/>
                                            <img src="images/heroes/Dirael.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Dirael')" title="Dirael"/>
                                            <img src="images/heroes/Vinrael.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Vinrael')" title="Vinrael"/>
                                            <img src="images/heroes/Gem.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Gem')" title="Gem"/>
                                            <img src="images/heroes/Ylthin.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Ylthin')" title="Ylthin"/>
                                            <img src="images/heroes/Tieru.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Tieru')" title="Tieru"/>
                                            <img src="images/heroes/Elleshar.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Elleshar')" title="Elleshar"/>
                                        </div>
                                        <div id="academyHeroesDiv" style="display: none">
                                            <img src="images/heroes/Davius.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Davius')" title="Davius"/>
                                            <img src="images/heroes/Havez.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Havez')" title="Havez"/>
                                            <img src="images/heroes/Razzak.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Razzak')" title="Razzak"/>
                                            <img src="images/heroes/Josephine.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Josephine')" title="Josephine"/>
                                            <img src="images/heroes/Minasli.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Minasli')" title="Minasli"/>
                                            <img src="images/heroes/Rissa.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Rissa')" title="Rissa"/>
                                            <img src="images/heroes/Cyrus.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Cyrus')" title="Cyrus"/>
                                            <img src="images/heroes/Faiz.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Faiz')" title="Faiz"/>
                                            <img src="images/heroes/Gurvilin.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Gurvilin')" title="Gurvilin"/>
                                            <img src="images/heroes/Maahir.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Maahir')" title="Maahir"/>
                                            <img src="images/heroes/Narxes.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Narxes')" title="Narxes"/>
                                            <img src="images/heroes/Jhora.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Jhora')" title="Jhora"/>
                                            <img src="images/heroes/Nur.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Nur')" title="Nur"/>
                                            <img src="images/heroes/Emilia.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Emilia')" title="Emilia"/>
                                            <img src="images/heroes/Nathir.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Nathir')" title="Nathir"/>
                                            <img src="images/heroes/Galib.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Galib')" title="Galib"/>
                                            <img src="images/heroes/Theodorus.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Theodorus')" title="Theodorus"/>
                                            <img src="images/heroes/Zehir.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Zehir')" title="Zehir"/>
                                        </div>
                                        <div id="dungeonHeroesDiv" style="display: none">
                                            <img src="images/heroes/Agbeth.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Agbeth')" title="Agbeth"/>
                                            <img src="images/heroes/Sorgal.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Sorgal')" title="Sorgal"/>
                                            <img src="images/heroes/Kythra.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Kythra')" title="Kythra"/>
                                            <img src="images/heroes/Ranleth.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Ranleth')" title="Ranleth"/>
                                            <img src="images/heroes/Sylsai.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Sylsai')" title="Sylsai"/>
                                            <img src="images/heroes/Yrwanna.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Yrwanna')" title="Yrwanna"/>
                                            <img src="images/heroes/Yrbeth.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Yrbeth')" title="Yrbeth"/>
                                            <img src="images/heroes/Lethos.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Lethos')" title="Lethos"/>
                                            <img src="images/heroes/Eruina.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Eruina')" title="Eruina"/>
                                            <img src="images/heroes/Vayshan.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Vayshan')" title="Vayshan"/>
                                            <img src="images/heroes/Thralsai.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Thralsai')" title="Thralsai"/>
                                            <img src="images/heroes/Welygg.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Welygg')" title="Welygg"/>
                                            <img src="images/heroes/Sinitar.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Sinitar')" title="Sinitar"/>
                                            <img src="images/heroes/Kastore.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Kastore')" title="Kastore"/>
                                            <img src="images/heroes/Shadya.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Shadya')" title="Shadya"/>
                                            <img src="images/heroes/Raelag.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Raelag')" title="Raelag"/>
                                            <img src="images/heroes/Sephinroth.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Sephinroth')" title="Sephinroth"/>
                                            <img src="images/heroes/Ylaya.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Ylaya')" title="Ylaya"/>
                                        </div>
                                        <div id="necropolisHeroesDiv" style="display: none">
                                            <img src="images/heroes/Kaspar.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Kaspar')" title="Kaspar"/>
                                            <img src="images/heroes/Ornella.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Ornella')" title="Ornella"/>
                                            <img src="images/heroes/Vladimir.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Vladimir')" title="Vladimir"/>
                                            <img src="images/heroes/Orson.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Orson')" title="Orson"/>
                                            <img src="images/heroes/Lucretia.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Lucretia')" title="Lucretia"/>
                                            <img src="images/heroes/Xerxon.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Xerxon')" title="Xerxon"/>
                                            <img src="images/heroes/Aislinn.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Aislinn')" title="Aislinn"/>
                                            <img src="images/heroes/Archilus.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Archilus')" title="Archilus"/>
                                            <img src="images/heroes/Giovanni.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Giovanni')" title="Giovanni"/>
                                            <img src="images/heroes/Naadir.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Naadir')" title="Naadir"/>
                                            <img src="images/heroes/Deirdre.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Deirdre')" title="Deirdre"/>
                                            <img src="images/heroes/Nimbus.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Nimbus')" title="Nimbus"/>
                                            <img src="images/heroes/Arantir.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Arantir')" title="Arantir"/>
                                            <img src="images/heroes/Zoltan.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Zoltan')" title="Zoltan"/>
                                            <img src="images/heroes/Raven.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Raven')" title="Raven"/>
                                            <img src="images/heroes/Sandro.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Sandro')" title="Sandro"/>
                                            <img src="images/heroes/Thant.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Thant')" title="Thant"/>
                                            <img src="images/heroes/Vidomina.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Vidomina')" title="Vidomina"/>
                                        </div>
                                        <div id="infernoHeroesDiv" style="display: none">
                                            <img src="images/heroes/Calh.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Calh')" title="Calh"/>
                                            <img src="images/heroes/Grawl.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Grawl')" title="Grawl"/>
                                            <img src="images/heroes/Harkenraz.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Harkenraz')" title="Harkenraz"/>
                                            <img src="images/heroes/Nebiros.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Nebiros')" title="Nebiros"/>
                                            <img src="images/heroes/Marbas.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Marbas')" title="Marbas"/>
                                            <img src="images/heroes/Sheltem.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Sheltem')" title="Sheltem"/>
                                            <img src="images/heroes/Ash.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Ash')" title="Ash"/>
                                            <img src="images/heroes/Alastor.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Alastor')" title="Alastor"/>
                                            <img src="images/heroes/Grok.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Grok')" title="Grok"/>
                                            <img src="images/heroes/Malustar.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Malustar')" title="Malustar"/>
                                            <img src="images/heroes/Nymus.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Nymus')" title="Nymus"/>
                                            <img src="images/heroes/Jezebeth.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Jezebeth')" title="Jezebeth"/>
                                            <img src="images/heroes/Agrael.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Agrael')" title="Agrael"/>
                                            <img src="images/heroes/Biara.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Biara')" title="Biara"/>
                                            <img src="images/heroes/Calid.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Calid')" title="Calid"/>
                                            <img src="images/heroes/Deleb.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Deleb')" title="Deleb"/>
                                            <img src="images/heroes/Kha-Beleth.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Kha-Beleth')" title="Kha-Beleth"/>
                                            <img src="images/heroes/Zydar.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Zydar')" title="Zydar"/>
                                        </div>
                                        <div id="fortressHeroesDiv" style="display: none">
                                            <img src="images/heroes/Ingvar.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Ingvar')" title="Ingvar"/>
                                            <img src="images/heroes/Maximus.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Maximus')" title="Maximus"/>
                                            <img src="images/heroes/Rolf.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Rolf')" title="Rolf"/>
                                            <img src="images/heroes/Karli.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Karli')" title="Karli"/>
                                            <img src="images/heroes/Tazar.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Tazar')" title="Tazar"/>
                                            <img src="images/heroes/Wulfstan.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Wulfstan')" title="Wulfstan"/>
                                            <img src="images/heroes/Ebba.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Ebba')" title="Ebba"/>
                                            <img src="images/heroes/Uland.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Uland')" title="Uland"/>
                                            <img src="images/heroes/King Tolghar.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('King Tolghar')" title="King Tolghar"/>
                                            <img src="images/heroes/Helmar.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Helmar')" title="Helmar"/>
                                            <img src="images/heroes/Ufretin.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Ufretin')" title="Ufretin"/>
                                            <img src="images/heroes/Vilma.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Vilma')" title="Vilma"/>
                                            <img src="images/heroes/Bart.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Bart')" title="Bart"/>
                                            <img src="images/heroes/Brand.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Brand')" title="Brand"/>
                                            <img src="images/heroes/Erling.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Erling')" title="Erling"/>
                                            <img src="images/heroes/Hangvul.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Hangvul')" title="Hangvul"/>
                                            <img src="images/heroes/Inga.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Inga')" title="Inga"/>
                                            <img src="images/heroes/Svea.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Svea')" title="Svea"/>
                                        </div>
                                        <div id="strongholdHeroesDiv" style="display: none">
                                            <img src="images/heroes/Kragh.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Kragh')" title="Kragh"/>
                                            <img src="images/heroes/Gorshak.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Gorshak')" title="Gorshak"/>
                                            <img src="images/heroes/Telsek.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Telsek')" title="Telsek"/>
                                            <img src="images/heroes/Kilghan.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Kilghan')" title="Kilghan"/>
                                            <img src="images/heroes/Kraal.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Kraal')" title="Kraal"/>
                                            <img src="images/heroes/Matewa.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Matewa')" title="Matewa"/>
                                            <img src="images/heroes/Gotai.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Gotai')" title="Gotai"/>
                                            <img src="images/heroes/Quroq.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Quroq')" title="Quroq"/>
                                            <img src="images/heroes/Kunyak.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Kunyak')" title="Kunyak"/>
                                            <img src="images/heroes/Azar.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Azar')" title="Azar"/>
                                            <img src="images/heroes/Crag Hack.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Crag Hack')" title="Crag Hack"/>
                                            <img src="images/heroes/Shak'Karukat.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Shak\'Karukat')" title="Shak'Karukat"/>
                                            <img src="images/heroes/Haggash.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Haggash')" title="Haggash"/>
                                            <img src="images/heroes/Kujin.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Kujin')" title="Kujin"/>
                                            <img src="images/heroes/Mukha.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Mukha')" title="Mukha"/>
                                            <img src="images/heroes/Shiva.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Shiva')" title="Shiva"/>
                                            <img src="images/heroes/Erika.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Erika')" title="Erika"/>
                                            <img src="images/heroes/Urgath.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Urgath')" title="Urgath"/>
                                            <img src="images/heroes/Garuna.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Garuna')" title="Garuna"/>
                                            <img src="images/heroes/Zouleika.png" style="margin:10px; cursor: pointer" width="64px" + height="64px" data-toggle="tooltip" data-placement="top" onclick="pickHero('Zouleika')" title="Zouleika"/>
                                        </div>
                                        <div id="heroConfirmDiv" style="display: none">
                                            <p id="heroPickPar"></p>
                                            <button id="submitHeroPickBtn" style="margin:20px;" class="btn btn-primary" data-loading-text="please wait...">Submit hero pick</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="text-center" id="errorMessage"></div>
                        </div>
                    </div>
                    <div id="confirmUserDiv" style="display:none;">
                        <form style="margin-top:30px" method="post">
                            <div class="form-group">
                                <label>Please enter your username</label>
                                <input type="username" class="form-control" id="inputUsername" placeholder="Username">
                                <label>Please enter password so that we confirm its you</label>
                                <input type="password" class="form-control" id="inputPassword" placeholder="Password">
                            </div>
                            <div class="text-center">
                                <button class="btn btn-primary" method="submit" type="button" onclick="loginWithIdenAndPass()">Submit</button>
                            </div>
                            <div class="text-center" id="confrimUsererrorMessage"></div>
                        </form>
                    </div>
                    <div id="errorDiv" style="display:none;">
                        <div class="panel-default">
                            <div class="panel-heading">
                                <h4>Error</h4>
                            </div>
                        </div>
                        <div>
                            <p id="errorParagraph"></p>
                        </div>
                    </div>
                </div>
                <div class="col-xs-12 col-sm-6 col-lg-4">
                </div>
            </div>
        </div>
    </div>
</div>
</body>
<script>
    var identificator = "<%=request.getParameter("iden")%>";
    var challenge;
    $(document).ready(function() {
        var inputObject = { "command": "getChallengeAndIsLoggedByIden",
            "identificator": identificator};
        $.post('GladiatorServlet', JSON.stringify(inputObject), onGetChallengetResponse, 'json');
        $('[data-toggle="tooltip"]').tooltip();
    });

    function loginWithIdenAndPass() {
        var password = $("#inputPassword").val();
        var username = $("#inputUsername").val();

        if(username == null || username == "") {
            $("#confrimUsererrorMessage").text("Please enter username");
            return false;
        }

        if(password == null || password == "") {
            $("#confrimUsererrorMessage").text("Please enter password");
            return false;
        }

        var inputObject = { "command": "loginByIdenAndPassword",
            "username": username,
            "identificator": identificator,
            "password": password}

        $.post('GladiatorServlet', JSON.stringify(inputObject), onGetChallengetResponse, 'json');
    }

    function onGetChallengetResponse(response) {
        if(response.success == false) {
            $("#errorDiv").show();
            $("#errorParagraph").text(response.errorMsg);
            return;
        }
        challenge = response;

        if(response.isLoggedIn == false) {
            $("#confirmUserDiv").show();
            return;
        }

        $("#errorDiv").hide();
        $("#confirmUserDiv").hide();
        $("#acceptChallengeDiv").show();

        if(response.mapToBePlayed == null) {
            $("#acceptOrDeclineDiv").show();
            $("#challengeInfoPar").text(response.challengingUsername + " challenges you in the Gladiator Arena tournament.");
            $("#heroPickDiv").hide();
        } else {
            $("#acceptOrDeclineDiv").hide();
            $("#mapPickDiv").hide();
            $("#heroPickDiv").show();
        }
    }

    function sendMapPick() {
        if(challenge.RankPositionOfChallengingUser == 1) {
            var priority1 = $("#firstPriority").val();
            var priority2 = $("#secondPriority").val();
            if(priority1 == priority2) {
                $("#modalContent").text("The two map vetoes must differ!");
                $("#exampleModal").modal("show");
                return;
            }
            $("#submitMapPickBtn").unbind('click');
            $("#errorMessage").empty();
            var output = {"command":"acceptChallenge", "challengeId": challenge.challengeId, "mapPriorities":[priority1, priority2]};
            $.post('GladiatorServlet', JSON.stringify(output), onAcceptChallengeResponse, 'json');
        } else {
            var mapVeto = $("#mapVeto").val();
            $("#submitMapPickBtn").unbind('click');
            $("#errorMessage").empty();
            var output = {"command":"acceptChallenge", "challengeId": challenge.challengeId, "mapVeto":mapVeto};
            $.post('GladiatorServlet', JSON.stringify(output), onAcceptChallengeResponse, 'json');
        }
    }

    function acceptChallenge() {
        $("#mapPickDiv").show();
        $("#acceptOrDeclineDiv").hide();
        if(challenge.RankPositionOfChallengingUser == 1)
            $("#mapPrioritiesDiv").show();
        else
            $("#mapVetoDiv").show();
    }

    function declineChallenge() {
        var inputObject = { "command": "declineChallenge",
            "identificator": identificator};
        $.post('GladiatorServlet', JSON.stringify(inputObject), onDeclineChallengeResponse, 'json');
    }

    function onDeclineChallengeResponse(response) {
        if(response.success) {
            $("#errorMessage").text(response.successMsg);
        }
        else {
            $("#errorMessage").text(response.errorMsg);
        }
    }

    function onAcceptChallengeResponse(response) {
        if(response.success) {
            $("#mapPickDiv").hide();
            $("#heroPickDiv").show();
            $("#mapToBePlayed").text("The map you will play this match is " + response.mapToBePlayed);
        }
        else
            $("#errorMessage").text(response.errorMsg);
    }

    function pickHero(heroName) {
        $("#heroConfirmDiv").show();
        $("#heroPickPar").text("You picked " + heroName + " as your hero for the challenge");
        $("#submitHeroPickBtn").click(function () { submitHeroPick(heroName);});
    }

    function submitHeroPick(heroName) {
        $("#submitHeroPickBtn").unbind('click');
        $.post('GladiatorServlet', JSON.stringify({"command":"pickHeroForChallenge", "challengeId":challenge.challengeId, "heroName": heroName}), onHeroPickResponse, 'json');
    }

    function onHeroPickResponse(repsonse) {
        $("#errorMessage").text(repsonse.errorMsg);
    }

    var factions = ['haven', 'sylvan', 'academy', 'dungeon', 'necropolis', 'inferno', 'fortress', 'stronghold'];

    function ShowHeroes(faction) {
        $("#heroImagesDiv").show();
        for(var i in factions) {
            var selector = '#' + factions[i] + "HeroesDiv";
            if(faction == factions[i])
                $(selector).show();
            else
                $(selector).hide();
        }
    }

</script>
</html>
