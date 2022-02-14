package art.cain.provider.rpc.spring;

import art.cain.provider.rpc.ProviderServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationFinishedListener implements ApplicationListener<ApplicationStartedEvent> {

    @Autowired
    private ProviderServer provider;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        new Thread(() -> {
            try {
                provider.startServer();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
