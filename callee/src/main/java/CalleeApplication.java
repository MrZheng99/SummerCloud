import com.zj.base.exception.RegisterPasswordCheckFail;
import com.zj.service.core.ZRPC;

import java.io.IOException;

public class CalleeApplication {
    public static void main(String[] args) throws IOException {
        try {
            ZRPC.run(CalleeApplication.class);
        } catch (RegisterPasswordCheckFail registerPasswordCheckFail) {
            registerPasswordCheckFail.printStackTrace();
        }
    }
}
