import java.util.Scanner;
public class ConsoleForTest{
    TaskManager taskManager;
    Scanner scanner = new Scanner(System.in);
    public void setTaskManager(TaskManager taskManager) {
        this.taskManager = taskManager;
    }
    public void run() {
        while (true) {
            printMenu();
            String actionTask = scanner.nextLine();
            if (actionTask.isEmpty())
                actionTask = scanner.nextLine();

            switch (ActionTaskManager.valueOf(actionTask)) {
                case ADDTask:
                    int epicId = 0;

                    System.out.println("Укажите тип задачи // TASK // EPIC // SUBTASK //");
                    String taskType = scanner.nextLine();
                    System.out.println("Укажите название задачи:");
                    String nameTask = scanner.nextLine();
                    System.out.println("Укажите описание задачи:");
                    String descrTask = scanner.nextLine();
                    switch(TaskType.valueOf(taskType)){
                        case TaskType.TASK:
                            Task task = taskManager.createTask(nameTask, descrTask);
                            System.out.println("Задача создана, ей присвоен Id: " + task.getTaskId());
                            break;
                        case TaskType.EPIC:
                            Epic epic = taskManager.createEpic(nameTask, descrTask);
                            System.out.println("Задача создана, ей присвоен Id: " + epic.getTaskId());
                            break;
                        case TaskType.SUBTASK:
                            System.out.println("Укажите номер Эпика:");
                            epicId = scanner.nextInt();
                            if (taskManager.getTask(epicId) == null || taskManager.getTask(epicId).getTaskType() != TaskType.EPIC) {
                                System.out.println("Эпика с номером " + epicId + " в трекере нет");
                                break;
                            }
                            SubTask subTask = taskManager.createSubTask(nameTask, descrTask, epicId);
                            System.out.println("Подзадача для Эпика " + epicId + " создана, ей присвоен Id: " + subTask.getTaskId());
                            break;
                    }
                    break;
                case GETTASK:
                    System.out.println("Укажите номер:");
                    int taskId = scanner.nextInt();
                    Task taskCurrent = taskManager.getTask(taskId);
                    if (taskCurrent == null) {
                        System.out.println("Задачи с номером " + taskId + " в трекере нет");
                    } else {
                        taskCurrent = taskManager.getTask(taskId);
                        System.out.println(taskCurrent.toString());
                    }
                    break;
                case GETALL:
                    taskManager.writeTasks();
                    break;
                case UPDTASK:
                    System.out.println("Укажите номер:");
                    String statusNew = "";
                    int taskIdUpd = scanner.nextInt();
                    Task taskUpd = taskManager.getTask(taskIdUpd);
                    SubTask subTaskUpd;
                    if (taskUpd == null) {
                        System.out.println("Задачи с номером " + taskIdUpd + " в трекере нет");
                    } else {
                        System.out.println("Укажите новое название задачи:");
                        String nameUpd = scanner.nextLine();
                        if (nameUpd.isEmpty()) {
                            nameUpd = scanner.nextLine();
                        }
                        System.out.println("Укажите новое описание задачи:");
                        String descrUpd = scanner.nextLine();
                        switch(taskManager.getTask(taskIdUpd).getTaskType()) {
                            case TaskType.TASK:
                                System.out.println("Укажите новый статус: // NEW // DONE //");
                                statusNew = scanner.nextLine();
                                Task task = taskManager.updateTask(taskIdUpd, nameUpd, descrUpd, TaskStatus.valueOf(statusNew));
                                break;
                            case TaskType.SUBTASK:
                                System.out.println("Укажите новый статус: // NEW // DONE //");
                                statusNew = scanner.nextLine();
                                subTaskUpd = taskManager.updateSubTask(taskIdUpd, nameUpd, descrUpd, TaskStatus.valueOf(statusNew));
                                break;
                            case TaskType.EPIC:
                                subTaskUpd= taskManager.updateSubTask(taskIdUpd, nameUpd, descrUpd, TaskStatus.valueOf(statusNew));
                                break;
                        }
                    }

                    break;
                case DELTASK:
                    System.out.println("Укажите номер:");
                    int taskIdDel = scanner.nextInt();
                    switch(taskManager.getTask(taskIdDel).getTaskType()) {
                        case TaskType.TASK:
                           taskManager.deleteTask(taskIdDel);
                           break;
                        case TaskType.EPIC:
                            taskManager.deleteEpic(taskIdDel);
                            break;
                        case TaskType.SUBTASK:
                            taskManager.deleteSubTask(taskIdDel);
                            break;
                    }
                    break;
                case DELLALL:
                    taskManager.deleteTasks();
                    break;
                case EXIT:
                    return;
            }
        }
    }
    private static void printMenu() {
        System.out.println("Выберите действие, указав Engl. наименование");
        System.out.println("// НовЗадача // ПолучитьПоID // ОбновитьПоID //  СисокВсех // УдадитьПоID // УдалитьВсе // Выход");
        System.out.println("// ADDTask   //    GETTASK   //   UPDTASK    //    GETALL  //   DELTASK   //   DELLALL  // EXIT");
    }
}
