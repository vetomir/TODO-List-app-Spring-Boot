package pl.gregorymartin.udemykursspring.model.projection;

import pl.gregorymartin.udemykursspring.model.TaskGroup;

import java.security.acl.Group;
import java.util.Set;
import java.util.stream.Collectors;

public class GroupWriteModel {
    private String description;
    private Set<GroupTaskWriteModel> tasks;

    public TaskGroup toGroup(){
        TaskGroup result = new TaskGroup();
        result.setDescription(description);
        result.setTasks(
                tasks.stream().map(GroupTaskWriteModel::toTask)
                .collect(Collectors.toSet())
        );

        return result;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Set<GroupTaskWriteModel> getTasks() {
        return tasks;
    }

    public void setTasks(final Set<GroupTaskWriteModel> tasks) {
        this.tasks = tasks;
    }
}
