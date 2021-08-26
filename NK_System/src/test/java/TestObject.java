import java.util.ArrayList;
import java.util.List;


/**
 * 测试转成数组对象
 */
public class TestObject {
    public static void main(String[] args) {
        List<Integer> list=new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);
        Integer[] integers = list.toArray(new Integer[list.size()]);
        for(int i:integers){
            System.out.println(i);
        }

    }
}
