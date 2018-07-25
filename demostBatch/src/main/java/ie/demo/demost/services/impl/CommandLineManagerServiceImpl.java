package ie.demo.demost.services.impl;

import ie.demo.demost.services.CommandLineManagerService;
import ie.demo.demost.services.EventManagerService;
import ie.demo.demost.services.EventManagerServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

@Service("commandLineManager")
public class CommandLineManagerServiceImpl implements CommandLineManagerService {

    private static final Logger logger = LoggerFactory.getLogger(CommandLineManagerServiceImpl.class);
    private static final boolean IS_DEBUG = logger.isDebugEnabled();

    @Autowired
    private EventManagerService eventManager;

    @Override
    public void execCommandLineApp() throws IOException {
        if (IS_DEBUG) logger.debug("Starting comand line application");

        try (Scanner commandLine = new Scanner(System.in);) {


            String filename = null;
            boolean fileExist = false;
            System.out.println("----------------------------Json Lines file importer----------------------------\n");

            do {
                System.out.print("Select Json file path :\n");

                filename = commandLine.nextLine();
                if ((filename == null) || (filename.trim().equals(""))) {
                    System.out.println("File path cannot be empty\n");
                    continue;

                }
                Path path = Paths.get(filename.trim());
                fileExist = Files.exists(path);

                if (!fileExist) {
                    System.out.println("File :" + filename + " does not exists\n select a valid path please\n");
                    continue;
                }

                if (Files.isDirectory(path)) {
                    fileExist = false;
                    System.out.println("File :" + filename + " is a directory\n select a valid path please\n");
                    continue;
                }


            } while (!fileExist);
            try {
                this.eventManager.storeEvents(filename);
            } catch (EventManagerServiceException e) {
                logger.error("Error :" + e.getMessage(), e);
            }
            if (IS_DEBUG) logger.debug("Ending comand line application");

        }

    }
}