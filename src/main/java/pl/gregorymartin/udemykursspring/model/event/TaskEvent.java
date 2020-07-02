package pl.gregorymartin.udemykursspring.model.event;

import pl.gregorymartin.udemykursspring.model.Task;

import java.time.Clock;
import java.time.Instant;

public abstract class TaskEvent {
    public static TaskEvent changed(Task source){
        return source.isDone() ? new TaskDone(source) : new TaskUndone(source);
    }

    private int taskId;
    private Instant occurrence;

    TaskEvent(final int taskId, Clock clock) {
        this.taskId = taskId;
        occurrence = Instant.now(clock);
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(final int taskId) {
        this.taskId = taskId;
    }

    public Instant getOccurrence() {
        return occurrence;
    }

    public void setOccurrence(final Instant occurrence) {
        this.occurrence = occurrence;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                "taskId=" + taskId +
                ", occurrence=" + occurrence +
                '}';
    }
}
