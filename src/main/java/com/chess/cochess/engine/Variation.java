package com.chess.cochess.engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Variation implements Comparable<Variation> {

    private int depth;
    private int selDepth;
    private int multipv;
    private int centipawns;
    private int nodes;
    private int nps;
    private int tbhits;
    private int time;
    private List<String> moves = new ArrayList<>();

    public Variation(String line) {
        parse(line);
    }

    public int getDepth() {
        return depth;
    }

    public int getSelDepth() {
        return selDepth;
    }

    public int getMultipv() {
        return multipv;
    }

    public int getCentipawns() {
        return centipawns;
    }

    public int getNodes() {
        return nodes;
    }

    public int getNps() {
        return nps;
    }

    public int getTbhits() {
        return tbhits;
    }

    public int getTime() {
        return time;
    }

    public List<String> getMoves() {
        return moves;
    }

    private void parse(String line) {
        List<String> words = Arrays.asList(line.split(" "));
        LinkedList<String> linkedList = new LinkedList<>(words);
        parse(linkedList);
    }

    private void parse(LinkedList<String> words) {
        String field = words.removeFirst();
        if ("info".equals(field)) {
            parse(words);
        } else if ("depth".equals(field)) {
            String depthString = words.removeFirst();
            this.depth = Integer.parseInt(depthString);
            parse(words);
        } else if ("seldepth".equals(field)) {
            String depthString = words.removeFirst();
            this.selDepth = Integer.parseInt(depthString);
            parse(words);
        } else if ("multipv".equals(field)) {
            String multipvString = words.removeFirst();
            this.multipv = Integer.parseInt(multipvString);
            parse(words);
        } else if ("score".equals(field)) {
            String unit = words.removeFirst();
            if (!"cp".equals(unit)) {
                throw new IllegalStateException("Expected centipawns ('cp')");
            }
            String value = words.removeFirst();
            this.centipawns = Integer.parseInt(value);
            parse(words);
        } else if ("upperbound".equals(field)) {
            // Is this part of the score?
            // TODO: Need to investigate what this means!
            parse(words);
        }
        else if ("nodes".equals(field)) {
            String nodesString = words.removeFirst();
            this.nodes = Integer.parseInt(nodesString);
            parse(words);
        } else if ("nps".equals(field)) {
            String npsString = words.removeFirst();
            this.nps = Integer.parseInt(npsString);
            parse(words);
        } else if ("tbhits".equals(field)) {
            String tbhitsString = words.removeFirst();
            this.tbhits = Integer.parseInt(tbhitsString);
            parse(words);
        } else if ("time".equals(field)) {
            String timeString = words.removeFirst();
            this.time = Integer.parseInt(timeString);
            parse(words);
        } else if ("pv".equals(field)) {
            while (!words.isEmpty()) {
                moves.add(words.removeFirst());
            }
        } else {
            throw new IllegalStateException("Unexpected field: "+field);
        }
    }

    @Override
    public String toString() {
        return "Variation{" +
                "depth=" + depth +
                ", selDepth=" + selDepth +
                ", multipv=" + multipv +
                ", centipawns=" + centipawns +
                ", nodes=" + nodes +
                ", nps=" + nps +
                ", tbhits=" + tbhits +
                ", time=" + time +
                ", moves=" + moves +
                '}';
    }

    @Override
    public int compareTo(Variation other) {
        int otherDepth = other.getDepth();
        int depthCompare = Integer.compare(otherDepth, depth);
        if (depthCompare == 0) {
            int otherCp = other.getCentipawns();
            return Integer.compare(otherCp, centipawns);
        } else {
            return depthCompare;
        }

    }
}
