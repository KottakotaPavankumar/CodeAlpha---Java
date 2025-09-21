package Project;

import javax.swing.*;   
import java.awt.*;      
import java.awt.event.*; 
import java.util.*;

public class AI_ChatBot extends JFrame implements ActionListener {
    private JTextArea chatArea;       
    private JTextField inputField;    
    private JButton sendButton;       
    private Map<String, String> knowledgeBase;

    public AI_ChatBot() {
        setTitle("AI ChatBot - Java");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false); 
        JScrollPane scrollPane = new JScrollPane(chatArea);

        inputField = new JTextField();
        sendButton = new JButton("Send");

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(this);
        inputField.addActionListener(this);

        initializeKnowledgeBase();

        setVisible(true);

        chatArea.append("🤖 Bot: Hello! How can I help you today?\n");
    }

    private void initializeKnowledgeBase() {
        knowledgeBase = new HashMap<>();
        knowledgeBase.put("Hello", "Hi there!");
        knowledgeBase.put("hi", "Hello!");
        knowledgeBase.put("How are you", "I'm Just a bot, but I'm doing fine!");
        knowledgeBase.put("What is your name", "I'm JavaBot, your virtual assistant.");
        knowledgeBase.put("Help", "Sure, I'm here to help! Ask me anything.");
        knowledgeBase.put("Bye", "Goodbye! Have a great day.");
        knowledgeBase.put("What is java", "Java is a high-level, class-based, object-oriented programming language.");
        knowledgeBase.put("Thanks", "You're Welcome!");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String userInput = inputField.getText().trim();
        if (userInput.isEmpty()) {
            return;
        }
        chatArea.append("👤 You: " + userInput + "\n");
        inputField.setText("");
        String response = generateResponse(userInput);
        chatArea.append("🤖 Bot: " + response + "\n");
    }

    private String generateResponse(String input) {
        input = input.toLowerCase().replaceAll("[^a-z0-9\\s]", "").trim();
        for (String key : knowledgeBase.keySet()) {
            if (input.contains(key)) {
                return knowledgeBase.get(key);
            }
        }
        return "I'm not sure how to respond to that. Can you rephrase?";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AI_ChatBot::new);
    }
}
