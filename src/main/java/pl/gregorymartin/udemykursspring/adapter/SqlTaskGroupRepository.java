package pl.gregorymartin.udemykursspring.adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.gregorymartin.udemykursspring.model.TaskGroup;
import pl.gregorymartin.udemykursspring.model.TaskGroupRepository;

import java.util.List;

@Repository
interface SqlTaskGroupRepository extends TaskGroupRepository, JpaRepository<TaskGroup,Integer> {

    @Override
    @Query("from TaskGroup g join fetch g.tasks")
    List<TaskGroup> findAll();
}
