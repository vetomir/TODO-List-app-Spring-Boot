package pl.gregorymartin.udemykursspring.contoller;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.gregorymartin.udemykursspring.TaskConfigurationProperties;

@RestController
public class InfoController {
    private DataSourceProperties dataSourceProperties;
    private TaskConfigurationProperties taskConfigurationProperties;

    public InfoController(final DataSourceProperties dataSourceProperties, final TaskConfigurationProperties taskConfigurationProperties) {
        this.dataSourceProperties = dataSourceProperties;
        this.taskConfigurationProperties = taskConfigurationProperties;
    }

    @GetMapping("/info/url")
    String url(){
        return dataSourceProperties.getUrl();
    }
    @GetMapping("/info/prop")
    boolean myProp(){
        return taskConfigurationProperties.getTemplate().isAllowMultipleTasks();
    }

}
