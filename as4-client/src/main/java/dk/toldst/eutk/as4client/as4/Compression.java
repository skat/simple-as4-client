package dk.toldst.eutk.as4client.as4;

import dk.toldst.eutk.as4client.exceptions.AS4Exception;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

//Maybe this is useful.
import javax.activation.DataHandler;


//https://jenkov.com/tutorials/java-zip/gzipinputstream.html
//https://stackoverflow.com/questions/16351668/compression-and-decompression-of-string-data-in-java
public class Compression {

    public static GZIPOutputStream compress(OutputStream outputStream) throws IOException {
        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputStream);
        return gzipOutputStream;
    }

    public static void decompress(OutputStream outputStream, InputStream inputStream) throws AS4Exception {
        try(GZIPInputStream gzipInputStream = new GZIPInputStream((inputStream)))
        {
            byte[] buffer = new byte[2048];
            int readindex = 0;
            while ((readindex = gzipInputStream.read(buffer)) > 0){
                outputStream.write(buffer, 0, readindex);
            }
        }
        catch (IOException io){
            throw new AS4Exception("Decompressing failed due to data corruption " + io.getMessage());
        }
    }

}
