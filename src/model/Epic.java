package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<SubTask> subTasksList;

    public Epic(int taskId, String taskName, String taskDescription) {
        super(taskId, taskName, taskDescription, TaskStatus.NEW);
        this.endTime = null;
    }

    public Epic(int taskId, String taskName, String taskDescription, TaskStatus status) {
        super(taskId, taskName, taskDescription, status);
        this.endTime = null;
    }

    public Epic(String taskName, String taskDescription) {
        super(taskName, taskDescription);
        this.duration = null;
        this.endTime = null;
    }
    public Epic(int taskId,String taskName, String taskDescription, TaskStatus status,LocalDateTime startTime,Duration duration) {
        super(taskId,taskName, taskDescription,status,startTime,duration);
        this.endTime = null;
    }
    @Override
    public String toString() {
        String epicWithSubTask;
        epicWithSubTask = "EPIC{" +
                " ID=" + this.getTaskId() +
                ", name='" + this.getTaskName() + '\'' +
                ", description='" + this.getTaskDescription() + '\'' +
                ", status=" + status + + '\'' +
                ", StartTime=" + startTime + '\'' +
                ", EndTime=" + endTime +
                '}';
        return epicWithSubTask;
    }

    public ArrayList<SubTask> getSubTasksList() {
        return this.subTasksList;
    }

    public void addSubTask(SubTask subTask) {
        if (this.subTasksList == null) {
            this.subTasksList = new ArrayList<SubTask>();
        }
        this.subTasksList.add(subTask);
    }

    public void removeSubTask(SubTask subTask) {
        if (this.subTasksList != null) {
            this.subTasksList.remove(subTask);
        }
    }

    public void removeSubAllSubTask() {
        if (this.subTasksList != null) {
            this.subTasksList.clear();
        }
    }
    public void calculateEpicFields() {
        Duration   localDuration = Duration.ofMinutes(0);

        for (SubTask subTask : this.subTasksList) {
            if (subTask.getTaskStatus() != TaskStatus.DONE) {
                this.status = TaskStatus.NEW;
            }
            if (this.endTime == null) {
                this.endTime = subTask.endTime;
            }
            if (this.startTime == null) {
                this.startTime = subTask.startTime;
            }
            if (this.duration == null) {
                this.duration = subTask.duration;
            }
            if (subTask.startTime.isBefore(this.startTime)) {
                this.startTime = subTask.getStartTime();
            }
            if (subTask.endTime.isAfter(this.endTime)) {
                this.endTime =  subTask.getEndTime();
            }

            localDuration = localDuration.plus(subTask.duration);
        }
        this.duration = localDuration;
    }
    @Override
    public TaskType getTaskType() {

        return TaskType.Epic;
    }
}
