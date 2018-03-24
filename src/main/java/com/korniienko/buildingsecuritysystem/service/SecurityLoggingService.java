package com.korniienko.buildingsecuritysystem.service;

import com.korniienko.buildingsecuritysystem.model.Gate;
import com.korniienko.buildingsecuritysystem.model.Guard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Author: Oleksandr Korniienko
 * Date: 3/24/18
 */
@Service
public class SecurityLoggingService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    void logSecurityEvent(Guard guard, Gate gate, Gate.State oldState) {
        log.info("New patrol event. Guard[" + guard.getId() + "] at Gate[" + gate.getId() + "] " + oldState.name() + " => " + gate.getState());
    }

    void logStartPatrolling(Guard guard) {
        log.info("<========Guard[" + guard.getId() + "] starts patrolling.========>");
    }

    void logEndPatrolling(Guard guard) {
        log.info("<========Guard[" + guard.getId() + "] finish patrolling.========>");
    }
}
