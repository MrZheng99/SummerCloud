import com.zj.zrpc.util.ZRpcTemplate;

public class TestCaller {
    public static void main(String[] args) {
        ZRpcTemplate summerTemplate=new ZRpcTemplate();
        Object o = summerTemplate.get(10002,"caller", "com.zj.rpc.caller.CallerImpl", "caller", new Object[]{4,
                9});
        System.out.println(o);
    }
}
