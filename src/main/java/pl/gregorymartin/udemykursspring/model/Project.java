package pl.gregorymartin.udemykursspring.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Table(name = "projects")
public
class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank(message = "Project description need to have description")
    private String description;

    @OneToMany(mappedBy = "project")
    private Set<TaskGroup> groups;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "project")
    private Set<ProjectStep> steps;


    public Project() {
    }

    public int getId() {
        return id;
    }

    void setId(final int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    Set<TaskGroup> getGroups() {
        return groups;
    }

    void setGroups(final Set<TaskGroup> groups) {
        this.groups = groups;
    }

    public Set<ProjectStep> getSteps() {
        return steps;
    }

    public void setSteps(final Set<ProjectStep> steps) {
        this.steps = steps;
    }
}
