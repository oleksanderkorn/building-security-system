***Run Project Locally***
```
./gradlew bootRun
```

***Building Security System***
You are going to build a system that represents a building with security gates and a guards.

Tested skills are:
Working in Java environment.
Design of the code.
Division of the code to logical sections and layers.
Using prebuilt Java classes and data structures wisely.
Using proper design pattern.
Parallel programming.

Notes
Using code templates from web is ok, but the final code should look like part of the system and not collections of codes from the internet. 
So if you do use code templates, make them looks like part of your system (In terms of consistent variable names, shape, division to sections, etc..) and remove unnecessary code and packages.
This way the task checker understands that you know what’s inside the code you use, and not only takes a working code.

You can use external library to implement the file management, but the system structure musn’t change.
It means for example if a method of parsing the file became a library one-line function call, you should keep the original method, 
and call the library function inside (Do not discard this method because it is not necessary any more or too short).
The structure and the division of the code to functions and layers are important. 

Our building is secured by gates and guards.
All the gates has 3 main properties:
Id (unique int)
Display name
State (open/close)


You should load the data of the gates and guards from a data file (use plain text, csv, xml, your choice).
For running,  fill the file with randomly data for 10-15 gates (the ID numbers shouldn’t be sorted nore serialized).
Example for ids in the file: 10, 5, 8, 6, 15, 100, 50, 60, 70, 90

All the gates are operated by guards who patrol in the building.
The first guard is going out to patrol every 15 seconds.
His job is to close all the even gates from the smallest Id number to the biggest one.
The second guard randomly chooses a gate and then goes over all the gates 
(from the smallest Id number to the biggest one and return to the smallest if began from the middle) 
when he skips every second gate, and toggle their state (close if open, open if close).
His patrol should happened every 10 seconds.
The third guard randomly chooses 2 gate and toggles them.
This happened every 7 seconds


Pay attention! 
The security department of the building consulting about changing the numbers of guards and gates next year. Maybe the guards patrol behavior can be changed too.
So you should plan your system in a way it won’t cost a lot of money to update the version you write right now.

Every time a guard arrives to a gate during his patrol, a message about it should be written to the security event log of the building (you can use the console, don’t implement a robust event logging system).
The message should include the guard and gate details, and the status of the gate before and after the arrival.

If you think about issues and bugs can happened but solving them is too much work for this kind of task, describe them and their solution in attached file.


***Design***

System is build on two main models Gate and Guard. 
And i would suggest to add another model that will be called `Building`,
that will be like a connector between `Gates` and `Guards`, both of them should have relation to `Building`. 
Models data structure is described below:
Building has `id` and has `Address` that provides the location of a building: 
```
public class Building {
    int id;
    Address address;
}

class Address {
    String city;
    String country;
    String street;
    String streetNumber;
}

```

Gate has constructor to instantiate it with `id`, `displayName` and initial `state`. 
And it has public setter method to update it's `state` by guard. 
```
public class Gate {
    private int id;
    private String displayName;
    private State state;
    
    @ManyToOne
    public Building building;

    public Gate() {
    }

    public Gate(int id, String displayName, State state) {
        this.id = id;
        this.displayName = displayName;
        this.state = state;
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

    enum State {
        OPEN,
        CLOSED
    }
}
```

Guard has `id` to identify itself and `patrolInterval` in seconds, 
with what he know's how often he need to patrol the gates. 
To describe the behaviour of every guard we use additional fields:
- to define starting position we use enum `startingPosition`
- to define permission to `open`, `close` or `toggle` we use enum `GatePermission`.
- to define allowance of gates to patrol we use enum `GatesAllowed`.
- to define the direction of `Guard` we use enum `direction`
- if direction is `RANDOM_GATES` we need to set the amount of gates to pick with additional field `gatesAmount`  

The behaviour of each `Guard` is quite complicated and it's a mix of different fields,
so it would be good to implement `Builder` pattern like `GuardBuilder` that will provide methods to add new Guards with specific behaviour. 
It can be inner class inside Guard. And it should contain constructor to have mandatory fields, and additional methods to fill in additional fields,
that will help to describe the behaviour of the concrete Guard.  

```
public class Guard {
    int id;
    int patrolInterval;
    StartingPosition startingPosition;
    GatePermission permission;
    GatesAllowed gatesAllowed;
    Direction direction;
    int gatesAmount;
    
    @ManyToOne
    public Building building;

    enum StartingPosition {
        BOTTOM,
        TOP,
        RANDOM
    }

    enum GatePermission {
        OPEN,
        CLOSE,
        TOGGLE
    }

    enum GatesAllowed {
        EVEN,
        ODD,
        ALL
    }

    enum Direction {
        TOP_TO_BOTTOM,
        BOTTOM_TO_TOP,
        RANDOM_GATES
    }

    public Guard(int id, int patrolInterval, StartingPosition startingPosition, GatePermission permission, GatesAllowed gatesAllowed) {
        this.id = id;
        this.patrolInterval = patrolInterval;
        this.startingPosition = startingPosition;
        this.permission = permission;
        this.gatesAllowed = gatesAllowed;
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

    public GatePermission getPermission() {
        return permission;
    }

    public GatesAllowed getGatesAllowed() {
        return gatesAllowed;
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

    public void setPermission(GatePermission permission) {
        this.permission = permission;
    }

    public void setGatesAllowed(GatesAllowed gatesAllowed) {
        this.gatesAllowed = gatesAllowed;
    }

    public void setGatesAmount(int gatesAmount) {
        this.gatesAmount = gatesAmount;
    }
}

```

Next we should have a `Service` layer, that should contain business logic inside itself.

So we can have `GuardService` class that will have methods, to load Guards and to make them patroling.   

To load the `Guards`  we need to delegate a request from service layer (`GuardService`) to DAO (Data Access Object) layer, like `GuardDao`.
`GuardDao` will load existing guards from some storage e.g. database, json file, xml file etc and return it as a list. To have support of different buildings, 
we can have a method like `GuardService.findGuardsByBuilding(Building building)` that will pass the building inside `GuardDao`, that will do a query to the datasource, 
and pass `building.id` as a parameter, to fetch guards related to specific building. 

To load the `Gates` we can have the similar structure the service `GateService` and the DAO `GateDao` to load gate for specific building by passing `building.id`.  

It will be fine to use relational database in this case, because we have an entity Building that is like a parent, and also we have Guard and Gate entities, 
that will have relation to Building by having Building.id as a foreign key.

For the `Building` class itself we should have also corresponding `BuildingService`. It will have load the building from the database. 

And we can have `SecurityService`, ito which we will inject all `BuildingService`, `GuardService` and `GateService`, and it will do the work. 
With such architecture, it will be easy and fast to operate with the data, to fetch and store it, and if necessary to update it.
And everything will be separated, and own service will have it's own responsibility.

To log security event's we will have additional service called `SecurityLoggingService`, 
that will have methods to log event's when guard arrives to the specific gate. 

To work with intervals we can have a job called `GuardPatrolingJob`, based on Quartz Job Scheduler library (http://www.quartz-scheduler.org/), 
which allows to easily run the jobs with specific intervals, that we need.

If the amount of gates or guards should be extended next year, they can be added to the datastore using `GuardService/GuardDao` and `GateService/GateDao` with some methods to `create/update/delete` an entity. 

Also new Building can be added into the system using `BuildingService`, if Security System will work in a new buildings.

Example of workflow:
1. Application starts. Main class has injected `SecurityService`, that loads the current building using `BuildService`.
2. Next step would be to load all the `Gates` and `Guards`, by calling the corresponding services.  
3. Then when we have all data loaded, we can call `GuardService` to do the work by passing the guard, 
and a list of gates into the method `GuardService.startPatroling(Guard guard, List<Gate> gates)`. 
4. Inside `GuardService.startPatroling` will be detected the behaviour of a guard, and the time interval of patroling, 
and according to these values, it will start working, by running the Job instance with specific time period.  