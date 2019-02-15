package com.schoolbuy.utils;

import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import net.paoding.analysis.analyzer.PaodingAnalyzer; 

public class Segment {
	public static String show(Analyzer a, String s) throws Exception {  
		  
        StringReader reader = new StringReader(s);  
        TokenStream ts = a.tokenStream(s, reader);  
        String s1 = "", s2 = "";  
        boolean hasnext= ts.incrementToken();  
        //Token t = ts.next();  
        while (hasnext) {  
            //AttributeImpl ta = new AttributeImpl();  
            CharTermAttribute ta = ts.getAttribute(CharTermAttribute.class);  
            //TermAttribute ta = ts.getAttribute(TermAttribute.class);  
              
            s2 = ta.toString() + " ";  
            s1 += s2;  
            hasnext = ts.incrementToken();  
        }  
        return s1;  
    }  
  
    public String segment(String s) throws Exception {  
        Analyzer a = new PaodingAnalyzer();  
        return show(a, s);  
    } 
}
