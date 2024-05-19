package ru.rsreu.groupservice.service.usergroup;

import ru.rsreu.commonservice.util.Response;

public interface UserGroupRelationService {
    Response addGroupUsers(Integer id, Integer userId);
}
