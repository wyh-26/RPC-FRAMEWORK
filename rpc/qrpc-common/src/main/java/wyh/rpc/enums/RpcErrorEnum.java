package wyh.rpc.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

public enum RpcErrorEnum {
    CLIENT_CONNECTION_FIAL(401 , "客户端连接失败");

    private Integer code;
    private String message;

}
