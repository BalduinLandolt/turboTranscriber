package ch.blandolt.turboTranscriber.util.datastructure;

import ch.blandolt.turboTranscriber.util.Log;

import java.util.AbstractMap;
import java.util.Map;

public class XMLLookup {

    // TODO: initialize from Default
    // TODO: add option to load in custom lookups

    public static Map.Entry<String, String> lookUpAttribute(String name, String val){
        String key = "";
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

            case "supplied":
                key = "reason";
                break;

            case "hi":
                key = "rend";
                break;

            case "name":
                key = "type";
                break;

            case "fw":
                key = "type";
                break;

            case "div":
                if (val.matches("\\d+"))
                    key = "n";
                else if (val.equals("miracle"))
                    key = "type";
                break;

            default:
                key = "unknownKey";
                Log.log("!!! Warning: undefined Attribute in tag '"+name+"': ??? = "+val);
        }
        return new AbstractMap.SimpleEntry<String, String>(key, val);
    }

    public static boolean isSpecialCase__valueForName(String tagName) {
        switch (tagName){
            case "miracle":
            case "catch":
                return true;
            default:
                return false;

        }
    }

    public static String getSpecialCase__nameByValue(String tagName) {
        switch (tagName){
            case "miracle":
                return "div";
            case "catch":
                return "fw";
            default:
                return "unknownTagName";
        }
    }
}
