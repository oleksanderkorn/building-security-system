package com.korniienko.buildingsecuritysystem.model;

import java.util.Objects;

/**
 * Author: Oleksandr Korniienko
 * Date: 3/24/18
 */
public class Gate {
    private int id;
    private String displayName;
    private State state = State.CLOSED;

    public Gate() {
    }

    public int getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void toggleState() {
        if (Objects.equals(getState(), State.OPEN)) {
            setState(State.CLOSED);
        } else {
            setState(State.OPEN);
        }
    }

    public enum State {
        OPEN,
        CLOSED
    }

    @Override
    public String toString() {
        return "Gate{" +
                "id=" + id +
                ", displayName='" + displayName + '\'' +
                ", state=" + state +
                '}';
    }
}
