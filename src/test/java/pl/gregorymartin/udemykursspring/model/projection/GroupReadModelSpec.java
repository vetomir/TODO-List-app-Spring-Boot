package pl.gregorymartin.udemykursspring.model.projection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.gregorymartin.udemykursspring.model.Task;
import pl.gregorymartin.udemykursspring.model.TaskGroup;

import java.beans.PropertyEditorSupport;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class GroupReadModelSpec {
    @Test
    @DisplayName("should create null deadline for group when no task deadlines")
    void contructor_noDeadlines_createNullDeadline(){
        //given
        TaskGroup source = new TaskGroup();
        Set<Task> taskSet = new HashSet<>();
        taskSet.add(new Task("bar", null));
        source.setTasks(taskSet);
        source.setDescription("food");

        //when
        GroupReadModel result = new GroupReadModel(source);

        //then
        assertThat(result).hasFieldOrPropertyWithValue("deadline" , null);


    }

}
