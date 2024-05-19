package ru.rsreu.userrelationservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ru.rsreu.commonservice.util.Response;
import ru.rsreu.userrelationservice.constant.CONSTANT;
import ru.rsreu.userrelationservice.domain.UserRelation;
import ru.rsreu.userrelationservice.domain.prikey.UserRelationPriKey;
import ru.rsreu.userrelationservice.repository.UserRelationRepository;
import ru.rsreu.userrelationservice.service.UserRelationService;

import java.sql.Timestamp;
import java.util.Date;

@Slf4j
@Service
public class UserRelationServiceImpl implements UserRelationService {

    @Autowired
    UserRelationRepository userRelationRepository;

    @Autowired
    RestTemplate restTemplate;

    /**
     * Добавление дружеских отношений между двумя пользователями по их ID и обновление их кэша друзей.
     *
     * @param userIdA
     * @param userIdB
     * @return
     */
    @Override
    @Transactional
    public Response buildRelation(Integer userIdA, Integer userIdB) {
        log.info("Я устанавливаю отношения между " + userIdA + " и " + userIdB);
        UserRelation userRelation = new UserRelation();
        userRelation.setUserIdA(userIdA);
        userRelation.setUserIdB(userIdB);
        userRelation.setRelationStatus(1);
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        userRelation.setRelationStart(timestamp);
        userRelationRepository.save(userRelation);
        // Здесь используется удаленный сетевой вызов, пока используем синхронный способ. Если возникнут проблемы с производительностью, можно рассмотреть асинхронный способ.
        MultiValueMap<String, Integer> requestEntity = new LinkedMultiValueMap<>();
        requestEntity.add("userIdA", userIdA);
        requestEntity.add("userIdB", userIdB);
        ResponseEntity<Response> responseEntity =
                restTemplate.postForEntity(CONSTANT.USER_SERVICE_UPDATE_RELATION, requestEntity, Response.class);
        Response response = responseEntity.getBody();
        if (response.getStatus() == 1) {
            return new Response(1, "Добавление друга успешно", null);
        } else {
            userRelationRepository.deleteById(new UserRelationPriKey(userIdA, userIdB));
            return new Response(-1, "Ошибка при добавлении друга", null);
        }
    }

    @Override
    public Response removeRelation(String jsonObj) {
        return null;
    }
}
