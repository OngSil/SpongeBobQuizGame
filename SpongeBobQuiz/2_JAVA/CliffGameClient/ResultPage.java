package cliffGame.client;


import java.awt.*;
import javax.swing.*;

public class ResultPage {
    Game game;
    JPanel resultPage;
        DTextArea result;
        JButton goToStartButton;

    ResultPage(Game game){
        this.game = game;
        resultPage = new DPanel("imgs/page04/page04_bg.png");
        resultPage.setLayout(new BorderLayout(0, 20) );
        resultPage.setBorder(BorderFactory.createEmptyBorder(25+160, 25, 25, 25) );
            result = new DTextArea("imgs/page04/page04_result.png");
            result.setPreferredSize(new Dimension(550, 272) );
            result.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5) );
            resultPage.add(result, BorderLayout.CENTER);

            goToStartButton = new DButton("imgs/page04/page04_goToStart.png");
            goToStartButton.setPreferredSize(new Dimension(550, 100) );
        resultPage.add(goToStartButton, BorderLayout.SOUTH);
    }
}

