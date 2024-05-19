package ru.rsreu.groupservice.service.group.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
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
import ru.rsreu.groupservice.service.group.GroupService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    UserGroupRelationRepository userGroupRelationRepository;

    @Autowired
    RestTemplate restTemplate;

    @Override
    public Response getGroupUsers(Integer id) {
        try {
            Optional<Groups> groupsOptional = groupRepository.findById(id);
            Groups groups = null;
            if (groupsOptional.isPresent()) {
                groups = groupsOptional.get();
            }
            JSONObject jsonObject1 = new JSONObject();
            if (!Common.isNull(groups) && !Common.isEmpty(groups.getGroupMembers())) {
                Response response = restTemplate.getForEntity(CONSTANT.USER_SERVICE_GET_USERS_BY_USERIDS, Response.class, groups.getGroupMembers()).getBody();
                if (response.getStatus() == 1) {
                    jsonObject1.put("users", response.getContent());
                } else {
                    jsonObject1.put("users", "[]");
                }
            } else {
                jsonObject1.put("users", "[]");
            }
            List<UserGroupRelation> userGroupRelations = userGroupRelationRepository.findByGroupId(id);
            jsonObject1.put("userGroups", JSONArray.toJSONString(userGroupRelations, SerializerFeature.UseSingleQuotes));

            return new Response(1, "Успешное получение участников группы", jsonObject1.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(-1, "Ошибка при получении участников группы", null);
        }
    }

    @Override
    public Response createGroup(String groupName, String groupIntroduction, Integer groupCreatorId) {
        try {
            Groups group = new Groups();
            String groupId = String.valueOf((int) (Math.random() * 100000));
            while (groupRepository.findByGroupId(groupId) != null) {
                groupId = String.valueOf((int) (Math.random() * 100000));
            }
//            Response response = restTemplate.getForEntity(CONSTANT.USER_SERVICE_GET_USER_BY_USERID, Response.class, groupCreatorId).getBody();
//            JSONObject user = JSONObject.parseObject((String)response.getContent());
            group.setGroupId(groupId);
            group.setGroupCreatorId(groupCreatorId);
            group.setGroupIntroduction(groupIntroduction);
            group.setGroupName(groupName);
            group.setGroupCreateTime(Common.getCurrentTime());
            group.setGroupUserCount(0);
            group.setGroupMembers("");
            groupRepository.save(group);
            UserGroupRelation userGroupRelation = new UserGroupRelation();
            // здесь два id не одно и то же, одно это логический id, другое это бизнес id, нужно различать
            Groups groups = groupRepository.findByGroupId(groupId);
            userGroupRelation.setGroupId(groups.getId());
            userGroupRelation.setUserId(groupCreatorId);
            userGroupRelation.setEnterGroupTime(Common.getCurrentTime());
            userGroupRelation.setGroupUserNickName("");
            userGroupRelation.setGroupLevel(10);
            userGroupRelationRepository.save(userGroupRelation);
            groups.setGroupMembers(String.valueOf(groupCreatorId));
            groups.setGroupUserCount(groups.getGroupUserCount() + 1);
            groupRepository.save(groups);
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("groupId", groupId);

            MultiValueMap<String, Integer> requestEntity = new LinkedMultiValueMap<>();
            requestEntity.add("userId", groupCreatorId);
            requestEntity.add("groupId", groups.getId());
            ResponseEntity<Response> responseEntity =
                    restTemplate.postForEntity(CONSTANT.USER_SERVICE_UPDATE_USER_GROUPS, requestEntity, Response.class);
            if (responseEntity.getBody().getStatus() != 1) {
                return new Response(-1, "Ошибка удаленного сервиса", null);
            }

            return new Response(1, "Группа успешно создана", jsonObject1.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(-1, "Ошибка при создании группы", null);
        }
    }

    @Override
    public Response findGroupById(Integer id) {
        try {
            Optional<Groups> groupsOptional = groupRepository.findById(id);
            if (groupsOptional.isPresent()) {
                Groups groups = groupsOptional.get();
                return new Response(1, "Успешное получение группы пользователя", JSON.toJSONString(groups, SerializerFeature.UseSingleQuotes));
            }
            return new Response(0, "Группа не существует", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(-1, "Ошибка при получении группы пользователя", null);
        }
    }

    @Override
    public Response findGroupByIds(String ids) {
        String[] strIds = ids.split(",");
        List<Integer> intIds = new ArrayList<>();
        for (String groupId : strIds) {
            intIds.add(Integer.valueOf(groupId));
        }
        List<Groups> groups = groupRepository.findByIdIn(intIds);
        return new Response(1, "Успешное получение группы", JSONArray.toJSONString(groups));
    }

}