import com.zj.zrpc.core.ZRPC;

import java.io.IOException;

public class CallerApplication {
    public static void main(String[] args) throws IOException {
        //调用callee的方法 打印结果
        ZRPC.run(CallerApplication.class);
    }
}
