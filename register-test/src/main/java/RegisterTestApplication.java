import com.zj.base.exception.RegisterPasswordCheckFail;
import com.zj.service.core.ZRPC;

import java.io.IOException;

/**
 * @author zj
 * @date 2020/12/30 17:02
 */
public class RegisterTestApplication {
    public static void main(String[] args) throws IOException {
        try {
            ZRPC.run(RegisterTestApplication.class);
        } catch (RegisterPasswordCheckFail registerPasswordCheckFail) {
            registerPasswordCheckFail.printStackTrace();
        }
    }
}
