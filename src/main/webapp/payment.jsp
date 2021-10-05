<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<html lang="ru">
<head>
    <title>Страница оплаты</title>
    <link type="text/css" rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>
    <div style="width: 100%; height: auto; text-align: center">
        <p style="font-size: 40px; font-family: Arial; color: #013b7a; font-weight: bold;">К оплате: ${price} рублей</p>
        <form action="Tacos" method="GET">
            <input type="hidden" name="cmd" value="paid">
            <div id="bankcard" style="background-image: url(img/bankcard.png);">
                <input id="num" min="0" type="number" name="cardnumber" placeholder="0000000000000000" required>
                <input id="month" min="1" max="12" type="number" name="cardmonth" placeholder="00" required>
                <input id="year" min="21" type="number" name="cardyear" placeholder="00" required>
                <input id="name" type="text" name="cardname" placeholder="CARDHOLDER NAME" required>
                <input id="cvc" min="0" max="999" type="number" name="cardcvc" placeholder="000" required>
            </div>
            <input type="submit" class="paybutton" value="ОПЛАТИТЬ">
        </form>
    </div>
</body>
</html>
