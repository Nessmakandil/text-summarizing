package org.example;
import java.io.*;
import java.util.*;

public class Main {



    public static void main(String[] args) throws ParagraphException {
        double quota=100;
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
                System.out.println(line);
                paragraph+=line;
            }
        } catch (FileNotFoundException e) {
            throw new ParagraphException(ErrorParapraph.filenot);
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

        String cleanSententence=paragraph.replaceAll("\\s+"," ");
       LinkedHashSet<String> sentences = new LinkedHashSet<>(Arrays.asList(cleanSententence.split("\\.|\\. ")));
        List<String> stopWords = Arrays.asList("and", "or", "in", "a", "an", "the", "without", "with", "is");
        ArrayList<Integer> WordsWeight = new ArrayList<>();
        ArrayList<Integer> sentencesScore = new ArrayList<>();
        HashMap<String, Integer> sentenceMap = new HashMap<>();

        ArrayList<String> words = new ArrayList<>();
        System.out.println("\n");
        for (String s:
             sentences) {
            words.addAll(Arrays.asList(s.split(" ")));
        }
        for (String s : words) {
            if (!stopWords.contains(s)) {
                WordsWeight.add(Collections.frequency(words, s));
                System.out.println(s + " :word freq is: " + Collections.frequency(words, s));
            }
        }
        System.out.println("\n");
        int index = 0;
        int iterate = 0;
        for (String s : sentences) {
            int sum = 0;
            List<String> foo = Arrays.asList(s.split(" "));
            int len = foo.size();
            for (String x : foo) {
                if (stopWords.contains(x))
                    len -= Collections.frequency(foo, x);
            }
            index += len;
            while (iterate < index) {
                sum += WordsWeight.get(iterate);
                iterate++;
            }

            sentencesScore.add(sum);
            sentenceMap.put(s, sum);
            System.out.println("weight of sntnce is "+sum);
        }
        System.out.println("\n");

        int count = 0;
        int NumberOfSentences = sentences.size();

        int iterations = 0;
        try {
            System.out.println("please enter quota number please");
            quota = sc.nextDouble();

        }
        catch (InputMismatchException  e) {
            deleteDirectory(ff);
            System.out.println(ErrorParapraph.invaildquota);
            System.exit(1);
        }

//        iterations = (int) (NumberOfSentences * quota/100);
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
                sentences.forEach((s)-> System.out.println(s));
                deleteDirectory(ff);
                System.exit(0);
            }
        }
        catch (ParagraphException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        System.out.println("quota sntncs is "+iterations);

        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(sentenceMap.entrySet());
//        entryList.sort(Map.Entry.comparingByValue(Collections.reverseOrder()));
        System.out.println("\n");
        if(deleteDirectory(ff))
            System.out.println("success");;

        File NewFile=new File("outputs");
        NewFile.mkdir();
        int nextIterations = 0;
        int fct= 0;

        for(double i=quota;i>=10;i-=10)
        {
            count=0;

            iterations = (int) (NumberOfSentences * i/100);
            nextIterations = (int) (NumberOfSentences * (i-10)/100);
            if (nextIterations==iterations){
                fct++;
                continue;                               ////AVOID FILE WITH SAME SNTNC RPTN
            }
            for (Map.Entry<String, Integer> entry : entryList) {
                if (count == iterations)
                    break;
                System.out.println(entry.getKey() + " weight is " + entry.getValue());
                ///////////////////////////////////////new file

                try (FileWriter fileWriter = new FileWriter(NewFile+"\\outputpercentis"+(i+fct*10)+".txt",true);
                     BufferedWriter br = new BufferedWriter(fileWriter);) {
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