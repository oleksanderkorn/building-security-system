package com.korniienko.buildingsecuritysystem.service;

import com.korniienko.buildingsecuritysystem.dao.GateDao;
import com.korniienko.buildingsecuritysystem.model.Gate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Author: Oleksandr Korniienko
 * Date: 3/24/18
 */
@Service
public class GateService {

    private final GateDao gateDao;

    @Autowired
    public GateService(GateDao gateDao) {
        this.gateDao = gateDao;
    }

    public Collection<Gate> findAll() {
        List<Gate> gates = gateDao.findAll();
        gates.sort(Comparator.comparing(Gate::getId));
        return Collections.synchronizedCollection(gates);
    }

}
