package wyh.rpc.remote.protocol;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RpcResponse<T> implements Serializable {
    private static final long serialVersionUID = 1905122041950251205L;
    private Integer code;
    private String message;
    private T data;
}
