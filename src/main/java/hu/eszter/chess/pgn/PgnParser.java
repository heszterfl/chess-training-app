package hu.eszter.chess.pgn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PgnParser {

    public PgnGame parse(String pgnText){

        if (pgnText == null || pgnText.isBlank()) {
            throw new IllegalArgumentException("Invalid input");
        }

        String[] lines = pgnText.trim().split("\n");

        StringBuilder sb = new StringBuilder();

        List<String> header = new ArrayList<>();
        for (String l : lines) {
            String line = l.trim();
            if (line.isBlank()) {
                continue;
            }
            if (line.charAt(0) == '[') {
                header.add(line);
            } else {
                sb.append(line);
                sb.append(" ");
            }
        }

        String moveText = sb.toString();

        Map<String, String> parsedHeader = parseHeader(header);
        String event = parsedHeader.get("Event");
        String site = parsedHeader.get("Site");
        String date = parsedHeader.get("Date");
        String white = parsedHeader.get("White");
        String black = parsedHeader.get("Black");
        String result = parsedHeader.get("Result");

        List<String> moveTokens = cleanMoveTokens(moveText);

        if (moveTokens.isEmpty()) {
            throw new IllegalArgumentException("Empty move text");
        }

        return new PgnGame(event, site, date, white, black, result, moveTokens);
    }

    private Map<String, String> parseHeader(List<String> header) {
        Map<String, String> headerMap = new HashMap<>();

        for (String h : header) {
            int firstSpace = h.indexOf(' ');
            if (firstSpace == -1) {
                continue;
            }

            String key = h.substring(1, firstSpace);
            String value = h.substring(firstSpace + 2, h.length() - 2);
            headerMap.put(key, value);
        }

        return headerMap;
    }

    private List<String> cleanMoveTokens(String moveText) {
        String[] splitToken = moveText.split(" ");
        List<String> tokenList = new ArrayList<>();
        for (String s : splitToken) {
            if (s.matches("[0-9]+\\.")) {
                continue;
            } else if (s.equals("1-0") || s.equals("0-1") || s.equals("1/2-1/2") || s.equals("*")) {
                continue;
            } else if (s.isEmpty()) {
                continue;
            } else {
                tokenList.add(s);
            }
        }

        return tokenList;
    }
}
