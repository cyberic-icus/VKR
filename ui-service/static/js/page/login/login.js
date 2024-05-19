function checkLogin() {
    var user = {};
    user.userName = document.getElementById("userName").value;
    user.userPassword = document.getElementById("userPassword").value;

    if (user.userName === '') {
        layer.msg('Имя пользователя не может быть пустым', {icon: 2});
        return;
    } else if (user.userName.length >= 12) {
        layer.msg('Длина имени пользователя не должна превышать 12 символов', {icon: 2});
        return;
    } else if (user.userPassword === '') {
        layer.msg('Пароль не может быть пустым', {icon: 2});
        return;
    }

    var loginResult = null;
    $.ajax({
        async: false, // Выполнять синхронно
        type: 'GET',
        url: address + 'v1/user/login',
        data: user,
        dataType: 'json',
        success: function (result) {
            loginResult = result;
        },
        error: function () {
            layer.alert('Ошибка сети');
        }
    });

    if (loginResult.status === 1) {
        layer.msg(loginResult.message, {icon: 1});
        window.location.href = makeURL("main.html", "token", loginResult.content);
    } else {
        layer.msg(loginResult.message, {icon: 2});
    }
}
