package com.nt.ntuas.inboundgateway.telegram.api.model;

import java.util.ArrayList;
import java.util.List;

public class Command {

    private final String action;

    private final String argument;

    public Command(String action, String argument) {
        this.action = action;
        this.argument = argument;
    }

    public String getAction() {
        return action;
    }

    public String getArgument() {
        return argument;
    }

    @Override
    public String toString() {
        return "Command{" +
                "action='" + action + '\'' +
                ", argument='" + argument + '\'' +
                '}';
    }
}
