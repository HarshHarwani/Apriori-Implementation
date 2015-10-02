package org.dm.apriori;
import java.util.*;

public class AprioriMain {
    static List<Map<String, Boolean>> dataList = new ArrayList<Map<String, Boolean>>();

    public static void main(String args[]) {
        long starttime=System.currentTimeMillis();
        dataList = AprioriUtil.getDataFromFile(dataList);
        System.out.println("The sample size is-->"+dataList.size());
        int support=50;
        int confidence=70;
        Map<Set<String>,Integer> L1=AprioriUtil.generateL1(dataList,support);
        System.out.println("Frequent item set of size 1 "+"is-->"+L1.size());
        Apriori apriori=new Apriori();
        Map<Set<String>,Integer>finalResultSet=apriori.executeApriori(L1, support);
        System.out.println(finalResultSet.size());
        List<String> assMap=apriori.generateAssociationRules(finalResultSet,confidence);
        System.out.println("Total number of Association rules for frequent items sets is "+assMap.size());
        System.out.println("Association rules are as follows:\n"+assMap.toString());
        long endTime=System.currentTimeMillis();
        long timetaken=(endTime-starttime);
        System.out.println("Total time taken is-->"+timetaken+" milliseconds");
    }
    
    
    
    
    
    
    
   
     

    



    
}
