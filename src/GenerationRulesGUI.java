import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;

public class GenerationRulesGUI {

    ArrayList<DefaultNode> SelectableGenerationNodes;
    public JFrame mainFrame;
    public JLabel label3;
    public JComboBox<String> source_list;
    public JLabel label4;
    public JComboBox<String> destination_list;
    public JPanel panel1;
    public JLabel label_trucks;
    public JLabel label_vans;
    public JLabel label_sedans;
    public JLabel label_select;
    public JSpinner spinner1;
    public JSpinner spinner2;
    public JSpinner spinner3;
    public JPanel panel2;
    public JPanel panel3;
    public JPanel panel2_5;
    public JSpinner spinner4;
    public JLabel label_intensity;
    public static ArrayList<GenerationRule> rules = new ArrayList<GenerationRule>();

    public GenerationRulesGUI(ArrayList<DefaultNode> SelectableGenerationNodes) throws IOException, ParseException {
        this.SelectableGenerationNodes = SelectableGenerationNodes;
        prepareGUI();
    }

    public static void main(String[] args){

    }

    private void prepareGUI() throws IOException, ParseException {
        mainFrame = new JFrame("Define traffic generation rules");

        Border border2 = BorderFactory.createLineBorder(Color.black, 2);

        label3 = new JLabel();
        label3.setText("Choose the source point: ");
        label3.setHorizontalTextPosition(JLabel.CENTER);
        label3.setVerticalTextPosition(JLabel.TOP);
        label3.setVerticalAlignment(JLabel.CENTER);
        label3.setHorizontalAlignment(JLabel.CENTER);
        label3.setBounds(10, 20, 170, 30);

        label4 = new JLabel();
        label4.setText("Choose the destination point: ");
        label4.setHorizontalTextPosition(JLabel.CENTER);
        label4.setVerticalTextPosition(JLabel.TOP);
        label4.setVerticalAlignment(JLabel.CENTER);
        label4.setHorizontalAlignment(JLabel.CENTER);
        label4.setBounds(200, 20, 170, 30);

        String[] node_ids = {};


        for(int i = 0; i < SelectableGenerationNodes.size(); i++){
            node_ids = addEl(node_ids, "Node " + (SelectableGenerationNodes.get(i).id));
        }

        source_list = new JComboBox<String>(node_ids);
        source_list.setBorder(border2);
        source_list.setBackground(Color.white);
        source_list.setBounds(45, 60, 100, 20);

        destination_list = new JComboBox<String>(node_ids);
        destination_list.setBorder(border2);
        destination_list.setBackground(Color.white);
        destination_list.setBounds(235, 60, 100, 20);

        panel1 = new JPanel();
        panel1.setBackground(Color.white);
//        panel1.setBounds(120, 90, 140, 35);
        panel1.setBounds(28, 90, 324, 35);


        label_trucks = new JLabel();
        label_trucks.setText("Trucks");
        label_trucks.setHorizontalTextPosition(JLabel.CENTER);
        label_trucks.setVerticalTextPosition(JLabel.TOP);
        label_trucks.setVerticalAlignment(JLabel.CENTER);
        label_trucks.setHorizontalAlignment(JLabel.CENTER);
        label_trucks.setBounds(40, 185, 50, 30);

        label_vans = new JLabel();
        label_vans.setText("Vans");
        label_vans.setHorizontalTextPosition(JLabel.CENTER);
        label_vans.setVerticalTextPosition(JLabel.TOP);
        label_vans.setVerticalAlignment(JLabel.CENTER);
        label_vans.setHorizontalAlignment(JLabel.CENTER);
        label_vans.setBounds(160, 185, 50, 30);

        label_sedans = new JLabel();
        label_sedans.setText("Sedans");
        label_sedans.setHorizontalTextPosition(JLabel.CENTER);
        label_sedans.setVerticalTextPosition(JLabel.TOP);
        label_sedans.setVerticalAlignment(JLabel.CENTER);
        label_sedans.setHorizontalAlignment(JLabel.CENTER);
        label_sedans.setBounds(280, 185, 50, 30);

        label_select = new JLabel();
        label_select.setText("Please define car types generation probability:");
        label_select.setHorizontalTextPosition(JLabel.CENTER);
        label_select.setVerticalTextPosition(JLabel.TOP);
        label_select.setVerticalAlignment(JLabel.CENTER);
        label_select.setHorizontalAlignment(JLabel.CENTER);
        label_select.setBounds(50, 160, 290, 30);

        SpinnerModel value =
                new SpinnerNumberModel(0.3, //initial value
                        0, //minimum value
                        1, //maximum value
                        0.1); //step

        SpinnerModel value1 =
                new SpinnerNumberModel(0.3, //initial value
                        0, //minimum value
                        1, //maximum value
                        0.1); //step

        SpinnerModel value2 =
                new SpinnerNumberModel(0.3, //initial value
                        0, //minimum value
                        1, //maximum value
                        0.1); //step

        spinner1 = new JSpinner(value);
        spinner1.setBounds(47, 220, 42, 32);

        spinner2 = new JSpinner(value1);
        spinner2.setBounds(167, 220, 42, 32);

        spinner3 = new JSpinner(value2);
        spinner3.setBounds(287, 220, 42, 32);

        panel2 = new JPanel();
        panel2.setBackground(Color.white);
        panel2.setBounds(78, 390, 100, 35);

        panel2_5 = new JPanel();
        panel2_5.setBackground(Color.white);
        panel2_5.setBounds(205, 390, 100, 35);

        panel3 = new JPanel();
        panel3.setBackground(Color.white);
        panel3.setBounds(123, 450, 130, 75);

        SpinnerModel value3 =
                new SpinnerNumberModel(5, //initial value
                        1, //minimum value
                        10, //maximum value
                        1); //step

        spinner4 = new JSpinner(value3);
        spinner4.setBounds(47, 320, 42, 32);

        label_intensity = new JLabel("Intensity");
        label_intensity.setHorizontalTextPosition(JLabel.CENTER);
        label_intensity.setVerticalTextPosition(JLabel.TOP);
        label_intensity.setVerticalAlignment(JLabel.CENTER);
        label_intensity.setHorizontalAlignment(JLabel.CENTER);
        label_intensity.setBounds(40, 285, 50, 30);

        mainFrame.add(label3);
        mainFrame.add(label4);
        mainFrame.add(source_list);
        mainFrame.add(destination_list);
        mainFrame.add(panel1);
        mainFrame.add(label_select);
        mainFrame.add(label_trucks);
        mainFrame.add(label_vans);
        mainFrame.add(label_sedans);
        mainFrame.add(spinner1);
        mainFrame.add(spinner2);
        mainFrame.add(spinner3);
        mainFrame.add(panel2);
        mainFrame.add(panel2_5);
        mainFrame.add(panel3);
        mainFrame.add(spinner4);
        mainFrame.add(label_intensity);

        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setLayout(null);
        mainFrame.setSize(400, 570);
        mainFrame.getContentPane().setBackground(Color.white);
        mainFrame.setVisible(true);
        mainFrame.setResizable(false);
    }

    public void showEvent(){
        JButton button1 = new JButton("                         View on the map                         ");
        button1.addActionListener(new GenerationRulesGUI.ButtonClickListener());
        button1.setBorder(new RoundedBorder(5));
        //panel1.add(button1);

//        JButton plus_button_1 = new JButton("+");
//        plus_button_1.setBorder(new RoundedBorder(60));
//        plus_button_1.setForeground(Color.BLUE);
//
//        JButton minus_button_1 = new JButton("-");
//        minus_button_1.setBorder(new RoundedBorder(10));
//        minus_button_1.setForeground(Color.BLUE);

        JButton button2 = new JButton("  Add rule  ");
        button2.addActionListener(new GenerationRulesGUI.ButtonClickListener());
        button2.setBorder(new RoundedBorder(5));

        JButton button2_5 = new JButton(" View Rules ");
        button2_5.addActionListener(new GenerationRulesGUI.ButtonClickListener());
        button2_5.setBorder(new RoundedBorder(5));

        JButton button3 = new JButton("         Start         ");
        button3.addActionListener(new GenerationRulesGUI.ButtonClickListener());
        button3.setBorder(new RoundedBorder(8));

        panel2.add(button2);
        panel2_5.add(button2_5);
        panel3.add(button3);

        panel1.add(button1);
        mainFrame.setVisible(true);
    }

    public DefaultNode getNodeById(ArrayList<DefaultNode> SelectableNodes, int id) {
        for(int i = 0; i < SelectableNodes.size(); i++){
            if(SelectableNodes.get(i).id == id){
                return SelectableNodes.get(i);
            }
        }
        return null;
    }

    public static String[] addEl(String[] arr, String el)
    {
        String[] new_arr = new String[arr.length + 1];
        System.arraycopy(arr, 0, new_arr, 0, arr.length);
        new_arr[arr.length] = el;
        return new_arr;
    }

    public boolean CheckForDuplicates(GenerationRule rule){
        for(int i = 0; i < rules.size(); i++){
            if(rules.get(i).source_node.id == rule.source_node.id &&
                    rules.get(i).destination_node.id == rule.destination_node.id) {
                return false;
            }
        }
        return true;
    }

    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if (command.equals("         Start         ")) {
                mainFrame.setVisible(false);
                StartGUI start = new StartGUI(rules);
                start.showEvent();
            }
            else if(command.equals("                         View on the map                         ")){
                String source_select = (String) source_list.getSelectedItem();
                assert source_select != null;
                source_select = source_select.substring(5);
                int resultSourceId = Integer.parseInt(source_select);
                String destination_select = (String) destination_list.getSelectedItem();
                assert destination_select != null;
                destination_select = destination_select.substring(5);
                int resultDestinationId = Integer.parseInt(destination_select);
                var values = new HashMap<String, Integer>() {{
                    try {
                        put("source", Generator.ReturnJsonIdByNodeId(resultSourceId));
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    try {
                        put ("destination", Generator.ReturnJsonIdByNodeId(resultDestinationId));
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }};
                var objectMapper = new ObjectMapper();
                String requestBody = "";
                try {
                    requestBody = objectMapper
                            .writeValueAsString(values);
                } catch (JsonProcessingException jsonProcessingException) {
                    jsonProcessingException.printStackTrace();
                }
                HttpClient client = HttpClient.newHttpClient();
                assert requestBody != null;
                //Change to localhost
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("https://httpbin.org/post"))
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                        .build();

                HttpResponse<String> response = null;
                try {
                    response = client.send(request,
                            HttpResponse.BodyHandlers.ofString());
                } catch (IOException | InterruptedException ioException) {
                    ioException.printStackTrace();
                }

                assert response != null;
                System.out.println(response.body());
            }
            else if(command.equals(" View Rules ")) {
                ViewRulesGUI view = new ViewRulesGUI(rules);
                view.showEvent();
            }
            else if(command.equals("  Add rule  ")) {
                String source_select = (String) source_list.getSelectedItem();
                assert source_select != null;
                source_select = source_select.substring(5);
                System.out.println("Selected source: " + source_select);

                String destination_select = (String) destination_list.getSelectedItem();
                assert destination_select != null;
                destination_select = destination_select.substring(5);
                System.out.println("Selected destination " + destination_select);

                int resultSourceId = Integer.parseInt(source_select);
                int resultDestinationId = Integer.parseInt(destination_select);

                DefaultNode resultSourceNode = getNodeById(SelectableGenerationNodes, resultSourceId);

                DefaultNode resultDestinationNode = getNodeById(SelectableGenerationNodes, resultDestinationId);

//                DefaultNode resultSourceNode = getNodeById(SelectableGenerationNodes, resultSourceId);
//
//                DefaultNode resultDestinationNode = getNodeById(SelectableDestinationNodes, resultDestinationId);
                System.out.println("Add command was called " + spinner1.getValue());
                if( (double) spinner1.getValue() <= 1.0 && (double) spinner2.getValue() <= 1.0 &&
                        (double) spinner3.getValue() <= 1.0 && (double) spinner1.getValue() >= 0 &&
                        (double) spinner2.getValue() >= 0 && (double) spinner3.getValue() >= 0 &&
                resultSourceId != resultDestinationId){
                    GenerationRule rule = new GenerationRule((double) spinner1.getValue(), (double) spinner2.getValue(),
                    (double) spinner3.getValue(), resultSourceNode, resultDestinationNode, (int) spinner4.getValue());
                    if(CheckForDuplicates(rule)){
                        rules.add(rule);
                        System.out.println("Rules added: " + rules.size());
                    }
                }
            }
        }
    }

    public static class RoundedBorder implements Border {

        private int radius;


        RoundedBorder(int radius) {
            this.radius = radius;
        }


        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius+1, this.radius+1, this.radius+2, this.radius);
        }


        public boolean isBorderOpaque() {
            return true;
        }


        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.drawRoundRect(x, y, width-1, height-1, radius, radius);
        }
    }

}
