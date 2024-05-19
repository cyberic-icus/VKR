package ru.rsreu.userrelationservice.service;

import ru.rsreu.commonservice.util.Response;

public interface UserRelationService {
    Response buildRelation(Integer userIdA, Integer userIdB);

    Response removeRelation(String jsonObj);

}
