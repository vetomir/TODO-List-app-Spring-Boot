package pl.gregorymartin.udemykursspring.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.gregorymartin.udemykursspring.model.TaskGroup;
import pl.gregorymartin.udemykursspring.model.TaskGroupRepository;
import pl.gregorymartin.udemykursspring.model.TaskRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskGroupServiceSpec {

    @Test
    @DisplayName("should throw IllegalStateException when any undone task in group is not existing")
    void toggleGroup_then_taskGroup_have_notUndone_tasks_throwsIllegalStateException() {
        //given

        TaskRepository mockTaskRepository = taskRepositoryReturning(true);
        //
        TaskGroupService toTest = new TaskGroupService(null,mockTaskRepository);

        //when
        Throwable exception = catchThrowable(() -> toTest.toggleGroup(anyInt()));

        //then
        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("has undone tasks");
    }

    @Test
    @DisplayName("should throw IllegalStateException when any task groups is not existing")
    void toggleGroup_then_taskGroup_notExists_throwsIllegalArgumentException() {
        //given
        TaskGroupRepository mockGroupRepository  = mock(TaskGroupRepository.class);
        when(mockGroupRepository.findById(anyInt())).thenReturn(Optional.empty());
        TaskRepository mockTaskRepository = mock(TaskRepository.class);
        //
        TaskGroupService toTest = new TaskGroupService(mockGroupRepository,mockTaskRepository);

        //when
        Throwable exception = catchThrowable(() -> toTest.toggleGroup(anyInt()));

        //then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("given id not found");
    }

    @Test
    @DisplayName("should change group's 'done' variable")
    void toggleGroup_then_conditionsOk() {
        TaskGroup group = new TaskGroup();
        group.setDone(false);
        //given
        TaskGroupRepository mockGroupRepository  = mock(TaskGroupRepository.class);
        when(mockGroupRepository.findById(999)).thenReturn(Optional.of(group));
        //
        TaskRepository mockTaskRepository = taskRepositoryReturning(false);
        //
        TaskGroupService toTest = new TaskGroupService(mockGroupRepository,mockTaskRepository);
        //when
        toTest.toggleGroup(999);
        //then
        assertThat(mockGroupRepository.findById(999)
                .get().isDone()).isTrue();


    }

    private TaskRepository taskRepositoryReturning(final boolean b) {
        TaskRepository taskRepository = mock(TaskRepository.class);
        when(taskRepository.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(b);
        return taskRepository;
    }
}
