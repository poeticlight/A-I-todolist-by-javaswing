package App;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

public class TaskManager {
    private final List<Task> tasks = new ArrayList<>();
//    �������
    public void addTask(Task task) {
        tasks.add(task);
    }
//    �Ƴ�����
    public void removeTask(Task task) {
        tasks.remove(task);
    }
//    ��ȡ�������񣬲��������ȼ������״̬��������
    public List<Task> getTasks() {
        tasks.sort(Comparator
                .comparing(Task::isCompleted)
                .thenComparing(t -> priorityValue(t.getPriority()), Comparator.reverseOrder())
            //tΪTask�������Զ�ƥ��ģ����Բ���Ҫָ�����ͣ� Comparator.reverseOrder()�ǽ�������
        );
        return new ArrayList<>(tasks);
    }
//    Ϊ���ȼ��ַ�������һ������ֵ
    private int priorityValue(String p) {
        switch (p) {
            case "��": return 3;
            case "��": return 2;
            case "��": return 1;
            default: return 0;
        }
    }


//     ���ѹ���
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
