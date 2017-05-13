package capstone.entity.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by FPT on 3/24/2017.
 */

@Entity
public class BackupSchedule {

    /**
     * The location of the batchfile that contains the commands for backup procedures
     */
    public static final String BATCH_FILE = "res/backup/BackupSchedule.bat";

    /**
     * The id of the backup schedule
     */
    @Id
    private int id;

    /**
     * The type of the backup schedule, either ONCE, DAILY, WEEKLY, or MONTHLY
     */
    @Enumerated(value = EnumType.STRING)
    private BackupType type;

    /**
     * The startDate of the backup
     */
    @Column(columnDefinition = "TIMESTAMP")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date startDate;

    /**
     * The startTime of the backup
     */
    private String startTime;

    protected BackupSchedule(){}

    public BackupSchedule(BackupType type, Date startDate, String startTime){
        this.id = -1;
        this.type = type;
        this.startDate = startDate;
        this.startTime = startTime;
    }

    /**
     * Method to get the id of the backupSchedule
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * Method to set the id of the BackupSchedule
     * @param id the id of the BackupSchedule
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Method to get the type of the BackupSchedule
     * @return the backupType of the BackupSchedule
     */
    public BackupType getType() {
        return type;
    }

    /**
     * Method to set the backupType of the BackupSchedule
     * @param type the type of the BackupSchedule
     */
    public void setType(BackupType type) {
        this.type = type;
    }

    /**
     * Method to get the startDate of the BackupSchedule
     * @return the startDate of the BackupSchedule
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Method to set the startSate of the BackupSchedule
     * @param runTime the startDate of the BackupSchedule
     */
    public void setStartDate(Date runTime) {
        this.startDate = runTime;
    }

    /**
     * Method to get the startTime of the BacjupSchedule
     * @return the startTime of the BackupSchedule
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * Method to set the startTime of the BackupSchedule
     * @param startTime the startTime of the BackupSchedule
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}
