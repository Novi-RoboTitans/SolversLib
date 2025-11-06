package com.seattlesolvers.solverslib.command;

import androidx.annotation.NonNull;

import com.seattlesolvers.solverslib.command.CommandBase;

import java.util.Arrays;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class LambdaCommand extends CommandBase {
    // Name, requirements, and subsystem are in CommandBase
    protected Runnable m_initialize = () -> {};
    protected Runnable m_execute = () -> {};
    protected BooleanSupplier m_isFinished = () -> true;
    protected Consumer<Boolean> m_end = interrupted -> {};
    protected boolean m_runWhenDisabled = false;

    /** Default constructor with empty actions and finishing immediately. */
    public LambdaCommand() {}

    /** Constructor with all actions specified. */
    public LambdaCommand(Runnable initialize,
                         Runnable execute,
                         BooleanSupplier isFinished,
                         Consumer<Boolean> end,
                         String name,
                         boolean runWhenDisabled) {
        this.m_initialize = initialize;
        this.m_execute = execute;
        this.m_isFinished = isFinished;
        this.m_end = end;
        this.m_name = name;
        this.m_runWhenDisabled = runWhenDisabled;
    }

    /** Constructor with all actions specified. */
    public LambdaCommand(Runnable initialize,
                         Runnable execute,
                         BooleanSupplier isFinished,
                         Runnable end,
                         String name,
                         boolean runWhenDisabled) {
        this.m_initialize = initialize;
        this.m_execute = execute;
        this.m_isFinished = isFinished;
        this.m_end = interrupted -> end.run();
        this.m_name = name;
        this.m_runWhenDisabled = runWhenDisabled;
    }

    /** Called once when the command is scheduled. */
    @Override
    public void initialize() {
        if (m_initialize != null) {
            m_initialize.run();
        }
    }

    /** Called repeatedly until finished. */
    @Override
    public void execute() {
        if (m_execute != null) {
            m_execute.run();
        }
    }

    /** Returns true if the command has finished. */
    @Override
    public boolean isFinished() {
        return m_isFinished != null && m_isFinished.getAsBoolean();
    }

    /** Called once after finishing or when interrupted. */
    @Override
    public void end(boolean interrupted) {
        if (m_end != null) {
            m_end.accept(interrupted);
        }
    }

    @Override
    public boolean runsWhenDisabled() {
        return m_runWhenDisabled;
    }

    // Override to return self type
    @Override
    public LambdaCommand addRequirements(Subsystem... requirements) {
        m_requirements.addAll(Arrays.asList(requirements));
        return this;
    }

    // Override to return self type
    @Override
    public LambdaCommand setName(String name) {
        m_name = name;
        return this;
    }

    public LambdaCommand setInitialize(Runnable initialize) {
        this.m_initialize = initialize;
        return this;
    }

    public LambdaCommand setExecute(Runnable execute) {
        this.m_execute = execute;
        return this;
    }

    public LambdaCommand setIsFinished(BooleanSupplier isFinished) {
        this.m_isFinished = isFinished;
        return this;
    }

    public LambdaCommand setEnd(Consumer<Boolean> end) {
        this.m_end = end;
        return this;
    }

    public LambdaCommand setEnd(Runnable end) {
        this.m_end = interrupted -> end.run();
        return this;
    }

    /** Allows setting whether this command runs when the robot is disabled. */
    public LambdaCommand setRunWhenDisabled(boolean runWhenDisabled) {
        this.m_runWhenDisabled = runWhenDisabled;
        return this;
    }

    /** Factory method to create a LambdaCommand from a CommandBase. */
    public static LambdaCommand from(CommandBase command) {
        return new LambdaCommand(
                command::initialize,
                command::execute,
                command::isFinished,
                command::end,
                command.getName(),
                command.runsWhenDisabled()
        );
    }
}
