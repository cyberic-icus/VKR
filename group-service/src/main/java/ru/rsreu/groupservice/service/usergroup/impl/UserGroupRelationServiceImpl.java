package ru.rsreu.groupservice.service.usergroup.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ru.rsreu.commonservice.util.Common;
import ru.rsreu.commonservice.util.Response;
import ru.rsreu.groupservice.constant.CONSTANT;
import ru.rsreu.groupservice.domain.Groups;
import ru.rsreu.groupservice.domain.UserGroupRelation;
import ru.rsreu.groupservice.repository.GroupRepository;
import ru.rsreu.groupservice.repository.UserGroupRelationRepository;
import ru.rsreu.groupservice.service.usergroup.UserGroupRelationService;

import javax.transaction.Transactional;

@Service
public class UserGroupRelationServiceImpl implements UserGroupRelationService {

    @Autowired
    UserGroupRelationRepository userGroupRelationRepository;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    RestTemplate restTemplate;

    @Override
    @Transactional
    public Response addGroupUsers(Integer id, Integer userId) {
        try {
            UserGroupRelation userGroupRelation = new UserGroupRelation();
            userGroupRelation.setGroupId(id);
            userGroupRelation.setGroupLevel(0);
            userGroupRelation.setUserId(userId);
            userGroupRelation.setEnterGroupTime(Common.getCurrentTime());
            userGroupRelation.setGroupUserNickName(" ");
            userGroupRelationRepository.save(userGroupRelation);
            Groups group = groupRepository.getOne(id);
            if (Common.isEmpty(group.getGroupMembers())) {
                group.setGroupMembers(String.valueOf(userGroupRelation.getUserId()));
            } else {
                group.setGroupMembers(group.getGroupMembers() + "," + userGroupRelation.getUserId());
            }
            group.setGroupUserCount(group.getGroupUserCount() + 1);
            groupRepository.save(group);

            MultiValueMap<String, Integer> requestEntity = new LinkedMultiValueMap<>();
            requestEntity.add("userId", userId);
            requestEntity.add("groupId", id);
            ResponseEntity<Response> responseEntity =
                    restTemplate.postForEntity(CONSTANT.USER_SERVICE_UPDATE_USER_GROUPS, requestEntity, Response.class);
            if (responseEntity.getBody().getStatus() != 1) {
                return new Response(-1, "Ошибка сервиса", null);
            }

            return new Response(1, "Успешно добавлен участник группы", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(-1, "Ошибка при получении участника группы", null);
        }
    }
}