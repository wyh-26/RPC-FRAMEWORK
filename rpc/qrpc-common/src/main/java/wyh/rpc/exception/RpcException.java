package wyh.rpc.exception;

import wyh.rpc.enums.RpcErrorEnum;

public class RpcException extends RuntimeException{
    private Integer code;
    public RpcException(Integer code , String message){
        super(message);
        this.code = code;
    }

    public RpcException(RpcErrorEnum rpcErrorEnum){
        super(rpcErrorEnum.getMessage());
        this.code = rpcErrorEnum.getCode();

    }
}
