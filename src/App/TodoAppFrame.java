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

//�̳�JFrame���
public class TodoAppFrame extends JFrame {
    /*
    ����������
    ���ݣ�
    ����˵���
    ���������ı���
    ���ȼ�������
    �����ı���
    ��ֹʱ���ı���
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

    //  ���캯��
    public TodoAppFrame(TaskManager manager) {
        try {
//          ����һ��
            UIManager.setLookAndFeel(new FlatLightLaf());
            UIManager.put("defaultFont", new Font("΢���ź�", Font.PLAIN, 14));
            UIManager.put("Button.arc", 20);

        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }

//      ʱ��ѡ��������

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
//      �������ں�ʱ��ѡ����
        DatePicker datePicker = deadlinePicker.getDatePicker();
        TimePicker timePicker = deadlinePicker.getTimePicker();

//      ���ó�ʼʱ��
        datePicker.setDate(LocalDate.now());
        timePicker.setTime(LocalTime.now());
//      ��ť
        Dimension buttonSize = new Dimension(27, 27);
//        ����ѡ��ť
        JButton calBtn = datePicker.getComponentToggleCalendarButton();
        calBtn.setIcon(new ImageIcon(getClass().getResource("/resources/���.png")));
        calBtn.setText("");
        calBtn.setPreferredSize(buttonSize);
//      ʱ��ѡ��ť
        JButton timeBtn = timePicker.getComponentToggleTimeMenuButton();
        timeBtn.setIcon(new ImageIcon(getClass().getResource("/resources/ʱ��.png")));
        timeBtn.setText("");
        timeBtn.setPreferredSize(buttonSize);
//      �����ı���
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
//      ���ȼ������˵���ֵ
        priorityBox = new JComboBox<>(new String[]{"��", "��", "��"});

        this.taskManager = manager;
        setTitle("I-�����������");
        setSize(1100, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        ImageIcon icon=new ImageIcon(getClass().getResource("/resources/OIP-C.jpg"));
        this.setIconImage(icon.getImage());

//         �������
        String[] cols = {"����", "���ȼ�", "��ֹʱ��", "����", "���"};
        tableModel = new DefaultTableModel(cols, 0){
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 4) return Boolean.class;
                return String.class;
            }
        };
        taskTable = new JTable(tableModel);
        JScrollPane scroll = new JScrollPane(taskTable);
//      ������������
        tableModel.addTableModelListener(e -> {
            int row    = e.getFirstRow();
            int column = e.getColumn();
            if (column == 4 && row >= 0) {
                Boolean isDone = (Boolean) tableModel.getValueAt(row, column);
                Task  t = taskManager.getTasks().get(row);
                t.setCompleted(isDone);
            }
        });



//         ����
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;


        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0; gbc.gridy = 0;
        form.add(new JLabel("���ݣ�"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        form.add(contentField, gbc);
        gbc.gridwidth = 1;
        contentField.setPreferredSize(new Dimension(0, 27));
        contentField.setText("�������������õ�����......");
        contentField.setForeground(Color.GRAY);
//        ���������������ʵ����ʾ����
        contentField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (contentField.getText().equals("�������������õ�����......")) {
                    contentField.setText("");
                    contentField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (contentField.getText().isEmpty()) {
                    contentField.setText("�������������õ�����......");
                    contentField.setForeground(Color.GRAY);
                }
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        form.add(new JLabel("���ȼ���"), gbc);
        gbc.gridx = 1;
        form.add(priorityBox, gbc);

//      ��ʼ������������
        categoryBox = new JComboBox<>(new String[]{"����", "ѧϰ", "����", "�Զ���"});
        gbc.gridx = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        form.add(new JLabel("���ࣺ"), gbc);
        gbc.gridx = 3;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        form.add(categoryBox, gbc);
        form.add(categoryField, gbc);
        categoryField.setVisible(false);
        categoryField.setPreferredSize(new Dimension(0, 27));
        categoryBox.setPreferredSize(new Dimension(0, 27));
//      ����������ļ�������ʵ���ı������������л�
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
        form.add(new JLabel("��ֹʱ�䣺"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        deadlinePicker.setPreferredSize(new Dimension(0, 27));
        form.add(deadlinePicker, gbc);
        gbc.gridwidth = 1;

        //      ���\ɾ����ť
        JButton addBtn = new JButton("�������");
        addBtn.addActionListener(e -> addTask());
        JButton delBtn = new JButton("ɾ��ѡ��");
        delBtn.addActionListener(e -> deleteSelected());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        btnPanel.add(addBtn);
        btnPanel.add(delBtn);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        form.add(btnPanel, gbc);

        Font formFont = new Font("΢���ź�", Font.BOLD, 15);
        for (Component comp : form.getComponents()) {
            if (comp instanceof JLabel) {
                comp.setFont(formFont);
            }
        }

//      ����form��߽����
        form.setBorder(BorderFactory.createEmptyBorder(20, 250, 20, 250));

//        ����һ�°�ť
        addBtn.setBackground(new Color(59, 89, 182));
        addBtn.setForeground(Color.WHITE);
        addBtn.setFocusPainted(false);
        delBtn.setBackground(new Color(220, 53, 69));
        delBtn.setForeground(Color.WHITE);
        delBtn.setFocusPainted(false);

//        ��������
        Font font = new Font("����", Font.PLAIN, 14);
        contentField.setFont(font);
        taskTable.setRowHeight(25);
        taskTable.getTableHeader().setFont(new Font("΢���ź�", Font.BOLD, 16));

//      ����Frame
        getContentPane().add(form, BorderLayout.SOUTH);
        getContentPane().add(scroll, BorderLayout.CENTER);

//         �Զ�ˢ������
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
                JOptionPane.showMessageDialog(this, "�������ݲ���Ϊ�գ�");
                return;
            }
            if (deadline == null) {
                JOptionPane.showMessageDialog(this, "��������Ч�Ľ�ֹʱ�䣡");
                return;
            }
            if (category.equals("�Զ���")) {
                category = categoryField.getText();
                categoryField.setText("");
                categoryBox.setSelectedIndex(0);
                categoryField.setVisible(false);
            }
            taskManager.addTask(new Task(content, priority, deadline, category, false));
            categoryBox.setVisible(true);

            refreshTable();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "��ʽ����");
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
            JOptionPane.showMessageDialog(this, "����" + t.getContent() + "�������ڣ�" );
        }
    }
}
