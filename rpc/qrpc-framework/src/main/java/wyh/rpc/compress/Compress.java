package wyh.rpc.compress;

public interface Compress {
    byte[] compress(byte[] data);
    byte[] uncompress(byte[] data);
}
