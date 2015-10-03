package org.dm.apriori;

import java.util.*;

public class AprioriMain {
    static List<Map<String, Boolean>> dataList = new ArrayList<Map<String, Boolean>>();

    public static void main(String args[]) {

        // Default values
        int support = 50;
        int confidence = 70;
        try {
            System.out.println("Enter Support: ");
            Scanner sc = new Scanner(System.in);
            support = sc.nextInt();
            System.out.println("Enter Confidence: ");
            confidence = sc.nextInt();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Using default values for Support (50) and Priori (70)");
        }


        System.out.println("\n ***************************** RUNNING APRIORI ALGORITHM ************************************* \n");

        //Apriori Implementation
        //******************************************************************
        long starttime = System.currentTimeMillis();
        // populating the data from the file
        // retrurned format is list of maps and key of map is genename and value
        // is true/false
        // example <gene_1_up,true> <gene_1_down,false>
        dataList = AprioriUtil.getDataFromFile(dataList);
        System.out.println("The sample size is-->" + dataList.size());

        // generating 1 frequent item set to give it to the apriori algorithm.
        Map<Set<String>, Integer> L1 = AprioriUtil
                .generateL1(dataList, support);
        System.out.println(
                "Frequent item set of size 1 " + "is-->" + L1.size());
        Apriori apriori = new Apriori();
        // executing the apriori algorithm
        // returned result is a map of sets and their support count
        // for example <[gene_96_down,gene_45_up],5> <[gene_96_down],45>
        Map<Set<String>, Integer> finalResultSet = apriori
                .executeApriori(L1, support);
        System.out.println(finalResultSet.size());
        // generating the association list based on the frequent item set
        // generated.
        List<String> assList = apriori
                .generateAssociationRules(finalResultSet, confidence);
        System.out.println(
                "Total number of Association rules for frequent items sets is "
                        + assList.size());
        List<String> newAssList = new ArrayList<String>();
        for (String str : assList) {
            str = str.replace("[", "").replace("]", "");
            newAssList.add(str);
        }
        System.out.println(newAssList.toString());
        long endTime = System.currentTimeMillis();
        long timetaken = (endTime - starttime);
        System.out.println("Total time taken is-->" + timetaken
                + " milliseconds");

        //**********************************************************************************

        for (String query : populateQueries()) {
            Set<String> result = AprioriUtil.getResultSet(newAssList, query);
            System.out.println("\n \nQUERY: " + query);
            System.out.println("SIZE: " + result.size());
            System.out.println("RESULT: " + result);
        }
    }

    private static List<String> populateQueries() {
        List<String> queries = new ArrayList<String>();
        queries.add("RULE HAS (ANY) of (Gene1_UP)");
        queries.add("RULE HAS (NONE) of (Gene1_UP)");
        queries.add("RULE HAS (1) of (Gene1_UP,Gene10_DOWN)");
        queries.add("BODY HAS (ANY) of (Gene1_UP)");
        queries.add("BODY HAS (NONE) of (Gene1_UP)");
        queries.add("BODY HAS (1) of (Gene1_UP,Gene10_DOWN)");
        queries.add("HEAD HAS (ANY) of (Gene1_UP)");
        queries.add("HEAD HAS (NONE) of (Gene1_UP)");
        queries.add("HEAD HAS (1) of (Gene1_UP,Gene10_DOWN)");
        // Template 2
        queries.add("SizeOf(RULE) >= 2");
        queries.add("SizeOf(BODY) >= 2");
        queries.add("SizeOf(HEAD) >= 2");
        //Template 3
        queries.add("BODY HAS (ANY) of (Gene1_UP) OR HEAD HAS (1) of (Gene59_UP)");
        queries.add("BODY HAS (ANY) of (Gene1_UP) AND HEAD HAS (1) of (Gene59_UP)");
        queries.add("BODY HAS (ANY) of (Gene1_UP) OR SizeOf(HEAD) >= 2 ");
        queries.add("BODY HAS (ANY) of (Gene1_UP) AND SizeOf(HEAD) >= 2");
        queries.add("SizeOf(BODY) >= 1 OR SizeOf(HEAD) >= 2");
        queries.add("SizeOf(BODY) >= 1 AND SizeOf(HEAD) >= 2");
        return queries;
    }
}
