package ch.blandolt.turboTranscriber.util.datastructure;

import ch.blandolt.turboTranscriber.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tokenizer {
    public static List<TranscriptionToken> tokenize(String text) {
        List<String> lines = Arrays.asList(text.split("\\n"));

        Log.log((List)lines);


        List<TranscriptionToken> res = new ArrayList<>();

        return res;
    }
    public static List<TranscriptionToken> tokenize(List<String> lines) {
        List<TranscriptionToken> res = new ArrayList<>();

        return res;
    }
}
