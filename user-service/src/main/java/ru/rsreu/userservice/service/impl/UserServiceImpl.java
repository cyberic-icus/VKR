package ru.rsreu.userservice.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.rsreu.commonservice.dto.AuthUser;
import ru.rsreu.commonservice.util.Common;
import ru.rsreu.commonservice.util.JwtUtils;
import ru.rsreu.commonservice.util.Response;
import ru.rsreu.userservice.constant.CONSTANT;
import ru.rsreu.userservice.domain.User;
import ru.rsreu.userservice.domain.UserDetail;
import ru.rsreu.userservice.repository.UserDetailRepository;
import ru.rsreu.userservice.repository.UserRepository;
import ru.rsreu.userservice.service.UserService;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserDetailRepository userDetailRepository;

    @Autowired
    RestTemplate restTemplate;

    @Override
    public Response login(String userName, String userPassword) {
        try {
            UserDetail userDetail = userDetailRepository.findByUserDetailName(userName);
            if (userDetail != null) {
                if (Common.isEquals(userDetail.getUserDetailPassword(), userPassword)) {
                    Timestamp expireTime = new Timestamp(System.currentTimeMillis() + CONSTANT.EXPIRE_TIME * 1000 * 60);
                    AuthUser authUser = new AuthUser(userDetail.getUserDetailId(),
                            userDetail.getUserDetailName(),
                            userDetail.getUserDetailNickName(),
                            expireTime);
                    String token = JwtUtils.createJWT(JSON.toJSONString(authUser));
                    return new Response(1, "Вход в систему успешен", token);

                } else {
                    return new Response(-1, "Неправильное имя пользователя или пароль", null);
                }
            } else {
                return new Response(-1, "Пользователь не существует", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(-1, "Исключение при входе в систему", null);
        }
    }

    @Override
    public Response logout(String token) {
        return null;
    }

    @Override
    @Transactional
    public Response register(String userName, String userNickName, String userPassword) {
        try {
            if (userRepository.findByUserName(userName) == null) {
                UserDetail userDetail = new UserDetail();
                userDetail.setUserDetailName(userName);
                userDetail.setUserDetailNickName(userNickName);
                userDetail.setUserDetailPassword(userPassword);
                userDetail.setUserRegisterTime(Common.getCurrentTime());
                userDetailRepository.save(userDetail);
                UserDetail userDetail1 = userDetailRepository.findByUserDetailName(userName);
                User user = new User();
                user.setUserId(userDetail1.getUserDetailId());
                user.setUserName(userDetail1.getUserDetailName());
                user.setHasOffLineMessage(0);
                user.setUserNickName(userDetail1.getUserDetailNickName());
                user.setUserGroups("");
                user.setUserRelations("");
                user.setUserRole(0);
                userRepository.save(user);
                return new Response(1, "Регистрация успешна", null);
            } else {
                return new Response(-1, "Имя пользователя уже существует", null);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new Response(-1, "Исключение при регистрации", null);
        }
    }

    @Override
    public Response getRelations(Integer userId) {
        try {
            List<User> userList = new ArrayList<>();
            User user = userRepository.findByUserId(userId);
            if (!Common.isNull(user)) {
                if (!Common.isEmpty(user.getUserRelations())) {
                    String[] relation = user.getUserRelations().split(",");
                    for (String friendId : relation) {
                        User user1 = userRepository.findByUserId(Integer.valueOf(friendId));
                        userList.add(user1);
                    }
                }
            }
            return new Response(1, "Получение друзей успешно", JSONArray.toJSONString(userList));
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(-1, "Исключение при получении друзей", null);
        }
    }

    @Override
    public Response getUserGroups(Integer userId) {
        try {
            User user = userRepository.findByUserId(userId);
            String groups = "";
            if (!Common.isNull(user)) {
                if (!Common.isEmpty(user.getUserGroups())) {
                    ResponseEntity<Response> responseResponseEntity = restTemplate.getForEntity(CONSTANT.GROUP_FIND_BY_IDS, Response.class, user.getUserGroups());
                    Response response = responseResponseEntity.getBody();
                    if (response.getStatus() == 1) {
                        return new Response(1, "Получение групп пользователя успешно", response.getContent());
                    }
                }
            }
            return new Response(1, "Исключение при получении групп пользователя", groups);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(-1, "Исключение при получении групп пользователя", null);
        }
    }

    @Override
    public Response getCurrentUser(String token) {
        try {
            AuthUser authUser = JwtUtils.parseJWT(token);
            return new Response(1, "Получение текущего пользователя успешно", JSON.toJSONString(authUser));
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(-1, "Исключение при получении текущего пользователя", null);
        }
    }

    @Override
    public Response findUserByName(String name) {
        try {
            User user = userRepository.findByUserName(name);
            AuthUser authUser = new AuthUser(user.getUserId(), user.getUserName(), user.getUserNickName(), Common.getCurrentTime());
            return new Response(1, "Получение информации о пользователе успешно", JSON.toJSONString(authUser));
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(-1, "Исключение при получении информации о пользователе", null);
        }
    }

    @Override
    public Response findUserById(String id) {
        try {
            User user = userRepository.findByUserId(Integer.valueOf(id));
            AuthUser authUser = new AuthUser(user.getUserId(), user.getUserName(), user.getUserNickName(), Common.getCurrentTime());
            return new Response(1, "Получение информации о пользователе успешно", JSON.toJSONString(authUser));
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(-1, "Исключение при получении информации о пользователе", null);
        }
    }

    @Override
    @Transactional
    public Response updateUserRelation(Integer userIdA, Integer userIdB) {
        try {
            User userA = userRepository.findByUserId(userIdA);
            User userB = userRepository.findByUserId(userIdB);
            if (Common.isEmpty(userA.getUserRelations())) {
                userA.setUserRelations(String.valueOf(userB.getUserId()));
            } else {
                userA.setUserRelations(userA.getUserRelations() + "," + userB.getUserId());
            }
            userRepository.save(userA);
            if (Common.isEmpty(userB.getUserRelations())) {
                userB.setUserRelations(String.valueOf(userA.getUserId()));
            } else {
                userB.setUserRelations(userB.getUserRelations() + "," + userA.getUserId());
            }
            userRepository.save(userB);
            return new Response(1, "Добавление дружеских отношений успешно", null);
        } catch (Exception e) {
            return new Response(-1, "Ошибка при обновлении дружеских отношений", null);
        }

    }

    @Override
    public Response getUsersByUserIds(String userIds) {
        try {
            String[] users = userIds.split(",");
            List<AuthUser> userList = new ArrayList<>();
            for (String userId : users) {
                //TODO оптимизация для избежания использования цикла
                User user = userRepository.findByUserId(Integer.valueOf(userId));
                AuthUser authUser = new AuthUser(user.getUserId(), user.getUserName(), user.getUserNickName(), Common.getCurrentTime());
                userList.add(authUser);
            }
            return new Response(1, "Получение пользователей успешно", JSONArray.toJSONString(userList));
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(-1, "Ошибка при получении пользователей", null);
        }
    }

    @Override
    public Response updateUserGroup(Integer userId, Integer groupId) {
        try {
            User user = userRepository.findByUserId(userId);
            if (Common.isEmpty(user.getUserGroups())) {
                user.setUserGroups(String.valueOf(groupId));
            } else {
                user.setUserGroups(user.getUserGroups() + "," + groupId);
            }
            userRepository.save(user);
            return new Response(1, "Обновление групп пользователя успешно", JSONArray.toJSONString(user));
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(-1, "Ошибка при обновлении групп пользователя", null);
        }
    }
}
