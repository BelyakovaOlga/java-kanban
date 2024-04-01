package dataModel;
import dataModel.Task;
import enumTask.TaskType;

public class SubTask extends Task {
    public int epicTaskId;
    public SubTask(int taskId, String taskName,String taskDescription, int epicTaskId){
        super(taskId,taskName,taskDescription);
        this.epicTaskId = epicTaskId;
    }
    public TaskType getTaskType(){
        return TaskType.SUBTASK;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "позадача Epic:" + epicTaskId +
                "  ID=" + taskId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
