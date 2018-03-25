package com.korniienko.buildingsecuritysystem.service;

import com.google.common.collect.Lists;
import com.korniienko.buildingsecuritysystem.dao.GuardDao;
import com.korniienko.buildingsecuritysystem.model.Gate;
import com.korniienko.buildingsecuritysystem.model.Guard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Author: Oleksandr Korniienko
 * Date: 3/24/18
 */
@Service
public class GuardService {

    private final GuardDao guardDao;
    private final SecurityLoggingService securityLoggingService;

    @Autowired
    public GuardService(GuardDao guardDao, SecurityLoggingService securityLoggingService) {
        this.guardDao = guardDao;
        this.securityLoggingService = securityLoggingService;
    }

    public List<Guard> findAll() {
        return guardDao.findAll();
    }

    public void startPatrolling(Guard guard, Collection<Gate> gates) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> patrollingJob(guard, gates), guard.getPatrolInterval(), guard.getPatrolInterval(), TimeUnit.SECONDS);
    }

    synchronized private void patrollingJob(Guard guard, Collection<Gate> gates) {
        securityLoggingService.logStartPatrolling(guard);
        if (Objects.equals(guard.getStartingPosition(), Guard.StartingPosition.BOTTOM)
                && Objects.equals(guard.getGatesAllowed(), Guard.GatesAllowed.EVEN)) {
            for (Gate gate : gates) {
                Gate.State oldState = gate.getState();
                if (gate.getId() % 2 == 0 && Objects.equals(gate.getState(), Gate.State.OPEN)) {
                    gate.setState(Gate.State.CLOSED);
                    securityLoggingService.logSecurityEvent(guard, gate, oldState);
                }
            }
        } else if (Objects.equals(guard.getStartingPosition(), Guard.StartingPosition.RANDOM)
                && Objects.equals(guard.getDirection(), Guard.Direction.BOTTOM_TO_TOP)) {
            int randomPosition = new Random().nextInt(gates.size());
            int index = 0;
            Iterator<Gate> it = gates.iterator();
            while (it.hasNext()) {
                Gate gate = it.next();
                Gate.State oldState = gate.getState();
                if (index >= randomPosition && ((index % 2 == 0 && randomPosition % 2 == 0)
                        || (index % 2 != 0 && randomPosition % 2 != 0))) {
                    gate.toggleState();
                    securityLoggingService.logSecurityEvent(guard, gate, oldState);
                }
                index = index + 1;
            }
            it = gates.iterator();
            index = 0;
            while (it.hasNext()) {
                Gate gate = it.next();
                Gate.State oldState = gate.getState();
                if (index < randomPosition && ((index % 2 == 0 && randomPosition % 2 == 0)
                        || (index % 2 != 0 && randomPosition % 2 != 0))) {
                    gate.toggleState();
                    securityLoggingService.logSecurityEvent(guard, gate, oldState);
                }
                index = index + 1;
            }
        } else if (Objects.equals(guard.getStartingPosition(), Guard.StartingPosition.RANDOM) && guard.getGatesAmount() > 0) {
            ArrayList<Integer> randomIntegers = Lists.newArrayList();
            for (int i = 0; i < guard.getGatesAmount(); i++) {
                randomIntegers.add(new Random().nextInt(gates.size()));
            }
            Iterator<Gate> it = gates.iterator();
            int index = 0;
            while (it.hasNext()) {
                Gate gate = it.next();
                if (randomIntegers.contains(index)) {
                    Gate.State oldState = gate.getState();
                    gate.toggleState();
                    securityLoggingService.logSecurityEvent(guard, gate, oldState);
                }
                index = index + 1;
            }
        }
        securityLoggingService.logEndPatrolling(guard);
    }
}
