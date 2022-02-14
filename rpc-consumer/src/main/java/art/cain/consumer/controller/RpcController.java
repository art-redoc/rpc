package art.cain.consumer.controller;

import art.cain.consumer.rpc.RpcClient;
import art.cain.interfaces.Account;
import art.cain.interfaces.TestService;
import art.cain.interfaces.User;
import art.cain.interfaces.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RpcController {

    @RpcClient
    private UserService userService;
    @RpcClient
    private TestService testService;

    @GetMapping("/rpc/user")
    public String getUsername(String username, String password) {
        return userService.getUserName(new Account(username, password));
    }

    @GetMapping("/rpc/login")
    public User login(String username, String password) {
        return userService.login(new Account(username, password));
    }

    @GetMapping("/rpc/test")
    public String test(String test) {
        return testService.test(test);
    }
}
