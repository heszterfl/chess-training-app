package hu.eszter.chess.ui;

import hu.eszter.chess.domain.Game;

import javax.swing.*;

public class GameViewerWindow extends JFrame {

    public GameViewerWindow(Game game) {
        setTitle("Game Viewer - " + game.getId());
        setSize(400, 300);
        setLocationRelativeTo(null);

        JLabel label = new JLabel(
                "Game: " + game.getWhitePlayer() + " vs " + game.getBlackPlayer(),
                SwingConstants.CENTER
        );

        add(label);
    }
}
