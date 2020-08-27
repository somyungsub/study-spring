package io.ssosso.springsecuritypractice1.service;


import io.ssosso.springsecuritypractice1.domain.entity.Resources;

import java.util.List;

public interface ResourcesService {

    Resources getResources(long id);

    List<Resources> getResources();

    void createResources(Resources Resources);

    void deleteResources(long id);
}