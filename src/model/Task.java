package model;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Task {
    private int taskId;
    private String name;
    private String description;
    protected TaskStatus status;
    protected Duration duration;
    protected LocalDateTime startTime;
    protected LocalDateTime endTime;

    public Task(int taskId, String taskName, String taskDescription, TaskStatus status, LocalDateTime startTime,Duration duration) {
        this.taskId = taskId;
        this.name = taskName;
        this.description = taskDescription;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
        this.endTime  = startTime.plus(duration.toMinutes(), ChronoUnit.MINUTES);
    }

    public Task(int taskId, String taskName, String taskDescription, TaskStatus status) {
        this.taskId = taskId;
        this.name = taskName;
        this.description = taskDescription;
        this.status = status;
        //this.startTime = startTime;
        //this.duration =  Duration.ofMinutes(15);;
        //this.endTime  = startTime.plus(duration.toMinutes(), ChronoUnit.MINUTES);
    }

    public Task(String taskName, String taskDescription, TaskStatus status,LocalDateTime startTime,Duration duration) {
        this.taskId = taskId;
        this.name = taskName;
        this.description = taskDescription;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
        this.endTime  = startTime.plus(duration.toMinutes(), ChronoUnit.MINUTES);
    }

    public Task(String taskName, String taskDescription) {
        this.name = taskName;
        this.description = taskDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task task)) return false;
        return taskId == task.taskId;
    }

    @Override
    public int hashCode() {
        return taskId;
    }

    @Override
    public String toString() {
        return "Task{" +
                " ID=" + taskId +
                ", name='" + name + '\'' +
                ", description '" + description + '\'' +
                ", status=" + status + '\'' +
                ", StartTime=" + startTime + '\'' +
                ", EndTime=" + endTime +
                '}';
    }

    public int getTaskId() {
        return taskId;
    }

    public String getTaskName() {
        return name;
    }

    public String getTaskDescription() {
        return description;
    }

    public TaskStatus getTaskStatus() {
        return status;
    }

    public Integer getEpicId() {
        return 0;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public TaskType getTaskType() {
        return TaskType.Task;
    }
    public LocalDateTime getStartTime() {
        return startTime;
    }
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public Duration getDuration() {
        return duration;
    }
}

