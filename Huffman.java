package huffman;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import javax.imageio.ImageIO;


class Node{
        Character c;
        int freq,height;
        Node left,right;
        String current_code="";
        Node(int freq,char c){
            this.freq = freq;
            this.c = c;
        }
        Node(){}
        String arr[] = new String[90000];
        public  void printCode(Node root, String s) {
        if (root.left == null&& root.right == null && root.c!=null) {
//            arr[root.c] = s;
            System.out.println(root.c+":"+s);
            return;
        }
        printCode(root.left, s+"0");
        printCode(root.right, s+"1");
    }
       public void set_cc(Node root){
            ArrayDeque<Node> q = new ArrayDeque();
            q.addFirst(root);
            while(!q.isEmpty()){
                Node temp = q.removeLast();
                if(temp.left==null && temp.right==null && temp.c!=null){ 
                    arr[temp.c]=temp.current_code;
                }
                if(temp.right!=null){temp.right.current_code = temp.current_code+"1"; q.addFirst(temp.right);}
                if(temp.left!=null) {temp.left.current_code = temp.current_code+"0"; q.addFirst(temp.left);}
            }
        }
        
 void p(Node no) {
    int h = height(no);
    int i;
    for (i=1; i<=h; i++){
        printGivenLevel(no, i);
        System.out.println();
    }
}

 int height(Node no){
            return no==null?0:(1+Math.max(height(no.left),height(no.right)));
     }
 void printGivenLevel(Node no, int level){
    if (no == null){
        System.out.print("#,");
        return;}
    String s = Integer.toString(no.freq);
    if(no.c!=null) s = new String(new char[]{no.c});
    if (level == 1)
        System.out.print(s+",");
    else if (level > 1){
        printGivenLevel(no.left, level-1);
        printGivenLevel(no.right, level-1);
  }
 }
}

public class Huffman {
    
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        Comparator<Node> c = (n1,n2) -> { if(n1.freq==n2.freq) return n1.height-n2.height;return n1.freq-n2.freq;};
        PriorityQueue<Node> hpr = new PriorityQueue(c);
       int arr[] = new int[90000];
       //Reading from File
        System.out.println("Enter the name of the file to be compressed...");
         System.out.println("Hint : Please add its extension with it , In example Khaled.txt etc..");
        System.out.println("Hint : If the file is NOT in the package of this project, enter its FULL URL");
       String Original_File = sc.nextLine();
       File file = new File(Original_File);
          FileReader fr = new FileReader(file);
          BufferedReader br = new BufferedReader(fr);        
            //Count frequency
            int ch;
            while((ch=br.read())!=-1){
            arr[ch]++;
            }
            //Adding to the Priority Queue
            for(int i =0;i<arr.length;i++){
            if(arr[i]!=0){
            hpr.add(new Node(arr[i],(char)i));
            }
            }
            // Creating the Huffman Tree
            Node root = null;
            while (hpr.size() > 1){
            Node x = hpr.poll();
            Node y = hpr.poll();
            Node f = new Node();
            f.freq = x.freq + y.freq;
            f.c= null;
            f.left = x;
            f.right = y;
            // Optimizing the height huffman Tree by using height variable to lessen the code length
            f.height = 1+Math.max(x.freq,y.freq);
            root = f;
            hpr.add(f);
            }
            root.set_cc(root);
//            root.p(root);
            System.out.println("----------------");
//            root.printCode(root,"");
            //Putting the compressed code into a file
            System.out.println("Enter the name of the compressed file...");
            System.out.println("Hint : Please add its extension with it , In example Khaled.txt etc..");
            System.out.println("Hint : It will be created in the package of this project");           
            String Compressed_File_Name = sc.nextLine();
            PrintWriter writer = new PrintWriter(Compressed_File_Name);
            file = new File(Original_File);
            fr = new FileReader(file);
            br = new BufferedReader(fr);
            while((ch=br.read())!=-1 ){
            writer.write(root.arr[ch]);
          }
            writer.close();
           // Decompressing the compressed file into a third one file
           System.out.println("Enter the name of decompressed file...");
            System.out.println("Hint : Please add its extension with it , In example Khaled.txt etc..");
             System.out.println("Hint : It will be created in the package of this project");
           String Decompressed_File = sc.nextLine();
          PrintWriter printer = new PrintWriter(Decompressed_File);
            Reader r = new BufferedReader(new InputStreamReader(
          new FileInputStream(new File(Compressed_File_Name)), "US-ASCII"));
            int cc;
          Node trav = root;
          while(true){
            if(trav.c!=null) {printer.write(trav.c); trav=root;}
            else {
               cc=r.read();
               if(cc==-1) break;
             if(cc==49) trav=trav.right;
             else if(cc==48) trav = trav.left;
            }
          }
          printer.close();
    }
}