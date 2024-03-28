import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Carro extends Thread {
    public int id; // id do carro
    public int distCorrida; // distância total da corrida
    public int dist=0; // distância percorrida pelo carro na corrida atual
    public int corridas_disputadas=0; // corridas disputadas pelo carro
    public int corridas_vencidas=0; // corridas vencidas pelo carro
    public boolean emExecucao = true; // flag para corrida
    private final CyclicBarrier barrier; // barreira de thread
    public long tempoFim; // tempo de término da corrida
    Random random = new Random(); // gerar número aleatório

    // construtor do carro
    public Carro(int id, int distCorrida, CyclicBarrier barrier) {
        this.id = id;
        this.distCorrida = distCorrida;
        this.barrier = barrier;
    }

    // inicializa a thread
    public void run() {
        // executa a lógica se o carro estiver correndo, ou seja, não terminou a corrida
        while (this.emExecucao) {
            correr(); // lógica de correr
            printProgressBar(this.dist, this.distCorrida); // lógica de printar o progresso do carro

            // quando carro termina a corrida
            if (this.dist >= this.distCorrida) {
                System.out.println("----------> Carro no. " + this.id + " acabou a corrida");
                this.corridas_disputadas++;
                tempoFim = System.currentTimeMillis(); // armazena o tempo em que ele chegou ao final

                try {
                    // aguarda até que todas as threads cheguem à barreira
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }

                // quando todos os carros terminarem a corrida, para a execução
                parar();
            }

            // pausa para simular a execução
            try {
                Thread.sleep(500); // delay de 500ms
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    // função para o carro progredir na corrida
    public void correr(){
        int distSorteada = random.nextInt(20) + 10; // carro vai correr uma distância entre 10 a 30
        this.dist += distSorteada;

        // se a distância percorrida do carro for maior ou igual ao total da pista
        if(this.dist >= this.distCorrida){
            this.dist = this.distCorrida; // distância do carro é igual ao total da pista (chegou ao final)
        }
    }

    // função para printar a barra de progresso do carro em relação ao fim da corrida
    public void printProgressBar(int currentStep, int totalSteps) {
        int barWidth = 25;
        double progress = (double) currentStep / totalSteps;
        int numChars = (int) (progress * barWidth);

        StringBuilder progressBar = new StringBuilder();
        progressBar.append('[');
        for (int i = 0; i < barWidth; i++) {
            if (i < numChars) {
                progressBar.append('-');
            } else {
                progressBar.append(' ');
            }
        }
        progressBar.append(']');
        progressBar.append(String.format(" %d%%", (int) (progress * 100)));

        System.out.println("Carro no. " + this.id + "    " + progressBar.toString());
    }

    // para o carro
    public void parar(){
        this.dist = 0;
        this.emExecucao = false;
    }

    // reinicia o carro, para começar a próxima corrida
    public void reiniciar(){
        this.dist = 0;
        this.emExecucao = true;
    }
}