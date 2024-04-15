package model;

import java.util.ArrayList;
public class Epic extends Task {
    private  ArrayList<SubTask> subTasksList = new ArrayList<>();
    public Epic(int  taskId, String taskName,String taskDescription){
        super(taskId,taskName,taskDescription,TaskStatus.NEW);
    }
    public Epic(String taskName,String taskDescription, TaskStatus status){
        super(taskName,taskDescription,status);
    }
    public Epic(String taskName,String taskDescription) {super(taskName,taskDescription,TaskStatus.NEW); }
    @Override
    public String toString() {
        String epicWithSubTask;
        epicWithSubTask = "EPIC{" +
                " ID=" + this.getTaskId() +
                ", name='" + this.getTaskName() + '\'' +
                ", description='" + this.getTaskDescription() + '\'' +
                ", status=" + status +
                '}' ;

        for (SubTask subTask : subTasksList) {
            epicWithSubTask = epicWithSubTask + " \n      " + subTask.toString();
        }
        epicWithSubTask = epicWithSubTask + " \n ";
        return epicWithSubTask;
    }
    public ArrayList<SubTask> getSubTasksList() {
        return this.subTasksList;
    }
    public void addSubTask(SubTask  subTask){
        ArrayList<SubTask> subTasksListNew = this.subTasksList;
        if (subTasksListNew == null){
            subTasksListNew = new ArrayList<SubTask>();
        }
        subTasksListNew.add(subTask);
    }
    public void removeSubTask(SubTask  subTask){
        ArrayList<SubTask> subTasksListNew = this.subTasksList;
        if (subTasksListNew != null){
            subTasksListNew.remove(subTask);
        }
    }
    public void removeSubAllSubTask() {
        ArrayList<SubTask> subTasksListNew = this.subTasksList;
        if (subTasksListNew != null){
            subTasksListNew.clear();
        }
    }
    public TaskStatus calulateEpicStatus() {
        ArrayList<SubTask> subTasksList = this.subTasksList;
        TaskStatus status;
        for (SubTask subTask : subTasksList) {
            if (subTask.getTaskStatus() != TaskStatus.DONE) {
                return TaskStatus.NEW;
            }
        }
        return TaskStatus.DONE;
    }
}
