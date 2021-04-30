import com.zj.service.core.ZRPC;

import java.io.IOException;

public class CalleeApplication {
    public static void main(String[] args) throws IOException {
        ZRPC.run(CalleeApplication.class);
    }
}
