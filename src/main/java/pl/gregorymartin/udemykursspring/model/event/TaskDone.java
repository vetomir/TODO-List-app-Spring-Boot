package pl.gregorymartin.udemykursspring.model.event;

import pl.gregorymartin.udemykursspring.model.Task;

import java.time.Clock;

public class TaskDone extends TaskEvent{
    TaskDone(final Task source) {
        super(source.getId(), Clock.systemDefaultZone());
    }
}
