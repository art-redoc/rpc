package art.cain.consumer;

import art.cain.consumer.controller.RpcController;
import art.cain.interfaces.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RPCTest {

    @Autowired
    private RpcController rpcController;

    @Test
    public void test01() throws InterruptedException {
        int count = 30;
        CountDownLatch c = new CountDownLatch(count);
        for (int i = 0; i < count; i++) {
            new Thread(() -> {
                int rand = new Random().nextInt(3);
                String username = "cain";
                String password = "qweq";
                if (rand <= 1) {
                    username = getRandomString(1024);
                }
                User user = rpcController.login(username, password);
                System.out.println(user.getName() + ":" + user.getAge());
                c.countDown();
            }).start();
        }
        c.await();
    }

    private String getRandomString(int length) {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(3);
            long result = 0;
            switch (number) {
                case 0:
                    result = Math.round(Math.random() * 25 + 65);
                    sb.append((char) result);
                    break;
                case 1:
                    result = Math.round(Math.random() * 25 + 97);
                    sb.append((char) result);
                    break;
                case 2:
                    sb.append(new Random().nextInt(10));
                    break;
            }


        }
        return sb.toString();
    }
}
