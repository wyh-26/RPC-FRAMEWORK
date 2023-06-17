package wyh.rpc.utils;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicNumberUtil {
    private static AtomicInteger atomicInteger = new AtomicInteger(0);
    public static Integer getAtomicInteger(){
        return atomicInteger.getAndIncrement();
    }
}
