package App;


import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        SwingUtilities.invokeLater(() -> {
            TodoAppFrame frame = new TodoAppFrame(taskManager);
            frame.setVisible(true);
        });
    }
}
