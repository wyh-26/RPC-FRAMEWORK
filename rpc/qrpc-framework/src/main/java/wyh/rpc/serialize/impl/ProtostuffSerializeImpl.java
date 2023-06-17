package wyh.rpc.serialize.impl;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import lombok.extern.slf4j.Slf4j;
import wyh.rpc.serialize.Serialize;

@Slf4j
public class ProtostuffSerializeImpl implements Serialize {
    @Override
    public byte[] serialize(Object obj) {
        Schema schema = (Schema) RuntimeSchema.getSchema(obj.getClass());
        LinkedBuffer buffer = LinkedBuffer.allocate(1024 * 1024);

        byte[] protostuff;
        protostuff = ProtostuffIOUtil.toByteArray(obj , schema, buffer);
        buffer.clear();
        return protostuff;
    }

    @Override
    public <T>  T unSerialize(byte[] bytes , Class<T> clazz) {
        Schema schema = (Schema<T>) RuntimeSchema.getSchema(clazz);
        T instance = null;
        try {
            instance = clazz.newInstance();
        } catch (Exception e) {
            log.error("反序列化{}类失败", clazz.getCanonicalName());
        }
        ProtostuffIOUtil.mergeFrom(bytes , instance , schema);
        return instance;
    }
}
