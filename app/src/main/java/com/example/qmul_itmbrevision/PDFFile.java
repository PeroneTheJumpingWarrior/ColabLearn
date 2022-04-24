package com.example.qmul_itmbrevision;

public class PDFFile {


    public String name;
    public String url;

    public PDFFile(){

    }

    public PDFFile (String name, String url){

        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
