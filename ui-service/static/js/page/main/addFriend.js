let addFriendIndex = null;

/**
 * Открывает окно для ввода имени пользователя
 */
function searchFriend() {
    layer.prompt({
        title: 'Введите имя пользователя:',
        zIndex: 20000000
    }, function (userName, index) {
        layer.close(index);
        findUser(userName);
    });
}

/**
 * Ищет пользователя и открывает окно с результатами поиска
 * @param userName
 */
function findUser(userName) {
    $.ajax({
        async: false, // Выполнять синхронно
        type: 'GET',
        url: address + 'v1/user/userName/' + userName,
        dataType: 'json',
        success: function (result) {
            if (result.status === 1) {
                let user = JSON.parse(result.content);
                addFriend(user.userId, user.userName, user.userNickName);
            } else {
                layer.msg('Пользователь не найден', {
                    icon: 6,
                    zIndex: 20000001,
                    time: 2000
                });
            }
        },
        error: function (result) {
            layer.msg('Ошибка поиска', {
                icon: 6,
                zIndex: 20000001,
                time: 2000
            });
        }
    });
}

/**
 * Открывает окно с результатами поиска пользователя
 * @param userId
 * @param userName
 * @param userNickName
 */
function addFriend(userId, userName, userNickName) {
    let html = '<div id="addFriendContent" class="addFriend">'
        + '<div class="container addFriendBox">'
        + '<div class="row addFriendRow">'
        + '<div class="col-md-5 col-sm-5 col-xs-5 col-lg-5 addFriendImgBox">'
        + '<img src="img/photo.jpg" class="img-circle addFriendImg"/>'
        + '</div>'
        + '<div class="col-md-7 col-sm-7 col-xs-7 col-lg-7 addFriendInfo">'
        + 'Имя пользователя:'
        + userName
        + '</div>'
        + '<div class="col-md-7 col-sm-7 col-xs-7 col-lg-7 addFriendInfo">'
        + 'Никнейм:'
        + userNickName
        + '</div>'
        + '<div class="col-md-6 col-sm-6 col-xs-6 col-lg-6 addFriendInfo">'
        + 'Сообщение:'
        + '<textarea id="addFriendMessage" class="form-control addFriendTextArea" rows="2"></textarea>'
        + '</div>'
        + '<div class="col-md-7 col-sm-7 col-xs-7 col-lg-7 addFriendInfo">'
        + '<button class="btn btn-default" onclick=\'addThisUser(' + userId + ')\'>Добавить в друзья</button>'
        + '</div>' + '</div>' + '</div>' + '</div>';
    addFriendIndex = layer.open({
        type: 1,
        title: 'Результаты поиска',
        content: html,
        area: ['500px', '275px'],
        resize: false,
        id: 'addFriend',
        zIndex: 20000001
    });
}

/**
 * Добавляет пользователя в друзья
 * @param userId
 */
function addThisUser(userId) {
    let text = document.getElementById('addFriendMessage').value;
    let usersId = new Array();
    usersId[0] = userId;
    sendMessage(text, usersId, 5);
    layer.close(addFriendIndex);
}

/**
 * Принимает запрос на добавление в друзья
 * @param userId
 */
function agreeAddThisUser(userId) {
    let text = '';
    let addUser = {};
    addUser.userIdA = userId;
    addUser.userIdB = currentUser.userId;
    $.ajax({
        async: false, // Выполнять синхронно
        type: 'POST',
        url: address + 'v1/userRelations',
        data: addUser,
        dataType: 'json',
        success: function (result) {
            if (result.status === 1) {
                text = 'Я принял твой запрос на добавление в друзья! Давай начнем общаться!';
            } else {
                text = 'Кажется, судьба не хочет, чтобы мы стали друзьями!';
            }
        },
        error: function (result) {
            layer.msg('Ошибка добавления', {
                icon: 6,
                zIndex: 20000001,
                time: 2000
            });
        }
    });
    let usersId = [];
    usersId[0] = userId;
    sendMessage(text, usersId, -1);
    let addFriendRow = document.getElementById(userId + 'addFriendRow');
    addFriendRow.style.display = 'none';
    relationApplyNumber--;
    if (relationApplyNumber === 0)
        layer.close(relationApply);
    // Обновляем список друзей
    let relationList = document.getElementById('relationList');
    if (relationList) {
        layer.close(relationListIndex);
    }
}

/**
 * Отклоняет запрос на добавление в друзья
 * @param userId
 */
function refuseAddThisUser(userId) {

    let text = 'Система подбросила монетку и решила, что вы не подходите друг другу, поэтому ваш запрос отклонен.';
    let usersId = [];
    usersId[0] = userId;
    sendMessage(text, usersId, -1);
    let addFriendRow = document.getElementById(userId + 'addFriendRow');
    addFriendRow.style.display = 'none';
    relationApplyNumber--;
    if (relationApplyNumber === 0)
        layer.close(relationApply);
}
