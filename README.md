Anwendung Disney Filmsortierung
=========================

Kurzbeschreibung
----------------

Dies ist eine Anwendung anhand der Beispielanwendung von Herrn Schulmeister-Zimolong, 
mit welcher man Filme anlegen und sortieren kann.
Dabei wurde sich vom Thema her auf Disney Filme spezialisiert. 

Zusätzlich zu Hauptanwendung befinden sich noch HTML/Javascript Seiten in den Dateien,
mit welchen man die REST-Schnittstelle testen kann. 

Verwendete Technologien
-----------------------

Die App nutzt Maven als Build-Werkzeug und zur Paketverwaltung. Auf diese Weise
werden die für Jakarta EE notwendigen APIs, darüber hinaus aber keine weiteren
Abhängigkeiten, in das Projekt eingebunden. Der Quellcode der Anwendung ist dabei
wie folgt strukturiert:

 * **Servlets** dienen als Controller-Schicht und empfangen sämtliche HTTP-Anfragen.
 * **Enterprise Java Beans** dienen als Model-Schicht und kapseln die fachliche Anwendungslogik.
 * **Persistence Entities** modellieren das Datenmodell und werden für sämtliche Datenbankzugriffe genutzt.
 * **Java Server Pages** sowie verschiedene statische Dateien bilden die View und generieren den
   auf dem Bildschirm angezeigten HTML-Code.

Folgende Entwicklungswerkzeuge kommen dabei zum Einsatz:

 * [NetBeans:](https://netbeans.apache.org/) Integrierte Entwicklungsumgebung für Java und andere Sprachen
 * [Maven:](https://maven.apache.org/) Build-Werkzeug und Verwaltung von Abhängigkeiten
 * [Git:](https://git-scm.com/") Versionsverwaltung zur gemeinsamen Arbeit am Quellcode
 * [TomEE:](https://tomee.apache.org/) Applikationsserver zum lokalen Testen der Anwendung
 * [Derby:](https://db.apache.org/derby/) In Java implementierte SQL-Datenbank zum Testen der Anwendung

Screenshots
-----------
Diese sind in der Testdokumentation zu finden. 

Copyright
---------

© 2018 – 2019 Viktoria Ibach, Julia Becker, Simon Rothmann <br/>

