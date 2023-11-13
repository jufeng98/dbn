package com.dbn.debugger.jdwp;

import com.dbn.common.compatibility.Compatibility;
import com.intellij.debugger.engine.DebugProcess;
import com.intellij.debugger.engine.DebugProcessImpl;
import com.intellij.debugger.engine.events.DebuggerCommandImpl;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public abstract class ManagedThreadCommand extends DebuggerCommandImpl{
    private final Priority priority;

    @Compatibility
    private ManagedThreadCommand(Priority priority) {
        //super(priority); // backward compatibility
        super();
        this.priority = priority;
    }

    @Override
    public Priority getPriority() {
        return super.getPriority();
    }

    public static void schedule(DebugProcess debugProcess, Priority priority, Runnable action) {
        ManagedThreadCommand command = create(priority, action);
        ((DebugProcessImpl) debugProcess).getManagerThread().schedule(command);
    }


    public static void invoke(DebugProcess debugProcess, Priority priority, Runnable action) {
        ManagedThreadCommand command = create(priority, action);
        ((DebugProcessImpl) debugProcess).getManagerThread().invoke(command);
    }

    @NotNull
    private static ManagedThreadCommand create(Priority priority, Runnable action) {
        return new ManagedThreadCommand(priority) {
            @Override
            protected void action() throws Exception {
                action.run();
            }
        };
    }
}
