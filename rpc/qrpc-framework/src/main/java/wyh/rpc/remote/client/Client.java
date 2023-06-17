package wyh.rpc.remote.client;

import wyh.rpc.remote.protocol.RpcMessage;
import wyh.rpc.remote.protocol.RpcRequest;
import wyh.rpc.remote.protocol.RpcResponse;

import java.util.concurrent.CompletableFuture;

public interface Client {
    public CompletableFuture<RpcResponse<Object>> sendRpcMessage(RpcRequest rpcRequest);
}
