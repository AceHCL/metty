package example3;

import java.io.Serializable;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-04-06 4:29 下午
 */
public class People implements Serializable {

    public People(String name, int age) {
        this.name = name;
        this.age = age;
    }

    private String name;
    private int age;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}