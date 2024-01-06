package spi;

import android.os.AsyncTask;
import models.LoginData;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.client.core.executors.ApacheHttpClient4Executor;

import java.util.concurrent.CountDownLatch;

public class LoginChecker implements LoginAccessor{

    private LoginAccessor restClient;
    public LoginChecker(String baseUrl){

        this.restClient = ProxyFactory.create(LoginAccessor.class,
                baseUrl,
                new ApacheHttpClient4Executor());
    }


    @Override
    public boolean isLoginValid(LoginData data) {
        final boolean[] valid = {false};
        final CountDownLatch latch = new CountDownLatch(1);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                valid[0] = restClient.isLoginValid(data);
                latch.countDown();  // Release the latch to unblock the main thread
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                // Do nothing
            }
        }.execute();

        try {
            latch.await();  // Block the main thread until the latch is released
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return valid[0];
    }
}
