package org.example;
import java.io.*;
import java.util.*;
import java.util.stream.*;

public class Main {
     public static void main(String[] args)  {

        File ff=new File("outputs");
        Scanner sc=new Scanner(System.in);
//        String paragraph = "i love android and java.java is most popular language.android is key to mobile development. ava is a widely-used programming language for coding web applications. It has been a popular choice among developers for over two decades. with millions of Java applications in use today. Java is a multi platform. object-oriented. and network centric language that can be used as a platform in itself.";
        System.out.println("///////////////////////////a///////////////////////////////////////////");/////////////input prgrf ^ cmnt
        System.out.println("hello user please enter the path");
        String paragraph="";
        String upath=sc.next();
        File f=new File(upath);
        try {
            checkedFound(f);

        }
        catch (ParagraphException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        try (
                FileReader fr=new FileReader(upath);
                BufferedReader br = new BufferedReader(fr);
        ){
            String line;
            while ((line = br.readLine()) != null) {
                paragraph+=line;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("//////////////////////////////////////////////////////////////////////");
        try {
            if (paragraph.matches("\\d+")){
                System.out.println("enter string only");
                throw new ParagraphException(ErrorParapraph.digitonly);
            }
        } catch (ParagraphException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

        try {
            if(paragraph.equals("")||paragraph.matches("\\s+"))
            {
                throw  new ParagraphException(ErrorParapraph.emptyfile);
            }
        }
        catch (ParagraphException  e) {
            System.out.println(e.getMessage());
            System.exit(1);

        }

        int  P=paragraph.split("\\.").length;

        try {
            if (P==1) {
                throw  new ParagraphException(ErrorParapraph.containonesentence);


            }
        }
        catch (ParagraphException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

        String cleanParagraph=paragraph.trim().toLowerCase().replaceAll("\\s+"," ")
                .replaceAll("\\s\\.", "\\.")
                .replaceAll("\\.\\s", "\\.")
                .replaceAll("\\s\\.\\s ", "\\.");

         LinkedHashSet<String> sentences = new LinkedHashSet<>(Arrays.asList(cleanParagraph.trim().split("[.|,]")));
        List<String> stopWords = Arrays.asList("and", "or", "in", "a", "an", "the", "without", "with", "is", "to", "for");

        List<String> words = sentences.stream()
                .map(s -> s.trim().split("[\\s|-]")).flatMap(Arrays::stream)
                .collect(Collectors.toList());

        Map<String, Integer> sums = sentences.stream()
                .collect(Collectors.toMap(
                        s -> s,
                        s -> {
                            String[] wordsInSentence = s.trim().split("[\\s|-]");
                            int sum = Arrays.stream(wordsInSentence)
                                    .filter(word -> !stopWords.contains(word))
                                    .mapToInt(word -> Collections.frequency(words,word))
                                    .sum();
                            return sum;
                        },
                        (a,b)->a, LinkedHashMap::new
                ));

        System.out.println(sums);

         int quota=100;
         try {
            System.out.println("please enter quota number please");
            quota = sc.nextInt();
        }
        catch (InputMismatchException  e) {
            deleteDirectory(ff);
            System.out.println(ErrorParapraph.invaildquota);
            System.exit(1);
        }

        try {
            if(quota <= 0)
            {
                deleteDirectory(ff);
                throw  new ParagraphException(ErrorParapraph.negativequota);
            }
            if (quota > 100){
                deleteDirectory(ff);
                throw new ParagraphException(ErrorParapraph.invaildquota);
            }
            if (quota==100){
                sentences.forEach(System.out::println);
                deleteDirectory(ff);
                System.exit(0);
            }
        } catch (ParagraphException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

/*        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(sums.entrySet());
    entryList.sort(Map.Entry.comparingByValue(Collections.reverseOrder()));*/

        System.out.println("\n");
        if(deleteDirectory(ff))
            System.out.println("success");

        File NewFile=new File("outputs");
        NewFile.mkdir();

        int NumberOfSentences = sentences.size();
        int fct= 0;

         for(int i=quota;i>=10;i-=10)
         {
             int count=0;
             int iterations =  (NumberOfSentences * i)/100;
             int nextIterations = (NumberOfSentences * (i-10))/100;
             if (nextIterations==iterations){
                 fct++;
                 continue;                               ////AVOID FILE WITH SAME SNTNC RPTN
             }
             for (Map.Entry<String, Integer> entry : sums.entrySet()) {
                 if (count == iterations)
                     break;
                 System.out.println(entry.getKey() + " weight is " + entry.getValue());
                 ///////////////////////////////////////new file
                 try (FileWriter fileWriter = new FileWriter(NewFile+"\\outputpercentis"+(i+fct*10)+".txt",true);
                      BufferedWriter br = new BufferedWriter(fileWriter)) {
                     br.write(entry.getKey() + " weight is " + entry.getValue());
                     br.newLine();
                 } catch (IOException e) {
                 }
                 //////////////////////////////////////
                 count++;
             }
         }
     }
    private static void checkedFound(File f) throws ParagraphException {
        if (!f.exists())  {

            throw  new ParagraphException(ErrorParapraph.filenot);
        }
    }
    private  static boolean  deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }


}