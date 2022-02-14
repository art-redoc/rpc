package art.cain.consumer.controller;

import art.cain.interfaces.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

@RestController
public class Controller {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RpcController rpcController;

    @GetMapping("/")
    public String hello() {
        return "hello world";
    }

    /**
     * 对RPC的测试
     */
    @GetMapping("/test")
    public void test() {
        for (int i = 0; i < 30; i++) {
            new Thread(() -> {
                int rand = new Random().nextInt(3);

                MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
                if (rand <= 1) {
                    params.add("username", getRandomString(1024));
                } else {
                    params.add("username", "cain");
                }
                params.add("password", "qweq");
                UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:30002/rpc/login");
                URI uri = builder.queryParams(params).build().encode().toUri();
                ResponseEntity<User> forEntity = restTemplate.getForEntity(uri, User.class);
                System.out.println(forEntity.getBody().getName() + ":" + forEntity.getBody().getAge());
            }).start();
        }
    }

    /**
     * 对RPC的测试
     */
    @GetMapping("/test01")
    public void test01() throws InterruptedException {
        int count = 100;
        CountDownLatch c = new CountDownLatch(count);
        for (int i = 0; i < count; i++) {
            new Thread(() -> {
                int rand = new Random().nextInt(3);
                String username = "cain";
                String password = "qweq";
                if (rand <= 1) {
                    int len = new Random().nextInt(1024*3);
                    username = getRandomString(len);
                }
                String userInfo = rpcController.getUsername(username, password);
                User user = rpcController.login(username, password);
                System.out.println(userInfo);
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
