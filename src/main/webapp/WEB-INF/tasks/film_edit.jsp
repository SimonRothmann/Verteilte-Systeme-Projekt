<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib tagdir="/WEB-INF/tags/templates" prefix="template"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<template:base>
    <jsp:attribute name="title">
        <c:choose>
            <c:when test="${edit}">
                Aufgabe bearbeiten
            </c:when>
            <c:otherwise>
                Aufgabe anlegen
            </c:otherwise>
        </c:choose>
    </jsp:attribute>

    <jsp:attribute name="head">
        <link rel="stylesheet" href="<c:url value="/css/film_edit.css"/>" />
    </jsp:attribute>

    <jsp:attribute name="menu">
        <div class="menuitem">
            <a href="<c:url value="/app/dashboard/"/>">Dashboard</a>
        </div>
        
        <div class="menuitem">
            <a href="<c:url value="/app/films/list/"/>">Liste</a>
        </div>
    </jsp:attribute>

    <jsp:attribute name="content">
        <form method="post" class="stacked">
            <div class="column">
                <%-- CSRF-Token --%>
                <input type="hidden" name="csrf_token" value="${csrf_token}">

                <%-- Eingabefelder --%>
                <label for="film_owner">Eigentümer:</label>
                <div class="side-by-side">
                    <input type="text" name="film_owner" value="${film_form.values["film_owner"][0]}" readonly="readonly">
                </div>

                <label for="film_genre">Genre:</label>
                <div class="side-by-side">
                    <select name="film_genre">
                        <option value="">Keine Kategorie</option>

                        <c:forEach items="${genres}" var="genre">
                            <option value="${genre.id}" ${film_form.values["film_genre"][0] == genre.id.toString() ? 'selected' : ''}>
                                <c:out value="${genre.name}" />
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <label for="film_due_date">
                    Fällig am:
                    <span class="required">*</span>
                </label>
                <div class="side-by-side">
                    <input type="text" name="film_due_date" value="${film_form.values["film_due_date"][0]}">
                    <input type="text" name="film_due_time" value="${film_form.values["film_due_time"][0]}">
                </div>

                <label for="film_status">
                    Status:
                    <span class="required">*</span>
                </label>
                <div class="side-by-side margin">
                    <select name="film_status">
                        <c:forEach items="${statuses}" var="status">
                            <option value="${status}" ${film_form.values["film_status"][0] == status ? 'selected' : ''}>
                                <c:out value="${status.label}"/>
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <label for="film_short_text">
                    Bezeichnung:
                    <span class="required">*</span>
                </label>
                <div class="side-by-side">
                    <input type="text" name="film_short_text" value="${film_form.values["film_short_text"][0]}">
                </div>

                <label for="film_long_text">
                    Beschreibung:
                </label>
                <div class="side-by-side">
                    <textarea name="film_long_text"><c:out value="${film_form.values['film_long_text'][0]}"/></textarea>
                </div>

                <%-- Button zum Abschicken --%>
                <div class="side-by-side">
                    <button class="icon-pencil" type="submit" name="action" value="save">
                        Sichern
                    </button>

                    <c:if test="${edit}">
                        <button class="icon-trash" type="submit" name="action" value="delete">
                            Löschen
                        </button>
                    </c:if>
                </div>
            </div>

            <%-- Fehlermeldungen --%>
            <c:if test="${!empty film_form.errors}">
                <ul class="errors">
                    <c:forEach items="${film_form.errors}" var="error">
                        <li>${error}</li>
                    </c:forEach>
                </ul>
            </c:if>
        </form>
    </jsp:attribute>
</template:base>