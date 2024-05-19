// Получить адрес сервера

let ws = wsAddress + parseURL("token");
// let ws = "ws://10.141.211.176:21002/" + parseURL("token");
let websocket = null;

// Проверить, поддерживает ли текущий браузер WebSocket
if ('WebSocket' in window) {
    websocket = new WebSocket(ws);
} else {
    layer.alert('Извините, ваш браузер не поддерживает WebSocket', {
        icon: 2
    });
}

// Метод обратного вызова при успешном установлении соединения
websocket.onopen = function () {
    // Показать статус "онлайн"
    // Уведомить всех своих друзей о том, что я в сети
    onLineStatusNotice(3);
    showOfflineMessage();
};

// Метод обратного вызова при получении сообщения
websocket.onmessage = function (event) {
    // Получить сообщение и обработать его
    handleReceiveMessage(event.data);
};

// Метод обратного вызова при возникновении ошибки соединения
websocket.onerror = function () {
    layer.alert('Произошла ошибка соединения, извините за неудобства');
};

// Метод обратного вызова при закрытии соединения
websocket.onclose = function () {
    // Соединение закрыто, уведомить всех друзей о том, что я оффлайн
    onLineStatusNotice(4);
    layer.alert('Спасибо за использование, до свидания');
};

// Слушатель события закрытия окна. При закрытии окна закрыть соединение WebSocket, чтобы сервер не выбросил исключение.
window.onbeforeunload = function () {
    closeWebSocket();
};

// Закрыть соединение WebSocket
function closeWebSocket() {
    websocket.close();
}

// Отправить сообщение
function sendMessage(content, usersId, type) {
    websocket.send(JSON.stringify({
        from: getCurrentUser().userId,
        to: usersId,
        content: content,
        type: type,
        time: getDateFull()
    }));
}