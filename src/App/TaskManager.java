package App;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

public class TaskManager {
    private final List<Task> tasks = new ArrayList<>();
//    添加任务
    public void addTask(Task task) {
        tasks.add(task);
    }
//    移除任务
    public void removeTask(Task task) {
        tasks.remove(task);
    }
//    获取所有任务，并按照优先级、完成状态进行排序
    public List<Task> getTasks() {
        tasks.sort(Comparator
                .comparing(Task::isCompleted)
                .thenComparing(t -> priorityValue(t.getPriority()), Comparator.reverseOrder())
            //t为Task对象，是自动匹配的，所以不需要指定类型， Comparator.reverseOrder()是降序排序
        );
        return new ArrayList<>(tasks);
    }
//    为优先级字符串返回一个整数值
    private int priorityValue(String p) {
        switch (p) {
            case "高": return 3;
            case "中": return 2;
            case "低": return 1;
            default: return 0;
        }
    }


//     提醒功能
    public List<Task> getTasksDueWithinMinutes(int minutes) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime limit = now.plusMinutes(minutes);
        List<Task> dueTasks = new ArrayList<>();
        for (Task t : tasks) {
            if (!t.isReminded() && !t.isCompleted() && !t.getDeadline().isBefore(now) && !t.getDeadline().isAfter(limit)) {
                dueTasks.add(t);
                t.setReminded(true);
            }
        }
        return dueTasks;
    }
}
