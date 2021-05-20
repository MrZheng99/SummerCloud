
import com.zj.base.exception.RegisterPasswordCheckFail;
import com.zj.service.core.ZRPC;

import java.io.IOException;

public class CallerApplication {
    public static void main(String[] args) throws IOException {
        //调用callee的方法 打印结果
        try {
            ZRPC.run(CallerApplication.class);
        } catch (RegisterPasswordCheckFail registerPasswordCheckFail) {
            registerPasswordCheckFail.printStackTrace();
        }
    }

}
