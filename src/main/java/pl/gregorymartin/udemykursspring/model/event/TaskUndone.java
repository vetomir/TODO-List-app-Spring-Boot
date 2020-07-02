package pl.gregorymartin.udemykursspring.model.event;

import pl.gregorymartin.udemykursspring.model.Task;

import java.time.Clock;

public class TaskUndone extends TaskEvent{
    TaskUndone(final Task source) {
        super(source.getId(), Clock.systemDefaultZone());
    }
}
