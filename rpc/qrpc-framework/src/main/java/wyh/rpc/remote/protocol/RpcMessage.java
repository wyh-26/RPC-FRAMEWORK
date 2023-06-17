package wyh.rpc.remote.protocol;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RpcMessage {
    private Integer messageId;
    private byte messageType;
    private byte compressType;
    private byte serializeType;
    private Object data;
}
