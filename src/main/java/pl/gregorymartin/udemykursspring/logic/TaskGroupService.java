package pl.gregorymartin.udemykursspring.logic;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import pl.gregorymartin.udemykursspring.model.TaskGroup;
import pl.gregorymartin.udemykursspring.model.TaskGroupRepository;
import pl.gregorymartin.udemykursspring.model.TaskRepository;
import pl.gregorymartin.udemykursspring.model.projection.GroupReadModel;
import pl.gregorymartin.udemykursspring.model.projection.GroupWriteModel;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequestScope
public class TaskGroupService {
    private TaskGroupRepository repository;
    private TaskRepository taskRepository;

    public TaskGroupService(final TaskGroupRepository repository, final TaskRepository taskRepository) {
        this.repository = repository;
        this.taskRepository = taskRepository;
    }

    public GroupReadModel createGroup(GroupWriteModel groupWriteModel){
        TaskGroup result = repository.save(groupWriteModel.toGroup());
        return new GroupReadModel(result);
    }

    public List<GroupReadModel> readAll(){
        return repository.findAll().stream()
                .map(GroupReadModel::new)
                .collect(Collectors.toList());
    }


    public void toggleGroup(int groupId){
        if (taskRepository.existsByDoneIsFalseAndGroup_Id(groupId)){
            throw new IllegalStateException("Group has undone tasks. Done all the tasks first");
        }
        TaskGroup result = repository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("TaskGroup with given id not found"));

        result.setDone(!result.isDone());
        repository.save(result);
    }

}
