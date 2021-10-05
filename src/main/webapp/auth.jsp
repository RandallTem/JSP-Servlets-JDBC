<%--
  Created by IntelliJ IDEA.
  User: Randall
  Date: 02.10.2021
  Time: 19:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <title>Страница авторизации</title>
    <link type="text/css" rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>
<div id="header">
    <img src="img/tacos_logo.png" width="350px" style="position: absolute;">
    <nav class="nav-block">
        <div onclick="document.location.href='Tacos?cmd=home'">О НАС</div>
        <div onclick="document.location.href='Tacos?cmd=menu'">МЕНЮ</div>
    </nav>
    <div class="account">
        <div id="unauthorized" onclick="document.location.href='Tacos?cmd=reg'">
            РЕГИСТРАЦИЯ
        </div>
    </div>
</div>
<div id="body" style="height: 705px;">
    <c:if test="${showmessage}">
        <p class="message">Для доступа к странице необходима авторизация</p>
    </c:if>
    <form action="Tacos" method="POST">
        <input type="hidden" name="cmd" value="authorize">
        <input type="hidden" name="page" value="${wantedPage}">
        <div id="authform">
            <div style="height:30px; width: 100%; text-align: center; font-size: 30px; font-family: Arial; font-weight: bold">
                ВХОД
            </div>
            <hr style="height: 12px; border: 0; box-shadow: inset 0 15px 12px -11px rgba(0,0,0,0.15);">
            <div class="form-group" style="margin-top: 15px">
                <label for="email_input">Email адрес</label>
                <input type="email" class="form-control" id="email_input" name="email" placeholder="Email адрес" required>
            </div>
            <div class="form-group" style="margin-top: 30px">
                <label for="password_input">Пароль</label>
                <input type="password" class="form-control" id="password_input" name="password" placeholder="Пароль" required>
            </div>
            <div class="form-check" style="margin-top: 25px">
                <input type="checkbox" class="form-check-input" id="remember" name="rememberme">
                <label class="form-check-label" for="remember">Запомнить меня</label>
            </div>
            <button type="submit" class="button" style="margin-top: 25px">ВОЙТИ</button>
        </div>
    </form>
    <c:if test="${failedauth}">
        <p class="message" style="font-size: 20px; font-weight: normal">Неверный логин или пароль</p>
    </c:if>
</div>
<div id="footer">
    Часы работы:<br/>
    Будние дни: 10:00 - 22:00<br/>
    Выходные дни: 10:00 - 20:00
</div>
</body>
</html>
