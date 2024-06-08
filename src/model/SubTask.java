package model;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    private int epicId;

    public SubTask(int taskId, String taskName, String taskDescription, TaskStatus status, int epicId, LocalDateTime startTime, Duration duration) {
        super(taskId, taskName, taskDescription, status,startTime,duration);
        this.epicId = epicId;
    }

    public SubTask(int taskId, String taskName, String taskDescription, TaskStatus status, int epicId) {
        super(taskId, taskName, taskDescription, status,LocalDateTime.now(), Duration.ofMinutes(15));
        this.epicId = epicId;
    }

    public SubTask(String taskName, String taskDescription, TaskStatus status, int epicId,LocalDateTime startTime, Duration duration) {
        super(taskName, taskDescription, status, startTime,duration);
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                " Epic:" + this.epicId +
                ", ID=" + this.getTaskId() +
                ", name='" + this.getTaskName() + '\'' +
                ", description='" + this.getTaskDescription() + '\'' +
                ", status=" + status + '\'' +
                ", StartTime=" + startTime + '\'' +
                ", EndTime=" + endTime +
                '}';
    }

    public void setEpicId(int epicId) {
       this.epicId = epicId;
    }

    @Override
    public Integer getEpicId() {
       return epicId;
    }

    public  TaskType getTaskType() {
        return TaskType.SubTask;
    }

  }
