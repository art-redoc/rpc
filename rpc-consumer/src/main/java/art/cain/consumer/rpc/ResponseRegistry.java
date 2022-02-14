package art.cain.consumer.rpc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.SynchronousQueue;

/**
 * RPC针对响应值的一个注册地，使用阻塞队列同步消费者的响应。
 */
public class ResponseRegistry {

    private final static Map<Long, SynchronousQueue<Object>> responseMap = new ConcurrentHashMap<>();

    public static void registerResponse(Long requestId) {
        SynchronousQueue<Object> synchronousQueue = new SynchronousQueue<>();
        responseMap.putIfAbsent(requestId, synchronousQueue);
    }

    public static Object getAndRemoveResponse(Long requestId) {
        try {
            Object response = responseMap.get(requestId).take();
            responseMap.remove(requestId);
            return response;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void putResponse(long requestId, Object response) {
        try {
            responseMap.get(requestId).put(response);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
