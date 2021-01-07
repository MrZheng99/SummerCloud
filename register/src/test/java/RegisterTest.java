import com.zj.base.constants.RegisterCenter;
import com.zj.base.entity.ServerInfo;
import org.junit.Test;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author zj
 * @date 2020/12/30 10:41
 */
public class RegisterTest {

    public void test() throws IOException {
        Socket sk=null;
        OutputStream outputStream=null;
        ObjectOutputStream oos=null;
        try {
            sk= new Socket("127.0.0.1",10000);
            outputStream = sk.getOutputStream();
            final ServerInfo info = new ServerInfo("test", "127.0.0.1",10002);
            oos= new ObjectOutputStream(outputStream);
            oos.writeObject(info);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            oos.close();
            outputStream.close();
            sk.close();
        }
    }

}
