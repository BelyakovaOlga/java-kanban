package dataModel;
import dataModel.SubTask;
import dataModel.Task;
import enumTask.TaskType;
import java.util.ArrayList;
public class Epic extends Task {
    ArrayList<SubTask> subTasksList = new ArrayList<>();
    public Epic(int taskId, String taskName,String taskDescription){
        super(taskId,taskName,taskDescription);
    }
    public TaskType getTaskType(){
        return TaskType.EPIC;
    }
    @Override
    public String toString() {
        String strRes;
        strRes = "EPIC{" +
                " ID=" + taskId +
                ", Название ='" + name + '\'' +
                ", Описание ='" + description + '\'' +
                ", Статус=" + status +
                '}';

        for (SubTask subTask : subTasksList) {
            strRes = strRes + " \n      " + subTask.toString();
        }

        return strRes;
    }

    public void setSubTasksList(ArrayList<SubTask> subTasksList) {

        this.subTasksList = subTasksList;
    }
}
