package pl.gregorymartin.udemykursspring.logic;

import org.springframework.stereotype.Service;
import pl.gregorymartin.udemykursspring.TaskConfigurationProperties;
import pl.gregorymartin.udemykursspring.model.*;
import pl.gregorymartin.udemykursspring.model.projection.GroupReadModel;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    private ProjectRepository projectRepository;
    private TaskGroupRepository taskGroupRepository;
    TaskConfigurationProperties taskConfigurationProperties;

    public ProjectService(final ProjectRepository projectRepository, final TaskGroupRepository taskGroupRepository, final TaskConfigurationProperties taskConfigurationProperties) {
        this.projectRepository = projectRepository;
        this.taskGroupRepository = taskGroupRepository;
        this.taskConfigurationProperties = taskConfigurationProperties;
    }

    public List<Project> readAll(){
        return projectRepository.findAll();
    }

    public Project create(Project source){
        Project result = projectRepository.save(source);
        return source;
    }

    public GroupReadModel createGroup(LocalDateTime deadline, int projectId){
        if(!taskConfigurationProperties.getTemplate().isAllowMultipleTasks()
            && taskGroupRepository.existsByDoneIsFalseAndProject_Id(projectId)){
            throw new IllegalStateException("Only one undone group from project is allowed");
        }
        TaskGroup result = projectRepository.findById(projectId)
                .map(project -> {
                    TaskGroup targetGroup = new TaskGroup();
                    targetGroup.setDescription(project.getDescription());
                    targetGroup.setTasks(project.getSteps().stream()
                            .map(step -> new Task(deadline.plusDays(step.getDaysToDeadline()), step.getDescription()))
                            .collect(Collectors.toSet())
                    );
                    targetGroup.setProject(project);
                    return taskGroupRepository.save(targetGroup);
                    }).orElseThrow(() -> new IllegalArgumentException("Project with given id not found"));
        return new GroupReadModel(result);
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
