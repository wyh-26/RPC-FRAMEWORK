package wyh.rpc.remote.protocol;

import lombok.*;

import java.io.Serializable;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RpcRequest implements Serializable {
    private static final long serialVersionUID = 715745420605631233L;
    private String interfaceName;
    private String group;
    private String version;
    private String methodName;
    private Class<?>[] paramsType;
    private Object[] params;

    public String getServiceName(){
        return interfaceName + group + version;
    }
}
