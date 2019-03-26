package uk.ac.nottingham.clientandroidapp_testing;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.net.UnknownHostException;


public class Client extends AsyncTask<Void, String, Void> {
    private WeakReference<MainActivity> activityReference;
    private final String ipAddress;
    private final int portNumber;
    private Boolean keepRunning;

    /**
     * Constructor.
     * @param context Activity context
     * @param ip ip address
     * @param port port number
     */
    Client(MainActivity context, String ip, int port) {
        // only keep a weak reference to the activity
        // solves the issue with leaks
        this.activityReference = new WeakReference<>(context);
        this.ipAddress = ip;
        this.portNumber = port;
        this.keepRunning = true;
    }

    /**
     * Method is running on the UI thread.
     * Updates the UI with the data produced by the {@link #doInBackground(Void...)} method from this {@link AsyncTask}.
     * @param progress text input
     */
    @Override
    protected void onProgressUpdate(String... progress) {
        // get a reference to the activity if it is still there
        MainActivity activity = activityReference.get();
        if (activity == null || activity.isFinishing())
            return;
        // modify the activity's UI
        TextView textView = activity.findViewById(R.id.textView);
        textView.setText(progress[0]);
    }

    /**
     * Execute the code for TCP IP client.
     * Connects to a socket and retrieves the data every 1 second.
     * @param voids no input
     * @return nothing
     */
    @Override
    protected Void doInBackground(Void... voids) {
        while (keepRunning) {
            Socket clientSocket = null;
            try {
                clientSocket = new Socket(ipAddress, portNumber);

                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
                DataInputStream in = new DataInputStream(clientSocket.getInputStream());

                // send out to server
                String message = "hey test from android";
                out.writeUTF(message);

                // receive from server
                String data = in.readUTF();
                Log.i("RECEIVED", data);
                publishProgress(data);

            } catch (UnknownHostException e) {
                Log.e("SOCKET", e.getMessage());
            } catch (EOFException e) {
                Log.e("EOF", e.getMessage());
            } catch (IOException e) {
                Log.e("IO", e.getMessage());
            }

            // This piece of code is executed no matter what. Even when an Exception is thrown.
            finally {
                if (clientSocket != null)
                    try {
                        clientSocket.close();
                    } catch (IOException e) {
                        Log.e("CLOSE", e.getMessage());
                    }
            }

            // Sleep for 1 second.
            try {
                Thread.sleep(1000);
            } catch (
                    InterruptedException e) {e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * Stop running the client.
     */
    void stopRunning() {
        this.keepRunning = false;
    }
}
