//import javax.swing.*;
//import javax.swing.border.Border;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.util.ArrayList;
//
//public class ModuleGUI {
//
//    public static ArrayList<GenerationRule> rules = new ArrayList<GenerationRule>();
//    ArrayList<DefaultNode> SelectableSourceNodes;
//    ArrayList<DefaultNode> SelectableDestinationNodes;
//
//    public JFrame mainFrame;
//    public JLabel headerLabel;
//    public JPanel interval_Panel;
//    public JLabel label2;
//    public JPanel type_panel;
//    public JLabel label3;
//    public JList<String> source_list;
//    public JLabel label4;
//    public JList<String> destination_list;
//    public JPanel add_panel;
//    //public JList<String> rules_list;
//
//
//    public ModuleGUI(ArrayList<DefaultNode> SelectableSourceNodes, ArrayList<DefaultNode> SelectableDestinationNodes){
//        this.SelectableSourceNodes = SelectableSourceNodes;
//        this.SelectableDestinationNodes = SelectableDestinationNodes;
//        prepareGUI();
//    }
//
//    public static void main(String[] args){
////        ModuleGUI main = new ModuleGUI();
////        main.showEvent();
//    }
//
//    public static String[] addEl(String[] arr, String el)
//    {
//        String[] new_arr = new String[arr.length + 1];
//        System.arraycopy(arr, 0, new_arr, 0, arr.length);
//        new_arr[arr.length] = el;
//        return new_arr;
//    }
//
//    private void prepareGUI(){
//        Border border = BorderFactory.createLineBorder(Color.yellow, 3);
//        Border border2 = BorderFactory.createLineBorder(Color.black, 3);
//        mainFrame = new JFrame("Set Car generation points");
//        headerLabel = new JLabel();
//        headerLabel.setText("Generate a car every:");
//        headerLabel.setHorizontalTextPosition(JLabel.CENTER);
//        headerLabel.setVerticalTextPosition(JLabel.TOP);
////        headerLabel.setBackground(Color.gray);
////        headerLabel.setOpaque(true);
////        headerLabel.setBorder(border);
//        headerLabel.setVerticalAlignment(JLabel.CENTER);
//        headerLabel.setHorizontalAlignment(JLabel.CENTER);
//        headerLabel.setBounds(0, 0, 586, 30);
//
//        interval_Panel = new JPanel();
////        interval_Panel.setBackground(Color.gray);
////        interval_Panel.setOpaque(true);
////        interval_Panel.setBorder(border2);
//        interval_Panel.setBounds(0, 30, 586, 44);
//
//        label2 = new JLabel();
//        label2.setText("Choose the types of vehicles to be generated: ");
//        label2.setHorizontalTextPosition(JLabel.CENTER);
//        label2.setVerticalTextPosition(JLabel.TOP);
////        label2.setBackground(Color.gray);
////        label2.setOpaque(true);
////        label2.setBorder(border);
//        label2.setVerticalAlignment(JLabel.CENTER);
//        label2.setHorizontalAlignment(JLabel.CENTER);
//        label2.setBounds(0, 84, 586, 30);
//
//        type_panel = new JPanel();
////        type_panel.setBackground(Color.gray);
////        type_panel.setOpaque(true);
////        type_panel.setBorder(border2);
//        type_panel.setBounds(0, 114, 586, 40);
//
//        label3 = new JLabel();
//        label3.setText("Choose the source point: ");
//        label3.setHorizontalTextPosition(JLabel.CENTER);
//        label3.setVerticalTextPosition(JLabel.TOP);
////        label3.setBackground(Color.gray);
////        label3.setOpaque(true);
////        label3.setBorder(border);
//        label3.setVerticalAlignment(JLabel.CENTER);
//        label3.setHorizontalAlignment(JLabel.CENTER);
//        label3.setBounds(50, 164, 200, 30);
//
//        label4 = new JLabel();
//        label4.setText("Choose the destination point: ");
//        label4.setHorizontalTextPosition(JLabel.CENTER);
//        label4.setVerticalTextPosition(JLabel.TOP);
////        label4.setBackground(Color.gray);
////        label4.setOpaque(true);
////        label4.setBorder(border);
//        label4.setVerticalAlignment(JLabel.CENTER);
//        label4.setHorizontalAlignment(JLabel.CENTER);
//        label4.setBounds(310, 164, 200, 30);
//
//
//        String[] source_node_ids = {};
//        String[] destination_node_ids = {};
//
//        for(int i = 0; i < SelectableSourceNodes.size(); i++){
//            source_node_ids = addEl(source_node_ids, "Node " + (String.valueOf(SelectableSourceNodes.get(i).id)));
//        }
//
//        for(int i = 0; i < SelectableDestinationNodes.size(); i++){
//            destination_node_ids = addEl(destination_node_ids, "Node " + (String.valueOf(SelectableDestinationNodes.get(i).id)));
//        }
//
//        String[] points = {"Node1", "Node2", "Node3", "Node4", "Node5"};
//        source_list = new JList<String>(source_node_ids);
//        source_list.setBorder(border2);
//        source_list.setBounds(110, 194, 80, 100);
//
//        destination_list = new JList<String>(destination_node_ids);
//        destination_list.setBorder(border2);
//        destination_list.setBounds(370, 194, 80, 100);
//
//        add_panel = new JPanel();
//        add_panel.setBounds(260, 304, 50, 105);
//
//
//        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        mainFrame.setLayout(null);
//        mainFrame.setSize(600, 600);
//        mainFrame.setVisible(true);
//        mainFrame.setResizable(false);
//        mainFrame.add(headerLabel);
//        mainFrame.add(interval_Panel);
//        mainFrame.add(label2);
//        mainFrame.add(label3);
//        mainFrame.add(source_list);
//        mainFrame.add(type_panel);
//        mainFrame.add(label4);
//        mainFrame.add(destination_list);
//        mainFrame.add(add_panel);
//    }
//
//    public void showEvent(){
//        JButton button_minus = new JButton("-");
//        JButton button_plus = new JButton("+");
//        JTextField Intensity = new JTextField("1",2);
//        Intensity.setEditable(false);
//        JLabel additional_label = new JLabel();
//        additional_label.setText("s");
//
//        button_minus.addActionListener(new ModuleGUI.ButtonClickListener());
//        button_plus.addActionListener(new ModuleGUI.ButtonClickListener());
//
//        JRadioButton vans = new JRadioButton("Vans");
//        JRadioButton trucks = new JRadioButton("Trucks");
//        JRadioButton normal_cars = new JRadioButton("Normal cars");
//
//        JButton add_rule = new JButton("Add");
//        add_rule.addActionListener(new ModuleGUI.ButtonClickListener());
//
//        JButton view_button = new JButton("View");
//        view_button.addActionListener(new ModuleGUI.ButtonClickListener());
//
//        JButton finish_button = new JButton("Start");
//        finish_button.addActionListener(new ModuleGUI.ButtonClickListener());
//
//        interval_Panel.add(button_minus);
//        interval_Panel.add(Intensity);
//        interval_Panel.add(additional_label);
//        interval_Panel.add(button_plus);
//        type_panel.add(vans);
//        type_panel.add(trucks);
//        type_panel.add(normal_cars);
//        add_panel.add(add_rule);
//        add_panel.add(view_button);
//        add_panel.add(finish_button);
//        mainFrame.setVisible(true);
//    }
//
//    private class ButtonClickListener implements ActionListener {
//        public void actionPerformed(ActionEvent e) {
//            String command = e.getActionCommand();
//            if (command.equals("-")) {
//                JTextField a = (JTextField) interval_Panel.getComponent(1);
//                int n = Integer.parseInt(a.getText());
//                n = n - 1;
//                a.setText(Integer.toString(n));
//            }
//            if (command.equals("+")) {
//                JTextField a = (JTextField) interval_Panel.getComponent(1);
//                int n = Integer.parseInt(a.getText());
//                n = n + 1;
//                a.setText(Integer.toString(n));
//            }
//            if (command.equals("View")) {
////                String[] rulesList = {};
////                for(int i = 0; i < rules.size(); i++){
////                    rulesList = addEl(rulesList, "Source id: " + rules.get(i).source_node.id +
////                            " Destination id: " + rules.get(i).destination_node.id);
////                }
//            }
//            if (command.equals("Start")) {
//                Generator.saveRules(rules);
//                try {
//                    Generator.Start();
//                } catch (Exception exception) {
//                    exception.printStackTrace();
//                }
//            }
//            if (command.equals("Add")) {
//                System.out.println("Add");
//
//                JTextField a = (JTextField) interval_Panel.getComponent(1);
//                int interval = Integer.parseInt(a.getText());
//                System.out.println("Interval is: " + interval);
//
//                JRadioButton r2 = (JRadioButton) type_panel.getComponent(1);
//                boolean trucks = r2.isSelected();
//                System.out.println("Trucks: " + trucks);
//
//                JRadioButton r1 = (JRadioButton) type_panel.getComponent(0);
//                boolean vans = r1.isSelected();
//                System.out.println("Vans: " + vans);
//
//                JRadioButton r3 = (JRadioButton) type_panel.getComponent(2);
//                boolean normal_cars = r3.isSelected();
//                System.out.println("Normal cars: " + normal_cars);
//
//                JList<String> l1 = (JList<String>) source_list;
//                String source_select = l1.getSelectedValue();
//                source_select = source_select.substring(5);
//                System.out.println("Selected source: " + source_select);
//
//                JList<String> l2 = (JList<String>) destination_list;
//                String destination_select = l2.getSelectedValue();
//                destination_select = destination_select.substring(5);
//                System.out.println("Selected destination " + destination_select);
//
//                int resultSourceId = Integer.parseInt(source_select);
//                int resultDestinationId = Integer.parseInt(destination_select);
//
//                DefaultNode resultSourceNode = getNodeById(SelectableSourceNodes, resultSourceId);
//
//                DefaultNode resultDestinationNode = getNodeById(SelectableDestinationNodes, resultDestinationId);
//
//                if(interval > 0){
//                    if(trucks || vans || normal_cars){
////                        GenerationRule rule = new GenerationRule(interval, trucks, vans, normal_cars, resultSourceNode,
////                                resultDestinationNode);
////                        rules.add(rule);
//                    }
//                }
////                System.out.println("RES Interval: " + rules.get(0).interval);
//            }
//        }
//
//        public DefaultNode getNodeById(ArrayList<DefaultNode> SelectableNodes, int id) {
//            for(int i = 0; i < SelectableNodes.size(); i++){
//                if(SelectableNodes.get(i).id == id){
//                    return SelectableNodes.get(i);
//                }
//            }
//            return null;
//        }
//    }
//}
