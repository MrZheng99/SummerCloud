import com.zj.summerboot.core.SummerBoot;

import java.io.IOException;

public class CalleeApplication {
//    public static void main(String[] args) {
//        try {
//            start();
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//    public static void start() throws IOException, ClassNotFoundException {
//        ServerSocket ssk = new ServerSocket(9999);
//        System.out.println("启动该服务成功...占用端口9999");
//        //TODO:这里可以将启动的服务注册到rpc中间件去
//        Socket sk =null;
//        while (true){
//            System.out.println("进来请求了");
//            sk= ssk.accept();
//            //获取流
//            InputStream inputStream = sk.getInputStream();
//            //序列化
//            ObjectInputStream ois = new ObjectInputStream(inputStream);
//            RpcRequestEntity requestEntity = (RpcRequestEntity)ois.readObject();
//        }
//    }
    public static void main(String[] args) throws IOException {
        SummerBoot.run(CalleeApplication.class);
    }
}
