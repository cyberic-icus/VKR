package ru.rsreu.groupservice.service.group;


import ru.rsreu.commonservice.util.Response;

public interface GroupService {
    Response getGroupUsers(Integer id);

    Response createGroup(String groupName, String groupIntroduction, Integer groupCreatorId);

    Response findGroupById(Integer id);

    // Response findGroupByGroupId(String jsonObj);
    Response findGroupByIds(String ids);

}
