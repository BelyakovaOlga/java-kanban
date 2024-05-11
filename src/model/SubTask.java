package model;

public class SubTask extends Task {
    private int epicId;

    public SubTask(int taskId, String taskName, String taskDescription, TaskStatus status, int epicId) {
        super(taskId, taskName, taskDescription, status);
        this.epicId = epicId;
    }

    public SubTask(String taskName, String taskDescription, TaskStatus status, int epicId) {
        super(taskName, taskDescription, status);
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                " Epic:" + this.epicId +
                ", ID=" + this.getTaskId() +
                ", name='" + this.getTaskName() + '\'' +
                ", description='" + this.getTaskDescription() + '\'' +
                ", status=" + status +
                '}';
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public int getEpicId() {
        return this.epicId;
    }
}
