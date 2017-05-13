package capstone.service;

import capstone.entity.dao.BackupScheduleDao;
import capstone.entity.model.BackupSchedule;
import capstone.entity.model.BackupType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Adam Mandel, Derek Albano, Nina Tham, Phat Duong, and Ryan Harnett.
 * This class is developed for providing BackupSchedule objects stored in database to be accessed throughout the system.
 */
@Service
public class BackupScheduleService {

    /**
     * The instance of the BackupSchedule Data Access Object
     */
    private BackupScheduleDao backupScheduleDao;
    /**
     * The current directory of the project
     */
    private String currentDir;
    /**
     * The batch file that contains the command for schedule to backup procedures
     */
    public static File BATCH_FILE = new File("res/batch/Backup.bat");

    /**
     * The constructor that instantiate the attributes of this BackupScheduleService.
     * @param backupScheduleDao to be wired
     */
    @Autowired
    public BackupScheduleService(BackupScheduleDao backupScheduleDao){
        this.backupScheduleDao = backupScheduleDao;
        //Get the project directory
        File currentDirFile = new File(".");
        String helper = currentDirFile.getAbsolutePath();
        this.currentDir = helper.substring(0, helper.length() - 1);
    }

    /**
     * The method to get the BackupSchedule from the database. Null if there is none
     * @return the BackupSchedule from the database. Null if there is none
     */
    public BackupSchedule getBackupSchedule(){
        if(backupScheduleDao.scheduleExist()){
            return backupScheduleDao.get(1);
        } else {
            return null;
        }
    }

    /**
     * Method to create a new BackupSchedule
     * @param backupSchedule the BackupSchedule to be created
     * @return whether the BackupSchedule is successfully created
     */
    public boolean createBackupSchedule(BackupSchedule backupSchedule){
        if(backupSchedule == null){
            return false;
        }
        backupScheduleDao.updateOrInsert(backupSchedule);
        String createCommand = "";
        String batchFileWithParameters = "\\\""+ BATCH_FILE.getAbsolutePath()
                + "\\\" " + new SimpleDateFormat("yyyyMMdd-hhmmss").format(new Date())
                + " \\\"" + currentDir +"\"";
        String startDate = new SimpleDateFormat("MM/dd/yyyy").format(backupSchedule.getStartDate());
        if(backupSchedule.getType() == BackupType.DAILY){
            createCommand = "SchTasks /Create /SC DAILY /TN \"BackupSchedule\" /TR \"" + batchFileWithParameters + "\" /ST " + backupSchedule.getStartTime() + " /SD " + startDate + " /F";
        } else if (backupSchedule.getType() == BackupType.WEEKLY){
            String dayOfWeek = new SimpleDateFormat("EE").format(backupSchedule.getStartDate()).toUpperCase();
            createCommand = "SchTasks /Create /SC WEEKLY /D " + dayOfWeek + " /TN \"BackupSchedule\" /TR \"" + batchFileWithParameters + "\" /ST " + backupSchedule.getStartTime() + " /SD " + startDate + " /F";
        } else if (backupSchedule.getType() == BackupType.MONTHLY){
            createCommand = "SchTasks /Create /SC MONTHLY /D " + backupSchedule.getStartDate().getDate() + " /TN \"BackupSchedule\" /TR \"" + batchFileWithParameters + "\" /ST " + backupSchedule.getStartTime() + " /SD " + startDate + " /F";
        } else if (backupSchedule.getType() == BackupType.ONCE){
            createCommand = "SchTasks /Create /SC ONCE /TN \"BackupSchedule\" /TR \"" + batchFileWithParameters + "\" /ST " + backupSchedule.getStartTime() + " /SD " + startDate + " /F";
        } else {
            throw new IllegalArgumentException("Invalid BackupSchedule Type");
        }
        try {
            Runtime rt = Runtime.getRuntime();
            int returnCode = rt.exec(createCommand).waitFor();
            if(returnCode == 0){
                return true;
            } else {
                return false;
            }
        } catch (IOException|InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Method to delete the existed BackupSchedule
     * @return whether the BackupSchedule is successfully deleted
     */
    public boolean deleteBackupSchedule(){
        String deleteCommand = "Schtasks /delete /tn \"Backup\" /F";
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec(deleteCommand);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Method to start backing up the system now
     * @return whether the system has been successfully backed up
     */
    public boolean backupNow(){
        String command = "\""+ BATCH_FILE.getAbsolutePath()
                + "\" " + new SimpleDateFormat("yyyyMMdd-hhmmss").format(new Date())
                + " \"" + currentDir.substring(0, currentDir.length()-1) +"\"";
        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
            int returnCode = pb.start().waitFor();

            //int returnCode = runtime.exec(command).waitFor();
            if(returnCode == 0){
                return true;
            }else {
                return false;
            }
        } catch (IOException|InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * The method to get the list of the local backup files
     * @return the list of all the local backup files
     */
    public ArrayList<String> getBackupFile(){
        String[] names = new File("BACKUP").list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".zip");
            }
        });
        ArrayList<String> nameList =  new ArrayList<>(Arrays.asList(names));
        Collections.sort(nameList, Collections.reverseOrder());
        return nameList;
    }

}
