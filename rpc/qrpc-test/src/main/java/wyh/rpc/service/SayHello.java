package wyh.rpc.service;


import wyh.rpc.annotations.RpcService;

@RpcService(group = "group1" , version = "version1")
public class SayHello implements Talk{
    @Override
    public String talk() {
        return "hello";
    }
}
