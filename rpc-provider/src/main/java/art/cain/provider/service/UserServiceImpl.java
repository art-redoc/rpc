package art.cain.provider.service;

import art.cain.interfaces.Account;
import art.cain.interfaces.User;
import art.cain.interfaces.UserService;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public String getUserName(Account account) {
        if ("cain".equals(account.getUsername()) && "qweq".equals(account.getPassword())) {
            return "Success";
        }
        return "Error";
    }

    @Override
    public User login(Account account) {
        if ("cain".equals(account.getUsername()) && "qweq".equals(account.getPassword())) {
            return new User("Cain-" + UUID.randomUUID().toString().replace("-", "").substring(25), new Random().nextInt(28));
        }
        return new User("Death", 100);
    }
}
