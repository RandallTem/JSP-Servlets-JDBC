<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<html>
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
            <div  style="border-bottom: 5px solid red;">О НАС</div>
            <div onclick="document.location.href='Tacos?cmd=menu'">МЕНЮ</div>

        </nav>
        <div class="account">
            <c:if test="${IS_AUTH == 'true'}">
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
            </c:if>
            <c:if test="${IS_AUTH == 'false'}">
                <div id="unauthorized" onclick="document.location.href='Tacos?cmd=auth'">
                    ВХОД
                </div>
            </c:if>
        </div>
    </div>
    <div id="body">
        <div id="intro">
            <div id="introtext">
                <div id="intro1">ТАКОС</div>
                <div id="intro2">Традиционная мексиканская кухня</div>
                <div id="intro3">
                    <p style="margin-top: 20px">
                        Мексиканские блюда берут свое начало в искусстве сочетания
                        свежих ингредиентов и старинных кулинарных техник.
                        Мы отдаем дань уважения, закупая лучшие и свежие ингредиенты
                        из Мексики и используя методы, разработанные ее коренным
                        доиспанским народом.
                    </p>
                </div>
            </div>
            <img id="intropic" src="img/tacos_home.jpg">
        </div>
        <div class="middleintro">
            Приготовлено по-домашнему
            <div style="width: 200px; border-bottom: 5px solid red; margin-right: auto; margin-left: auto"></div>
            <p>
                Мы предлагаем вам домашние, «сделанные
                с нуля» блюда, приготовленные на медленном огне и
                сохраняющие свое мексиканское культурное кулинарное
                наследие. Наши блюда обычно готовятся порядка 20 минут,
                так как они готовятся индивидуально по заказу.
            </p>
        </div>

        <div>
            <div id="carousel" class="carousel slide" data-ride="carousel">
                <!-- Indicators -->
                <ol class="carousel-indicators">
                    <li data-target="#carousel" data-slide-to="0" class="active"></li>
                    <li data-target="#carousel" data-slide-to="1"></li>
                    <li data-target="#carousel" data-slide-to="2"></li>
                </ol>

                <div class="carousel-inner">
                    <div class="item active">
                        <img src="img/taco_carousel_1.jpg" style="width:100%;">
                    </div>

                    <div class="item">
                        <img src="img/taco_carousel_2.png" style="width:100%;">
                    </div>

                    <div class="item">
                        <img src="img/taco_carousel_3.jpg" style="width:100%;">
                    </div>
                </div>

                <a class="left carousel-control" href="#carousel" data-slide="prev">
                    <span class="glyphicon glyphicon-chevron-left"></span>
                    <span class="sr-only">Previous</span>
                </a>
                <a class="right carousel-control" href="#carousel" data-slide="next">
                    <span class="glyphicon glyphicon-chevron-right"></span>
                    <span class="sr-only">Next</span>
                </a>
            </div>
        </div>
        <div class="middleintro">
            Оригинальный вкус
            <div style="width: 200px; border-bottom: 5px solid red; margin-right: auto; margin-left: auto"></div>
            <p>
                В нашем ресторане подают вкусные мексиканские блюда, приготовленные
                только из натуральных ингридиентов с использование традиционных
                методов приготовления и старинных кулинарных техник.
            </p>
        </div>
    </div>
    <div id="footer">
        Часы работы:<br/>
        Будние дни: 10:00 - 22:00<br/>
        Выходные дни: 10:00 - 20:00
    </div>
</body>
</html>
