package ch.blandolt.turboTranscriber.util.datastructure;

import ch.blandolt.turboTranscriber.util.Log;

import java.util.AbstractMap;
import java.util.Map;

public class XMLLookup {

    // TODO: initialize from Default
    // TODO: add option to load in custom lookups

    public static Map.Entry<String, String> lookUpAttribute(String name, Map.Entry<String, String> attribute){
        String key = "";
        String val = attribute.getValue();
        switch (name){

            case "pb":
                if (val.matches("\\d+"))
                    key = "n";
                break;

            case "cb":
                if (val.matches("\\d+"))
                    key = "n";
                break;

            case "lb":
                if (val.matches("\\d+"))
                    key = "n";
                break;

            default:
                key = "unknownKey";
                Log.log("!!! Warning: undefined Attribute in tag '"+name+"': ??? = "+val);
        }
        return new AbstractMap.SimpleEntry<String, String>(key, val);
    }

}
