/** Strategy: cada tipo de item calcula su multa por retraso de forma distinta. */
@FunctionalInterface
public interface PoliticaMulta {
    double calcular(long diasRetraso);
}