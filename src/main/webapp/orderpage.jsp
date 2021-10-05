<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<html lang="ru">
<head>
    <title>Корзина</title>
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
<div id="body" style="height: auto;">
    <div id="authform" style="width: 95%; height: auto; vertical-align: top">
        <div style="height:30px; width: 100%; text-align: left; font-size: 30px; font-family: Arial; font-weight: bold; margin-bottom: 40px; color: #013b7a;">
            КОРЗИНА
        </div>
        <hr>
        <div class="orders" style="vertical-align: top">
            <c:choose>
                <c:when test="${ORDER == null}">
                    <div style="min-height: 500px">
                        <p style="margin-bottom: 40px">Вы пока еще ничего не заказали</p>
                        <button type="button" class="button" style="width: 400px; height: 50px;"
                                onclick="document.location.href='Tacos?cmd=menu'">ЗАКАЗАТЬ</button>
                    </div>
                </c:when>
                <c:otherwise>
                    <c:set var="totalprice" value="0"/>
                    <div style="min-height: 500px">
                        <p style="margin-bottom: 40px">Ваш заказ:</p>
                        <c:forEach var="tempOrder" items="${ORDER}">
                            <c:set var="price" value="0"/>
                            <div class="order" style="vertical-align: top">
                                <p style="display:inline-block;">ТАКО:</p>
                                <span class="glyphicon glyphicon-remove" style="float:right; font-size: 20px; cursor: pointer"
                                    onclick="document.location.href='Tacos?cmd=deleteOrder&id=${tempOrder.id}'"></span>
                                <hr>
                                <ul>
                                    <c:if test="${not tempOrder.tortilla}">
                                        <li>Пшеничная лепешка</li>
                                        <c:set var="price" value="${price + 30}"/>
                                    </c:if>
                                    <c:if test="${tempOrder.tortilla}">
                                        <li>Кукурузная лепешка</li>
                                        <c:set var="price" value="${price + 30}"/>
                                    </c:if>
                                    <c:if test="${tempOrder.meat == 0}"><li>Без мяса</li></c:if>
                                    <c:if test="${tempOrder.meat == 1}">
                                        <li>Говядина</li>
                                        <c:set var="price" value="${price + 50}"/>
                                    </c:if>
                                    <c:if test="${tempOrder.meat == 2}">
                                        <li>Курица</li>
                                        <c:set var="price" value="${price + 40}"/>
                                    </c:if>
                                    <c:if test="${tempOrder.cucumber}">
                                        <li>Огурец</li>
                                        <c:set var="price" value="${price + 20}"/>
                                    </c:if>
                                    <c:if test="${tempOrder.tomato}">
                                        <li>Помидор</li>
                                        <c:set var="price" value="${price + 25}"/>
                                    </c:if>
                                    <c:if test="${tempOrder.salad}">
                                        <li>Салат</li>
                                        <c:set var="price" value="${price + 25}"/>
                                    </c:if>
                                    <c:if test="${tempOrder.onion}">
                                        <li>Лук</li>
                                        <c:set var="price" value="${price + 15}"/>
                                    </c:if>
                                    <c:if test="${tempOrder.pepper}">
                                        <li>Перец</li>
                                        <c:set var="price" value="${price + 20}"/>
                                    </c:if>
                                    <c:if test="${tempOrder.beans}">
                                        <li>Фасоль</li>
                                        <c:set var="price" value="${price + 20}"/>
                                    </c:if>
                                    <c:if test="${tempOrder.parsley}">
                                        <li>Петрушка</li>
                                        <c:set var="price" value="${price + 10}"/>
                                    </c:if>
                                    <c:if test="${tempOrder.spices}">
                                        <li>Специи</li>
                                        <c:set var="price" value="${price + 15}"/>
                                    </c:if>
                                </ul>
                                <span id="price">Цена: ${price}</span>
                                <c:set var="totalprice" value="${totalprice + price}"/>
                            </div>
                        </c:forEach>
                        <hr>
                        <span style="font-size: 23px; font-family: Arial; color: #013b7a; font-weight: bold; display: inline-block">
                            Стоимость заказа: ${totalprice} рублей
                        </span>
                        <%
                            HttpSession httpSession = request.getSession();
                            httpSession.setAttribute("price", pageContext.getAttribute("totalprice"));
                        %>
                        <button type="button" class="button" style="width: 400px; height: 50px; margin-left: 100px"
                                onclick="document.location.href='Tacos?cmd=pay'">ОПЛАТИТЬ</button>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>
<div id="footer">
    Часы работы:<br/>
    Будние дни: 10:00 - 22:00<br/>
    Выходные дни: 10:00 - 20:00
</div>
</body>
</html>
