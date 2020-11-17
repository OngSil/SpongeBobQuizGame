package cliffGame.client;

import java.awt.*;
import javax.swing.*;

class ReadyPage{
    Game game;
    JPanel readyPage;
        JPanel upperPanel;
            DPanel chatArea;
                DScrollPane messageScroll;
                    DTextArea chatLog;
            DTextArea connectList;
        
        JPanel centerPanel;
            DTextField messageInput;
            JButton sendButton;

        JPanel lowerPanel;
            JButton readyButton;
            JButton backButton;


    ReadyPage(Game game){
        this.game = game;
        readyPage = new DPanel("imgs/page02/page02_bg.png");
        readyPage.setPreferredSize(new Dimension(game.MAIN_WIDTH, game.MAIN_HEIGHT) );
        readyPage.setLayout(new BorderLayout() );
        readyPage.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25) );
            upperPanel = new JPanel();
            upperPanel.setLayout(new BorderLayout(0, 0) );
            upperPanel.setPreferredSize(new Dimension(550, 365) );
            upperPanel.setOpaque(false);
            upperPanel.setBackground( game.BLANKCOLOR );
                chatLog = new DTextArea();
                chatLog.setEditable(false);
                messageScroll = new DScrollPane(chatLog);
                chatArea = new DPanel("imgs/page02/page02_chatLog.png");
                chatArea.setLayout(new BorderLayout() );
                chatArea.setPreferredSize(new Dimension(359, 365) );
                chatArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5) );
                chatArea.add(messageScroll, BorderLayout.CENTER);
                upperPanel.add(chatArea, BorderLayout.WEST);

                connectList = new DTextArea(24, "imgs/page02/page02_connectList.png");
                connectList.setPreferredSize(new Dimension(170, 365) );
                upperPanel.add(connectList, BorderLayout.EAST);
            readyPage.add(upperPanel, BorderLayout.NORTH);


            centerPanel = new JPanel();
            centerPanel.setLayout(new BorderLayout(20, 0) );
            centerPanel.setPreferredSize(new Dimension(550, 95) );
            centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0) );
            centerPanel.setOpaque(false);
            centerPanel.setBackground( game.BLANKCOLOR );
                messageInput = new DTextField("imgs/page02/page02_input.png");
                //messageInput.setBounds(25, 410, 359, 45);
                messageInput.setPreferredSize(new Dimension(359, 45) );
                centerPanel.add(messageInput, BorderLayout.WEST);
                sendButton = new DButton("imgs/page02/page02_send.png");
                //sendButton.setBounds(405, 410, 170, 45);
                sendButton.setPreferredSize(new Dimension(170, 45) );
                centerPanel.add(sendButton, BorderLayout.EAST);
            readyPage.add(centerPanel, BorderLayout.CENTER);


            lowerPanel = new JPanel();
            lowerPanel.setLayout(new BorderLayout(0, 10) );
            lowerPanel.setPreferredSize(new Dimension(550, 100) );
            lowerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0) );
            lowerPanel.setOpaque(false);
            lowerPanel.setBackground( game.BLANKCOLOR );
                readyButton = new DButton("imgs/page02/page02_ready.png");
                //readyButton.setBounds(25, 475, 550, 45);
                readyButton.setPreferredSize(new Dimension(550, 45) );
                lowerPanel.add(readyButton, BorderLayout.NORTH);

                backButton = new DButton("imgs/page02/page02_back.png");
                //backButton.setBounds(25, 530, 550, 45);
                backButton.setPreferredSize(new Dimension(550, 45) );
                lowerPanel.add(backButton, BorderLayout.SOUTH);
            readyPage.add(lowerPanel, BorderLayout.SOUTH);

    }
}


