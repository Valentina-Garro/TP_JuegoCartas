import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import javax.swing.JPanel;

public class Jugador {

    private final int TOTAL_CARTAS = 10;
    private final int MARGEN = 10;
    private final int DISTANCIA = 50;

    private Carta[] cartas = new Carta[TOTAL_CARTAS];
    private Random r = new Random();

    public void repartir() {
        for (int i = 0; i < TOTAL_CARTAS; i++) {
            cartas[i] = new Carta(r);
        }
    }

    public void mostrar(JPanel pnl) {
        pnl.removeAll();

        int p = 1;
        for (Carta c : cartas) {
            c.mostrar(pnl, MARGEN + TOTAL_CARTAS * DISTANCIA - p++ * DISTANCIA, MARGEN);
        }

        pnl.repaint();
    }

    public String getGrupos() {
        String mensaje = "No se encontraron grupos";

        int[] contadores = new int[NombreCarta.values().length];
        for (Carta c : cartas) {
            contadores[c.getNombre().ordinal()]++;
        }

        boolean hayGrupos = false;
        for (int i = 0; i < contadores.length; i++) {
            if (contadores[i] >= 2) {
                if (!hayGrupos) {
                    hayGrupos = true;
                    mensaje = "Se encontraron los siguientes grupos: \n";
                }
                mensaje += Grupo.values()[contadores[i]] + " DE " + NombreCarta.values()[i] + "\n";
            }
        }

        return mensaje;
    }

/////////////////////////////////////////////////////////////////

    // EMPIEZA LA SOLUCION DEL PUNTO 4

    public String obtenerEscalera() {
        // Mapa para agrupar las pintas
        Map<Pinta, List<Integer>> pintasMap = new HashMap<>();

        // Agrupamos las cartas por pinta
        for (Carta carta : cartas) {
            Pinta pinta = carta.getPinta();
            pintasMap.putIfAbsent(pinta, new ArrayList<>());
            pintasMap.get(pinta).add(carta.getNombre().ordinal());
        }

        StringBuilder resultado = new StringBuilder(); //StringBuilder para acumular resultado
        
        //Recorremos los conjuntos de las cartas agrupadas
        for (Map.Entry<Pinta, List<Integer>> entry : pintasMap.entrySet()) {
            Pinta pinta = entry.getKey();
            List<Integer> valores = entry.getValue();
            Collections.sort(valores); //Ordena valores para secuencia

            // Verificar la secuencia
            List<List<Integer>> escaleras = new ArrayList<>();
            List<Integer> escaleraActual = new ArrayList<>();

            //Recorre los valores de las cartas
            for (int i = 0; i < valores.size(); i++) {
                if (escaleraActual.isEmpty() || valores.get(i) == escaleraActual.get(escaleraActual.size() - 1) + 1) {
                    escaleraActual.add(valores.get(i));
                } else {
                    if (escaleraActual.size() >= 3) {
                        escaleras.add(new ArrayList<>(escaleraActual)); //Agregamos la escalera actual
                    }
                    escaleraActual.clear();
                    escaleraActual.add(valores.get(i)); //Inicia una nueva escalera
                }
            }

            if (escaleraActual.size() >= 3) {
                escaleras.add(new ArrayList<>(escaleraActual));
            }

            // Damos el resultado
            for (List<Integer> escalera : escaleras) {
                for (Integer valor : escalera) {
                    if (valor >= 0 && valor < NombreCarta.values().length) {
                        resultado.append(NombreCarta.values()[valor]).append(" ");
                    } else {
                        resultado.append("Valor desconocido ");
                    }
                }
                resultado.append("de ").append(pinta).append("\n");
            }
        }

        return resultado.length() > 0 ? resultado.toString() : "No hay escaleras";
    }

    // Para calcular el puntaje del jugador
    public int calcularPuntaje() {
        int puntaje = 0; //Acumula el puntaje
        boolean[] enGrupo = new boolean[cartas.length];
    
        // Marca las cartas en grupos
        for (int i = 0; i < cartas.length; i++) {
            int cantidad = 0;
            for (int j = 0; j < cartas.length; j++) {
                if (cartas[i].getNombre() == cartas[j].getNombre()) {
                    cantidad++;
                }
            }
            if (cantidad >= 2) {
                enGrupo[i] = true; //Si esta en grupo se marca
            }
        }

        // Calcular los puntos de las cartas que no están en grupos
        for (int i = 0; i < cartas.length; i++) {
            if (!enGrupo[i]) {
                int valor = cartas[i].getValor();
                puntaje += valor;
            }
        }

        return puntaje;
    }
}
