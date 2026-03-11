package hu.eszter.chess.domain;

public class Game {

    private Long id;
    private String whitePlayer;
    private String blackPlayer;
    private String result;
    private String date;

    public Game() {
    }

    public Game(String whitePlayer, String blackPlayer) {
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        this.result = "*";
    }

    public Long getId() {
        return id;
    }

    public String getWhitePlayer() {
        return whitePlayer;
    }

    public String getBlackPlayer() {
        return blackPlayer;
    }

    public String getResult() {
        return result;
    }

    public String getDate() {
        return date;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setWhitePlayer(String whitePlayer) {
        this.whitePlayer = whitePlayer;
    }

    public void setBlackPlayer(String blackPlayer) {
        this.blackPlayer = blackPlayer;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
