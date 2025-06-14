package App;
import java.time.LocalDateTime;

public class Task {
    /*
    ���ݣ�
    ���ȼ���
    ��ֹ���ڣ�
    ���࣬
    ���״̬��
    ���ѱ�־
    */
    private String content;
    private String priority;
    private LocalDateTime deadline;
    private String category;
    private boolean completed;
    private boolean reminded;
//    ���캯��
    public Task(String content, String priority, LocalDateTime deadline, String category, boolean completed) {
        this.content = content;
        this.priority = priority;
        this.deadline = deadline;
        this.category = category;
        this.completed = completed;
        this.reminded = false;
    }
    /*
    �������ݣ�
    ��ȡ���ݣ�
    ��ȡ��ֹ���ڣ�
    ��ȡ���ȼ���
    ��ȡ���࣬
    �������״̬��
    �������״̬��
    �������ѱ�־��
    �������ѱ�־
    */
    public void setContent(String content) { this.content = content; }
    public String getContent() { return content; }
    public LocalDateTime getDeadline() { return deadline; }
    public String getPriority() { return priority; }
    public String getCategory() { return category; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    public boolean isCompleted() { return completed; }
    public void setReminded(boolean b) { reminded = b; }
    public boolean isReminded() { return reminded; }

}
