package com.korniienko.buildingsecuritysystem.component;

import com.korniienko.buildingsecuritysystem.model.Gate;
import com.korniienko.buildingsecuritysystem.model.Guard;
import com.korniienko.buildingsecuritysystem.service.GateService;
import com.korniienko.buildingsecuritysystem.service.GuardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;

/**
 * Author: Oleksandr Korniienko
 * Date: 3/24/18
 */
@Component
public class SecuritySystem {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final GuardService guardService;
    private final GateService gateService;

    @Autowired
    public SecuritySystem(GuardService guardService, GateService gateService) {
        this.guardService = guardService;
        this.gateService = gateService;
    }

    @PostConstruct
    public void startPatroling() {
        log.info("Start Patroling");
        Collection<Gate> gates = gateService.findAll();
        List<Guard> guards = guardService.findAll();
        guards.forEach(guard -> guardService.startPatrolling(guard, gates));
    }
}
