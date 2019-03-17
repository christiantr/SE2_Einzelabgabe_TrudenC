package com.example.myapplication;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Optional;

public class MainActivity extends AppCompatActivity {
    Button sendButton;
    EditText matrikelText;
    TextView answerText;

    private static String HOST = "se2-isys.aau.at";
    private static int PORT = 53212;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sendButton = findViewById(R.id.buttonSend);
        matrikelText = findViewById(R.id.editTextMatrikel);
        answerText = findViewById(R.id.textViewResponse);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("TAG", "Send Button clicked.");
                final String matrikelN = matrikelText.getText().toString();
                Log.i("TAG", String.format("Matrikelnr: %s", matrikelN));


                new Thread(new Runnable() {
                    public void run() {
                        try {
                            Log.i("TAG", "Send to request to server.");
                            final String answer = getAnswerFromServer(matrikelN);
                            Log.i("TAG", String.format("Answer: %s", answer));


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    answerText.setText(answer);

                                }
                            });

                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.e("TAG", "Server request failed");

                        }
                    }

                }).start();


            }


        });


    }

    private String getAnswerFromServer(String matrikelN) throws IOException {
        String answer;
        Socket clientSocket = new Socket(HOST, PORT);
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        outToServer.writeBytes(matrikelN + '\n');
        answer = inFromServer.readLine();
        clientSocket.close();

        return answer;
    }
}
