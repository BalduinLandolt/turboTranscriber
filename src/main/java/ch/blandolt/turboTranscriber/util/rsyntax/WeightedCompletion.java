package ch.blandolt.turboTranscriber.util.rsyntax;

import org.fife.ui.autocomplete.BasicCompletion;
import org.fife.ui.autocomplete.Completion;
import org.fife.ui.autocomplete.CompletionProvider;

public class WeightedCompletion extends BasicCompletion {

    private int weight = 1;

    public WeightedCompletion(CompletionProvider provider, String replacementText) {
        this(provider, replacementText, 1);
    }

    public WeightedCompletion(CompletionProvider provider, String replacementText, int weight) {
        super(provider, replacementText, "("+weight+") ");
        this.weight = weight;
    }

    public WeightedCompletion(CompletionProvider provider, String replacementText, String shortDesc) {
        this(provider, replacementText, shortDesc, 1);
    }

    public WeightedCompletion(CompletionProvider provider, String replacementText, String shortDesc, int weight) {
        super(provider, replacementText, "("+weight+") "+shortDesc);
        this.weight = weight;
    }

    public WeightedCompletion(CompletionProvider provider, String replacementText, String shortDesc, String summary) {
        this(provider, replacementText, shortDesc, summary, 1);
    }

    public WeightedCompletion(CompletionProvider provider, String replacementText, String shortDesc, String summary, int weight) {
        super(provider, replacementText, "("+weight+") "+shortDesc, summary);
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public int compareTo(Completion other){
        if (other instanceof WeightedCompletion){
            WeightedCompletion wc = (WeightedCompletion)other;
            return -1*Integer.valueOf(getWeight()).compareTo(Integer.valueOf(wc.getWeight()));
        } else {
            return super.compareTo(other);
        }

    }
}
