package com.sinohydro.temp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;  
class point{  
    public double x;  
    public double y;  
    point(){  
        this.x=0;  
        this.y=0;  
    }  
}  
public class TempCode2 {  
        public static float caculate(point vertex[],int pointNum)  
        {  
            int i=0;  
            float temp=0;  
            for(;i<pointNum-1;i++)  
            {  
                temp+=(vertex[i].x-vertex[i+1].x)*(vertex[i].y+vertex[i+1].y);  
            }  
            temp+=(vertex[i].x-vertex[0].x)*(vertex[i].y+vertex[0].y);  
            return temp/2;  
        }  
        public static void main(String args[]) throws IOException   
        {  
        	readAndWriterTest3();
        } 
        public static void readAndWriterTest3() throws IOException { 
        	File file = new File("C:/Users/MY/Desktop/20180826.doc");
        	String str = ""; 
        	try {
        		FileInputStream fis = new FileInputStream(file);
        		HWPFDocument doc = new HWPFDocument(fis); 
        		String doc1 = doc.getDocumentText(); 
        		handleStr(doc1);
        		fis.close(); 
        		} catch (Exception e) {
        			e.printStackTrace(); 
        			} 
        	}
        public static List<String[]> handleStr(String str){
        	List<String[]> list=new ArrayList<String[]>();
        	String[] split = str.split("\r\n");
        	for (int i = 0; i < split.length; i++) {
        		System.out.println(split[i]);
			}
			return list;
        }
}  