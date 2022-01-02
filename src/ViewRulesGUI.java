import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ViewRulesGUI {
    public JFrame mainFrame;
    public JLabel label;
    public ArrayList<GenerationRule> rules = new ArrayList<GenerationRule>();
    public String[] list_of_rules;
    public JList<String> rules_view;
    public JPanel panel;
    public JPanel panel1;

    public ViewRulesGUI(ArrayList<GenerationRule> rules){
        this.rules = rules;
        prepareGUI();
    }

    public static void Main(String[] args){

    }

    public static String[] addEl(String[] arr, String el)
    {
        String[] new_arr = new String[arr.length + 1];
        System.arraycopy(arr, 0, new_arr, 0, arr.length);
        new_arr[arr.length] = el;
        return new_arr;
    }

    private void prepareGUI(){
        mainFrame = new JFrame("Define traffic generation rules");

        list_of_rules = new String[0];

        Border border2 = BorderFactory.createLineBorder(Color.black, 2);

        label = new JLabel("List of defined generation rules: ");
        label.setBackground(Color.BLUE);
        label.setBounds(45, 20, 200, 30);

        System.out.println("RULES SIZE: " + rules.size());

        for(int i = 0; i < rules.size(); i++){
            System.out.println("CALL");
            list_of_rules = addEl(list_of_rules, "Rule#" + (i+1) + ":   Source: " + rules.get(i).source_node.id + " => Destination: " +
                    rules.get(i).destination_node.id);
        }

        rules_view = new JList<String>(list_of_rules);
        rules_view.setBorder(border2);
        rules_view.setBounds(20, 60, 245, 320);

        panel = new JPanel();
        panel.setBackground(Color.white);
//        panel1.setBounds(120, 90, 140, 35);
        panel.setBounds(17, 400, 120, 35);

        panel1 = new JPanel();
        panel1.setBackground(Color.white);
        panel1.setBounds(150, 400, 120, 35);

        mainFrame.add(label);
        mainFrame.add(rules_view);
        mainFrame.add(panel);
        mainFrame.add(panel1);

        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setLayout(null);
        mainFrame.setSize(300, 500);
        mainFrame.getContentPane().setBackground(Color.white);
        mainFrame.setVisible(true);
        mainFrame.setResizable(false);
    }

    public void showEvent(){
        JButton button1 = new JButton("            View on the map            ");
        button1.setBorder(new GenerationRulesGUI.RoundedBorder(5));
        button1.addActionListener(new ViewRulesGUI.ButtonClickListener());
        panel.add(button1);

        JButton remove_button = new JButton("            Remove            ");
        remove_button.setBorder(new GenerationRulesGUI.RoundedBorder(5));
        remove_button.addActionListener(new ViewRulesGUI.ButtonClickListener());
        panel1.add(remove_button);
    }

    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if (command.equals("            Remove            ")) {
                if(!rules_view.isSelectionEmpty()) {
                    System.out.println("RULES SIZE: " + GenerationRulesGUI.rules.size() + " SELECTED INDEX: " + rules_view.getSelectedIndex());
                    GenerationRulesGUI.rules.remove(rules_view.getSelectedIndex());
                    ViewRulesGUI view = new ViewRulesGUI(rules);
                    view.showEvent();
                    mainFrame.setVisible(false);
                }
            }
            else if(command.equals("            View on the map            ")){
                var values = new HashMap<String, Integer>() {{
                    GenerationRule selected_rule = GenerationRulesGUI.rules.get(rules_view.getSelectedIndex());
                    put("source", selected_rule.source_node.id);
                    put ("destination", selected_rule.destination_node.id);
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

        }
    }
}
