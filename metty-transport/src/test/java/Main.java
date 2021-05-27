import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.concurrent.Callable;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-03-31 7:22 PM
 */
public class Main {



    public static void main(String[] args) {
        byte[] bytes = "abcde".getBytes();
        ByteBuffer a  = ByteBuffer.allocate(bytes.length);
        a.put(bytes);
        a.flip();
        ByteBuffer b = ByteBuffer.allocate(bytes.length);
        b.put(bytes);
        b.flip();

        ByteBuffer newBuffer = ByteBuffer.allocate(bytes.length+bytes.length);
        newBuffer.put(a);
        newBuffer.put(b);
        newBuffer.flip();
        bytes = new byte[newBuffer.remaining()];
        newBuffer.get(bytes);
        System.out.println(new String(bytes));
    }

}