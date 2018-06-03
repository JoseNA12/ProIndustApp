package reque.proyecto2.jose_.proindust_app.modelo;

public class DistNormal {

    private double error;
    private double valorZ;
    private int total_tareas;
    private int t_productivas;

    public DistNormal(double e, double v, int t, int p){
        error = e;
        valorZ = v;
        total_tareas = t;
        t_productivas = p;
    }

    //Generador del valor normal
    public double CalcularProbabilidadZ(double x)
    {
        int neg = (x < 0d) ? 1 : 0;
        if ( neg == 1)
            x *= -1d;

        double k = (1d / ( 1d + 0.2316419 * x));
        double y = (((( 1.330274429 * k - 1.821255978) * k + 1.781477937) *
                k - 0.356563782) * k + 0.319381530) * k;
        y = 1.0 - 0.398942280401 * Math.exp(-0.5 * x * x) * y;

        return (1d - neg) * y + neg * (1d - y);
    }

    //Funcion para calcular muestras faltantes
    public double CalcularNMuestras(){
        double cant_p = t_productivas / total_tareas;
        double probabilidad_Z = CalcularProbabilidadZ(valorZ);
        return ((probabilidad_Z * probabilidad_Z) * (cant_p * (1 - cant_p))) / (error * error);
    }
}
