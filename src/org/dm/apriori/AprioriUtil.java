package org.dm.apriori;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class AprioriUtil {

    public static List<Map<String, Boolean>> getDataFromFile(
            List<Map<String, Boolean>> dataList) {
        try {
            BufferedReader bufferedReader = new BufferedReader(
                    new FileReader("association-rule-test-data.txt"));
            String line = null;
            String[] brokenLine = null;
            try {
                while ((line = bufferedReader.readLine()) != null) {
                    brokenLine = line.split("\\t");
                    Map<String, Boolean> map = new TreeMap<String, Boolean>();
                    for (int i = 1; i < brokenLine.length - 1; i++) {
                        String actualValue = brokenLine[i];
                        String gene_up = "Gene" + i + "_UP";
                        String gene_down = "Gene" + i + "_DOWN";
                        if (actualValue.equalsIgnoreCase("up")) {
                            map.put(gene_up, true);
                            map.put(gene_down, false);
                        } else {
                            map.put(gene_up, false);
                            map.put(gene_down, true);
                        }
                    }
                    map.put("ALL", false);
                    map.put("AML", false);
                    map.put("Beast Cancer", false);
                    map.put("Colon Cancer", false);
                    map.put(brokenLine[brokenLine.length - 1], true);
                    dataList.add(map);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return dataList;
    }

    public static void print(List<Map<String, Boolean>> dataList) {
        for (int i = 0; i < dataList.size(); i++) {
            Map<String, Boolean> map = dataList.get(i);
            System.out.println(map.toString());
        }
    }

    public static Map<Set<String>, Integer> generateL1(
            List<Map<String, Boolean>> dataList, int minsupport) {
        // first generate candidate set
        Set<String> allGenes = dataList.get(0).keySet();
        Map<Set<String>, Integer> candidateSet = new HashMap<Set<String>, Integer>();
        for (String str : allGenes) {
            Set<String> set = new HashSet<String>();
            set.add(str);
            candidateSet.put(set, 0);
        }
        // now calculate support for each set
        Map<Set<String>, Integer> candidateSupportSet = calculateSupportCount(
                candidateSet, dataList);
        // remove the sets with minimum support
        return generateLFromCBySupport(candidateSupportSet,
                minsupport);
    }

    public static Map<Set<String>, Integer> calculateSupportCount(
            Map<Set<String>, Integer> candidateSet,
            List<Map<String, Boolean>> dataList) {

        // calculating support for each of the set in the candidate set.
        for (Set<String> set : candidateSet.keySet())
            for (Map<String, Boolean> map : dataList) {
                boolean flag = true;
                for (String ele : set)
                    flag = flag && map.get(ele);
                if (flag) {
                    if (candidateSet.containsKey(set)) {
                        candidateSet.put(set,
                                candidateSet.get(set) + 1);
                    } else {
                        candidateSet.put(set, 1);
                    }
                }
            }
        return candidateSet;
    }

    public static Map<Set<String>, Integer> generateLFromCBySupport(
            // removing the elements from the candidateset with their support
            // less than the minimum support
            Map<Set<String>, Integer> candidateSet, int minsupport) {
        Map<Set<String>, Integer> Lset = new HashMap<Set<String>, Integer>();
        for (Set<String> set : candidateSet.keySet()) {
            int support = candidateSet.get(set);
            if (support >= minsupport) {
                Lset.put(set, support);
            }
        }
        return Lset;
    }

    public static Set<String> getMatchingAssociations(List<String> associations, String type, int count, List<String> terms, int templateType ) {

        Set<String> resultSet = new HashSet<String>();
        for(String association: associations) {
            if(templateType == 1){
                if(isMatchingAssociation(association, type, count, terms)) {
                    resultSet.add(association);
                }
            } else if(templateType == 2){
                if(templateTwoFilter(association, type, count)) {
                    resultSet.add(association);
                }
            }
        }

        return resultSet;
    }

    private static boolean isMatchingAssociation(String association, String type, int count, List<String> terms) {
        //Set<String> resultSet = new HashSet<String>();
        int matched = 0;
        String body = association.split("-->")[0];
        String head = association.split("-->")[1];
        for(String term: terms) {
            switch(type) {
                case "RULE":
                    if(association.contains(term))
                        matched++;
                    break;

                case "BODY":
                    if(body.contains(term))
                        matched++;
                    break;

                case "HEAD":
                    if(head.contains(term))
                        matched++;
                    break;

                default:
                    break;
            }
        }

        if(count == 0 && matched == 0) {
            return true;
        } else if(count > 0 && matched == count) {
            return true;
        } else if(count == -1 && matched >= 1) {
            return true;
        }

        return false;
    }

    private static boolean templateTwoFilter(String association, String type, int count){
        int size = 0;
        switch(type) {
            case "RULE":
                size = association.split("-->")[0].split(",").length + association.split("-->")[1].split(",").length;
                break;

            case "BODY":
                size = association.split("-->")[0].split(",").length;
                break;

            case "HEAD":
                size = association.split("-->")[1].split(",").length;
                break;

            default:
                break;
        }

        if(size >= count){
            return true;
        }
        return false;
    }
}
