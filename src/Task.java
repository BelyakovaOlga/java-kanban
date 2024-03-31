public class Task {
    int       taskId;
    String    name;
    String    description;
    TaskStatus status;
    public Task(int taskId, String taskName,String taskDescription){
        this.taskId = taskId;
        this.name = taskName;
        this.description = taskDescription;
    }

    @Override
    public String toString() {
        return "Task{" +
                " ID=" + taskId +
                ", Название ='" + name + '\'' +
                ", Описание ='" + description + '\'' +
                ", Статус=" + status +
                '}';
    }
    public TaskType getTaskType(){
        return TaskType.TASK;
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
}
