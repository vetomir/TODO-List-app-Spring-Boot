package pl.gregorymartin.udemykursspring.logic;

import pl.gregorymartin.udemykursspring.TaskConfigurationProperties;
import pl.gregorymartin.udemykursspring.model.*;
import pl.gregorymartin.udemykursspring.model.projection.GroupReadModel;
import pl.gregorymartin.udemykursspring.model.projection.GroupTaskWriteModel;
import pl.gregorymartin.udemykursspring.model.projection.GroupWriteModel;
import pl.gregorymartin.udemykursspring.model.projection.ProjectWriteModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
public class ProjectService {
    private ProjectRepository repository;
    private TaskGroupRepository taskGroupRepository;
    private TaskGroupService taskGroupService;
    private TaskConfigurationProperties config;

    public ProjectService(final ProjectRepository repository, final TaskGroupRepository taskGroupRepository, final TaskGroupService taskGroupService, final TaskConfigurationProperties config) {
        this.repository = repository;
        this.taskGroupRepository = taskGroupRepository;
        this.taskGroupService = taskGroupService;
        this.config = config;
    }

    public List<Project> readAll() {
        return repository.findAll();
    }

    public Project save(final ProjectWriteModel toSave) {
        return repository.save(toSave.toProject());
    }

    public GroupReadModel createGroup(LocalDateTime deadline, int projectId) {
        if (!config.getTemplate().isAllowMultipleTasks() && taskGroupRepository.existsByDoneIsFalseAndProject_Id(projectId)) {
            throw new IllegalStateException("Only one undone group from project is allowed");
        }
        return repository.findById(projectId)
                .map(project -> {
                    GroupWriteModel targetGroup = new GroupWriteModel();
                    targetGroup.setDescription(project.getDescription());
                    targetGroup.setTasks(
                            project.getSteps().stream()
                                    .map(projectStep -> {
                                        GroupTaskWriteModel task = new GroupTaskWriteModel();
                                                task.setDescription(projectStep.getDescription());
                                                task.setDeadline(deadline.plusDays(projectStep.getDaysToDeadline()));
                                                return task;
                                            }
                                    ).collect(Collectors.toList())
                    );
                    return taskGroupService.createGroup(targetGroup, project);
                }).orElseThrow(() -> new IllegalArgumentException("Project with given id not found"));
    }

    /*public GroupReadModel createGroup(int projectId, LocalDateTime deadline){
        if(!taskGroupRepository.existsByDoneIsFalseAndProject_Id(projectId) ||
                ( taskGroupRepository.existsByDoneIsFalseAndProject_Id(projectId)
                && taskConfigurationProperties.getTemplate().isAllowMultipleTasks()) ){

            Project project = projectRepository.findById(projectId).get();
            TaskGroup result = new TaskGroup();

            result.setProject(project);
            result.setDescription(project.getDescription());

            Set<Task> newTasks = new HashSet<>();

            project.getSteps().stream()
                    .map(step -> newTasks
                            .add(new Task(
                                    deadline.minusDays(
                                            step.getDaysToDeadline()),step.getDescription()
                                    )
                            ));
            result.setTasks(newTasks);
            return new GroupReadModel(result);
        }
        else
            throw new IllegalStateException("Project Multiplication is not allowed");
    }*/
}
