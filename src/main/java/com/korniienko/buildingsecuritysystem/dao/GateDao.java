package com.korniienko.buildingsecuritysystem.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.korniienko.buildingsecuritysystem.model.Gate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * Author: Oleksandr Korniienko
 * Date: 3/24/18
 */
@Repository
public class GateDao {

    private static final String GATES_JSON_PATH = "gates.json";

    public List<Gate> findAll() {
        try {
            Resource resource = new ClassPathResource(GATES_JSON_PATH);
            InputStream is = resource.getInputStream();
            ObjectMapper objectMapper = new ObjectMapper();
            return Arrays.asList(objectMapper.readValue(is, Gate[].class));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return Lists.newArrayList();
    }
}
