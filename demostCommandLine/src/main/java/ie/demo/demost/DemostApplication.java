package ie.demo.demost;

import ie.demo.demost.services.CommandLineManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemostApplication implements CommandLineRunner {

    @Autowired
    @Qualifier("commandLineManager")
    private CommandLineManagerService commandLineManager;

    @Value("${command.line.enabled}")
    private Boolean isCommandLineEnabled;

    public static void main(String[] args) {
        SpringApplication.run(DemostApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (isCommandLineEnabled)
            commandLineManager.execCommandLineApp();

    }
}
