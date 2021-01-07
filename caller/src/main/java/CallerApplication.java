import com.zj.summerboot.core.SummerBoot;

import java.io.IOException;

public class CallerApplication {
    public static void main(String[] args) throws IOException {
        //调用callee的方法 打印结果
        SummerBoot.run(CallerApplication.class);
    }
}
