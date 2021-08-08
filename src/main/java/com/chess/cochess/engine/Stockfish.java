package com.chess.cochess.engine;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A simple and efficient client to run Stockfish from Java.
 * Adapted from code by A R Rahul
 */
public class Stockfish {

    private BufferedReader processReader;
    private OutputStreamWriter processWriter;

    private String commandLine = "/usr/local/bin/stockfish";

    public Stockfish() {
        super();
    }

    public Stockfish(String commandLinePath) {
        this();
        this.commandLine = commandLinePath;
    }

    /**
     * Retrieve the path to the stockfish executable
     */
    public String getCommandLine() {
        return commandLine;
    }

    /**
     * Specify the path to the stockfish executable
     */
    public void setCommandLine(String commandLine) {
        this.commandLine = commandLine;
    }

    /**
     * Starts Stockfish engine as a process and initializes it
     *
     * @return True on success. False otherwise
     */
    public boolean startEngine() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(commandLine);
            Process engineProcess = processBuilder.start();
            processReader = new BufferedReader(new InputStreamReader(engineProcess.getInputStream()));
            processWriter = new OutputStreamWriter(engineProcess.getOutputStream());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Takes in any valid UCI command and executes it
     *
     * @param command the command to send to the chess engine
     */
    public void sendCommand(String command) {
        if (processWriter == null) {
            throw new IllegalStateException("Have you called startEngine?");
        }
        try {
            processWriter.write(command + "\n");
            processWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This is generally called right after 'sendCommand' for getting the raw
     * output from Stockfish
     *
     * @param waitTime
     *            Time in milliseconds for which the function waits before
     *            reading the output. Useful when a long running command is
     *            executed
     * @return Raw output from Stockfish
     */
    public String getOutput(int waitTime) {
        StringBuilder builder = new StringBuilder();
        try {
            Thread.sleep(waitTime);
            sendCommand("isready");
            while (true) {
                String text = processReader.readLine();
                if (text.equals("readyok"))
                    break;
                else
                    builder.append(text + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    /**
     * This function returns the best move for a given position after
     * calculating for 'waitTime' ms
     *
     * @param fen
     *            Position string
     * @param waitTime
     *            in milliseconds
     * @return Best Move in PGN format, or null if there is no best move
     */
    @Nullable
    public String getBestMove(String fen, int waitTime) {
        sendCommand("position fen " + fen);
        String positionOutput = getOutput(100);
        sendCommand("go movetime " + waitTime);
        String output = getOutput(waitTime + 30);
        String bestMove = output.split("bestmove ")[1].split(" ")[0].trim();
        return "(none)".equals(bestMove) ? null : bestMove;
    }


    @Nullable
    public List<Variation> getNBestMoves(int n, String fen, int waitTime) {
        sendCommand("position fen " + fen);
        String positionOutput = getOutput(100);
        sendCommand("go movetime " + waitTime);
        String output = getOutput(waitTime + 30);
        VariationParser variationParser = new VariationParser(output);
        System.out.println("Found "+variationParser.getVariationCount()+" variations");
        List<Variation> variations = variationParser.getVariations();
        return variations.stream().filter(v->v.getMoves().size() > 1).sorted().limit(n).collect(Collectors.toList());
    }

    /**
     * Stops Stockfish and cleans up before closing it
     */
    public void stopEngine() throws IOException {
            sendCommand("quit");
            processReader.close();
            processWriter.close();
    }

    /**
     * Get the evaluation score of a given board position
     * @param fen Position string
     * @param waitTime in milliseconds
     * @return evalScore
     */
    public float getEvalScore(String fen, int waitTime) {
        sendCommand("position fen " + fen);
        sendCommand("go movetime " + waitTime);

        float evalScore = 0.0f;
        String[] dump = getOutput(waitTime + 20).split("\n");
        for (int i = dump.length - 1; i >= 0; i--) {
            if (dump[i].startsWith("info depth ")) {
                try {
                    evalScore = Float.parseFloat(dump[i].split("score cp ")[1]
                            .split(" nodes")[0]);
                } catch(Exception e) {
                    evalScore = Float.parseFloat(dump[i].split("score cp ")[1]
                            .split(" upperbound nodes")[0]);
                }
            }
        }
        return evalScore/100;
    }
}