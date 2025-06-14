package App;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.formdev.flatlaf.FlatLightLaf;
import com.github.lgooddatepicker.components.*;

//继承JFrame框架
public class TodoAppFrame extends JFrame {
    /*
    任务管理对象，
    数据，
    任务菜单，
    任务输入文本框，
    优先级下拉框，
    分类文本框，
    截止时间文本框，
     */
    private final TaskManager taskManager;
    private final DefaultTableModel tableModel;
    private final JTable taskTable;
    private final JTextField contentField = new JTextField();
    private final JComboBox<String> priorityBox;
    private final JComboBox<String> categoryBox;
    private final JTextField categoryField = new JTextField();
    private final DateTimePicker deadlinePicker;
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    //  构造函数
    public TodoAppFrame(TaskManager manager) {
        try {
//          美化一下
            UIManager.setLookAndFeel(new FlatLightLaf());
            UIManager.put("defaultFont", new Font("微软雅黑", Font.PLAIN, 14));
            UIManager.put("Button.arc", 20);

        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }

//      时间选择器设置

        DatePickerSettings dateSettings = new DatePickerSettings(Locale.CHINA);
        TimePickerSettings timeSettings = new TimePickerSettings(Locale.CHINA);
        timeSettings.generatePotentialMenuTimes(
                TimePickerSettings.TimeIncrement.FifteenMinutes,
                LocalTime.MIN,
                LocalTime.MAX
        );
        dateSettings.setFormatForDatesCommonEra("yyyy-MM-dd");
        timeSettings.setFormatForDisplayTime("HH:mm");
        timeSettings.setFormatForMenuTimes("HH:mm");
        deadlinePicker = new DateTimePicker(dateSettings, timeSettings);
//      分离日期和时间选择器
        DatePicker datePicker = deadlinePicker.getDatePicker();
        TimePicker timePicker = deadlinePicker.getTimePicker();

//      设置初始时间
        datePicker.setDate(LocalDate.now());
        timePicker.setTime(LocalTime.now());
//      按钮
        Dimension buttonSize = new Dimension(27, 27);
//        日期选择按钮
        JButton calBtn = datePicker.getComponentToggleCalendarButton();
        calBtn.setIcon(new ImageIcon(getClass().getResource("/resources/表格.png")));
        calBtn.setText("");
        calBtn.setPreferredSize(buttonSize);
//      时间选择按钮
        JButton timeBtn = timePicker.getComponentToggleTimeMenuButton();
        timeBtn.setIcon(new ImageIcon(getClass().getResource("/resources/时钟.png")));
        timeBtn.setText("");
        timeBtn.setPreferredSize(buttonSize);
//      两个文本框
        JTextField date_Field = datePicker.getComponentDateTextField();
        JTextField time_Field = timePicker.getComponentTimeTextField();
        date_Field.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                LocalDate current = datePicker.getDate();
                int notches = e.getWheelRotation();
                LocalDate updated = current.plusDays(-notches);
                datePicker.setDate(updated);
            }
        });
        time_Field.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                LocalTime current = timePicker.getTime();
                int notches = e.getWheelRotation();
                LocalTime updated = current.plusMinutes(-notches);
                timePicker.setTime(updated);
            }
        });
//      优先级下拉菜单赋值
        priorityBox = new JComboBox<>(new String[]{"高", "中", "低"});

        this.taskManager = manager;
        setTitle("I-待办事项管理");
        setSize(1100, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        ImageIcon icon=new ImageIcon(getClass().getResource("/resources/OIP-C.jpg"));
        this.setIconImage(icon.getImage());

//         表格设置
        String[] cols = {"内容", "优先级", "截止时间", "分类", "完成"};
        tableModel = new DefaultTableModel(cols, 0){
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 4) return Boolean.class;
                return String.class;
            }
        };
        taskTable = new JTable(tableModel);
        JScrollPane scroll = new JScrollPane(taskTable);
//      创建表格监听器
        tableModel.addTableModelListener(e -> {
            int row    = e.getFirstRow();
            int column = e.getColumn();
            if (column == 4 && row >= 0) {
                Boolean isDone = (Boolean) tableModel.getValueAt(row, column);
                Task  t = taskManager.getTasks().get(row);
                t.setCompleted(isDone);
            }
        });



//         表单区
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;


        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0; gbc.gridy = 0;
        form.add(new JLabel("内容："), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        form.add(contentField, gbc);
        gbc.gridwidth = 1;
        contentField.setPreferredSize(new Dimension(0, 27));
        contentField.setText("请输入你想设置的任务......");
        contentField.setForeground(Color.GRAY);
//        创建焦点监听器，实现提示功能
        contentField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (contentField.getText().equals("请输入你想设置的任务......")) {
                    contentField.setText("");
                    contentField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (contentField.getText().isEmpty()) {
                    contentField.setText("请输入你想设置的任务......");
                    contentField.setForeground(Color.GRAY);
                }
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        form.add(new JLabel("优先级："), gbc);
        gbc.gridx = 1;
        form.add(priorityBox, gbc);

//      初始化分类下拉框
        categoryBox = new JComboBox<>(new String[]{"工作", "学习", "生活", "自定义"});
        gbc.gridx = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        form.add(new JLabel("分类："), gbc);
        gbc.gridx = 3;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        form.add(categoryBox, gbc);
        form.add(categoryField, gbc);
        categoryField.setVisible(false);
        categoryField.setPreferredSize(new Dimension(0, 27));
        categoryBox.setPreferredSize(new Dimension(0, 27));
//      设置下拉框的监听器，实现文本框和下拉框的切换
        categoryBox.addItemListener( e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                if (categoryBox.getSelectedIndex() == 3) {
                    categoryBox.setVisible(false);
                    categoryField.setVisible(true);
                } else {
                    categoryField.setVisible(false);
                    categoryBox.setVisible(true);
                }
            }
        });
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        form.add(new JLabel("截止时间："), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        deadlinePicker.setPreferredSize(new Dimension(0, 27));
        form.add(deadlinePicker, gbc);
        gbc.gridwidth = 1;

        //      添加\删除按钮
        JButton addBtn = new JButton("添加任务");
        addBtn.addActionListener(e -> addTask());
        JButton delBtn = new JButton("删除选中");
        delBtn.addActionListener(e -> deleteSelected());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        btnPanel.add(addBtn);
        btnPanel.add(delBtn);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        form.add(btnPanel, gbc);

        Font formFont = new Font("微软雅黑", Font.BOLD, 15);
        for (Component comp : form.getComponents()) {
            if (comp instanceof JLabel) {
                comp.setFont(formFont);
            }
        }

//      设置form与边界距离
        form.setBorder(BorderFactory.createEmptyBorder(20, 250, 20, 250));

//        美化一下按钮
        addBtn.setBackground(new Color(59, 89, 182));
        addBtn.setForeground(Color.WHITE);
        addBtn.setFocusPainted(false);
        delBtn.setBackground(new Color(220, 53, 69));
        delBtn.setForeground(Color.WHITE);
        delBtn.setFocusPainted(false);

//        字体设置
        Font font = new Font("宋体", Font.PLAIN, 14);
        contentField.setFont(font);
        taskTable.setRowHeight(25);
        taskTable.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 16));

//      放入Frame
        getContentPane().add(form, BorderLayout.SOUTH);
        getContentPane().add(scroll, BorderLayout.CENTER);

//         自动刷新提醒
        Timer timer = new Timer(10000, e -> checkReminders());
        timer.start();
    }

    private void addTask() {
        try {

            String content = contentField.getText();
            String priority = (String) priorityBox.getSelectedItem();
            String category = (String) categoryBox.getSelectedItem();
            LocalDateTime deadline = deadlinePicker.getDateTimeStrict();
            if (content.trim().isEmpty() ) {
                JOptionPane.showMessageDialog(this, "任务内容不能为空！");
                return;
            }
            if (deadline == null) {
                JOptionPane.showMessageDialog(this, "请输入有效的截止时间！");
                return;
            }
            if (category.equals("自定义")) {
                category = categoryField.getText();
                categoryField.setText("");
                categoryBox.setSelectedIndex(0);
                categoryField.setVisible(false);
            }
            taskManager.addTask(new Task(content, priority, deadline, category, false));
            categoryBox.setVisible(true);

            refreshTable();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "格式错误！");
        }
    }

    private void deleteSelected() {
        int[] rows = taskTable.getSelectedRows();
        if (rows.length > 0) {
            for (int i = rows.length - 1; i >= 0; i--) {
                Task task = taskManager.getTasks().get(rows[i]);
                taskManager.removeTask(task);
            }
            contentField.setText("");
            categoryField.setText("");
            deadlinePicker.clear();
            refreshTable();
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Task t : taskManager.getTasks()) {
            tableModel.addRow(new Object[]{
                    t.getContent(),
                    t.getPriority(),
                    t.getDeadline().format(dtf),
                    t.getCategory(),
                    t.isCompleted()
            });
        }
    }

    private void checkReminders() {
        for (Task t : taskManager.getTasksDueWithinMinutes(2)) {
            JOptionPane.showMessageDialog(this, "任务：" + t.getContent() + "即将到期！" );
        }
    }
}
