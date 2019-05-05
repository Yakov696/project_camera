package primitives;

public class Material {
    private double _Kd; // Diffusion attenuation coefficient
    private double _Ks; // Specular attenuation coefficient
    private double _Kr; // Reflection coefficient (1 for mirror)
    private double _Kt; // Refraction coefficient (1 for transparent)
    private double _n; // Refraction index
    // ************* Constructors ****************** //
    public Material()
    {
        _Kd = 1;
        _Ks = 1;
        _Kr = 0;
        _Kt = 0;
        _n = 1;
    }
    public Material(Material material){}
    // ************* Getters/Setters ****************** //
//    public double getKd();
//    public double getKs();
//    public double getKr();
//    public double getKt();
//    public double getN();
//    public void setKd(double _Kd);
//    public void setKs(double _Ks);
//    public void setKr(double _Kr);
//    public void setKt(double _Kt);
//    public void setN (double _n);
}
