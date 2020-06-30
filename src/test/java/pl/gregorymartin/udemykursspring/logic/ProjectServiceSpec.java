package pl.gregorymartin.udemykursspring.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.gregorymartin.udemykursspring.TaskConfigurationProperties;
import pl.gregorymartin.udemykursspring.model.*;
import pl.gregorymartin.udemykursspring.model.projection.GroupReadModel;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectServiceSpec {


    @Test
    @DisplayName("should throw IllegalStateException when configured to allow just 1 group and the other undone group exists")
    void createGroup_noMultipleGroupsConfig_And_undoneGroupExist_throwsIllegalStateException() {
        //given
        TaskGroupRepository mockGroupRepository  = groupRepositoryReturning(true);
        //
        TaskConfigurationProperties mockConfig = configurationReturning(false);
        //
        ProjectService toTest = new ProjectService(null,mockGroupRepository, null,mockConfig);

        //when
        Throwable exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));

        //then
        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("one undone group");
    }


    @Test
    @DisplayName("should throw IllegalArgumentException when configuration ok and no projects for a given id")
    void createGroup_configurationOk_And_noProjects_throwsIllegalArgumentException() {
        //given
        ProjectRepository mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());
        //
        TaskConfigurationProperties mockConfig = configurationReturning(true);
        //system under test
        ProjectService toTest = new ProjectService(mockRepository,null, null,mockConfig);
        //when
        Throwable exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));

        //then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");
    }


    @Test
    @DisplayName("should throw IllegalArgumentException when configured to allow just 1 group and no groups and projects for a given id")
    void createGroup_noMultipleGroupsConfig_And_undoneGroupExists_noProject_throwsIllegalArgumentException() {
        //given
        ProjectRepository mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());
        //
        TaskGroupRepository mockGroupRepository = groupRepositoryReturning(false);
        //
        TaskConfigurationProperties mockConfig = configurationReturning(true);
        //system under test
        ProjectService toTest = new ProjectService(mockRepository,mockGroupRepository, null,mockConfig);
        //when
        Throwable exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));

        //then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");
    }

    private TaskGroupRepository groupRepositoryReturning(final boolean b) {
        TaskGroupRepository mockGroupRepository = mock(TaskGroupRepository.class);
        when(mockGroupRepository.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(b);

        return mockGroupRepository;
    }

    @Test
    @DisplayName("should create a new group from project")
    void createGroup_configurationOk_existingProject_createAndSavesGroup(){
        //given
        LocalDateTime today = LocalDate.now().atStartOfDay();


        Set<Integer> ex= new HashSet<>();
        ex.add(-1);
        ex.add(-2);
        Project project = projectWith("bar", ex);
        //
        ProjectRepository mockRepository = mock(ProjectRepository.class);

        when(mockRepository.findById(anyInt()))
                .thenReturn(Optional.of(project));
        //
        InMemoryGroupRepository inMemoryGroupRepo = inMemoryGroupRepository();
        TaskGroupService serviceWithInMemRepo = dummyGroupService(inMemoryGroupRepo);
        int countBeforeCall = inMemoryGroupRepository().count();
        //
        TaskConfigurationProperties mockConfig = configurationReturning(true);

        // system under test
        ProjectService toTest = new ProjectService(mockRepository,inMemoryGroupRepo, serviceWithInMemRepo,mockConfig);

        //when
        GroupReadModel result = toTest.createGroup(today, 1);

        //then
        assertThat(result.getDescription()).isEqualTo("bar");
        assertThat(result.getDeadline()).isEqualTo(today.minusDays(1));
        assertThat(result.getTasks()).allMatch(task -> task.getDescription().equals("foo"));
        assertThat(countBeforeCall + 1)
                .isEqualTo(inMemoryGroupRepo.count());
    }

    private Project projectWith(String projectDescription, Set<Integer> daysToDeadline) {
        Set<ProjectStep> steps = daysToDeadline.stream()
                .map(days -> {
                    ProjectStep step = mock(ProjectStep.class);
                    when(step.getDescription()).thenReturn("foo");
                    when(step.getDaysToDeadline()).thenReturn(days);
                    return step;
                }).collect(Collectors.toSet());

        Project result = mock(Project.class);

        when(result.getDescription()).thenReturn(projectDescription);
        when(result.getSteps()).thenReturn(
                steps
        );
        return result;
    }

    private TaskConfigurationProperties configurationReturning(final boolean b) {
        TaskConfigurationProperties.Template mockTemplate = mock(TaskConfigurationProperties.Template.class);
        when(mockTemplate.isAllowMultipleTasks()).thenReturn(b);
        //
        TaskConfigurationProperties mockConfig = mock(TaskConfigurationProperties.class);
        when(mockConfig.getTemplate()).thenReturn(mockTemplate);
        return mockConfig;
    }

    //

    private TaskGroupService dummyGroupService(final InMemoryGroupRepository inMemoryGroupRepo) {
        return new TaskGroupService(inMemoryGroupRepo, null);
    }

    //

    private InMemoryGroupRepository inMemoryGroupRepository(){ return new InMemoryGroupRepository(); }

    private static class InMemoryGroupRepository implements TaskGroupRepository{
            private int index = 0;
            private Map<Integer,TaskGroup> map = new HashMap<>();
            @Override
            public List<TaskGroup> findAll() {
                return new ArrayList<>(map.values());
            }

            public int count() {
                return map.values().size();
            }

            @Override
            public Optional<TaskGroup> findById(final Integer id) {
                return Optional.ofNullable(map.get(id));
            }

            @Override
            public TaskGroup save(final TaskGroup entity) {
                if(entity.getId() == 0){
                    try {
                        Field field = TaskGroup.class.getSuperclass().getDeclaredField("id");
                        field.setAccessible(true);
                        field.set(entity,++index);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
                map.put(entity.getId(),entity);

                return entity;
            }

            @Override
            public boolean existsByDoneIsFalseAndProject_Id(final Integer projectId) {
                return map.values().stream()
                        .filter(group -> !group.isDone())
                        .anyMatch(group -> group.getProject() != null && group.getProject().getId() == projectId);
            }
        }

}
