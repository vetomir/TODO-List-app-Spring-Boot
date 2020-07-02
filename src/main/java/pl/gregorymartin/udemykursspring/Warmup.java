package pl.gregorymartin.udemykursspring;

import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import pl.gregorymartin.udemykursspring.model.Task;
import pl.gregorymartin.udemykursspring.model.TaskGroup;
import pl.gregorymartin.udemykursspring.model.TaskGroupRepository;

import java.util.HashSet;
import java.util.Set;

@Component
class Warmup implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(Warmup.class);

    private final TaskGroupRepository taskGroupRepository;

    public Warmup(final TaskGroupRepository taskGroupRepository) {
        this.taskGroupRepository = taskGroupRepository;
    }

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent contextRefreshedEvent) {
        logger.info("Application warmup after context refreshed");
        final String description = "ApplicationContextEvent";

        if (!taskGroupRepository.existsByDescription(description)){
            logger.info("No required group found! Adding it!");
            //

            TaskGroup group = new TaskGroup();

            group.setDescription(description);

            Set<Task> taskSet = new HashSet<>();
            taskSet.add(new Task("ContextClosedEvent", null, group));
            taskSet.add(new Task("ContextRefreshedEvent", null, group));
            taskSet.add(new Task("ContextStoppedEvent", null, group));
            taskSet.add(new Task("ContextStartedEvent", null, group));
            //

            group.setTasks(taskSet);
            taskGroupRepository.save(group);
        }


    }
}
