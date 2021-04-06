package serializable;

import java.io.Serializable;

/**
 * 描述:
 *
 * @author ace-huang
 * @create 2021-04-01 1:59 PM
 */
public class People  implements Serializable {
    private String name;
    private int age;

    public People(String name, int age) {
        this.name = name;
        this.age = age;
    }

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