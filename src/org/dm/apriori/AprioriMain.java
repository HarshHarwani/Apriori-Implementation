package org.dm.apriori;
import java.util.*;

public class AprioriMain {
    static List<Map<String, Boolean>> dataList = new ArrayList<Map<String, Boolean>>();

    public static void main(String args[]) {
        long starttime=System.currentTimeMillis();
        //populating the data from the file
        //retrurned format is list of maps and key of map is genename and value is true/false
        //example <gene_1_up,true> <gene_1_down,false>
        dataList = AprioriUtil.getDataFromFile(dataList);
        System.out.println("The sample size is-->"+dataList.size());
        int support=70;
        int confidence=70;
        //generating 1 frequent item set to give it to the apriori algorithm.
        Map<Set<String>,Integer> L1=AprioriUtil.generateL1(dataList,support);
        System.out.println("Frequent item set of size 1 "+"is-->"+L1.size());
        Apriori apriori=new Apriori();
        //executing the apriori algorithm
        //returned result is a map of sets and their support count
        //for example <[gene_96_down,gene_45_up],5> <[gene_96_down],45>
        Map<Set<String>,Integer>finalResultSet=apriori.executeApriori(L1, support);
        System.out.println(finalResultSet.size());
        //generating the association list based on the frequent item set generated.
        List<String> assList=apriori.generateAssociationRules(finalResultSet,confidence);
        System.out.println("Total number of Association rules for frequent items sets is "+assList.size());
        System.out.println("Association rules are as follows:\n"+assList.toString());
        long endTime=System.currentTimeMillis();
        long timetaken=(endTime-starttime);
        System.out.println("Total time taken is-->"+timetaken+" milliseconds");
    }
    
    
    
    
    
    
    
   
     

    



    
}
