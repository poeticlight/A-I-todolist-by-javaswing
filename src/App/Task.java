package App;
import java.time.LocalDateTime;

public class Task {
    /*
    内容，
    优先级，
    截止日期，
    分类，
    完成状态，
    提醒标志
    */
    private String content;
    private String priority;
    private LocalDateTime deadline;
    private String category;
    private boolean completed;
    private boolean reminded;
//    构造函数
    public Task(String content, String priority, LocalDateTime deadline, String category, boolean completed) {
        this.content = content;
        this.priority = priority;
        this.deadline = deadline;
        this.category = category;
        this.completed = completed;
        this.reminded = false;
    }
    /*
    设置内容，
    获取内容，
    获取截止日期，
    获取优先级，
    获取分类，
    设置完成状态，
    返回完成状态，
    设置提醒标志，
    返回提醒标志
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
