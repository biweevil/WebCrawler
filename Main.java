package com.company;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static final Pattern urlPattern = Pattern.compile("(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)" + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*" + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

    public static void main(String[] args) throws Exception {


        Scanner sc = new Scanner(System.in);
        boolean rootValid = false;
        String rootURL = null;
        do {
            try {
                openPage(rootURL = sc.nextLine());
                rootValid = true;
            } catch (IOException e) {
                System.out.println("Invalid URL " + e.getMessage());
            }
        } while (!rootValid);


        HashSet<String> hs = new HashSet<String>();


        Site currentSite;
        Site rootSite = new Site(rootURL, hs);

        currentSite = rootSite;
        hs.add(rootSite.getUrl());
        long compTest = 0;

        while (true) {


            while (currentSite.getParent().hasNext()) {
                while (currentSite.hasNext()) {
                    Site currentChild = currentSite.nextSite();
                    if (currentChild.isValid()) {
                        hs.add(currentChild.getUrl());
                        currentSite = currentChild;
                        for (int i = currentChild.getDepth()%20; i>0; i--) {
                            System.out.print('.');
                        }
                        System.out.println(currentChild.getUrl());
                    }
                }

                currentSite = currentSite.getParent();
                for (int i = currentSite.getDepth()%20; i>0; i--) {
                    System.out.print('.');
                }
                System.out.println(currentSite.getUrl());

            }
            currentSite = currentSite.getParent();
            for (int i = currentSite.getDepth()%20; i>0; i--) {
                System.out.print('.');
            }
            System.out.println(currentSite.getUrl());
        }
    }





    private static InputStream openPage(String url) throws IOException {
        return new URL(url).openStream();
    }

}

