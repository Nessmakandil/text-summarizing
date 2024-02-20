package org.example;
import java.util.HashMap;

public class ParagraphException extends  Exception {
    private ErrorParapraph errorCodes;

    private  static HashMap<ErrorParapraph,String> message=new HashMap<>();

    static  {
        message.put(ErrorParapraph.emptyfile,"file is empty");
        message.put(ErrorParapraph.filenot,"file not found");
        message.put(ErrorParapraph.invaildquota,"invaild percentage !! \n enter the correct percentage");
        message.put(ErrorParapraph.negativequota,"the percentage can't be negative");
        message.put(ErrorParapraph.containonesentence,"contain only one sentance");
        message.put(ErrorParapraph.digitonly,"contain only digits");
    }

    public ParagraphException(  ErrorParapraph errorCodes) {
        this.errorCodes = errorCodes;
    }

    @Override
    public String getMessage() {
        return message.get(errorCodes);
    }
}