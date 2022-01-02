//import javax.swing.*;
//import javax.swing.border.Border;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.util.ArrayList;
//
//public class GUI {
//    ArrayList<DefaultNode> SelectableSourceNodes;
//    ArrayList<DefaultNode> SelectableDestinationNodes;
//    public JFrame mainFrame;
//    public JLabel label3;
//    public JList<String> source_list;
//    public JLabel label4;
//    public JList<String> destination_list;
//    public JLabel TruckLabel;
//    public JLabel VanLabel;
//    public JLabel SedanLabel;
//    public JSpinner Spinner1;
//    public JSpinner Spinner2;
//    public JSpinner Spinner3;
//    public JPanel panel1;
//    public JPanel panel2;
//
//
//    public GUI(ArrayList<DefaultNode> SelectableSourceNodes, ArrayList<DefaultNode> SelectableDestinationNodes){
//        this.SelectableSourceNodes = SelectableSourceNodes;
//        this.SelectableDestinationNodes = SelectableDestinationNodes;
//        prepareGUI();
//    }
//
//    public static void main(String[] args){
//
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
//        mainFrame = new JFrame("Set Car generation points");
//        Border border2 = BorderFactory.createLineBorder(Color.black, 3);
//        label3 = new JLabel();
//        label3.setText("Choose the source point: ");
//        label3.setHorizontalTextPosition(JLabel.CENTER);
//        label3.setVerticalTextPosition(JLabel.TOP);
//        label3.setVerticalAlignment(JLabel.CENTER);
//        label3.setHorizontalAlignment(JLabel.CENTER);
//        label3.setBounds(50, 60, 200, 30);
//
//        label4 = new JLabel();
//        label4.setText("Choose the destination point: ");
//        label4.setHorizontalTextPosition(JLabel.CENTER);
//        label4.setVerticalTextPosition(JLabel.TOP);
//        label4.setVerticalAlignment(JLabel.CENTER);
//        label4.setHorizontalAlignment(JLabel.CENTER);
//        label4.setBounds(310, 60, 200, 30);
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
//        source_list.setBounds(110, 90, 80, 100);
//
//        destination_list = new JList<String>(destination_node_ids);
//        destination_list.setBorder(border2);
//        destination_list.setBounds(370, 90, 80, 100);
//
//        TruckLabel = new JLabel();
//        TruckLabel.setText("Trucks");
//        TruckLabel.setHorizontalTextPosition(JLabel.CENTER);
//        TruckLabel.setVerticalTextPosition(JLabel.TOP);
//        TruckLabel.setVerticalAlignment(JLabel.CENTER);
//        TruckLabel.setHorizontalAlignment(JLabel.CENTER);
//        TruckLabel.setBounds(105, 220, 50, 30);
//
//        VanLabel = new JLabel("Vans");
//        VanLabel.setHorizontalTextPosition(JLabel.CENTER);
//        VanLabel.setVerticalTextPosition(JLabel.TOP);
//        VanLabel.setVerticalAlignment(JLabel.CENTER);
//        VanLabel.setHorizontalAlignment(JLabel.CENTER);
//        VanLabel.setBounds(255, 220, 50, 30);
//
//        SedanLabel = new JLabel("Sedans");
//        SedanLabel.setHorizontalTextPosition(JLabel.CENTER);
//        SedanLabel.setVerticalTextPosition(JLabel.TOP);
//        SedanLabel.setVerticalAlignment(JLabel.CENTER);
//        SedanLabel.setHorizontalAlignment(JLabel.CENTER);
//        SedanLabel.setBounds(405, 220, 50, 30);
//
//        SpinnerModel value =
//                new SpinnerNumberModel(0.3, //initial value
//                        0, //minimum value
//                        1, //maximum value
//                        0.1); //step
//
//        SpinnerModel value1 =
//                new SpinnerNumberModel(0.3, //initial value
//                        0, //minimum value
//                        1, //maximum value
//                        0.1); //step
//
//        SpinnerModel value2 =
//                new SpinnerNumberModel(0.3, //initial value
//                        0, //minimum value
//                        1, //maximum value
//                        0.1); //step
//
//        Spinner1 = new JSpinner(value);
//        Spinner1.setBounds(110, 250, 40, 50);
//
//        Spinner2 = new JSpinner(value1);
//        Spinner2.setBounds(260, 250, 40, 50);
//
//        Spinner3 = new JSpinner(value2);
//        Spinner3.setBounds(410, 250, 40, 50);
//
//        panel1 = new JPanel();
//        panel1.setBounds(225, 320, 120, 40);
//
//        panel2 = new JPanel();
//        panel2.setBounds(205, 380, 160, 40);
//
//        mainFrame.add(label3);
//        mainFrame.add(source_list);
//        mainFrame.add(label4);
//        mainFrame.add(destination_list);
//        mainFrame.add(TruckLabel);
//        mainFrame.add(VanLabel);
//        mainFrame.add(SedanLabel);
//        mainFrame.add(Spinner1);
//        mainFrame.add(Spinner2);
//        mainFrame.add(Spinner3);
//        mainFrame.add(panel1);
//        mainFrame.add(panel2);
//        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        mainFrame.setLayout(null);
//        mainFrame.setSize(600, 560);
//        mainFrame.setVisible(true);
//        mainFrame.setResizable(false);
//    }
//
//    public void showEvent(){
//        JButton button1 = new JButton("View on the map");
//        panel1.add(button1);
//
//        JButton button2 = new JButton("Add rule");
//        JButton button3 = new JButton("Start");
//        button3.addActionListener(new GUI.ButtonClickListener());
//        panel2.add(button2);
//        panel2.add(button3);
//
//        mainFrame.setVisible(true);
//    }
//
//    public DefaultNode getNodeById(ArrayList<DefaultNode> SelectableNodes, int id) {
//        for(int i = 0; i < SelectableNodes.size(); i++){
//            if(SelectableNodes.get(i).id == id){
//                return SelectableNodes.get(i);
//            }
//        }
//        return null;
//    }
//
//    private class ButtonClickListener implements ActionListener {
//        public void actionPerformed(ActionEvent e) {
//            String command = e.getActionCommand();
//            if (command.equals("Start")) {
//                mainFrame.setVisible(false);
//                StartGUI start = new StartGUI();
//                start.showEvent();
//            }
//            }
//        }
//}
