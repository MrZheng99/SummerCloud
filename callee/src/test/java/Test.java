import com.zj.zrpc.util.ZRpcTemplate;

import java.io.*;

/**
 * @author zj
 * @date 2021/1/5 19:05
 */
public class Test {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        ZRpcTemplate summerTemplate=new ZRpcTemplate();
        Object o = summerTemplate.get(10002,"caller", "com.zj.rpc.callee.CallerImpl", "caller", new Object[]{1, 2});
        System.out.println( o);
    }
}
