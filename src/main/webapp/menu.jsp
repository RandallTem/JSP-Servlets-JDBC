<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<html lang="ru">
<head>
    <title>Tacos</title>
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
        <div  style="border-bottom: 5px solid red;">МЕНЮ</div>
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
<div id="body" style="height: auto;">
    <form action="Tacos" method="POST">
        <input type="hidden" name="cmd" value="addToOrder">
    <div id="authform" style="width: 95%; height: auto; vertical-align: top">
        <div style="height:30px; width: 100%; text-align: left; font-size: 30px; font-family: Arial; font-weight: bold; margin-bottom: 40px; color: #013b7a;">
            СОБЕРИ СВОЙ ТАКО
        </div>
        <hr>
        <div class="orderposition">
            <img src="img/tortilla.jpg" style="width: 100%">
            <div class="desc">
                <p>Лепешка</p>
                <span><input type="radio" name="tortilla" value="wheat" checked>Пшеничная: 30р</span><br/><br/>
                <span><input type="radio" name="tortilla" value="corn">Кукурузная: 30р</span>
            </div>
        </div>
        <div class="orderposition">
            <img src="img/meat.jpg" style="width: 100%">
            <div class="desc">
                <p>Мясо</p>
                <span><input type="radio" name="meat" value="beef" checked>Говядина: 50р</span><br/>
                <span><input type="radio" name="meat" value="chicken">Курица: 40р</span><br/>
                <span><input type="radio" name="meat" value="no">Без мяса: 0р</span>
            </div>
        </div>
        <div class="orderposition">
            <img src="img/cucumber.jpg" style="width: 100%">
            <div class="desc">
                <p>Огурец</p>
                <br/>
                <span><input type="checkbox" name="cucumber" checked>Добавить огурец: 20р</span>
            </div>
        </div>
        <div class="orderposition">
            <img src="img/tomato.jpg" style="width: 100%">
            <div class="desc">
                <p>Помидор</p>
                <br/>
                <span><input type="checkbox" name="tomato" checked>Добавить помидор: 25р</span>
            </div>
        </div>
        <div class="orderposition">
            <img src="img/salad.jpg" style="width: 100%">
            <div class="desc">
                <p>Салат</p>
                <br/>
                <span><input type="checkbox" name="salad" checked>Добавить салат: 25р</span>
            </div>
        </div>
        <div class="orderposition">
            <img src="img/onion.jpg" style="width: 100%">
            <div class="desc">
                <p>Лук</p>
                <br/>
                <span><input type="checkbox" name="onion" checked>Добавить лук: 15р</span>
            </div>
        </div>
        <div class="orderposition">
            <img src="img/pepper.jpg" style="width: 100%">
            <div class="desc">
                <p>Перец</p>
                <br/>
                <span><input type="checkbox" name="pepper" checked>Добавить перец: 20р</span>
            </div>
        </div>
        <div class="orderposition">
            <img src="img/beans.jpg" style="width: 100%">
            <div class="desc">
                <p>Фасоль</p>
                <br/>
                <span><input type="checkbox" name="beans" checked>Добавить фасоль: 20р</span>
            </div>
        </div>
        <div class="orderposition">
            <img src="img/parsley.jpg" style="width: 100%">
            <div class="desc">
                <p>Петрушка</p>
                <br/>
                <span><input type="checkbox" name="parsley" checked>Добавить петрушку: 10р</span>
            </div>
        </div>
        <div class="orderposition">
            <img src="img/spices.jpg" style="width: 100%">
            <div class="desc">
                <p>Специи</p>
                <br/>
                <span><input type="checkbox" name="parsley" checked>Добавить специи: 15р</span>
            </div>
        </div>
        <hr>
        <button type="submit" class="button" style="margin-top: 10px; margin-bottom: 20px; width: 400px; height: 50px">ДОБАВИТЬ В КОРЗИНУ</button>
        <button type="button" class="button" style="margin-top: 10px; margin-bottom: 20px; width: 400px; height: 50px; float:right"
            onclick="document.location.href='Tacos?cmd=orderpage'">ПЕРЕЙТИ В КОРЗИНУ</button>
        <c:if test="${param.status == 'ok'}">
            <p style="color: red; font-family: Arial; font-size: 20px; font-weight: bold">Тако добавлен в корзину</p>
        </c:if>
    </div>
    </form>
</div>
<div id="footer">
    Часы работы:<br/>
    Будние дни: 10:00 - 22:00<br/>
    Выходные дни: 10:00 - 20:00
</div>
</body>
</html>
