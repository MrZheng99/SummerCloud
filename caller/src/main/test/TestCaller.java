import com.zj.summerboot.util.SummerTemplate;

public class TestCaller {
    public static void main(String[] args) {
        SummerTemplate summerTemplate=new SummerTemplate();
        Object o = summerTemplate.get(10002,"caller", "com.zj.rpc.caller.CallerImpl", "caller", new Object[]{4,
                9});
        System.out.println(o);
    }
}
