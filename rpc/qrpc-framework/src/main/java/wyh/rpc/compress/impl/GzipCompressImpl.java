package wyh.rpc.compress.impl;

import wyh.rpc.compress.Compress;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GzipCompressImpl implements Compress {
    @Override
    public byte[] compress(byte[] data) {
        if(data == null || data.length == 0){
            return data;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            GZIPOutputStream gzip = new GZIPOutputStream(out);
            gzip.write(data);
            gzip.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    @Override
    public byte[] uncompress(byte[] data) {
        if(data == null || data.length == 0){
            return data;
        }
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        try {
            GZIPInputStream gzip = new GZIPInputStream(in);
            int n;
            //从data中读取数据
            while((n = gzip.read(buffer)) >= 0){
                //读多少写多少
                out.write(buffer , 0 , n);
            }
            gzip.close();
            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }
}
