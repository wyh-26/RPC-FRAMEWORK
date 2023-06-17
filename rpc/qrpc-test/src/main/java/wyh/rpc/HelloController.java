package wyh.rpc;

import org.springframework.stereotype.Component;
import wyh.rpc.annotations.RpcReference;
import wyh.rpc.annotations.RpcService;
import wyh.rpc.service.Talk;

@Component
public class HelloController {
    @RpcReference(group = "group1" , version="version1")
    private Talk talk;
    public void test(){

        System.out.println(this.talk.talk());
    }

}
