package org.dm.apriori;
import java.util.*;

public class AprioriMain {
    static List<Map<String, Boolean>> dataList = new ArrayList<Map<String, Boolean>>();

    public static void main(String args[]) {
        dataList = AprioriUtil.getDataFromFile(dataList);
        System.out.println(dataList.size());
        int support=50;
        int confidence=70;
        Map<Set<String>,Integer> L1=AprioriUtil.generateL1(dataList,support);
        System.out.println("The size of 1 frequent item set with support "+support+" is-->"+L1.size());
        Apriori apriori=new Apriori();
        Map<Set<String>,Integer>finalResultSet=apriori.executeApriori(L1, support);
        System.out.println(finalResultSet.size());
        List<String> assMap=apriori.generateAssociationRules(finalResultSet,confidence);
        System.out.println(assMap.size());
    }
    
    
    
    
    
    
    
   
     

    



    
}
