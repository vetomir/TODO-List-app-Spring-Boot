package pl.gregorymartin.udemykursspring.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    List<Task> findAll();
    Page<Task> findAll(Pageable page);

    List<Task> findByDone(@Param( "state" ) boolean done);

    Optional<Task> findById(Integer id);

    boolean existsById(Integer id);

    boolean existsByDoneIsFalseAndGroup_Id(Integer groupId);

    //

    Task save(Task entity);

    void delete(Task x);

    void deleteById(Integer id);

    List<Task> findAllByGroup_Id(Integer groupId);
}
