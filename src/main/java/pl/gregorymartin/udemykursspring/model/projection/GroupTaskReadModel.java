package pl.gregorymartin.udemykursspring.model.projection;

import pl.gregorymartin.udemykursspring.model.Task;

public class GroupTaskReadModel {
    private String description;
    private boolean done;

    public GroupTaskReadModel(Task source) {
        this.description = source.getDescription();
        this.done = source.isDone();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(final boolean done) {
        this.done = done;
    }
}
