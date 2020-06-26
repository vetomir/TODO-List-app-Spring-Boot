package pl.gregorymartin.udemykursspring.logic;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.*;
import pl.gregorymartin.udemykursspring.TaskConfigurationProperties;
import pl.gregorymartin.udemykursspring.model.ProjectRepository;
import pl.gregorymartin.udemykursspring.model.TaskGroupRepository;
import pl.gregorymartin.udemykursspring.model.TaskRepository;

@Configuration
class LogicConfiguration {
    @Bean
    ProjectService projectService(
            final ProjectRepository repository,
            final TaskGroupRepository taskGroupRepository,
            final TaskConfigurationProperties configurationProperties
            ){
        return new ProjectService(repository,taskGroupRepository,configurationProperties);
    }
    @Bean
    TaskGroupService taskGroupService(
            final TaskGroupRepository taskGroupRepository,
            final TaskRepository taskRepository
    ){
        return new TaskGroupService(taskGroupRepository,taskRepository);
    }

}
