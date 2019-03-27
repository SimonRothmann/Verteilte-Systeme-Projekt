<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib tagdir="/WEB-INF/tags/templates" prefix="template"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<template:base>
    <jsp:attribute name="title">
        Eine Anmeldung ist leider nicht möglich 😔
    </jsp:attribute>

    <jsp:attribute name="head">
        <link rel="stylesheet" href="<c:url value="/css/login.css"/>" />
    </jsp:attribute>

    <jsp:attribute name="menu">
        <div class="menuitem">
            <a href="<c:url value="/logout/"/>">Versuchs' nochmal!</a>
        </div>
    </jsp:attribute>

    <jsp:attribute name="content">
        <p>
            Da ist leider etwas schief gelaufen 👩‍🔧👨‍🔧
        </p>
    </jsp:attribute>
</template:base>