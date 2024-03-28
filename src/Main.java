import java.util.Scanner;
import java.util.concurrent.CyclicBarrier;

public class Main {
    public static void main(String[] args) {
        // entrada
        Scanner in = new Scanner(System.in);

        // variavéis de configuração
        int nCarros; // número de carros
        int distCorrida; // distância da corrida
        int nCorridas; // número total de corridas
        int nCorridasDisputadas = 0; // número de corridas já disputadas

        // entrada
        System.out.print("Digite a quantidade de carros: ");
        nCarros = in.nextInt();
        in.nextLine();
        System.out.print("Digite a distância da corrida: ");
        distCorrida = in.nextInt();
        in.nextLine();
        System.out.print("Digite a quantidade de corridas: ");
        nCorridas = in.nextInt();
        in.nextLine();
        System.out.println("\n============================================");

        // barreira - verifica quando todos carros terminaram a corrida atual
        CyclicBarrier barrier = new CyclicBarrier(nCarros);

        // vetor de carros (threads)
        Carro[] carros = new Carro[nCarros];

        // instancia os carros do vetor
        for (int i = 1; i <= nCarros; i++) {
            carros[i - 1] = new Carro(i, distCorrida, barrier);
        }

        // inicia as threads
        for (int i = 1; i <= nCarros; i++) {
            carros[i - 1].start();
        }

        // varredura das corridas
        nCorridasDisputadas = 0;
        while (nCorridasDisputadas < nCorridas) {
            int nCarrosAcabaram = 0;
            while (nCarrosAcabaram < nCarros) {
                nCarrosAcabaram = 0;
                for (int i = 1; i <= nCarros; i++) {
                    if (!(carros[i - 1].emExecucao)) {
                        nCarrosAcabaram++;
                    }
                }
            }
            System.out.println("\n============================================");
            System.out.println("Corrida encerrada!");


            // verifica quem ganhou a corrida
            Carro vencedor = null;
            long menorTempo = Long.MAX_VALUE;

            for (Carro carro : carros) {
                if (carro.tempoFim < menorTempo) {
                    menorTempo = carro.tempoFim;
                    vencedor = carro;
                }
            }

            System.out.println("O vencedor foi o carro no. " + vencedor.id);
            System.out.println("============================================");

            vencedor.corridas_vencidas++;
            nCorridasDisputadas++;

            // inicia a próxima corrida
            if (nCorridasDisputadas < nCorridas) {
                System.out.println("\n\nIniciando próxima corrida...");
                for (int i = 1; i <= nCarros; i++) {
                    carros[i - 1].reiniciar();
                }
            }
        }

        // após as corridas, mostra as estatísticas dos carros
        System.out.println("\n\n============================================");
        System.out.println("Resultados:");
        System.out.println("============================================\n");

        for (int i = 1; i <= nCarros; i++) {
            System.out.println("Carro no. " + carros[i-1].id);
            System.out.println("Partidas disputadas: " + carros[i-1].corridas_disputadas);
            System.out.println("Partidas vencidas: " + carros[i-1].corridas_vencidas);
            System.out.println("");
        }

        in.close();
    }
}