package pl.gregorymartin.udemykursspring.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@MappedSuperclass
class Audit {

    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;

    @PrePersist
    void prePersist(){
        createdOn = LocalDateTime.now();
    }
    @PreUpdate
    void preMerge(){
        updatedOn = LocalDateTime.now();
    }
}
