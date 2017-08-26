package com.company;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ListIterator;
import java.util.Scanner;

/**
 * Created by Jordan on 24/08/2017.
 */
public class Site {
    private String url;
    private ArrayList<String> sites;
    private boolean connectable;
    private URL thisURL;
    private Site parent;
    private ListIterator<String> siteIterator;
    private int depth;
    private HashSet<String> hs;

    public Site(String url, HashSet<String> hs) {
        this.hs = hs;
        this.parent = this;
        this.depth = 0;
        connectable = true;
        this.url = url;
        try {
            if(hs.contains(url)){
                throw new Exception("Already Hit");
            }
            thisURL = new URL(url);
            URLConnection connection = thisURL.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 5.1; rv:19.0) Gecko/20100101 Firefox/19.0");
            setSites(getURLs(connection.getInputStream()));
            this.siteIterator = sites.listIterator();
        }catch (MalformedURLException e){
            connectable = false;
            //BROKEN this.siteIterator = sites.listIterator();
        } catch (IOException e) {
            connectable = false;
            //BROKEN this.siteIterator = sites.listIterator();
        } catch (Exception e){
            connectable = false;
        }


    }

    public Site(String url, Site Parent, HashSet<String> hs) {
        this.hs = hs;
        this.parent = Parent;
        this.depth = parent.getDepth()+1;

        connectable = true;
        this.url = url;
        try {
            if(hs.contains(url)){
                throw new Exception("Already Hit");
            }
            thisURL = new URL(url);
            setSites(getURLs(thisURL.openStream()));
            this.siteIterator = sites.listIterator();

        }catch (MalformedURLException e){
            connectable = false;
            //BROKEN this.siteIterator = sites.listIterator();
        } catch (IOException e) {
            connectable = false;
            //BROKEN this.siteIterator = sites.listIterator();
        } catch (Exception e){
            connectable = false;
        }
    }

    private void setSites(ArrayList <String> links) {
        sites = links;
    }


    private ArrayList<String> getURLs(InputStream is) {
        ArrayList<String> al = new ArrayList<String>();
        Scanner sc = new Scanner(is);


        while(sc.hasNextLine()){
            String line = sc.nextLine();
            for(String sector : line.split("\"")){
                if( validURL(sector)) {
                    //urlPattern.matcher(sector).matches()
                    try {
                        String sectorURL = new URL(sector).toString();
                            if(!sectorURL.contains("www.w3.org") && !sectorURL.contains("ns.adobe.com") && !sectorURL.contains("%") && !sectorURL.contains("?")) {
                            al.add(sectorURL);
                            }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }

        }
        return al;
    }

    private boolean validURL(String url){
        boolean valid = true;
        try{
            new URL(url);
        }catch (MalformedURLException e){
            valid = false;
        }
        return valid;
    }

    public Site getParent(){
        return parent;
    }

    public boolean isValid(){
        return connectable;
    }

    public boolean hasNext(){
        return siteIterator.hasNext();
    }

    public Site nextSite(){
        long before = System.currentTimeMillis();
        Site newSite = new Site(siteIterator.next(), this, hs);
        long total = System.currentTimeMillis() -before;
        return newSite;
    }

    public String getUrl() {
        return url;
    }

    public int getDepth(){
        return depth;
    }
}
