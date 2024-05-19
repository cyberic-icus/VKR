function checkRegister() {
    var user = {};
    user.userName = document.getElementById("userName").value;
    user.userNickName = document.getElementById("userNickName").value;
    user.userPassword = document.getElementById("userPassword").value;

    if (user.userName === '') {
        layer.msg('Имя пользователя не может быть пустым', {icon: 2});
        return;
    } else if (user.userName.length >= 12) {
        layer.msg('Длина имени пользователя не должна превышать 12 символов', {icon: 2});
        return;
    }

    if (user.userNickName === '') {
        layer.msg('Никнейм не может быть пустым', {icon: 2});
        return;
    } else if (user.userNickName.length >= 15) {
        layer.msg('Длина никнейма не должна превышать 15 символов', {icon: 2});
        return;
    } else if (user.userPassword === '') {
        layer.msg('Пароль не может быть пустым', {icon: 2});
        return;
    } else if (user.userPassword.length >= 20) {
        layer.msg('Длина пароля не должна превышать 20 символов', {icon: 2});
        return;
    }

    var registerResult = null;
    $.ajax({
        async: false,
        type: 'POST',
        url: address + 'v1/user/register',
        data: user,
        dataType: 'json',
        success: function (result) {
            registerResult = result;
        },
        error: function () {
            layer.alert('Ошибка сети');
        }
    });

    if (registerResult.status === 1) {
        layer.msg('Регистрация успешна', {icon: 1});
        window.location.href = "login.html";
    } else {
        layer.msg(registerResult.message, {icon: 2});
    }
}