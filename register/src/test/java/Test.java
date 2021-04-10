import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Test {
    public static void main(String[] args) {
        /**
         * 1.处理折行符
         * 2.统计出现次数
         */
        String fpath="D:\\Desktop\\毕业设计\\code\\register\\src\\main\\resources\\test.txt";
//        try {
//            FileReader fis = new FileReader(new File(fpath));
//            BufferedReader bis=new BufferedReader(fis);
//            StringBuilder sbf=new StringBuilder();
//            String str="";
//            int len=-1;
//            while ((str=bis.readLine()).length()>0){
//                sbf.append(str);
//            }
//            System.out.println(sbf.toString());
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        String str="a b big sys-\n" +
                "tem,oppo is\n" +
                "pig a b c c-\n" +
                "d";
        String[] strings=str.split("\n");
        Map<String,Integer> map=new HashMap<String, Integer>();
        boolean flag=false;
        for (int i = 0; i < strings.length; i++) {
            String temp=strings[i];
            String[] sp = temp.split("[ .,]");
            int len= sp.length;
            String rs= sp[len-1].replace("-","");
            if(!flag){
                for (int j = 0; j < len-1 ; j++) {
                    Integer integer = map.getOrDefault(sp[j],0);
                    map.put(sp[j],++integer);
                }
            }else{
                for (int j = 1; j < len-1 ; j++) {
                    Integer integer = map.getOrDefault(sp[j],0);
                    map.put(sp[j],++integer);
                }
            }
            if ("-".equals(sp[len-1].substring(sp[len-1].length()-1))){
                String nextLine = strings[i + 1];
                String split = nextLine.split("[ ,.]")[0];
                rs+=split;
//                System.out.println(rs);
                Integer integer = map.getOrDefault(rs,0);
                map.put(rs,++integer);
                flag=true;
            }else{
                Integer integer = map.getOrDefault(sp[len-1],0);
                map.put(sp[len-1],++integer);
                flag=false;
            }
        }
        for (Map.Entry<String,Integer> entry:map.entrySet()){
            System.out.println(entry.getKey()+":"+entry.getValue());
        }
    }
}
