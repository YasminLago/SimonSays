package com.example.yasmin.myapplication;

import android.content.res.Resources;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import java.lang.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    TimerTask timerTask;
    Timer time;
    Random random = new Random();

    ArrayList<Integer> simonDice = new ArrayList();
    ArrayList<Integer> yoDigo = new ArrayList();

    int contador = 0;
    int startTime = 0;

    int btnStart = R.id.btnStart;
    int[] botones = {
            R.id.btnVerde,
            R.id.btnRojo,
            R.id.btnAmarillo,
            R.id.btnAzul
    };

    Resources resources;
    int [] colorBotones;
    int blanco = Color.WHITE;

    int [] sonidos = {
            R.raw.sonido_verde,
            R.raw.sonido_rojo,
            R.raw.sonido_amarillo,
            R.raw.sonido_azul
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resources = getResources();
        colorBotones = resources.getIntArray(R.array.colorBotones);
    }

    /**
     * Comienza el juego y desactiva el botón start hasta que se
     * terminen de pulsar el número de botones correcto
     * @param btnSt
     */
    public void empezar(View btnSt) {
        for (int i : botones) {
            findViewById(i).setEnabled(true);
        }
        runTask();
        findViewById(btnStart).setEnabled(false);
    }

    /**
     * Cambia el color de cada botón cuando es pulsado y guarda
     * su id para compararlo más tarde
     * @param btns
     */
    public void efectoBoton(View btns) {
        contador++; //Número de botones pulsados
        String color = (String) btns.getTag(); //Se recoge en un string un atributo dado en el layout
        int button = Integer.parseInt(color);
        yoDigo.add(button);
        parpadear(button);
        if (contador == 4) { //Si el contador es igual a 4 los botones se activarán para poder usarlos
            for (int i : botones) {
                findViewById(i).setEnabled(true);
            }
            comprobar();
            contador = 0;
        }
    }

    /**
     * Compara los ArrayList simonDice y yoDigo y muestra en una
     * Toast si se ha ganado o perdido
     */
    public void comprobar() {

        if (simonDice.toString().equals(yoDigo.toString())) {
            Toast.makeText(this, "Has acertado!", Toast.LENGTH_SHORT).show();
            for (int i : botones) {
                findViewById(i).setEnabled(true);
            }
            findViewById(btnStart).setEnabled(true);
        } else {
            Toast.makeText(this, "Has perdido...", Toast.LENGTH_SHORT).show();
            findViewById(btnStart).setEnabled(true);
        }
        yoDigo.clear();
        simonDice.clear();
        startTime = 0;
    }

    /**
     * Recoge el arrayList simonDice y lo recorre, pasándole
     * cada elemento como parámetro al método parpadear
     */
    public void memorizar() {
        int dice = simonSays();
        for (Integer i : simonDice) {
            if (dice == i) {
                parpadear(i);
            }
        }
        startTime++;
        if (startTime == 4) {
            cancelTask();
        }
     }

    /**
     * Crea el efecto de que los botones están parpadeando cuando son pulsados
     * y reproduce el sonido que cada uno tiene asignado
     * @param posicionBotn
     */
    public void parpadear(final int posicionBotn) {
        findViewById(botones[posicionBotn]).setBackgroundColor(blanco);
        final MediaPlayer reproducir = MediaPlayer.create(this, sonidos[posicionBotn]);
        reproducir.start();
        findViewById(botones[posicionBotn]).postDelayed(new Runnable() {
            public void run() {
                reproducir.reset();
                findViewById(botones[posicionBotn]).setBackgroundColor(colorBotones[posicionBotn]);
            }
        }, 300);
    }

    /**
     * Genera el número aleatorio y lo guarda en el ArrayList simonDice
     */
    public int simonSays() {
        int dice = random.nextInt(3);
        simonDice.add(dice);
        return dice;
    }

    /**
     * Ejecuta la tarea del método memorizar
     */
    public void runTask() {
        time = new Timer();
        timerTask =  new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        memorizar();
                    }
                });
            }
        };
        time.schedule(timerTask, 200, 1000);
    }

    /**
     * Termina la ejecución aleatoria de los botones
     */
    public void cancelTask() {
        if (time != null) {
            time.cancel();
            time = null;
        }
    }
}

