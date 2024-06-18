package service;
import exception.MExceptionanagerSaveException;
import model.*;
import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import java.time.format.DateTimeFormatter;

public class FileBackedTaskManager extends InMemoryTaskManager {
    File file;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");

    public FileBackedTaskManager(HistoryManager historyManager, File file) {
        super(historyManager);
        this.file = file;
    }

    public FileBackedTaskManager(HistoryManager historyManager) {
        super(historyManager);
    }

    @Override
    public Task createTask(Task task) {
        super.createTask(task);
        save();
        return task;
    }

    @Override
    public Epic createEpic(Epic epic) {
        super.createEpic(epic);
        save();
        return epic;
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        super.createSubTask(subTask);
        save();
        return subTask;
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }

    @Override
    public void deleteSubTasks() {
        super.deleteSubTasks();
        save();
    }

    @Override
    public void deleteTask(int taskId) {
        super.deleteTask(taskId);
        save();
    }

    @Override
    public void deleteEpic(int taskId) {
        super.deleteEpic(taskId);
        save();
    }

    @Override
    public void deleteSubTask(int taskId) {
        super.deleteSubTask(taskId);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    private void save() {
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(this.headerToString());
            for (Map.Entry<Integer, Task> entry : tasks.entrySet()) {
                fileWriter.write(taskToString(entry.getValue()));
            }
            for (Map.Entry<Integer, Epic> entry : epics.entrySet()) {
                fileWriter.write(taskToString(entry.getValue()));
            }
            for (Map.Entry<Integer, SubTask> entry : subTasks.entrySet()) {
                fileWriter.write(taskToString(entry.getValue()));
            }
        } catch (IOException e) {
            throw new MExceptionanagerSaveException("Ошибка в файле:" + file.getAbsolutePath());
        }
    }

    private String headerToString() {

        StringBuffer taskStringBuffer = new StringBuffer();
        taskStringBuffer.append("Id").append(",");
        taskStringBuffer.append("Type").append(",");
        taskStringBuffer.append("Name").append(",");
        taskStringBuffer.append("Status").append(",");
        taskStringBuffer.append("Description").append(",");
        taskStringBuffer.append("Epic");
        taskStringBuffer.append("StartDate");
        taskStringBuffer.append("Duration").append("\n");

        return taskStringBuffer.toString();
    }

    private String taskToString(Task task) {
        String taskToString;
        String startTimeString;
        int duration;

        if (task.getStartTime() != null) {
            startTimeString = task.getStartTime().format(formatter);
        } else {
            startTimeString = "";
        }
        if (task.getDuration() != null) {
            duration = (int) task.getDuration().toMinutes();
        } else {
            duration = 0;
        }

        taskToString = task.getTaskId() + "," + task.getTaskType() + "," + task.getTaskName()
                + "," + task.getTaskStatus() + "," + task.getTaskDescription() + ","
                + task.getEpicId() + "," + startTimeString + ","
                + duration + "\n";
        return taskToString;

    }

    private Task fromString(String strTask) {
        String[] split = strTask.split(",");
        String strTaskType = split[1];
        String stringStatusValue = split[3];
        String strinfStartValue = split[6];
        int id = Integer.parseInt(split[0]);
        int epicId = 0;
        Task newTask = null;
        int maxId = 0;
        Duration duration = Duration.ofMinutes(Long.parseLong(split[7]));
        LocalDateTime startTime;
        if (split[6] != "") {
            startTime = LocalDateTime.parse(split[6], formatter);
        } else {
            startTime = null;
        }
        if (id > maxId) {
            maxId = id;
        }
        epicId = Integer.parseInt(split[5]);
        switch (TaskType.valueOf(strTaskType)) {
            case Task:
                newTask = createTask(new Task(id, split[2], split[4], TaskStatus.valueOf(stringStatusValue), startTime, duration));
                this.getTask(newTask.getTaskId());
                return newTask;
            case SubTask:
                newTask = createSubTask(new SubTask(id, split[2], split[4], TaskStatus.valueOf(stringStatusValue), epicId, startTime, duration));
                this.getSubTask(newTask.getTaskId());
                return newTask;
            case Epic:
                newTask = createEpic(new Epic(id, split[2], split[4], TaskStatus.valueOf(stringStatusValue), startTime, duration));
                this.getEpic(newTask.getTaskId());
                return newTask;
        }
        counterTask = maxId;
        return null;
    }

    private void restoreTasks(File file) {
        try (final BufferedReader reader = new BufferedReader(new FileReader(file, UTF_8))) {
            reader.readLine();
            while (reader.ready()) {
                String line = reader.readLine();
                fromString(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static FileBackedTaskManager loadFromFile(HistoryManager historyManager, File file) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(historyManager, file);
        fileBackedTaskManager.restoreTasks(file);
        return fileBackedTaskManager;
    }
}


