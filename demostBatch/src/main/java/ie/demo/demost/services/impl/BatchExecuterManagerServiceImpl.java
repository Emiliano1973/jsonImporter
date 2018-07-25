package ie.demo.demost.services.impl;

import ie.demo.demost.services.BatchExecuterManagerService;
import ie.demo.demost.services.EventManagerService;
import ie.demo.demost.services.EventManagerServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class BatchExecuterManagerServiceImpl implements BatchExecuterManagerService {

    private static final Logger logger = LoggerFactory.getLogger(BatchExecuterManagerServiceImpl.class);
    private static final boolean IS_DEBUG = logger.isDebugEnabled();

    @Value("${event.app.json.toWork.path}")
    private String sourceDirName;

    @Value("${event.app.json.worked.path}")
    private String workedFilesDirName;

    @Autowired
    private EventManagerService eventManager;


    @Override
    public void execute() {
        final File sourceDir = new File(sourceDirName);
        String[] subFlieNames = sourceDir.list();
        try {
            for (String filename : subFlieNames) {
                manageJsonFile(filename);

            }
        } catch (EventManagerServiceException ex) {

            logger.error("error in import batch" + ex.getMessage(), ex);
            throw new RuntimeException("error in import batch" + ex.getMessage(), ex);
        } catch (IOException ex) {
            logger.error("error moving the  worked files  from" + sourceDirName + " to " + workedFilesDirName, ex);
            throw new RuntimeException("error moving the  worked files  from" + sourceDirName + " to " + workedFilesDirName, ex);
        }

    }

    /**
     * Check if the file is already locked by another process
     *
     * @param filename
     * @return
     */
    private boolean isFileLock(String filename) {
        File file = new File(filename);
        boolean isLock = true;
        if (file.exists()) {


            FileLock lock = null;
            try (RandomAccessFile rf = new RandomAccessFile(file, "rw");
                 FileChannel fileChannel = rf.getChannel();) {
                // let us try to get a lock. If file already has an exclusive lock by another process

                if (lock != null) {
                    isLock = false;
                }
            } catch (Exception ex) {
                logger.error("Error when checkFileLock: " + ex);
            }
        } else {
            logger.warn("File is not exist");
        }
        return isLock;
    }


    /**
     * Check if it's a valid file
     *
     * @param filename
     * @return
     */
    private synchronized boolean isValid(String filename) {
        Path path = Paths.get(filename.trim());
        boolean fileExist = Files.exists(path);

        if (!fileExist) {
            logger.warn("File :" + filename + " does not exists\n select a valid path please\n");
            return false;
        }

        if (Files.isDirectory(path)) {
            logger.warn("File :" + filename + " is a directory\n select a valid path please\n");
            return false;
        }

        if (!isFileLock(filename)) {
            logger.warn("The file " + filename + " is already open");
            return false;
        }

        return true;
    }


    private  void manageJsonFile(String filename) throws EventManagerServiceException, IOException {
        StringBuilder pathNamesBuilder = new StringBuilder();
        String filenameWithPath = pathNamesBuilder.append(sourceDirName)
                .append(File.separator).append(filename).toString();
        pathNamesBuilder.setLength(0);
        if (isValid(filenameWithPath)) {
            if (IS_DEBUG) logger.debug("Executing import from :" + filenameWithPath);
            eventManager.storeEvents(filenameWithPath);
            if (IS_DEBUG) logger.debug("Executed import from :" + filenameWithPath);
            String workedFileNameWithPath = pathNamesBuilder.append(workedFilesDirName)
                    .append(File.separator).append(filename).toString();
            pathNamesBuilder.setLength(0);
            Files.move(Paths.get(filenameWithPath), Paths.get(workedFileNameWithPath));
        }
    }
}
