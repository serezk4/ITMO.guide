package com.serezk4.command;

import java.util.List;

public class Active {
    public static final List<Command> LIST = List.of(
            new Add(),
            new Clear(),
            new ExecuteScript(),
            new Exit(),
            new Head(),
            new PrintFieldDescendingHairColor(),
            new RemoveById(),
            new RemoveFirst(),
            new RemoveGreater(),
            new Save(),
            new Show(),
            new SumOfHeight()
    );
}
