import com.zj.base.exception.RegisterPasswordCheckFail;
import com.zj.service.core.ZRPC;

import java.io.IOException;

public class CallerApplicationB {
    public static void main(String[] args) throws IOException {
        try {
            ZRPC.run(CallerApplicationB.class);
        } catch (RegisterPasswordCheckFail | IOException registerPasswordCheckFail) {
            registerPasswordCheckFail.printStackTrace();
        }
    }
}
