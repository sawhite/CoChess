package com.chess.cochess.engine;

import java.util.ArrayList;
import java.util.List;

public class VariationParser {

    private List<Variation> variations = new ArrayList<>();

    public VariationParser(String output) {
        super();
        parse(output);
    }

    private List<Variation> parse(String output) {
        String[] lines = output.split("\\R");
        for (String line : lines) {
            if (line.startsWith("info") && line.contains("depth")) {
                variations.add(new Variation(line));
            }
        }
        return variations;
    }

    public List<Variation> getVariations() {
        return variations;
    }

    public int getVariationCount() {
        return variations.size();
    }
}
