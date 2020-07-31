package ch.blandolt.turboTranscriber.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SuggestionCounter<E> extends ArrayList<E> {
    private Map<E,Integer> counter = new HashMap<E,Integer>();

    @Override
    public synchronized boolean add(E key) {
        if (counter.containsKey(key)) {
            counter.put(key, counter.get(key) + 1);
        } else {
            counter.put(key, 1);
        }
        return super.add(key);
    }

    @Override
    public synchronized boolean addAll(Collection<? extends E> collection) {
        if (collection == null){
            return false;
        }
        for (E key : collection) {
            add(key);
        }
        return super.addAll(collection);
    }

    public int getCount(E key) {
        return counter.containsKey(key) ? counter.get(key) : 0;
    }

	public Map<E, Integer> asMap() {
		return counter;
	}
}