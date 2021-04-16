package serializable;

import org.junit.Test;
import proto.entries.PersonMsg;

import java.io.*;
import java.util.ConcurrentModificationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-04-01 1:49 PM
 */
public class ProtoBufTest {

    @Test
    public void powTest(){
        System.out.println(1 << 30);
    }

    @Test
    public void writeObj() throws Exception {
        PersonMsg.Person person = PersonMsg.Person.newBuilder().setId(1).setEmail("a.163.com").setName("小明").build();
        person.writeTo(new FileOutputStream("/Users/ace-huang/idea/metty-parent/metty-transport/src/test/java/serializable/result"));
    }

    @Test
    public void readObj() throws Exception {
        PersonMsg.Person person = PersonMsg.Person.parseFrom(new FileInputStream("/Users/ace-huang/idea/metty-parent/metty-transport/src/test/java/serializable/result"));
        System.out.println(String.format("id=%s  name=%s  email=%s", person.getId(), person.getName(), person.getEmail()));
    }


    @Test
    public void intTest(){
        Integer a = new Integer(155);
        Integer b = new Integer(1);
        System.out.println(a.byteValue());
        System.out.println(b.byteValue());
    }

    @Test
    public void javaSerializableTest(){
        People people = new People("hcl",24);
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("/Users/ace-huang/idea/metty-parent/metty-transport/src/test/java/serializable/result"));
            outputStream.writeObject(people);
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("/Users/ace-huang/idea/metty-parent/metty-transport/src/test/java/serializable/result"));
            People nePeople = (People) inputStream.readObject();
            System.out.println(nePeople.getAge());
            System.out.println(nePeople.getName());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println();
    }

    @Test
    public void intAndByteTest(){
        int a = 100;
        byte b = intToByte(a);
        System.out.println(b);
        System.out.println("---------------------------");
        System.out.println(byteToInt(b));
        System.out.println("---------------------------");
        int e = 7;
        byte[] bytes = intToByteArray(e);
        for (byte f:bytes
             ) {
            System.out.println(f);
        }
        System.out.println("---------------------------");
        System.out.println(byteArrayToInt(bytes));

    }


    //byte 与 int 的相互转换
    public static byte intToByte(int x) {
        return (byte) x;
    }

    public static int byteToInt(byte b) {
        //Java 总是把 byte 当做有符处理；我们可以通过将其和 0xFF 进行二进制与得到它的无符值
        return b & 0xFF;
    }

    //byte 数组与 int 的相互转换
    public static int byteArrayToInt(byte[] b) {
        return   b[3] & 0xFF |
                (b[2] & 0xFF) << 8 |
                (b[1] & 0xFF) << 16 |
                (b[0] & 0xFF) << 24;
    }

    public static byte[] intToByteArray(int a) {
        return new byte[] {
                (byte) ((a >> 24) & 0xFF),
                (byte) ((a >> 16) & 0xFF),
                (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF)
        };
    }

    @Test
    public void threadTest(){

    }


}