import java.io.File; 
import java.io.IOException; 

class TSWriter {
    public static void main(String args[]) throws IOException { 
        long curr = System.currentTimeMillis();
        System.out.printf("WRITER: The current OS system time appears to me as: %s\n", curr); 
        
        File f1 = new File("/tmp/file1.ts");
        File f2 = new File("/tmp/file2.ts");
        System.out.printf("WRITER: -> Creating new files: %s %s\n", f1, f2);
        f1.createNewFile(); f2.createNewFile();

        long currFloored = (curr / 1000) * 1000; // truncate current time to just seconds
        long f1_x = currFloored + 111; // below midpoint
        long f2_x = currFloored + 999; // above midpoint
        System.out.printf("WRITER: -> Fudging modification timestamp of %s to %s\n", f1, f1_x);
        f1.setLastModified(f1_x);
        System.out.printf("WRITER: -> Fudging modification timestamp of %s to %s\n", f2, f2_x);
        f2.setLastModified(f2_x);
    } 
}

class TSReader { 
    public static void main(String args[]) throws IOException {
        File f1 = new File("/tmp/file1.ts");
        File f2 = new File("/tmp/file2.ts");
        System.out.printf("READER: Reading file lastModified timestamp of %s: %s\n", f1, f1.lastModified());
        System.out.printf("READER: Reading file lastModified timestamp of %s: %s\n", f2, f2.lastModified());
    }
}
