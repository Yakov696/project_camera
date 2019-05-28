package primitives;

public class Material {
    private double _Kd; // Diffusion attenuation coefficient
    private double _Ks; // Specular attenuation coefficient
    private double _Kr; // Reflection coefficient (1 for mirror)
    private double _Kt; // Refraction coefficient (1 for transparent)
    private double _n; // Refraction index

    /************* Constructors ******************/

    public Material()
    {
        _Kd = 1;
        _Ks = 1;
        _Kr = 0;
        _Kt = 0;
        _n = 19;
    }

    public Material (double n, double kt,double kr,double kd,double ks) {
        setN(n);
        setKt(kt);
        setKr(kr);
        setKd(kd);
        setKs(ks);
    }

    public Material(Material material){
        this._Kd = material._Kd;
        this._Ks = material._Ks;
        this._Kr = material._Kr;
        this._Kt = material._Kt;
        this._n = material._n;
    }

    /************* Getters/Setters ******************/

    public double getKd(){ return _Kd; }
    public double getKs(){ return  _Ks; }
    public double getKr(){ return _Kr; }
    public double getKt(){return _Kt; }
    public double getN(){return _n; }
    public void setKd(double Kd){ this._Kd = Kd; }
    public void setKs(double Ks){ this._Ks = Ks; }
    public void setKr(double Kr){ this._Kr = Kr; }
    public void setKt(double Kt){ this._Kt = Kt; }
    public void setN (double n){ this._n = n; }
}
