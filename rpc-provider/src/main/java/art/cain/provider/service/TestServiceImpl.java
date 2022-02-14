package art.cain.provider.service;

import art.cain.interfaces.TestService;
import org.springframework.stereotype.Service;

/**
 * todo zk P和C在启动时，都需要检查一下zk，如果没有为RPC创建的节点空间，需要主动创建一下，节点为永久节点；
 * todo zk P需要自定义一个注解，扫描到这个注解，然后将其注册到zk中，以类名为节点1，ip:端口作为子节点；
 * todo zk 这个节点使用zk的临时节点，当P断开连接时，临时节点自动删除。
 */
@Service
public class TestServiceImpl implements TestService {
    @Override
    public String test(String test) {
        return "Hello world";
    }

    @Override
    public String test(String q1, String q2) {
        return "Hello params";
    }
}
