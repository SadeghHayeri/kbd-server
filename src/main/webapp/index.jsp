<%@ page contentType="text/html; charset=UTF-8" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <title>Document</title>
    <style>
        * {
            direction: rtl;
        }
    </style>
</head>
<body>

    <jsp:include page="header.jsp"/>

    <br>

    <form action="" method="POST">
        <input type="text">
        <input type="text">
        <select name="" id="">
            <option value="0">خرید</option>
            <option value="1">اجاره</option>
        </select>
        <input type="submit" value="جست‌و‌جو">
    </form>

    <br/>

    <form action="add-house" method="POST">
        <select name="building-type">
            <option value="0">ویلایی</option>
            <option value="1">آپارتمانی</option>
        </select>
        <input type="text" name="area" placeholder="متراژ">
        <select name="deal-type">
            <option value="0">خرید</option>
            <option value="1">اجاره</option>
        </select>
        <input type="text" name="price" placeholder="قیمت">
        <input type="text" name="address" placeholder="آدرس">
        <input type="text" name="phone" placeholder="شماره تلفن">
        <input type="text" name="description" placeholder="توضیحات">
        <input type="submit" value="اضافه‌کردن خانه جدید">
    </form>

    <br/>

    <form action="pay" method="POST">
        <input type="number" name="balance-value" placeholder="اعتبار">
        <input type="submit" value="افزایش اعتبار">
    </form>

</body>
</html>