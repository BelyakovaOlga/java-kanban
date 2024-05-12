package model;

public class Task {
    private int taskId;
    private String name;
    private String description;
    protected TaskStatus status;

    public Task(int taskId, String taskName, String taskDescription, TaskStatus status) {
        this.taskId = taskId;
        this.name = taskName;
        this.description = taskDescription;
        this.status = status;
    }

    public Task(String taskName, String taskDescription, TaskStatus status) {
        this.name = taskName;
        this.description = taskDescription;
        this.status = status;
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
                ", status=" + status +
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
}
