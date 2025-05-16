package ink.whi;

import ink.whi.api.model.enums.PhotoUtil;
import ink.whi.core.utils.DateUtil;
import org.junit.Test;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: qing
 * @Date: 2023/4/27
 */
public class MyTest {


    @Test
    public void test() {
        String plainPwd = "123456";
        System.out.println(DigestUtils.md5DigestAsHex(plainPwd.getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    public void test3() {
        Integer i1 = 40;
        Integer i2 = new Integer(40);
        System.out.println(i1 == i2);
    }


    @Test
    public void test2() {
    }

    @Test
    public void modify() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(4);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == 4) list.remove(i);
        }
        System.out.println(list);

    }

    @Test
    public void testGenPhoto() {
        String url = PhotoUtil.genPhoto();
        System.out.println(url);
    }

    @Test
    public void testList() {
        class Item {
            int a;
            int b;
            Item(int a, int b) {
                this.a = a;
                this.b = b;
            }

            public String toString() {
                return a + " " + b;
            }
        }
        ArrayList<Item> list = new ArrayList<>();
        Item item = new Item(1, 2);
        list.add(item);
        System.out.println(list);
        item.a = 3;
        System.out.println(list);
    }

    @Test
    public void test4() {
        Date timer = DateUtil.format("2025-05-10");
        Date timer2 = DateUtil.format("2025-05-30");
        System.out.println(timer);
        System.out.println(timer2);
        System.out.println(timer.before(timer2));
    }

}
