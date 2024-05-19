// Функции, связанные с групповыми чатами

// Получает все группы пользователя
function getUserAllGroups() {
    let allGroups = [];
    $.ajax({
        async: false, // Выполнять синхронно
        type: 'GET',
        url: address + 'v1/users/groups/' + currentUser.userId,
        dataType: 'json',
        success: function (result) {
            if (result.status === 1) {
                allGroups = result.content;
            } else {
                layer.alert(result.message());
            }
        },
        error: function (result) {
            layer.alert('Ошибка запроса');
        }
    });
    // Важно: здесь используется eval(), а не parse(). Обратите внимание на скобки.
    if (allGroups !== "") {
        allGroups = eval("(" + allGroups + ")");
    }
    return allGroups;
}

// Получает всех пользователей группы
function getGroupAllUsers(id) {
    let usersAndUserGroup = {};
    let response = null;
    $.ajax({
        async: false, // Выполнять синхронно
        type: 'GET',
        url: address + 'v1/group/users/' + id,
        dataType: 'json',
        success: function (result) {
            response = result;
        },
        error: function (result) {
            layer.alert('Ошибка запроса');
        }
    });
    if (response.status === 1) {
        let content = JSON.parse(response.content);
        usersAndUserGroup.userGroups = content.userGroups;
        usersAndUserGroup.users = content.users;
    } else {
        layer.alert(response.message);
    }
    usersAndUserGroup.userGroups = eval("(" + usersAndUserGroup.userGroups + ")");
    usersAndUserGroup.users = eval("(" + usersAndUserGroup.users + ")");
    return usersAndUserGroup;
}

// Индекс окна создания группы
let createGroupIndex = null;

// Открывает окно создания группы
function createGroup() {
    // Здесь можно добавить функционал загрузки аватара группы
    let html = '<div class="createGroup">'
        + '<div class="container createGroupBox">'
        + '<div class="row createGroupRow">'
        + '<div class="col-md-5 col-sm-5 col-xs-5 col-lg-5 createGroupImgBox">'
        + '<img src="img/photo.jpg" class="img-circle createGroupImg">'
        + '</div>'
        + '<div class="col-md-2 col-sm-2 col-xs-2 col-lg-2 createGroupInfo">'
        + 'Название группы:'
        + '</div>'
        + '<div class="col-md-4 col-sm-4 col-xs-4 col-lg-4 createGroupInfo">'
        + '<input id="createGroupName" type="text" class="form-control" placeholder="Отряд самоубийц">'
        + '</div>'
        + '<div class="col-md-6 col-sm-6 col-xs-6 col-lg-6 createGroupInfo">'
        + 'Описание группы:'
        + '<textarea id="createGroupIntroduction" class="form-control createGroupTextArea" placeholder="Жизнь не вечна, а дурачиться можно всегда!" rows="2"></textarea>'
        + '</div>'
        + '<div class="col-md-6 col-sm-6 col-xs-6 col-lg-6 createGroupInfo">'
        + '<button class="btn btn-default" onclick="ajaxCreateGroup()">Создать группу</button>'
        + '</div>'
        + '</div>'
        + '</div>'
        + '</div>';
    createGroupIndex = layer.open({
        type: 1,
        title: 'Создать группу',
        content: html,
        area: ['500px', '275px'],
        resize: false,
        zIndex: layer.zIndex,
        success: function (layero) {
            layer.setTop(layero);
        }
    });
}

// Создает группу через AJAX
function ajaxCreateGroup() {
    if (createGroupIndex !== null) {
        let createGroup = {};
        createGroup.groupName = document.getElementById('createGroupName').value;
        createGroup.groupIntroduction = document.getElementById('createGroupIntroduction').value;
        createGroup.groupCreatorId = currentUser.userId;
        layer.close(createGroupIndex);
        let response = null;
        $.ajax({
            async: false, // Выполнять синхронно
            type: 'POST',
            url: address + 'v1/group',
            data: createGroup,
            dataType: 'json',
            success: function (result) {
                response = result;
            },
            error: function (result) {
                layer.msg('Ошибка при создании группы', {
                    icon: 2,
                    zIndex: 20000001,
                    time: 2000
                });
            }
        });
        if (response.status === 1) {
            layer.alert('Группа успешно создана. ID вашей группы: ' + JSON.parse(response.content).groupId);
        } else {
            layer.msg('Ошибка создания группы', {
                icon: 2,
                zIndex: 20000001,
                time: 2000
            });
        }
    } else {
        layer.msg('Неполные параметры запроса для создания группы. Попробуйте снова.', {
            icon: 2,
            zIndex: 20000001,
            time: 2000
        });
    }
}

// Получает историю сообщений между пользователем и группой через AJAX
function getMessageRecordBetweenUserAndGroup(id, page, number) {
    let response = null;
    $.ajax({
        async: false, // Выполнять синхронно
        type: 'GET',
        url: address + 'v1/groupMessages/' + currentUser.userId + "/" + id + "/" + page + "/" + number,
        dataType: 'json',
        success: function (result) {
            response = result;
        },
        error: function (result) {
            layer.alert('Ошибка запроса');
        }
    });
    if (response.status === 1) {
        let allMessages = response.content;
        allMessages = eval("(" + allMessages + ")");
        return allMessages;
    } else {
        layer.alert(response.content);
        return null;
    }
}

// Открывает окно группового чата
function chatWithGroup(id, groupId, groupName, groupCreatorId) {
    let chatWith = '<div class="chatWith">'
        + '<div id="' + id + 'outputGroup" class="container output" style="height: 78%;">'
        + '</div>'
        + '<hr/>'
        + '<div class="options">'
        + '<img class="option" src="img/face.png">'
        + '<img class="option" src="img/img.png">'
        + '<button type="button" class="btn btn-default pull-right" onclick="sendGroupMessagePre('
        + id
        + ')">Отправить</button>'
        + '<button type="button" class="btn btn-default pull-right smallOffset" onclick="groupUserList(' + id + ')">Список участников</button>';
    if (groupCreatorId === currentUser.userId)
        chatWith += '<button type="button" class="btn btn-default pull-right smallOffset" onclick="groupInvite(' + id + ')">Пригласить участников</button>';
    chatWith += '</div>'
        + '<div class="input" style="height: 10%">'
        + '<textarea id="'
        + id
        + 'messageTextGroup" class="form-control" onkeydown="binfEnterGroup(event,'
        + id + ')"></textarea>' + '</div>' + '</div>';
    layer.open({
        type: 1,
        title: [groupName, 'font-family:Microsoft YaHei;font-size: 24px;height: 50px;'],
        content: chatWith,
        area: ['600px', '400px'],
        maxmin: true,
        offset: ['100px', '100px'],
        shade: 0,
        id: groupId,
        resize: true,
        zIndex: layer.zIndex,
        success: function (layero) {
            layer.setTop(layero);
        }
    });

    // Получаем историю сообщений
    let allMessages = getMessageRecordBetweenUserAndGroup(id, 0, 30);
    for (let i = 0; i < allMessages.length; i++) {
        let usersId = new Array();
        usersId[0] = id;
        usersId[1] = allMessages[i].toId;
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

// Обрабатывает нажатие Enter в поле ввода группового чата
function binfEnterGroup(obj, id) {
    if (obj.keyCode === 13) {
        sendGroupMessagePre(id);
        obj.preventDefault();
    }
}

// Получает группу по ID через AJAX
function ajaxGetGroupById(id) {
    let group = null;
    let response = null;
    $.ajax({
        async: false, // Выполнять синхронно
        type: 'GET',
        url: address + 'v1/group/' + id,
        dataType: 'json',
        success: function (result) {
            response = result;
        },
        error: function (result) {
            layer.msg('Ошибка при открытии группы', {
                icon: 2,
                zIndex: 20000001,
                time: 2000
            });
        }
    });
    if (response.status === 1) {
        group = eval("(" + response.content + ")");
    } else {
        layer.msg('Ошибка открытия группы', {
            icon: 2,
            zIndex: 20000001,
            time: 2000
        });
    }
    return group;
}

// Подготавливает отправку сообщения в группу
function sendGroupMessagePre(id) {
    let group = ajaxGetGroupById(id);

    let textAreaId = id + 'messageTextGroup';
    let message = document.getElementById(textAreaId).value;
    if (message !== '') {
        let toUsersIdString = (group.groupMembers).split(',');
        let toUsersId = [];
        toUsersId[0] = group.id;
        for (let i = 0; i < toUsersIdString.length; i++) {
            toUsersId[i + 1] = parseInt(toUsersIdString[i]);
        }
        sendMessage(message, toUsersId, 1);
        document.getElementById(textAreaId).value = '';
    }
}

// Открывает список друзей для приглашения в группу
function groupInvite(id) {
    let allRelations = getAllRelations();
    let html = '<div class="groupInviteList">' +
        '<table class="table table-striped table-hover">' +
        '<tr>' +
        '<th>#</th>' +
        '<th>Имя пользователя</th>' +
        '<th>Никнейм</th>' +
        '<th>Пригласить</th>' +
        '</tr>';
    for (let i = 0; i < allRelations.length; i++) {
        html += '<tr>' +
            '<td>' +
            (i + 1) +
            '</td>' +
            '<td>' + allRelations[i].userName + '</td>' +
            '<td>' + allRelations[i].userNickName + '</td>' +
            '<td>' +
            '<button type="button" class="btn btn-default" onclick="groupInviteUser(' + id + ',' + allRelations[i].userId + ')">Пригласить в группу</button>' +
            '</td>' +
            '</tr>';
    }
    html += '</table>' +
        '</div>';
    layer.open({
        type: 1,
        title: 'Пригласить друзей в группу',
        content: html,
        area: ['400px', '600px'],
        resize: false,
        zIndex: layer.zIndex,
        success: function (layero) {
            layer.setTop(layero);
        }
    });
}

// Отправляет приглашение пользователю в группу
function groupInviteUser(id, userId) {
    let message = id;
    let toUsersId = new Array();
    toUsersId[0] = userId;
    sendMessage(message, toUsersId, 6);
}

// Принимает приглашение в группу
function agreeGroupInvite(id) {
    let addGroup = {};
    addGroup.id = id;
    addGroup.userId = currentUser.userId;
    let response = null;
    $.ajax({
        async: false, // Выполнять синхронно
        type: 'POST',
        url: address + 'v1/group/user',
        data: addGroup,
        dataType: 'json',
        success: function (result) {
            response = result;
        },
        error: function (result) {
            layer.msg('Ошибка добавления', {
                icon: 6,
                zIndex: 20000001,
                time: 2000
            });
        }
    });
    if (response.status === 1) {
        layer.close(relationListIndex);
    }
    // else {
    //     text = 'Кажется, судьба не хочет, чтобы мы стали друзьями!';
    // }
    let addFriendRow = document.getElementById(id + 'addFriendRow');
    addFriendRow.style.display = 'none';
    relationApplyNumber--;
    if (relationApplyNumber === 0)
        layer.close(relationApply);
}

// Отклоняет приглашение в группу
function refuseGroupInvite(id) {
    let addFriendRow = document.getElementById(id + 'addFriendRow');
    addFriendRow.style.display = 'none';
    relationApplyNumber--;
    if (relationApplyNumber === 0)
        layer.close(relationApply);
}

// Показывает список участников группы
function groupUserList(id) {
    let usersAndUserGroup = getGroupAllUsers(id);
    let users = usersAndUserGroup.users;
    let userGroup = usersAndUserGroup.userGroups;
    let html = '<div class="groupInviteList">' +
        '<table class="table table-striped table-hover">' +
        '<tr>' +
        '<th>#</th>' +
        '<th>Имя пользователя</th>' +
        '<th>Никнейм</th>' +
        '<th>Ник в группе</th>' +
        '<th>Уровень</th>' +
        '<th>Дата вступления</th>' +
        '</tr>';
    for (let i = 0; i < users.length; i++) {
        html += '<tr>' +
            '<td>' +
            (i + 1) +
            '</td>' +
            '<td>' + users[i].userName + '</td>' +
            '<td>' + users[i].userNickName + '</td>' +
            '<td>' + userGroup[i].groupUserNickName + '</td>' +
            '<td>' + userGroup[i].groupLevel + '</td>' +
            '<td>' + userGroup[i].enterGroupTime + '</td>' +
            '</tr>';
    }
    html += '</table>' +
        '</div>';
    layer.open({
        type: 1,
        title: 'Список участников группы',
        content: html,
        area: ['500px', '600px'],
        resize: false,
        zIndex: layer.zIndex,
        success: function (layero) {
            layer.setTop(layero);
        }
    });
}