package pl.gregorymartin.udemykursspring.contoller;

import com.sun.org.apache.regexp.internal.RE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.gregorymartin.udemykursspring.model.Task;
import pl.gregorymartin.udemykursspring.model.TaskRepository;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
class TaskController {
    private Logger logger = LoggerFactory.getLogger(TaskController.class);

    private final TaskRepository repository;



    TaskController(final TaskRepository repository) {
        this.repository = repository;
    }

    @GetMapping(value = "/tasks", params = {"!sort", "!page", "!size"})
    ResponseEntity<List<Task>> readAllTasks(){
        logger.warn("Exposing all the tasks");
        return ResponseEntity.ok(repository.findAll());
    }
    @GetMapping("/tasks")
    ResponseEntity<List<Task>> readAllTasks(Pageable page){
        logger.info("Custom pageable");
        return ResponseEntity.ok(repository.findAll(page).getContent());
    }

    @GetMapping("/tasks/{id}")
    ResponseEntity<Task> readTaskById(@PathVariable Integer id){
        logger.info("Custom pageable");
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
/*
    @PostMapping("/tasks")
    ResponseEntity<?> addTask2( @RequestBody @Valid Task toAdd ) {
        Task result = repository.save(toAdd);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(toAdd);
    }*/

    @PostMapping("/tasks")
    ResponseEntity<?> addTask( @RequestBody @Valid Task toAdd ) {
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(toAdd.getId())
                .toUri();

        repository.save(toAdd);
        return ResponseEntity.created(location).body(toAdd);
    }

    @PutMapping("/tasks/{id}")
    ResponseEntity<Task> updateTask(@RequestBody @Valid Task task, @PathVariable Integer id){
        if(!repository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        repository.findById(id)
                .ifPresent(x -> {
                    x.updateFrom(task);
                    repository.save(x);
                });
        return ResponseEntity.noContent().build();
    }
    @Transactional
    @PatchMapping("/tasks/{id}")
    public ResponseEntity<?> toggleTask(@PathVariable int id){
        if (!repository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        repository.findById(id)
                .ifPresent(x -> x.setDone(!x.isDone()));

        return ResponseEntity.ok().build();
    }


}
