package org.dm.apriori;

import java.util.*;

public class Apriori {

    public Map<Set<String>, Integer> executeApriori(
            Map<Set<String>, Integer> lSet, int support) {
        Map<Set<String>, Integer> finalResult = new HashMap<Set<String>, Integer>();
        finalResult.putAll(lSet);
        for (int i = 1; !lSet.isEmpty(); i++) {
            // first calculate the candidate set and get the pruned result.
            Map<Set<String>, Integer> cSet = generateCandidatesAndPrune(
                    lSet);
            // calculate the supportcount of these results.
            Map<Set<String>, Integer> cSetWithSupportCount = AprioriUtil
                    .calculateSupportCount(cSet,
                            AprioriMain.dataList);
            // filter the results according to the support provided.
            lSet = AprioriUtil.generateLFromCBySupport(
                    cSetWithSupportCount, support);
            System.out.println("Frequent item set of size " + (i + 1)
                    + " is --> " + lSet.size());
            finalResult.putAll(lSet);
        }
        return finalResult;
    }

    // for creating join of two sets
    private Map<Set<String>, Integer> selfJoin(
            Map<Set<String>, Integer> lSet) {
        List<Set<String>> set = new ArrayList<Set<String>>();
        set.addAll(lSet.keySet());
        Map<Set<String>, Integer> resultSet = new HashMap<Set<String>, Integer>();
        for (int i = 0; i < set.size(); i++) {
            Set<String> set1 = set.get(i);
            for (int j = i + 1; j < set.size(); j++) {
                Set<String> set2 = set.get(j);
                int count = 0;
                for (String s : set1) {
                    if (set2.contains(s))
                        count++;
                }
                if (count == set1.size() - 1) {
                    Set<String> newSet = new HashSet<String>();
                    newSet.addAll(set1);
                    newSet.addAll(set2);
                    resultSet.put(newSet, 0);
                }
            }
        }
        return resultSet;
    }

    private Map<Set<String>, Integer> generateCandidatesAndPrune(
            Map<Set<String>, Integer> lSet) {

        Map<Set<String>, Integer> joinResult = selfJoin(lSet);
        Map<Set<String>, Integer> prunedResult = prune(joinResult,
                lSet);
        return prunedResult;
    }

    // for pruning the data based on the apriori property
    private Map<Set<String>, Integer> prune(
            Map<Set<String>, Integer> joinResult,
            Map<Set<String>, Integer> lSet) {
        Map<Set<String>, Integer> prunedResult = new HashMap<Set<String>, Integer>();
        for (Set<String> set : joinResult.keySet()) {
            boolean flag = true;
            List<Set<String>> subsets = generateSubsets(set);
            for (Set<String> subset : subsets) {
                if (!lSet.containsKey(subset))
                    flag = false;
                if (flag) {
                    prunedResult.put(set, joinResult.get(set));
                }
            }
        }
        return prunedResult;
    }

    // for generating subsets of size n-1
    private List<Set<String>> generateSubsets(Set<String> set) {
        List<Set<String>> subsets = new ArrayList<Set<String>>();
        List<String> list = new ArrayList<String>();
        list.addAll(set);
        int n = list.size();
        for (int j = 1; j < n; j++) {
            Set<String> newSet = new HashSet<String>();
            newSet.add(list.get(0));
            for (int x = 1; x < n; x++)
                if (x != j)
                    newSet.add(list.get(x));
            subsets.add(newSet);
        }

        Set<String> newSet = new HashSet<String>();
        newSet.addAll(list.subList(1, list.size()));
        subsets.add(newSet);
        return subsets;
    }

    // generating association rules frpm the frequent item sets.
    public List<String> generateAssociationRules(
            Map<Set<String>, Integer> finalSet, int confidence) {
        Set<Set<String>> listsets = finalSet.keySet();
        List<String> assList = new ArrayList<String>();
        for (Set<String> lSet : listsets) {
            if (lSet.size() > 1) {
                Set<Set<String>> subsets = getPowerSet(lSet);
                for (Set<String> subset : subsets) {
                    if (!subset.isEmpty()
                            && subset.size() != lSet.size()) {
                        double lSupport = calculateSupport(lSet);
                        double sSupport = calculateSupport(subset);
                        if ((lSupport / sSupport)
                                * 100.0 >= confidence) {
                            Set<String> lcopy = new HashSet<String>(
                                    lSet);
                            lcopy.removeAll(subset);
                            String s = subset.toString() + "-->"
                                    + lcopy.toString();
                            assList.add(s);
                        }
                    }
                }
            }
        }
        return assList;
    }

    // for calculating the support of a particular set in the dataList
    private int calculateSupport(Set<String> set) {
        List<Map<String, Boolean>> dataList = AprioriMain.dataList;
        Map<Set<String>, Integer> subMap = new HashMap<Set<String>, Integer>();
        for (Map<String, Boolean> map : dataList) {
            boolean flag = true;
            for (String ele : set)
                flag = flag && map.get(ele);
            if (flag) {
                if (subMap.containsKey(set)) {
                    subMap.put(set, subMap.get(set) + 1);
                } else {
                    subMap.put(set, 1);
                }
            }
        }
        return subMap.get(set);
    }

    // for getting all the subsets of a set, it is useful in generating
    // association rules
    private Set<Set<String>> getPowerSet(Set<String> originalSet) {
        Set<Set<String>> sets = new HashSet<Set<String>>();
        if (originalSet.isEmpty()) {
            sets.add(new HashSet<String>());
            return sets;
        }
        List<String> list = new ArrayList<String>(originalSet);
        String head = list.get(0);
        Set<String> rest = new HashSet<String>(
                list.subList(1, list.size()));
        for (Set<String> set : getPowerSet(rest)) {
            Set<String> newSet = new HashSet<String>();
            newSet.add(head);
            newSet.addAll(set);
            sets.add(newSet);
            sets.add(set);
        }
        return sets;
    }
}
