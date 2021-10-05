<%--
  Created by IntelliJ IDEA.
  User: Randall
  Date: 03.10.2021
  Time: 10:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <title>Страница регистрации</title>
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
        <div id="unauthorized" onclick="document.location.href='Tacos?cmd=auth'">
            ВХОД
        </div>
    </div>
</div>
<div id="body" style="height: 705px;">
    <form action="Tacos" method="POST">
        <input type="hidden" name="cmd" value="register">
        <div id="regform" style="height: 430px">
            <div style="height:30px; width: 100%; text-align: center; font-size: 30px; font-family: Arial; font-weight: bold">
                РЕГИСТРАЦИЯ
            </div>
            <hr style="height: 12px; border: 0; box-shadow: inset 0 15px 12px -11px rgba(0,0,0,0.15);">
            <div class="form-group" style="margin-top: 15px">
                <label for="email_input">Имя</label>
                <input type="text" class="form-control" id="nickname_input" name="nickname" placeholder="Имя" required>
            </div>
            <div class="form-group" style="margin-top: 15px">
                <label for="email_input">Email адрес</label>
                <input type="email" class="form-control" id="email_input" name="email" placeholder="Email адрес" required>
            </div>
            <div class="form-group" style="margin-top: 15px">
                <label for="password_input">Пароль</label>
                <input type="password" class="form-control" id="password_input" name="password" placeholder="Пароль" required>
            </div>
            <button type="submit" class="button" style="margin-top: 25px">ЗАРЕГИСТРИРОВАТЬСЯ</button>
        </div>
    </form>
    <c:if test="${failedreg}">
        <p class="message" style="font-size: 20px; font-weight: normal">Пользователь с такими данными уже зарегистрирован</p>
    </c:if>
</div>
<div id="footer">
    Часы работы:<br/>
    Будние дни: 10:00 - 22:00<br/>
    Выходные дни: 10:00 - 20:00
</div>
</body>
</html>
