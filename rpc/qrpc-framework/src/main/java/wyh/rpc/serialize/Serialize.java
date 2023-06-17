package wyh.rpc.serialize;

public interface Serialize {
    public byte[] serialize(Object obj);
    public <T> T unSerialize(byte[] bytes , Class<T> clazz);

}
