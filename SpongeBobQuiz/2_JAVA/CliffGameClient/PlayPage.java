package cliffGame.client;

import java.awt.*;
import javax.swing.*;

public class PlayPage {
    Game game;
    JPanel playPage;
        JPanel playInfoPanel;
            JPanel timeAndHeightPanel;
                JPanel upperAreaPanel;
                    JPanel westPanel;
                    DPanel timerPanel;
                        DLabel timer; //DTextArea timer;
                DPanel totalHeightPanel;
                    DLabel totalHeight; //DTextArea totalHeight;
            DTextArea infoList;

        JPanel buttonPanel;
            JButton oneMeterButton;
            JPanel centerButtonArea;
                JButton twoMeterButton;
            JButton threeMeterButton;

    PlayPage(Game game){
        this.game = game;
        playPage = new DPanel("imgs/page03/page03_bg.png");
        playPage.setPreferredSize(new Dimension(game.MAIN_WIDTH, game.MAIN_HEIGHT) );
        playPage.setLayout(new BorderLayout() );
        playPage.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25) );
            playInfoPanel = new JPanel();
            playInfoPanel.setLayout(new BorderLayout() );
            playInfoPanel.setOpaque(false);
            playInfoPanel.setBackground(game.BLANKCOLOR);
                timeAndHeightPanel = new JPanel();
                timeAndHeightPanel.setLayout(new BorderLayout() );
                timeAndHeightPanel.setPreferredSize(new Dimension(359, 430) );
                timeAndHeightPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0) );
                timeAndHeightPanel.setOpaque(false);
                timeAndHeightPanel.setBackground( game.BLANKCOLOR );
                    upperAreaPanel = new JPanel();
                    upperAreaPanel.setLayout(new BorderLayout() );
                    upperAreaPanel.setOpaque(false);
                    upperAreaPanel.setBackground(game.BLANKCOLOR);
                        westPanel = new JPanel();
                        westPanel.setPreferredSize(new Dimension(140,70) );
                        westPanel.setOpaque(false);
                        westPanel.setBackground(game.BLANKCOLOR);
                    upperAreaPanel.add(westPanel, BorderLayout.WEST);

                        timerPanel = new DPanel("imgs/page03/page03_timer.png");
                        timerPanel.setPreferredSize(new Dimension(70, 70) );
                        timerPanel.setLayout(new BorderLayout() );
                        timerPanel.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 0) );
                             timer = new DLabel(40);
                        timerPanel.add(timer, BorderLayout.CENTER);
                    upperAreaPanel.add(timerPanel, BorderLayout.CENTER);
                    timeAndHeightPanel.add(upperAreaPanel, BorderLayout.NORTH);

                    totalHeightPanel = new DPanel("imgs/page03/page03_totalHeight.png");
                    totalHeightPanel.setPreferredSize(new Dimension(359, 340) );
                    totalHeightPanel.setBorder(BorderFactory.createEmptyBorder(0, 80, 0, 0) );
                    totalHeightPanel.setLayout(new BorderLayout() );
                    totalHeight = new DLabel(80);
                    totalHeightPanel.add(totalHeight, BorderLayout.CENTER);
                    timeAndHeightPanel.add(totalHeightPanel, BorderLayout.SOUTH);
                playInfoPanel.add(timeAndHeightPanel, BorderLayout.WEST);

                infoList = new DTextArea(30, "imgs/page03/page03_infoList.png");
                infoList.setPreferredSize(new Dimension(172, 430) );                
                playInfoPanel.add(infoList, BorderLayout.EAST);
            playPage.add(playInfoPanel, BorderLayout.CENTER);

            buttonPanel = new JPanel();
            buttonPanel.setPreferredSize(new Dimension(550, 100) );
            buttonPanel.setLayout(new BorderLayout() );
            buttonPanel.setOpaque(false);
            buttonPanel.setBackground( game.BLANKCOLOR );
                oneMeterButton = new DButton("imgs/page03/page03_1m.png");
                oneMeterButton.setPreferredSize(new Dimension(170, 100) );
                //oneMeterButton.setBounds(25, 475, 170, 100);
                buttonPanel.add(oneMeterButton, BorderLayout.WEST);
                centerButtonArea = new JPanel();
                centerButtonArea.setLayout(new BorderLayout() );
                centerButtonArea.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20) );
                centerButtonArea.setOpaque(false);
                centerButtonArea.setBackground( game.BLANKCOLOR );
                    twoMeterButton = new DButton("imgs/page03/page03_2m.png");
                    twoMeterButton.setPreferredSize(new Dimension(170, 100) );
                centerButtonArea.add(twoMeterButton, BorderLayout.CENTER);
                buttonPanel.add(centerButtonArea, BorderLayout.CENTER);

                threeMeterButton = new DButton("imgs/page03/page03_3m.png");
                threeMeterButton.setPreferredSize(new Dimension(170, 100) );
                buttonPanel.add(threeMeterButton, BorderLayout.EAST);
            playPage.add(buttonPanel, BorderLayout.SOUTH);
    }

}