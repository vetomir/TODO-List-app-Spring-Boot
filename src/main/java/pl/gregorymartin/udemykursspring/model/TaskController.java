package pl.gregorymartin.udemykursspring.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RepositoryRestController
class TaskController {
    private Logger logger = LoggerFactory.getLogger(TaskController.class);

    private final TaskRepository taskRepository;

    TaskController(final TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping
    ResponseEntity<?> reakAllTasks(){
        logger.warn("Exposing all the tasks");
        return ResponseEntity.ok(taskRepository.findAll());
    }
    

}
