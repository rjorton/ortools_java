package ortools;

import java.io.*;
import java.util.Random;


public class Ortools {


    public static void main(String[] args) {

        System.out.println("\nortools started...\n");
        
        String help="To interleave 2 FASTQ files into 1 FASTQ file:\n";
        help+="java -jar ortools.jar -i PairedFile1.fastq PairedFile2.fastq OutputName\n";
        help+="Will create OutputName.fastq\n\n";
        
        help+="To de-intereave (split) an interleaved FASTQ into 2 paired files:\n";
        help+="java -jar ortools.jar -d InputFile.fastq\n";
        help+="Will filter out reads with no matching pair - relies on interleaving of original file\n";
        help+="Will create InputFile_1.fastq and InputFile_2.fastq\n\n";
        
        help+="To convert a FASTQ file to FASTA\n";
        help+="java -jar ortools.jar -a InputFile.fasta\n";
        help+="Will create InputFile.fasta\n\n";
        
        help+="To randomly sample N number of reads from a FASTQ file\n";
        help+="java -jar ortools.jar -r1 InputFile.fastq NumberOfReadsToSample\n";
        help+="Will create InputFile_N.fastq\n\n";
        
        help+="To randomly sample N number of pairs from a pair of FASTQ files\n";
        help+="java -jar ortools.jar -r2 PairedFile1.fastq PairedFile2.fastq NumberOfReadsToSample\n";
        help+="Will create PairedFile1_N.fastq and PairedFile2_N.fastq\n\n";
        
        help+="To filter FASTA sequences for those greater than length N\n";
        help+="java -jar ortools.jar -l InputFile.fasta MinimumSequenceLength\n";
        help+="Will create InputFile_N.fasta\n\n";
        
        help+="java -jar ortools.jar -h\n";
        help+="For help\n";
        
        String stub="", inFilename="", outFilename="", inPairname1="", inPairname2="", outPairname="", outPairname1="", outPairname2="";
        int state=0, rand=0, sLen=0;
        
        if(args.length>=1) {
            if(args[0].equalsIgnoreCase("-i")) {//interleave

                if(args.length!=4) {
                    System.out.println("Incorrect usage: -i argument expects 3 arguments: 2 input FASTQ filenames and 1 output name");
                    System.out.println(args.length+" arguments were supplied\n");
                }
                else {
                    inPairname1=args[1];
                    inPairname2=args[2];
                    stub=args[3];
                    
                    if(inPairname1.indexOf("/")>=0) 
                        outPairname=inPairname1.substring(0, inPairname1.lastIndexOf("/")+1)+stub+".fastq";
                    else
                        outPairname=stub+".fastq";
                    
                    state=1;
                }
            }
            else if(args[0].equalsIgnoreCase("-d")) {//deinterleave
                
                if(args.length!=2) {
                    System.out.println("Incorrect usage: -d argument expects 1 argument: 1 input FASTQ filename");
                    System.out.println(args.length+" arguments were supplied\n");
                }
                else {
                    inFilename=args[1];
                    outPairname1=inFilename.substring(0, inFilename.lastIndexOf("."))+"_1.fastq";
                    outPairname2=inFilename.substring(0, inFilename.lastIndexOf("."))+"_2.fastq";
                    
                    state=2;
                }
            }
            else if(args[0].equalsIgnoreCase("-a")) {//fq to fa

                if(args.length!=2) {
                    System.out.println("Incorrect usage: -a argument expects 1 argument: 1 input FASTQ filename");
                    System.out.println(args.length+" arguments were supplied\n");
                }
                else {
                    inFilename=args[1];
                    outFilename=inFilename.substring(0, inFilename.lastIndexOf("."))+".fasta";
                    
                    state=3;
                }
            }
            else if(args[0].equalsIgnoreCase("-r1")) {//random single end
                if(args.length!=3) {
                    System.out.println("Incorrect usage: -r argument expects 2 arguments: 1 input FASTQ filename and 1 number of random reads to select");
                    System.out.println(args.length+" arguments were supplied\n");
                }
                else {
                    inFilename=args[1];
                    rand=Integer.parseInt(args[2]);
                    outFilename=inFilename.substring(0, inFilename.lastIndexOf("."))+"_"+rand+".fastq";
                    
                    state=4;
                }
            }
            else if(args[0].equalsIgnoreCase("-r2")) {//random paired end
                if(args.length!=4) {
                    System.out.println("Incorrect usage: -r argument expects 3 arguments: 2 input FASTQ filename and 1 number of random reads to select");
                    System.out.println(args.length+" arguments were supplied\n");
                }
                else {
                    inPairname1=args[1];
                    inPairname2=args[2];
                    rand=Integer.parseInt(args[3]);
                    outPairname1=inPairname1.substring(0, inPairname1.lastIndexOf("."))+"_"+rand+".fastq";
                    outPairname2=inPairname2.substring(0, inPairname2.lastIndexOf("."))+"_"+rand+".fastq";

                    state=5;
                }
            }
            else if(args[0].equalsIgnoreCase("-l")) {//filter FASTA seqs based on length
                if(args.length!=3) {
                    System.out.println("Incorrect usage: -r argument expects 2 arguments: 1 input FASTA filename and 1 length to filter sequences");
                    System.out.println(args.length+" arguments were supplied\n");
                }
                else {
                    inFilename=args[1];
                    sLen=Integer.parseInt(args[2]);
                    outFilename=inFilename.substring(0, inFilename.lastIndexOf("."))+"_"+sLen+".fasta";

                    state=6;
                }
            }
            else if(args[0].equalsIgnoreCase("-h")) {//help
                System.out.println("Displaying help\n");
            }
            else {
                System.out.println("Incorrect usage: unrecognised argument: "+args[0]+"\n");
            }
        }
        else {
            System.out.println("Incorrect usage: no arguments supplied\n");
        }
        
        
        if(state==0) {
            System.out.println(help);
            System.exit(0);
        }
        
        else if(state==1) {//interleave
            
            System.out.println("-i option: Interleaving 2 paired FASTQ files into 1 FASTQ file");
            System.out.println("FASTQ pair file 1 = "+inPairname1);
            System.out.println("FASTQ pair file 2 = "+inPairname2);
            System.out.println("FASTQ interleaved output file = "+outPairname);
                    
            int count=0;
            int qCount=0;
            int rCount=0;
            
            try{
                FileWriter fstream = new FileWriter(outPairname);
                BufferedWriter out = new BufferedWriter(fstream);

                try {
                    BufferedReader streamIn1 =  new BufferedReader(new FileReader(inPairname1));
                    BufferedReader streamIn2 =  new BufferedReader(new FileReader(inPairname2));
                    
                    try {
                        String inLine1 = null;
                        String inLine2 = null;
                        
                        String read1="", read2="", name1="",name2="";
                        
                        while (( inLine1 = streamIn1.readLine()) != null) {
                            
                            inLine2 = streamIn2.readLine();
                            
                            count++;
                            qCount++;
                            
                            read1+=inLine1+"\n";
                            read2+=inLine2+"\n";
                            
                            if(qCount==1) {
                                
                                if(inLine1.indexOf("/")>0) {
                                    name1=inLine1.substring(1, inLine1.indexOf("/"));
                                    name2=inLine2.substring(1, inLine2.indexOf("/"));
                                }
                                else if(inLine1.indexOf(" ")>0) {
                                    name1=inLine1.substring(1, inLine1.indexOf(" "));
                                    name2=inLine2.substring(1, inLine2.indexOf(" "));
                                }
                                else {
                                    name1=inLine1;
                                    name2=inLine2;
                                }
                                
                                if(!name1.equalsIgnoreCase(name2)) {
                                    System.out.println("Warning - non-matching reads in the pair files "+count+" "+name1+" "+name2+" "+inLine1+" "+inLine2);
                                }    
                            
                                rCount++;
                            }
                            
                            if(qCount==4) {
                                
                                out.write(read1);
                                out.write(read2);
                                
                                read1="";
                                read2="";
                                qCount=0;
                            }
                        
                            //wondering if the very last line in file having a new line will cause a problem
                        }
                        
                        while (( inLine2 = streamIn2.readLine()) != null) {
                            System.out.println("Warning - extra lines present in 2nd pair file "+inLine2);
                        }
                    }

                    finally {
                        streamIn1.close();
                        streamIn2.close();
                    }
                } 

                catch (IOException ex) {
                    ex.printStackTrace();
                }


                out.close();

            }

            catch (Exception e) {
                e.printStackTrace();
                System.err.println("Error: " + e.getMessage());
            }
            
            System.out.println(rCount+" paired reads");
            System.out.println(count+" paired lines");
            System.out.println(rCount*2+" single reads outputted");
            System.out.println(count*2+" single lines outputted");
            
        }
        
        else if(state==2) {//deinterleave
            
            System.out.println("-d option: de-interleaving (splitting) 1 FASTQ files into 2 pairs");
            System.out.println("FASTQ interleaved input file = "+inFilename);
            System.out.println("FASTQ pair output file 1 = "+outPairname1);
            System.out.println("FASTQ pair output file 2 = "+outPairname2);
            
            int count=0;
            int qCount=0;
            int rCount=0;
            int oCount=0;
            int mCount=0;
            
            try{
                FileWriter fstream1 = new FileWriter(outPairname1);
                BufferedWriter out1 = new BufferedWriter(fstream1);
                
                FileWriter fstream2 = new FileWriter(outPairname2);
                BufferedWriter out2 = new BufferedWriter(fstream2);
                
                try {
                    BufferedReader streamIn =  new BufferedReader(new FileReader(inFilename));
                    
                    try {
                        String inLine = null;
                        
                        String read="", prevRead="", name="", prevName="blank", readline="", prevReadline="";
                        boolean test=false, prevTest=false
                                ;
                        while (( inLine = streamIn.readLine()) != null) {

                            count++;
                            qCount++;
                            
                            read+=inLine+"\n";
                            
                            if(qCount==1) {
                                
                                readline=inLine;
              
                                if(inLine.indexOf("/")>0) {
                                    name=inLine.substring(1, inLine.indexOf("/"));
                                }
                                else if(inLine.indexOf(" ")>0) {
                                    name=inLine.substring(1, inLine.indexOf(" "));
                                }
                                else {
                                    name=inLine;
                                }
                                
                                rCount++;
                            }
                            
                            if(qCount==4) {
                                
                                if(!prevName.equalsIgnoreCase("blank") & name.equalsIgnoreCase(prevName)) {
                                    out1.write(prevRead);
                                    out2.write(read);
                                    oCount++;
                                    test=true;
                                    
                                }
                                
                                if(test & prevTest) {
                                    System.out.println("Error? - read outputting twice "+count+" "+readline+" "+prevReadline);
                                }
                                if(!prevName.equalsIgnoreCase("blank") & !test & !prevTest) {
                                    mCount++;
                                }
                                prevName=name;
                                prevRead=read;
                                prevReadline=readline;
                                prevTest=test;
                                name=read=readline="";
                                test=false;
                                
                                qCount=0;
                            }

                            //wondering if the very last line in file having a new line will cause a problem with some progs
                        }
                    }

                    finally {
                        streamIn.close();
                    }
                } 

                catch (IOException ex) {
                    ex.printStackTrace();
                }


                out1.close();
                out2.close();

            }

            catch (Exception e) {
                e.printStackTrace();
                System.err.println("Error: " + e.getMessage());
            }
            
            System.out.println(rCount+" reads in interleaved file");
            System.out.println(count+" lines in interleaved file");
            System.out.println(oCount+" reads outputted into each pair file");
            System.out.println(oCount*4+" lines outputted into each pair file");
            System.out.println(mCount+" reads with no matching pair - not outputted");
        }
        
        else if(state==3) {//fq to fa
            
            System.out.println("-a option: converting FASTQ to FASTA");
            
            int count=0;
            int qCount=0;
            int rCount=0;
                
            try{
                FileWriter fstream = new FileWriter(outFilename);
                BufferedWriter out = new BufferedWriter(fstream);

                try {
                    BufferedReader streamIn =  new BufferedReader(new FileReader(inFilename));

                    try {
                        String inLine = null;

                        while (( inLine = streamIn.readLine()) != null) {

                            qCount++;
                            
                            if(qCount<3) {
                                if(inLine.indexOf("@")==0) {
                                    out.write(">"+inLine.substring(1,inLine.length())+"\n");
                                    rCount++;
                                }

                                else {
                                    out.write(inLine+"\n");
                                }
                            }
                            else if(qCount==4) {
                                qCount=0;
                            }
                            
                            count++;
                        }
                    }

                    finally {
                        streamIn.close();
                    }
                } 

                catch (IOException ex) {
                    ex.printStackTrace();
                }


                out.close();

            }

            catch (Exception e) {
                e.printStackTrace();
                System.err.println("Error: " + e.getMessage());
            }
            
            System.out.println(rCount+" reads");
            System.out.println(count+" lines");
            
        }
        
        else if (state==4) {
            System.out.println("-r1 option: randomly sampling reads from one FASTQ file - selecting "+rand+" reads");
            
            //This was used to investigate how much to elevate the random number by to alway achieve success - 5% seems good
            //But if the target number is low say 10 or 100 reads - it fails alot
            /*
            int failedCount=0;
            Random nextRand=new Random();
            for(int i=0;i<10000;i++) {
                int numberReads=6197676;
                int ouputTarget=10;
                
                double r=(double)ouputTarget/(double)numberReads*1.05;
                
                int outputReads=0;
                
                for(int j=0;j<numberReads;j++) {
                    if(nextRand.nextDouble()<=r)
                        outputReads++;
                }
                
                if(outputReads<ouputTarget)
                    failedCount++;
                
                System.out.println(i+" Number ="+outputReads+" "+failedCount);
            }
            */
            
            File inFile = new File(inFilename);

            int lCount=0;

            try {
                BufferedReader input =  new BufferedReader(new FileReader(inFile));

                try {
                    String line = null;

                    while (( line = input.readLine()) != null) {
                        lCount++;
                    }
                }
                finally {
                    input.close();
                }
            }

            catch (IOException ex) {
                ex.printStackTrace();
            }


            int rCount=lCount/4;
            
            if(lCount%4!=0)
                System.out.println("Error but continuing - number of lines in read file not divisible by 4 = "+lCount);
            
            int selectEvery=(lCount/4/rand)-1;
            int selectCount=0;
            
            double select=(double)rand*1.05/((double)lCount/4);//was 1.01 - changed to 1.05
            int eCount=0, sCount=0, nCount=0;
            
            boolean output=false;
            Random random=new Random();
            
            lCount=0;

            try{
                FileWriter fstream = new FileWriter(outFilename);
                BufferedWriter out = new BufferedWriter(fstream);

                try {
                    BufferedReader input =  new BufferedReader(new FileReader(inFile));

                    try {
                        String line = null;

                        while (( line = input.readLine()) != null) {

                            if(lCount==0) {
                                selectCount++;
                                
                                /*
                                if(selectCount==selectEvery) {
                                    output=true;
                                    sCount++;
                                    selectCount=0;
                                }
                                else {
                                    output=false;
                                    nCount++;
                                }
                                */
                                //Old way
                                if(random.nextDouble()<=select) {
                                    output=true;
                                    sCount++;
                                }
                                else {
                                    output=false;
                                    nCount++;
                                }
                            }

                            if(output) {
                                out.write(line="\n");
                                eCount++;
                            }

                            lCount++;

                            if(lCount==4) {
                                lCount=0;
                            }

                            if((eCount/4)==rand)
                                break;
                        }
                    }
                    finally {
                        input.close();
                    }
                }

                catch (IOException ex) {
                    ex.printStackTrace();
                }

                out.close();
            }

            catch (Exception e) {
                e.printStackTrace();
                System.err.println("Error: " + e.getMessage());
            }

            System.out.println("Randomly sampled reads outputted = "+sCount);
            System.out.println("Reads not outputted = "+(rCount-sCount));
            System.out.println("Total number of reads = "+rCount);
            System.out.println("Escaped at "+(nCount+sCount));
        }
        
        if(state==5) {
            System.out.println("-r2 option: randomly sampling paired reads from both paired files - selecting "+rand+" paired reads");
        
            File inFile1 = new File(inPairname1);
            File inFile2 = new File(inPairname2);

            int lCount=0;

            try {
                BufferedReader input =  new BufferedReader(new FileReader(inFile1));

                try {
                    String line = null;

                    while (( line = input.readLine()) != null) {
                        lCount++;
                    }
                }
                finally {
                    input.close();
                }
            }

            catch (IOException ex) {
                ex.printStackTrace();
            }
            
            int lCount2=0;
            
            try {
                BufferedReader input =  new BufferedReader(new FileReader(inFile2));

                try {
                    String line = null;

                    while (( line = input.readLine()) != null) {
                        lCount2++;
                    }
                }
                finally {
                    input.close();
                }
            }

            catch (IOException ex) {
                ex.printStackTrace();
            }

            int rCount=lCount/4;
            
            if(lCount!=lCount2) {
                System.out.println("Error but continuing - the paired files do not have the same number of lines: File1 = "+lCount+" File2 = "+lCount2);
            }
            
            if(lCount%4!=0) 
                System.out.println("Error but continuing - number of lines in read file not divisible by 4 = "+lCount);
            
            int selectEvery=(lCount/4/rand)-1;
            int selectCount=0;
            
            double select=(double)rand*1.05/((double)lCount/4);
            int eCount=0, sCount=0, nCount=0;
            
            boolean output=false;
            Random random=new Random();
            
            lCount=0;

            try{
                FileWriter fstream1 = new FileWriter(outPairname1);
                BufferedWriter out1 = new BufferedWriter(fstream1);
                FileWriter fstream2 = new FileWriter(outPairname2);
                BufferedWriter out2 = new BufferedWriter(fstream2);

                try {
                    BufferedReader input1 =  new BufferedReader(new FileReader(inFile1));
                    BufferedReader input2 =  new BufferedReader(new FileReader(inFile2));

                    try {
                        String line1 = null;
                        String line2 = null;

                        while (( line1 = input1.readLine()) != null) {

                            line2 = input2.readLine();

                            if(lCount==0) {
                                selectCount++;
                                /*
                                if(selectCount==selectEvery) {
                                    output=true;
                                    sCount++;
                                    selectCount=0;
                                }
                                else {
                                    output=false;
                                    nCount++;
                                }
                                */
                                //Old way
                                if(random.nextDouble()<=select) {
                                    output=true;
                                    sCount++;
                                }
                                else {
                                    output=false;
                                    nCount++;
                                }
                                
                            }

                            if(output) {
                                out1.write(line1+"\n");
                                out2.write(line2+"\n");
                                eCount++;
                            }

                            lCount++;

                            if(lCount==4) {
                                lCount=0;
                            }

                            if((eCount/4)==rand)
                                break;

                        }
                    }
                    finally {
                        input1.close();
                        input2.close();
                    }
                }

                catch (IOException ex) {
                    ex.printStackTrace();
                }

                out1.close();
                out2.close();
            }

            catch (Exception e) {
                e.printStackTrace();
                System.err.println("Error: " + e.getMessage());
            }

            System.out.println("Randomly sampled paired reads outputted = "+sCount);
            System.out.println("Paired reads not outputted = "+(rCount-sCount));
            System.out.println("Total number of paired reads = "+rCount);
            System.out.println("Escaped at "+(nCount+sCount));
        }
        
        if(state==6) {
            //public static void extractContigs(String thisFile, int thisLen) {

            System.out.println("-l option: extracting sequences above or equal to a minumum length: length = "+sLen);

            File inFile = new File(inFilename);

            String name="", seq="";
            int eCount=0, nCount=0, lCount=0;

            try{
                FileWriter fstream = new FileWriter(outFilename);
                BufferedWriter out = new BufferedWriter(fstream);

                try {
                    BufferedReader input =  new BufferedReader(new FileReader(inFile));

                    try {
                        String line = null;

                        while (( line = input.readLine()) != null) {

                            if(line.indexOf(">")==0) {

                                if(lCount>0) {
                                    if(seq.length()>=sLen & sLen>0) {
                                        eCount++;
                                        out.write(name+"\n"+seq+"\n");
                                    }
                                    else {
                                        nCount++;
                                    }
                                }
                                
                                name=line;
                                seq="";
                                    
                            }
                            else {
                                seq+=line;
                            }
                            
                            lCount++;
                        }
                    }
                    finally {
                        input.close();
                    }
                    
                    //catch the last
                    if(seq.length()>=sLen & sLen>0) {
                        eCount++;
                        out.write(name+"\n"+seq+"\n");
                    }
                    else {
                        nCount++;
                    }
                }

                catch (IOException ex) {
                    ex.printStackTrace();
                }

                out.close();
            }

            catch (Exception e) {
                e.printStackTrace();
                System.err.println("Error: " + e.getMessage());
            } 

            System.out.println("Outputted contigs above "+sLen+" = "+eCount);
            System.out.println("Total number of contigs = "+(eCount+nCount));
            System.out.println("Total number of lines = "+lCount);
       }
    

        System.out.println("\n...ortools finished");
    }
   
}
