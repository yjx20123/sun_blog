import com.yang.blog.service.IUserService;
import com.yang.blog.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Timer;
import java.util.TimerTask;

public class user {
    @Autowired
private IUserService usersevice;
    public static void main(String[] args) {
    }
    @mytest1(name = "s")
    public void test(){

    }
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface mytest1{
 String name();
 int age() default 1;
}
}
