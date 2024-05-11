package model;

import java.util.ArrayList;
public class Epic extends Task {
    private  ArrayList<SubTask> subTasksList;
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

        /*if (subTasksList != null) {
            for (SubTask subTask : subTasksList) {
                epicWithSubTask = epicWithSubTask + " \n      " + subTask.toString();
            }
        }
        epicWithSubTask = epicWithSubTask + " \n ";*/
        return epicWithSubTask;
    }
    public ArrayList<SubTask> getSubTasksList() {
        return this.subTasksList;
    }
    public void addSubTask(SubTask  subTask){
        if (this.subTasksList == null){
            this.subTasksList = new ArrayList<SubTask>();
        }
        this.subTasksList.add(subTask);
    }
    public void removeSubTask(SubTask  subTask){
        if (this.subTasksList != null){
            this.subTasksList.remove(subTask);
        }
    }
    public void removeSubAllSubTask() {
        if (this.subTasksList != null){
            this.subTasksList.clear();
        }
    }
    public TaskStatus calulateEpicStatus() {
        TaskStatus status;
        for (SubTask subTask : this.subTasksList) {
            if (subTask.getTaskStatus() != TaskStatus.DONE) {
                return TaskStatus.NEW;
            }
        }
        return TaskStatus.DONE;
    }
}
