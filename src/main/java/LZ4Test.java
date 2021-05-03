import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Random;

import net.jpountz.lz4.LZ4BlockInputStream;
import net.jpountz.lz4.LZ4BlockOutputStream;
import net.jpountz.lz4.LZ4Factory;
import net.jpountz.xxhash.XXHashFactory;

public class LZ4Test {
    public static void LZ4compress(String generatedString){
        byte[] buf = new byte[2048];
        byte[] buf2 = new byte[2048];
        try {
            System.out.println("In compress");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            LZ4BlockOutputStream lz4out = new LZ4BlockOutputStream(
                    baos,
                    32*1024*1024,
                    LZ4Factory.fastestInstance().fastCompressor(),
                    XXHashFactory.fastestInstance().newStreamingHash32(0x9747b28c).asChecksum(),
                    false);

            InputStream in = new ByteArrayInputStream(generatedString.getBytes());

            System.out.println("Storing compressed bytes in stream");
            int len;
            while((len = in.read(buf)) > 0){
                lz4out.write(buf, 0, len);
            }
            System.out.println("Finished compression");
            //System.out.println("printing boas" + Arrays.toString(baos.toByteArray()));

            System.out.println("In un-compress");
            InputStream in2 = new ByteArrayInputStream(baos.toByteArray());
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            LZ4BlockInputStream lz4in = new LZ4BlockInputStream(
                    in2,
                    LZ4Factory.fastestInstance().fastDecompressor(),
                    XXHashFactory.fastestInstance().newStreamingHash32(0x9747b28c).asChecksum(),
                    false);

            System.out.println("Storing un-compressed bytes in stream");
            int len2;
            while((len2 = lz4in.read(buf2)) > 0){
                out.write(buf2, 0, len2);
            }
            in.close();
            out.close();
            in2.close();
            lz4in.close();
            lz4out.close();

            System.out.println("Finished un-compression");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        public static void main(String[] args) throws Exception {

            System.out.println("Generating random string of size 512 MB");

            byte[] array = new byte[512000000]; // 512MB
            new Random().nextBytes(array);
            String generatedString = new String(array, Charset.forName("UTF-8"));

            LZ4compress(generatedString);
        }
}

