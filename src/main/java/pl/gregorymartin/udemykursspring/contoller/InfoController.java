package pl.gregorymartin.udemykursspring.contoller;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.gregorymartin.udemykursspring.TaskConfigurationProperties;

@RestController
@RequestMapping("/info")
class InfoController {
    private DataSourceProperties dataSource;
    private TaskConfigurationProperties myProp;

    InfoController(final DataSourceProperties dataSource, final TaskConfigurationProperties myProp) {
        this.dataSource = dataSource;
        this.myProp = myProp;
    }

    @GetMapping("/url")
    String url() {
        return dataSource.getUrl();
    }

    @GetMapping("/prop")
    boolean myProp() {
        return myProp.getTemplate().isAllowMultipleTasks();
    }
}
