package pl.gregorymartin.udemykursspring.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class Task extends BaseTask{
    private LocalDateTime deadline;

    @ManyToOne
    @JoinColumn(name = "task_group_id")
    private TaskGroup group;


    public Task() {
    }

    public Task(final LocalDateTime deadline, String description) {
        this.description = description;
        this.deadline = deadline;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(final LocalDateTime deadline) {
        this.deadline = deadline;
    }

    TaskGroup getGroup() {
        return group;
    }

    public void setGroup(final TaskGroup group) {
        this.group = group;
    }

    public void updateFrom(Task source) {
        description = source.description;
        done = source.done;
        deadline = source.deadline;
        group = source.group;
    }
}

