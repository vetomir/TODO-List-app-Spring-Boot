package pl.gregorymartin.udemykursspring.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "task_groups")
public class TaskGroup extends BaseTask{

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "group")
    private Set<Task> tasks;

    public TaskGroup() {
    }


    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(final Set<Task> tasks) {
        this.tasks = tasks;
    }
}

