package cliffGame.client;


import java.awt.*;
import javax.swing.*;

class StartPage{
    Game game;
    DPanel startPage;
        JPanel helpButtonPanel;
            DButton helpButton;

        JPanel infoInputPanel;
            DButton connectButton;
            JPanel idAndIpPanel;
                DTextField userName;
                DTextField ipAddress;

    DDialog jd;
        DPanel rolePanel;
        DButton closeButton;

    StartPage(Game game){
        this.game = game;
        //startPage = new JPanel();
        startPage = new DPanel("imgs/page01/page01_bg.png");
        startPage.setLayout(new BorderLayout() );
        startPage.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25) );
                helpButton = new DButton("imgs/page01/page01_help.png");
                helpButton.setPreferredSize(new Dimension(40, 40) );
            helpButtonPanel = new JPanel();
            helpButtonPanel.setLayout(new BorderLayout() );
            helpButtonPanel.setOpaque(false);
            helpButtonPanel.setBackground( game.BLANKCOLOR );
            helpButtonPanel.add(helpButton, BorderLayout.EAST);
            startPage.add(helpButtonPanel, BorderLayout.NORTH);
            
                connectButton = new DButton("imgs/page01/page01_connect.png");
                connectButton.setPreferredSize(new Dimension(170, 100) );
            infoInputPanel = new JPanel();
            infoInputPanel.setLayout(new BorderLayout() );
            infoInputPanel.setOpaque(false);
            infoInputPanel.setBackground( game.BLANKCOLOR );
            infoInputPanel.add(connectButton, BorderLayout.EAST);

            idAndIpPanel = new JPanel();
            idAndIpPanel.setLayout(new BorderLayout() );
            idAndIpPanel.setOpaque(false);
            idAndIpPanel.setBackground( game.BLANKCOLOR );
                userName = new DTextField("imgs/page01/page01_input.png");
                userName.setPreferredSize(new Dimension(359, 45) );
                userName.setText(game.userName);
                idAndIpPanel.add(userName, BorderLayout.NORTH);

                ipAddress = new DTextField("imgs/page01/page01_input.png");
                ipAddress.setPreferredSize(new Dimension(359, 45) );
                ipAddress.setText(game.defaultIP);
                idAndIpPanel.add(ipAddress, BorderLayout.SOUTH);
            infoInputPanel.add(idAndIpPanel, BorderLayout.WEST);

            startPage.add(infoInputPanel, BorderLayout.SOUTH);

        jd = new DDialog();
        jd.setLayout(null);
        jd.setBounds(0,0,450,411);
        jd.setResizable(false);
        jd.setUndecorated(true);
            closeButton = new DButton("imgs/page01/page01_close.png");
            closeButton.setBounds(125, 300, 170, 45);

            rolePanel = new DPanel("imgs/page01/page01_role.png");
            rolePanel.setLayout(null);
            rolePanel.setBounds(0, 0, 450, 411);
            rolePanel.add(closeButton);
            jd.add(rolePanel);

    }
}
