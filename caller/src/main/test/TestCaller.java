import com.zj.service.util.ZRpcTemplate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestCaller {
    public static void main(String[] args) {
        ZRpcTemplate summerTemplate=new ZRpcTemplate();
        Object o = summerTemplate.get("127.0.0.1",10000,"caller", "com.zj.rpc.caller.CallerImpl", "caller",
                new Object[]{4,
                9});
        log.info("执行结果:{}",o);
    }
}
