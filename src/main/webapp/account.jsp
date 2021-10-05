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
        <div id="authorized">
            <div>Добро пожаловать,<br/><b style="font-size: 23px">${CLIENT_NAME}</b></div>
        </div>

        <div class="btn-group">
            <button type="button" class="btn btn-secondary dropdown-toggle glyphicon glyphicon-menu-hamburger dropdownbutton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"></button>
            <div class="dropdown-menu dropdown-menu-right" style="border-radius: 10px; width: 220px; border: 4px solid red">
                <button class="dropdown-item menubutton" type="button" onclick="document.location.href='Tacos?cmd=account'">Управление аккаунтом</button>
                <button class="dropdown-item menubutton" type="button" onclick="document.location.href='Tacos?cmd=orderpage'">Корзина</button>
                <hr>
                <button class="dropdown-item menubutton" type="button" onclick="document.location.href='Tacos?cmd=unauth'">Выход</button>
            </div>
        </div>
    </div>
</div>
<div id="body" style="height: 705px;">
    <c:if test="${showmessage}">
        <p class="message">Для доступа к странице необходима авторизация</p>
    </c:if>

        <div id="authform" style="width: 90%; height: 500px">
            <div style="height:30px; width: 100%; text-align: left; font-size: 30px; font-family: Arial; font-weight: bold">
                ПРОФИЛЬ
            </div>
            <hr style="height: 12px; border: 0; box-shadow: inset 0 15px 12px -11px rgba(0,0,0,0.15);">
            <form action="Tacos" method="POST">
                <input type="hidden" name="cmd" value="updateProfile">
                <input type="hidden" name="curNickname" value="${CLIENT_NAME}">
                <input type="hidden" name="curEmail" value="${CLIENT_EMAIL}">
                <div class="form-group" style="margin-top: 15px; width: 49%; display: inline-block">
                    <label for="new_name">Ваше имя: ${CLIENT_NAME}</label>
                    <input type="text" style="width: 80%" class="form-control" id="new_name" name="newNickname" value="${CLIENT_NAME}" required>
                </div>
                <div class="form-group" style="margin-top: 15px; width: 49%; display: inline-block">
                    <label for="new_email">Ваш email: ${CLIENT_EMAIL}</label>
                    <input type="email" style="width: 80%" class="form-control" id="new_email" name="newEmail" value="${CLIENT_EMAIL}" required>
                </div>
                <div class="form-group" style="margin-top: 15px; width: 49%; display: inline-block">
                    <label for="new_password">Ваш новый пароль:</label>
                    <input type="password" style="width: 80%" class="form-control" id="new_password" name="newPassword" placeholder="Новый пароль" required>
                </div>
                <div class="form-group" style="margin-top: 15px; width: 49%; display: inline-block">
                    <label for="old_password">Старый пароль для подтверждения:</label>
                    <input type="password" style="width: 80%" class="form-control" id="old_password" name="oldPassword" placeholder="Старый пароль" required>
                </div>
                <button type="submit" class="button" style="margin-top: 15px; width: 40%">ОБНОВИТЬ ДАННЫЕ</button>
            </form>
            <button onclick="if (!(confirm('Вы уверены, что хотите удалить аккаунт?'))) return false; document.location.href='Tacos?cmd=delete'" class="button" style="margin-top: 15px; width: 40%">УДАЛИТЬ АККАУНТ</button>
            <c:if test="${param.mf1}">
                <div style="margin-top: 15px; font-family: Arial; color: red; font-weight: bold">
                    Имя или Email уже используются
                </div>
            </c:if>
            <c:if test="${param.mf2}">
                <div style="margin-top: 15px; font-family: Arial; color: red; font-weight: bold">
                    Введен неверный пароль
                </div>
            </c:if>
        </div>


</div>
<div id="footer">
    Часы работы:<br/>
    Будние дни: 10:00 - 22:00<br/>
    Выходные дни: 10:00 - 20:00
</div>
</body>
</html>
