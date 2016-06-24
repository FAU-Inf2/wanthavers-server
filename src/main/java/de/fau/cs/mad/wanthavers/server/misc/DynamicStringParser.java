package de.fau.cs.mad.wanthavers.server.misc;


public class DynamicStringParser {

    private static String escapeSymbolStart = "{{";
    private static String escapeSymbolEnd = "}}";
    private String str;

    private DynamicStringParser(String str){
        this.str = str;
    }

    public static DynamicStringParser parse(String str){
        return new DynamicStringParser(str);
    }

    public DynamicStringParser set(String key, String value){
        DynamicStringParser tsp = new DynamicStringParser(str.replace(this.escapeSymbolStart+key+this.escapeSymbolEnd, value));
        return tsp;
    }

    public String getValue(){
        return str;
    }

}
