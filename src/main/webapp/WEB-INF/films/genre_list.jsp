<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib tagdir="/WEB-INF/tags/templates" prefix="template"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<template:base>
    <jsp:attribute name="title">
        Genres bearbeiten
    </jsp:attribute>

    <jsp:attribute name="head">
        <link rel="stylesheet" href="<c:url value="/css/category_list.css"/>" />
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
        <form method="post" class="stacked">
            <%-- CSRF-Token --%>
            <input type="hidden" name="csrf_token" value="${csrf_token}">

            <%-- Feld zum Anlegen einer neuen Kategorie --%>
            <div class="column margin">
                <label for="j_username">Neues Genre:</label>
                <input type="text" name="name" value="${genres_form.values["name"][0]}">

                <button type="submit" name="action" value="create" class="icon-pencil">
                    Erstellen
                </button>
            </div>

            <%-- Fehlermeldungen --%>
            <c:if test="${!empty genres_form.errors}">
                <ul class="errors margin">
                    <c:forEach items="${genres_form.errors}" var="error">
                        <li>${error}</li>
                        </c:forEach>
                </ul>
            </c:if>

            <%-- Vorhandene Kategorien --%>
            <c:choose>
                <c:when test="${empty genres}">   <%-- Änderung von genre auf genres --%>
                    <p>
                        Es wurde noch kein Genre erstellt. 🦄
                    </p>
                </c:when>
                <c:otherwise>
                    <div>
                        <div class="margin">
                            <c:forEach items="${genres}" var="genre">
                                <input type="checkbox" name="genre" id="${'genre-'.concat(genre.id)}" value="${genre.id}" />
                                <label for="${'genre-'.concat(genre.id)}">
                                    <c:out value="${genre.name}"/>
                                </label>
                                <br />
                            </c:forEach>
                        </div>

                        <button type="submit" name="action" value="delete" class="icon-trash">
                            Markierte löschen
                        </button>
                    </div>
                </c:otherwise>
            </c:choose>
        </form>
    </jsp:attribute>
</template:base>