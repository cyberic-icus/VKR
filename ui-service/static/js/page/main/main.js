// "message"{
//			"from": "xxxx",
//			"to":["xxx"],
//			"content":"xxxxx",
//			"type":0,
//			"time":"xxxx.xx.xx xx.xx.xx"
//		}

let noticeIndex = null;
let noticeUser = new Array();
let noticeMessage = new Array();
let noticeCount = 0;
let statusChangeMark = 0;

let currentUser = getCurrentUser();

// Обработка полученных данных
function handleReceiveMessage(message) {
    messages = JSON.parse(message);
    // Проверка, является ли сообщение уведомлением о подключении/отключении
    if (messages.type == 3 || messages.type == 4) {
        changeToOnlineStatus(messages.content, messages.from, messages.to, messages.type, messages.time, message);
    } else if (messages.type == 5 || messages.type == 6) {
        openRelationApply(messages.content, messages.from, messages.to, messages.type, messages.time, message);
    } else if (messages.type == 0 || messages.type == -1 || messages.type == 1)
        showReceiveMessage(messages.content, messages.from, messages.to, messages.type, messages.time, message);
}

// Отображение сообщения на веб-странице
function showReceiveMessage(content, from, to, type, time, message) {
    if (type === 3 || type === 4) {
        return;
    }
    let times = time.split(' ');
    let now = getDateFull();
    let nows = now.split(' ');
    let showTime = times[1];
    if (nows[0] != times[0]) {
        showTime = time;
    }
    if (from == currentUser.userId) {
        let messageReceiver = '#' + to[0] + 'output';
        let target = document.getElementById(to[0] + 'output');
        if (type == 1) {
            messageReceiver = '#' + to[0] + 'outputGroup';
            target = document.getElementById(to[0] + 'outputGroup');
        }
        let rightArrow = '<div class="row singleMessage">'
            + '<div class="col-md-11 col-sm-11 col-xs-11 col-lg-11 text">'
            + '<div class="col-md-12 col-sm-12 col-xs-12 col-lg-12 timePositionRight">'
            + '<div id="time" class="timeRight">'
            + showTime
            + '</div>'
            + '</div>'
            + '<div class="arrowBoxRight">'
            + '<div class="message">'
            + content
            + '</div>'
            + '</div>'
            + '</div>'
            + '<div id="icons" class="col-md-1 col-sm-1 col-xs-1 col-lg-1  iconsRight">'
            + '<img class="img-circle iconssRight" src="img/photo.jpg">'
            + '</div>' + '</div>';

        if (target) {
            $(messageReceiver).append(rightArrow);
            target.scrollTop = target.scrollHeight;
        } else {
            doMessageNotice(content, from, to, type, time, message);
        }
    } else {
        let messageReceiver = '#' + from + 'output';
        let target = document.getElementById(from + 'output');
        if (type === 1) {
            messageReceiver = '#' + to[0] + 'outputGroup';
            target = document.getElementById(to[0] + 'outputGroup');
            let fromUser = getUserById(from);

            showTime = fromUser.userNickName + '&nbsp;&nbsp;' + showTime;
        }
        leftArrow = '<div class="row singleMessage">'
            + '<div id="icons" class="col-md-1 col-sm-1 col-xs-1 col-lg-1  iconsLeft">'
            + '<img class="img-circle iconssLeft" src="img/photo.jpg">'
            + '</div>'
            + '<div class="col-md-11 col-sm-11 col-xs-11 col-lg-11 text">'
            + '<div class="col-md-12 col-sm-12 col-xs-12 col-lg-12 timePositionLeft">'
            + '<div class="timeLeft">'
            + showTime
            + '</div>'
            + '</div>' + '<div class="arrowBoxLeft">'
            + '<div id="leftMessage" class="message">' + content
            + '</div>' + '</div>' + '</div>' + '</div>';
        if (target) {
            $(messageReceiver).append(leftArrow);
            target.scrollTop = target.scrollHeight;
        } else
            doMessageNotice(content, from, to, type, time, message);
    }
}

// Уведомления о сообщениях
function doMessageNotice(content, from, to, type, time, message) {
    let fromUser = null;
    if (type !== 7) {
        fromUser = getUserById(from);
    }
    if (noticeIndex == null) {
        let html = '<div class="notice">' +
            '<div class="noticePosition" onclick="openNotice();">' +
            '<marquee id="noticeText">' +
            '</marquee>' +
            '</div>' +
            '</div>';
        noticeIndex = layer.open({
            type: 1,
            title: false,
            closeBtn: 0,
            content: html,
            shade: 0,
            offset: 't',
            area: ['-webkit-fill-available', '40px'],
            anim: 6,
            id: 'notice'
        });
    }
    if (type === 0 || type === -1 || type === 1 || type === 7) {
        noticeUser[noticeCount] = fromUser;
        noticeMessage[noticeCount] = message;
        noticeCount++;
    }
    let noticeText = document.getElementById('noticeText');
    let text = '';
    if (type === 0 || type === -1) {
        text += fromUser.userNickName + ':' + content;
    } else if (type === 3) {
        text = 'Ваш контакт&nbsp;' + fromUser.userNickName + '&nbsp;в сети';
        statusChangeMark = 1;
    } else if (type === 4) {
        text = 'Ваш контакт &nbsp;' + fromUser.userNickName + '&nbsp;вышел из сети';
        statusChangeMark = 1;
    } else if (type === 1) {
        let group = ajaxGetGroupById(to[0]);
        text = group.groupName + '|' + fromUser.userNickName + ':' + content;
    } else if (type === 7) {
        let group = ajaxGetGroupById(from);
        text = group.groupName + ' имеет ' + content + " новых сообщений";
    }
    text += '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
    noticeText.innerHTML += text;
}

function openNotice() {
    layer.close(noticeIndex);
    if (statusChangeMark === 1) {
        listRelation();
    }
    for (let i = 0; i < noticeCount; i++) {
        let messages = JSON.parse(noticeMessage[i]);
        if (messages.type === 0 || messages.type === -1) {
            chatWithSomeBody(noticeUser[i].userId, noticeUser[i].userName, noticeUser[i].userNickName);
        } else if (messages.type === 1) {
            let group = ajaxGetGroupById(messages.to[0]);
            chatWithGroup(group.id, group.groupId, group.groupName, group.groupCreatorId);
        } else if (messages.type === 7) {
            let group = ajaxGetGroupById(messages.from);
            chatWithGroup(group.id, group.groupId, group.groupName, group.groupCreatorId);
        }
    }
    noticeIndex = null;
    noticeUser.splice(0, noticeUser.length);
    noticeMessage.splice(0, noticeMessage.length);
    noticeCount = 0;
    statusChangeMark = 0;
}

function appendZero(s) {
    return ("00" + s).substr((s + "").length);
} // Функция добавления нуля

// Получить текущую дату и время
function getDateFull() {
    let date = new Date();
    let currentdate = date.getFullYear() + "-"
        + appendZero(date.getMonth() + 1) + "-"
        + appendZero(date.getDate()) + " "
        + appendZero(date.getHours()) + ":"
        + appendZero(date.getMinutes()) + ":"
        + appendZero(date.getSeconds());
    return currentdate;
}

function closeWindow() {
    window.opener = null;
    window.open('', '_self');
    window.close();
}

layer.ready(function () {
    listRelation();
});

// Использовать AJAX для получения всех друзей текущего пользователя
function getAllRelations() {
    let response = null;
    $.ajax({
        async: false, // Установить синхронный режим
        type: 'GET',
        url: address + 'v1/users/friends/' + currentUser.userId,
        dataType: 'json',
        success: function (result) {
            response = result;
        },
        error: function (result) {
            layer.alert('Ошибка запроса');
        }
    });
    if (response.status === -1) {
        layer.alert('Ошибка запроса');
        return null;
    } else {
        if (response.content != "") {
            return eval("(" + response.content + ")");
        } else
            return [];
    }
}

function onLineStatusNotice(type) {
    let allRelations = getAllRelations();
    let content = null;
    if (type === 3)
        content = 'Уведомление о подключении';
    else if (type === 4)
        content = 'Уведомление о выходе';
    let usersId = [];
    for (let i = 0; i < allRelations.length; i++) {
        usersId[i] = allRelations[i].userId;
    }
    sendMessage(content, usersId, type);
}

// Изменяет статус пользователя на "онлайн"
function changeToOnlineStatus(content, from, to, type, time, message) {
    let onLineStatus = document.getElementById(from + 'onlineStatus');
    let offLineStatus = document.getElementById(from + 'offlineStatus');
    // Проверяем, если не найден элемент с таким ID, то сообщение обрабатывается как обычное и помещается в буфер
    if (onLineStatus && type === 3) {
        onLineStatus.style.opacity = 1;
        offLineStatus.style.opacity = 0;
    } else if (offLineStatus && type === 4) {
        onLineStatus.style.opacity = 0;
        offLineStatus.style.opacity = 1;
    } else {
        doMessageNotice(content, from, to, type, time, message);
    }
}

// Формирует список друзей и групп
function listRelation() {
    let allRelations = getAllRelations(); // Получаем список всех друзей
    let allGroups = getUserAllGroups(); // Получаем список всех групп пользователя
    let html = '<div id="relationList" class="relation">' +
        '<div class="lists">' +
        '<ul class="nav nav-tabs">' +
        '<li class="active justifile text-center">' +
        '<a href="#relationPeople" data-toggle="tab">' +
        'Список контактов' +
        '</a>' +
        '</li>' +
        '<li class="justifile text-center">' +
        '<a href="#groupPeople" data-toggle="tab">' +
        'Список групп' +
        '</a>' +
        '</li>' +
        '<li class="justifile text-center">' +
        '<a href="#groupPeople" data-toggle="tab">' +
        'Список курсов' +
        '</a>' +
        '</li>' +
        '</ul>' +
        '<div class="tab-content">' +
        '<div class="tab-pane fade in active" id="relationPeople">';
    for (let i = 0; i < allRelations.length; i++) {
        // Внимание! Внимание! Внимание!  В параметрах функции onclick, если это строка, можно использовать только одинарные кавычки!!!! Только одинарные кавычки!
        // Долго мучился, исправляя эту ошибку. Раньше одинарные кавычки работали, но когда я изменил код на ajax,  из-за конкатенации строк одинарные кавычки стали двойными, и все сломалось, а я не мог понять, где ошибка.
        // Теперь я понимаю, что, хотя в js строки можно заключать в одинарные или двойные кавычки, в html при добавлении параметров это не так! Для html двойные кавычки - это начало и конец атрибута тега, а одинарные кавычки не имеют значения.
        // Поэтому использование здесь двойных кавычек приведет к путанице в html, а одинарные кавычки не вызовут проблем. Запишем, запишем. (При конкатенации строк в js используется символ +, который рассматривается как обычный символ строки и не имеет специального значения!)
        html += '<div class="relationSingle" onclick="chatWithSomeBody(' + allRelations[i].userId + ',\'' + allRelations[i].userName + '\',\'' + allRelations[i].userNickName + '\');">' +
            '<div class="photoBox">' +
            '<img class="img-circle photo" src="img/photo.jpg">' +
            '</div>' +
            '<div id="currentNickName" class="list">' +
            allRelations[i].userNickName +
            '</div>' +
            '<div id="' + allRelations[i].userId + 'onlineStatus" class="onlineStatus">' +
            'Онлайн' +
            '</div>' +
            '<div id="' + allRelations[i].userId + 'offlineStatus" class="offlineStatus">' +
            'Оффлайн' +
            '</div>' +
            '<div class="recent">' +
            '</div>' +
            '</div>';
    }
    html += '</div>' +
        '<div class="tab-pane fade" id="groupPeople">';
    for (let i = 0; i < allGroups.length; i++) {
        html += '<div class="relationSingle" onclick="chatWithGroup(' + allGroups[i].id + ',\'' + allGroups[i].groupId + '\',\'' + allGroups[i].groupName + '\',' + allGroups[i].groupCreatorId + ');">' +
            '<div class="photoBox">' +
            '<img class="img-circle photo" src="img/photo.jpg"/>' +
            '</div>' +
            '<div class="list">' +
            allGroups[i].groupName +
            '</div>' +
            '<div class="recent">' +
            '</div>' +
            '</div>';
    }
    html += '</div>' +
        '</div>' +
        '</div>' +
        '<div class="functions">' +
        '<div class="btn-group btn-group-justified">' +
        '<div class="btn-group">' +
        '<button type="button" class="btn btn-default" onclick="searchFriend();">Добавить друга</button>' +
        '</div>' +
        // '<div class="btn-group">' +
        // '<button type="button" class="btn btn-default" onclick="test();">Тест</button>' +
        // '</div>' +
        // '<div class="btn-group">' +
        // '<button type="button" class="btn btn-default">Ожидание</button>' +
        // '</div>' +
        '<div class="btn-group">' +
        '<button type="button" class="btn btn-default" onclick="createGroup()">Создать группу</button>' +
        '</div>' +
        '</div>' +
        '</div>' +
        '</div>';
    relationListIndex = layer.open({
        type: 1,
        skin: 'layer-ext-lists',
        title: [currentUser.userNickName, 'font-family:Microsoft YaHei;font-size: 24px;height: 80px;'],
        closeBtn: 1,
        content: html,
        area: ['400px', '600px'],
        offset: 'rb',
        anim: 2,
        id: 'relationList',
        shade: 0,
        zIndex: layer.zIndex,
        success: function (layers) {
            layer.setTop(layers);
        }
    });
    for (let i = 0; i < allRelations.length; i++) {
        let onLineStatus = document.getElementById(allRelations[i].userId + 'onlineStatus');
        let offLineStatus = document.getElementById(allRelations[i].userId + 'offlineStatus');
        if (allRelations[i].userIsOnline === 0) {
            onLineStatus.style.opacity = 0;
            offLineStatus.style.opacity = 1;
        } else {
            onLineStatus.style.opacity = 1;
            offLineStatus.style.opacity = 0;
        }
    }
}

let relationApply = null;
let relationApplyNumber = 0;

function openRelationApply(content, from, to, type, time, message) {
    let fromUser = getUserById(from);
    let addFriendApply = document.getElementById('addFriendApply');
    if (!addFriendApply) {
        let html = '<div id="addFriendApply" class="addFriend">'
            + '<div id="friendApplyBox" class="container addFriendBox">'
            + '</div>'
            + '</div>';
        relationApply = layer.open({
            type: 1,
            title: 'Системное сообщение',
            content: html,
            area: ['500px', '275px'],
            shade: 0,
            resize: false,
            zIndex: layer.zIndex,
            success: function (layero) {
                layer.setTop(layero);
            }
        });
    }
    relationApplyNumber++;
    if (type === 5)
        var friendApplyHtml = '<div id="' + from + 'addFriendRow" class="row addFriendRow">'
            + '<div class="col-md-5 col-sm-5 col-xs-5 col-lg-5 addFriendImgBox">'
            + '<img src="img/photo.jpg" class="img-circle addFriendImg">'
            + '</div>'
            + '<div class="col-md-7 col-sm-7 col-xs-7 col-lg-7 addFriendInfo">'
            + 'Имя пользователя:'
            + fromUser.userName
            + '</div>'
            + '<div class="col-md-7 col-sm-7 col-xs-7 col-lg-7 addFriendInfo">'
            + 'Никнейм:'
            + fromUser.userNickName
            + '</div>'
            + '<div class="col-md-7 col-sm-7 col-xs-7 col-lg-7 addFriendInfo">'
            + 'Дополнительное сообщение:'
            + '</div>'
            + '<div class="col-md-7 col-sm-7 col-xs-7 col-lg-7 addFriendInfo">'
            + content
            + '</div>'
            + '<div class="col-md-3 col-sm-3 col-xs-3 col-lg-3 addFriendInfo">'
            + '<button class="btn btn-default" onclick=\'agreeAddThisUser(' + from + ')\'>Добавить этого парня</button>'
            + '</div>'
            + '<div class="col-md-3 col-sm-3 col-xs-3 col-lg-3 addFriendInfo">'
            + '<button class="btn btn-default" onclick=\'refuseAddThisUser(' + from + ')\'>Жестоко отказать</button>'
            + '</div>'
            + '</div>';
    else if (type === 6) {
        let group = ajaxGetGroupById(content);
        friendApplyHtml = '<div id="' + group.id + 'addFriendRow" class="row addFriendRow">'
            + '<div class="col-md-5 col-sm-5 col-xs-5 col-lg-5 addFriendImgBox">'
            + '<img src="img/photo.jpg" class="img-circle addFriendImg">'
            + '</div>'
            + '<div class="col-md-7 col-sm-7 col-xs-7 col-lg-7 addFriendInfo">'
            + 'ID группы:'
            + group.groupId
            + '</div>'
            + '<div class="col-md-7 col-sm-7 col-xs-7 col-lg-7 addFriendInfo">'
            + 'Название группы:'
            + group.groupName
            + '</div>'
            + '<div class="col-md-7 col-sm-7 col-xs-7 col-lg-7 addFriendInfo">'
            + 'Тип: Приглашение в группу'
            + '</div>'
            + '<div class="col-md-7 col-sm-7 кол-xs-7 col-lg-7 addFriendInfo">'
            + 'Приглашающий:'
            + fromUser.userNickName
            + '</div>'
            + '<div class="col-md-3 col-sm-3 col-xs-3 col-lg-3 addFriendInfo">'
            + '<button class="btn btn-default" onclick=\'agreeGroupInvite(' + content + ')\'>Зайти посмотреть</button>'
            + '</div>'
            + '<div class="col-md-3 col-sm-3 col-xs-3 col-lg-3 addFriendInfo">'
            + '<button class="btn btn-default" onclick=\'refuseGroupInvite(' + content + ')\'>Не интересно</button>'
            + '</div>'
            + '</div>';
    }
    let friendApplyBox = document.getElementById('friendApplyBox');
    friendApplyBox.innerHTML += friendApplyHtml;
}

// Тест
function test() {
    addFriend(1, 'Тест', 'Тест');
}