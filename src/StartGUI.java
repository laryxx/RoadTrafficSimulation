import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class StartGUI {
    public JFrame mainFrame;
    public JLabel label_start_time;
    public JLabel label_end_time;
    public JSpinner start_hour;
    public JSpinner start_minute;
    public JLabel h1;
    public JLabel m1;
    public JSpinner end_hour;
    public JSpinner end_minute;
    public JLabel h2;
    public JLabel m2;
    public JLabel framerate;
    public JRadioButton fps1;
    public JRadioButton fps2;
    public JPanel start_panel;
    public ArrayList<GenerationRule> rules = new ArrayList<>();

    public StartGUI(ArrayList<GenerationRule> rules){
        this.rules = rules;
        prepareGUI();
    }

    public static void main(String[] args){

    }

    private void prepareGUI(){
        mainFrame = new JFrame("Set Properties for the simulation");

        label_start_time = new JLabel("Set Start Time");
        label_start_time.setHorizontalTextPosition(JLabel.CENTER);
        label_start_time.setVerticalTextPosition(JLabel.TOP);
        label_start_time.setVerticalAlignment(JLabel.CENTER);
        label_start_time.setHorizontalAlignment(JLabel.CENTER);
        label_start_time.setBounds(20, 20, 100, 30);

        label_end_time = new JLabel("Set End Time");
        label_end_time.setHorizontalTextPosition(JLabel.CENTER);
        label_end_time.setVerticalTextPosition(JLabel.TOP);
        label_end_time.setVerticalAlignment(JLabel.CENTER);
        label_end_time.setHorizontalAlignment(JLabel.CENTER);
        label_end_time.setBounds(195, 20, 100, 30);

        h1 = new JLabel("H");
        h1.setHorizontalTextPosition(JLabel.CENTER);
        h1.setVerticalTextPosition(JLabel.TOP);
        h1.setVerticalAlignment(JLabel.CENTER);
        h1.setHorizontalAlignment(JLabel.CENTER);
        h1.setBounds(55, 46, 10, 30);

        m1 = new JLabel("m");
        m1.setHorizontalTextPosition(JLabel.CENTER);
        m1.setVerticalTextPosition(JLabel.TOP);
        m1.setVerticalAlignment(JLabel.CENTER);
        m1.setHorizontalAlignment(JLabel.CENTER);
        m1.setBounds(121, 46, 10, 30);

        h2 = new JLabel("H");
        h2.setHorizontalTextPosition(JLabel.CENTER);
        h2.setVerticalTextPosition(JLabel.TOP);
        h2.setVerticalAlignment(JLabel.CENTER);
        h2.setHorizontalAlignment(JLabel.CENTER);
        h2.setBounds(230, 46, 10, 30);

        m2 = new JLabel("m");
        m2.setHorizontalTextPosition(JLabel.CENTER);
        m2.setVerticalTextPosition(JLabel.TOP);
        m2.setVerticalAlignment(JLabel.CENTER);
        m2.setHorizontalAlignment(JLabel.CENTER);
        m2.setBounds(295, 46, 10, 30);

        framerate = new JLabel("Framerate:");
        framerate.setHorizontalTextPosition(JLabel.CENTER);
        framerate.setVerticalTextPosition(JLabel.TOP);
        framerate.setVerticalAlignment(JLabel.CENTER);
        framerate.setHorizontalAlignment(JLabel.CENTER);
        framerate.setBounds(20, 90, 100, 30);

        SpinnerModel value =
                new SpinnerNumberModel(10, //initial value
                        0, //minimum value
                        24, //maximum value
                        1); //step
        start_hour = new JSpinner(value);
        start_hour.setBounds(15, 50, 38, 22);

        SpinnerModel value2 =
                new SpinnerNumberModel(30, //initial value
                        0, //minimum value
                        60, //maximum value
                        1); //step
        start_minute = new JSpinner(value2);
        start_minute.setBounds(80, 50, 38, 22);

        SpinnerModel value3 =
                new SpinnerNumberModel(11, //initial value
                        0, //minimum value
                        24, //maximum value
                        1); //step
        end_hour = new JSpinner(value3);
        end_hour.setBounds(190, 50, 38, 22);

        SpinnerModel value4 =
                new SpinnerNumberModel(0, //initial value
                        0, //minimum value
                        60, //maximum value
                        1); //step
        end_minute = new JSpinner(value4);
        end_minute.setBounds(255, 50, 38, 22);

        fps1 = new JRadioButton("10");
        fps1.setBackground(Color.white);
        fps1.setBounds(20, 110, 50, 30);

        fps2 = new JRadioButton("30");
        fps2.setBackground(Color.white);
        fps2.setBounds(80, 110, 50, 30);
        ButtonGroup group = new ButtonGroup();
        group.add(fps1);
        group.add(fps2);

        start_panel = new JPanel();
        start_panel.setBackground(Color.white);
        start_panel.setBounds(120, 170, 100, 80);

        mainFrame.add(label_start_time);
        mainFrame.add(label_end_time);
        mainFrame.add(start_hour);
        mainFrame.add(start_minute);
        mainFrame.add(h1);
        mainFrame.add(m1);
        mainFrame.add(end_hour);
        mainFrame.add(end_minute);
        mainFrame.add(h2);
        mainFrame.add(m2);
        mainFrame.add(framerate);
        mainFrame.add(fps1);
        mainFrame.add(fps2);
        mainFrame.add(start_panel);

        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setLayout(null);
        mainFrame.setSize(350, 300);
        mainFrame.getContentPane().setBackground(Color.white);
        mainFrame.setVisible(true);
        mainFrame.setResizable(false);
    }

    public void showEvent(){
        JButton button3 = new JButton("       Start       ");
        button3.addActionListener(new StartGUI.ButtonClickListener());
        button3.setBorder(new GenerationRulesGUI.RoundedBorder(8));
        start_panel.add(button3);

        mainFrame.setVisible(true);
    }

    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if (command.equals("       Start       ")) {
                try {
                    Generator.Start(rules);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }

        }
    }

}
