// Окно чата с одним человеком
function chatWithSomeBody(userId, userName, userNickName) {
    let chatWith = '<div class="chatWith">'
        + '<div id="' + userId + 'output" class="container output">'
        + '</div>'
        + '<hr/>'
        + '<div class="options">'
        + '<img class="option" src="img/face.png">'  // Лицо
        + '<img class="option" src="img/img.png">'   // Изображение
        + '<button type="button" class="btn btn-default pull-right" onclick="sendMessagePre('
        + userId
        + ')">Отправить</button>'
        + '</div>'
        + '<div class="input" style="height: 10%">'
        + '<textarea id="'
        + userId
        + 'messageText" class="form-control" onkeydown="binfEnter(event,'
        + userId + ')"></textarea>' + '</div>' + '</div>';
    layer.open({
        type: 1,
        title: [userNickName, 'font-family:Microsoft YaHei;font-size: 24px;height: 50px;'],
        content: chatWith,
        area: ['600px', '400px'],
        maxmin: true,
        offset: ['100px', '100px'],
        shade: 0,
        id: userName,
        resize: true,
        zIndex: layer.zIndex,
        success: function (layero) {
            layer.setTop(layero);
        }
    });

    // Получить историю сообщений
    let allMessages = getMessageRecordBetweenUsers(userId, 0, 30);
    for (let i = 0; i < allMessages.length; i++) {
        let usersId = new Array();
        usersId[0] = allMessages[i].toId;
        let jsonMessage = JSON.stringify({
            from: allMessages[i].fromId,
            to: usersId,
            content: allMessages[i].content,
            type: allMessages[i].type,
            time: allMessages[i].time
        });
        showReceiveMessage(allMessages[i].content, allMessages[i].fromId, usersId, allMessages[i].type, allMessages[i].time, jsonMessage);
    }
}

// Прослушиватель нажатия клавиши Enter в поле ввода
function binfEnter(obj, toUserId) {
    if (obj.keyCode == 13) {
        sendMessagePre(toUserId);
        obj.preventDefault();
    }
}

// Подготовка к отправке сообщения
function sendMessagePre(toUserId) {
    let textAreaId = toUserId + 'messageText';
    let message = document.getElementById(textAreaId).value;
    if (message != '') {
        let toUsersId = new Array();
        toUsersId[0] = toUserId;
        sendMessage(message, toUsersId, 0);
        document.getElementById(textAreaId).value = '';
    }
}

// AJAX запрос для получения истории сообщений между двумя пользователями
function getMessageRecordBetweenUsers(userId, page, number) {
    let allMessages = null;
    let twoUser = {};
    twoUser.userIdA = currentUser.userId;
    twoUser.userIdB = userId;
    twoUser.page = page;
    twoUser.number = number;
    $.ajax({
        async: false, // Установить синхронный режим
        type: 'GET',
        url: address + 'v1/messages/' + twoUser.userIdA + "/" + twoUser.userIdB + "/" + twoUser.page + "/" + twoUser.number,
        dataType: 'json',
        success: function (result) {
            if (result.status === 1) {
                allMessages = result.content;
            } else {
                layer.alert('Ошибка запроса');
            }
        },
        error: function (result) {
            layer.alert('Ошибка запроса');
        }
    });
    // Важно! Метод eval отличается от метода parse, добавьте скобки
    allMessages = eval("(" + allMessages + ")");
    return allMessages;
}