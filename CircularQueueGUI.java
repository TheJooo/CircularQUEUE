import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CircularQueueGUI extends JFrame {

    private CircularQueue queue;
    private DefaultListModel<Integer> listModel;
    private JList<Integer> queueList;
    private JTextField inputField;
    private JButton enqueueButton;
    private JButton dequeueButton;
    private CirclePanel circlePanel;

    public CircularQueueGUI(int capacity) {
        queue = new CircularQueue(capacity);
        listModel = new DefaultListModel<>();
        queueList = new JList<>(listModel);
        inputField = new JTextField(10);
        enqueueButton = new JButton("Enqueue");
        dequeueButton = new JButton("Dequeue");
        circlePanel = new CirclePanel();

        setTitle("Circular Queue GUI");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Enter value:"));
        inputPanel.add(inputField);
        inputPanel.add(enqueueButton);

        JScrollPane listScrollPane = new JScrollPane(queueList);

        add(inputPanel, BorderLayout.NORTH);
        add(listScrollPane, BorderLayout.CENTER);
        add(dequeueButton, BorderLayout.SOUTH);
        add(circlePanel, BorderLayout.EAST);

        enqueueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int value = Integer.parseInt(inputField.getText());
                    queue.enqueue(value);
                    updateQueueDisplay();
                    inputField.setText("");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid integer.");
                }
            }
        });

        dequeueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!queue.isEmpty()) {
                    queue.dequeue();
                    updateQueueDisplay();
                } else {
                    JOptionPane.showMessageDialog(null, "Queue is empty. Cannot dequeue.");
                }
            }
        });
    }

    private void updateQueueDisplay() {
        listModel.clear();
        int size = queue.getSize();
        for (int i = 0; i < size; i++) {
            listModel.addElement(queue.getElementAt(i));
        }
        circlePanel.setQueueSize(size);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CircularQueueGUI(5).setVisible(true);
            }
        });
    }
}

class CirclePanel extends JPanel {
    private int queueSize;

    public CirclePanel() {
        this.queueSize = 0;
        setPreferredSize(new Dimension(150, 150));
    }

    public void setQueueSize(int size) {
        this.queueSize = size;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int diameter = 10 * queueSize;
        int x = (getWidth() - diameter) / 2;
        int y = (getHeight() - diameter) / 2;
        g.setColor(Color.BLUE);
        g.fillOval(x, y, diameter, diameter);
    }
}

class CircularQueue {
    private int[] queue;
    private int front;
    private int rear;
    private int size;
    private int capacity;

    public CircularQueue(int capacity) {
        this.capacity = capacity;
        this.queue = new int[capacity];
        this.front = 0;
        this.rear = -1;
        this.size = 0;
    }

    public void enqueue(int value) {
        if (isFull()) {
            System.out.println("Queue is full. Cannot enqueue " + value);
            return;
        }
        rear = (rear + 1) % capacity;
        queue[rear] = value;
        size++;
    }

    public int dequeue() {
        if (isEmpty()) {
            System.out.println("Queue is empty. Cannot dequeue.");
            return -1;
        }
        int value = queue[front];
        front = (front + 1) % capacity;
        size--;
        return value;
    }

    public int peek() {
        if (isEmpty()) {
            System.out.println("Queue is empty. Nothing to peek.");
            return -1;
        }
        return queue[front];
    }

    public boolean isFull() {
        return size == capacity;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int getSize() {
        return size;
    }

    public int getElementAt(int index) {
        return queue[(front + index) % capacity];
    }
}
