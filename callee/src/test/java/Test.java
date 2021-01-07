import com.zj.base.entity.DataType;
import com.zj.base.entity.RpcRequestEntity;
import com.zj.base.entity.RpcResponseEntity;
import com.zj.summerboot.entity.InvokeData;
import com.zj.summerboot.util.SummerTemplate;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author zj
 * @date 2021/1/5 19:05
 */
public class Test {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        SummerTemplate summerTemplate=new SummerTemplate();
        Object o = summerTemplate.get(10002,"caller", "com.zj.rpc.callee.CallerImpl", "caller", new Object[]{1, 2});
        System.out.println( o);
    }
}
