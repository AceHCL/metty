import java.io.UnsupportedEncodingException;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-03-29 1:21 PM
 */
public class Main {

    public static void main(String[] args) {
        byte[] bytes = new byte[0];
        try {
            bytes = "123".getBytes("UTF-16");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        for (byte e:bytes
             ) {
            System.out.println(e);
        }
    }
    interface C{}

     static class A implements C{
        public A() {
        }
    }

    static class B{

    }

}