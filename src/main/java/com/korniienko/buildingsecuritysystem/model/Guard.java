package com.korniienko.buildingsecuritysystem.model;

/**
 * Author: Oleksandr Korniienko
 * Date: 3/24/18
 */
public class Guard {
    private int id;
    private int patrolInterval;
    private StartingPosition startingPosition;
    private GatesAllowed gatesAllowed;
    private Direction direction;
    private int gatesAmount;

    public enum StartingPosition {
        BOTTOM,
        TOP,
        RANDOM
    }

    public enum GatesAllowed {
        EVEN,
        ODD,
        ALL
    }

    public enum Direction {
        TOP_TO_BOTTOM,
        BOTTOM_TO_TOP,
        RANDOM_GATES
    }

    Guard(int id, int patrolInterval, StartingPosition startingPosition, GatesAllowed gatesAllowed, Direction direction, int gatesAmount) {
        this.id = id;
        this.patrolInterval = patrolInterval;
        this.startingPosition = startingPosition;
        this.gatesAllowed = gatesAllowed;
        this.direction = direction;
        this.gatesAmount = gatesAmount;
    }

    public int getId() {
        return id;
    }

    public int getPatrolInterval() {
        return patrolInterval;
    }

    public StartingPosition getStartingPosition() {
        return startingPosition;
    }

    public GatesAllowed getGatesAllowed() {
        return gatesAllowed;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getGatesAmount() {
        return gatesAmount;
    }

    public void setPatrolInterval(int patrolInterval) {
        this.patrolInterval = patrolInterval;
    }

    public void setStartingPosition(StartingPosition startingPosition) {
        this.startingPosition = startingPosition;
    }

    public void setGatesAllowed(GatesAllowed gatesAllowed) {
        this.gatesAllowed = gatesAllowed;
    }

    public void setGatesAmount(int gatesAmount) {
        this.gatesAmount = gatesAmount;
    }

    public static class Builder {
        int id;
        int patrolInterval;
        StartingPosition startingPosition;
        GatesAllowed gatesAllowed;
        Direction direction;
        int gatesAmount;

        public Builder(int id, int patrolInterval) {
            this.id = id;
            this.patrolInterval = patrolInterval;
        }

        public Builder startingPosition(StartingPosition position) {
            this.startingPosition = position;
            return this;
        }

        public Builder gatesAllowed(GatesAllowed gatesAllowed) {
            this.gatesAllowed = gatesAllowed;
            return this;
        }

        public Builder direction(Direction direction) {
            this.direction = direction;
            return this;
        }

        public Builder gatesAmount(int gatesAmount) {
            this.gatesAmount = gatesAmount;
            return this;
        }

        public Guard build() {
            return new Guard(id, patrolInterval, startingPosition, gatesAllowed, direction, gatesAmount);
        }
    }

    @Override
    public String toString() {
        return "Guard{" +
                "id=" + id +
                ", patrolInterval=" + patrolInterval +
                ", startingPosition=" + startingPosition +
                ", gatesAllowed=" + gatesAllowed +
                ", direction=" + direction +
                ", gatesAmount=" + gatesAmount +
                '}';
    }
}