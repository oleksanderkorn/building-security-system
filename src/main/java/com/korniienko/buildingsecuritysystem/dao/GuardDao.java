package com.korniienko.buildingsecuritysystem.dao;

import com.google.common.collect.Lists;
import com.korniienko.buildingsecuritysystem.model.Guard;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Oleksandr Korniienko
 * Date: 3/24/18
 */
@Repository
public class GuardDao {

    public List<Guard> findAll() {
        return initializeGuards();
    }

    private List<Guard> initializeGuards() {
        ArrayList<Guard> guards = Lists.newArrayList();
        guards.add(new Guard.Builder(1, 15)
                .direction(Guard.Direction.BOTTOM_TO_TOP)
                .gatesAllowed(Guard.GatesAllowed.EVEN)
                .startingPosition(Guard.StartingPosition.BOTTOM)
                .build());

        guards.add(new Guard.Builder(2, 10)
                .direction(Guard.Direction.BOTTOM_TO_TOP)
                .gatesAllowed(Guard.GatesAllowed.EVEN)
                .startingPosition(Guard.StartingPosition.RANDOM)
                .build());

        guards.add(new Guard.Builder(3, 7)
                .direction(Guard.Direction.RANDOM_GATES)
                .gatesAllowed(Guard.GatesAllowed.ALL)
                .gatesAmount(2)
                .startingPosition(Guard.StartingPosition.RANDOM)
                .build());

        return guards;
    }

}
