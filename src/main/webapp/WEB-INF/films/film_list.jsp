<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib tagdir="/WEB-INF/tags/templates" prefix="template"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<template:base>
    <jsp:attribute name="title">
        Status-Liste
    </jsp:attribute>

    <jsp:attribute name="head">
        <link rel="stylesheet" href="<c:url value="/css/task_list.css"/>" />
    </jsp:attribute>

    <jsp:attribute name="menu">
        <div class="menuitem">
            <a href="<c:url value="/app/dashboard/"/>">Startseite</a>
        </div>

        <div class="menuitem">
            <a href="<c:url value="/app/films/list/"/>">Film-Liste</a>
        </div>

        <div class="menuitem">
            <a href="<c:url value="/app/films/film/new/"/>">Film anlegen</a>
        </div>

        <div class="menuitem">
            <a href="<c:url value="/app/films/genre/"/>">Genres bearbeiten</a>
        </div>
        <div class="menuitem">
            <a href="<c:url value="/app/changeData/"/>">Benutzerverwaltung</a>
        </div>
    </jsp:attribute>

    <jsp:attribute name="content">
        <%-- Suchfilter --%>
        <form method="GET" class="horizontal" id="search">
            <input type="text" name="search_text" value="${param.search_text}" placeholder="Beschreibung"/>

            <select name="search_genre">
                <option value="">Alle Genres</option>

                <c:forEach items="${genres}" var="genre">
                    <option value="${genre.id}" ${param.search_genre == genre.id ? 'selected' : ''}>
                        <c:out value="${genre.name}" />
                    </option>
                </c:forEach>
            </select>

            <select name="search_status">
                <option value="">Alle Stati</option>

                <c:forEach items="${statuses}" var="status">
                    <option value="${status}" ${param.search_status == status ? 'selected' : ''}>
                        <c:out value="${status.label}"/>
                    </option>
                </c:forEach>
            </select>

            <button class="icon-search" type="submit">
                Suchen
            </button>
        </form>

        <%-- Gefundene Aufgaben --%>
        <c:choose>
            <c:when test="${empty films}">
                <p>
                    Es wurden noch keine Stati erstellt. ❌
                </p>
            </c:when>
            <c:otherwise>
                <jsp:useBean id="utils" class="dhbwka.wwi.vertsys.javaee.filmsortierung.common.web.WebUtils"/>

                <table>
                    <thead>
                        <tr>
                            <th>Film-Titel</th>
                            <th>Genre</th>
                            <th>Spieldauer</th>
                            <th>Angelegt von</th>
                            <th>Status</th>
                            <th>Muss gesehen werden bis</th>
                        </tr>
                    </thead>
                    <c:forEach items="${films}" var="film">
                        <tr>
                            <td>
                                <a href="<c:url value="/app/films/film/${film.id}/"/>">
                                    <c:out value="${film.shortText}"/>
                                </a>
                            </td>
                            <td>
                                <c:out value="${film.genre.name}"/>
                            </td>
                            <td>
                                <c:out value="${film.runTime}"/>
                            </td>
                            <td>
                                <c:out value="${film.owner.username}"/>
                            </td>
                            <td>
                                <c:out value="${film.status.label}"/>
                            </td>
                            <td>
                                <c:out value="${utils.formatDate(film.dueDate)}"/>
                                <c:out value="${utils.formatTime(film.dueTime)}"/>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </c:otherwise>
        </c:choose>
    </jsp:attribute>
</template:base>