package Model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static java.lang.Math.*;
import java.io.Serializable;

public class Voluntarios implements Serializable{
    private static final long serialVersionUID = 5583135800979722684L;
    private String password;
    private String codVoluntario;
    private String nome;
    private double latitude;
    private double longitude;
    private double raio;
    private int classificacoes; // 0 a 5
    private int nrClassif;
    private double tempoMedporKm;

    public boolean livre;
    private boolean transportaMed;
    private List<Encomendas> enc;


    private Map<Encomendas, Double> regTempo;


    public Voluntarios(){
        this.password = new String();
        this.codVoluntario = new String();
        this.nome = new String();
        this.latitude = 0.0;
        this.longitude = 0.0;
        this.raio = 0.0;
        this.livre = true;
        this.transportaMed = false;
        this.enc = new ArrayList<Encomendas>();
        this.classificacoes=0;
        this.nrClassif=0;
        this.tempoMedporKm = 0.0;

        this.regTempo= new HashMap<>();
    }


    public Voluntarios(String password, String cdV, String no, double latitude, double longitude, double raio, boolean trMed, double tpM){
        this.password=password;
        this.codVoluntario=cdV;
        this.nome=no;
        this.latitude=latitude;
        this.longitude=longitude;
        this.raio=raio;
        this.livre=true;
        this.transportaMed=trMed;
        this.enc = new ArrayList<Encomendas>();
        this.classificacoes=0;
        this.nrClassif=0;
        this.tempoMedporKm=tpM;
        this.regTempo= new HashMap<Encomendas,Double>();
    }

    public Voluntarios(Voluntarios v){
        this.password=v.getPassword();
        this.codVoluntario=v.getCodVoluntario();
        this.nome=v.getNome();
        this.latitude=v.getLatitude();
        this.longitude=v.getLongitude();
        this.raio=v.getRaio();
        this.livre=v.isLivre();
        this.transportaMed=v.isTransportaMed();
        setEnc(v.getEnc());
        this.classificacoes=v.getClassi();
        this.nrClassif=v.getNrC();
        this.tempoMedporKm = v.getTempoMedio();
        setRegTempo(v.getRegTempo());
    }


    //GETS----------------------------------------------------------------------------------------------------------------------------------
    public String getPassword() { return password; }
    
    public String getCodVoluntario() { return codVoluntario; }

    public String getNome() { return nome; }

    public double getLatitude() { return this.latitude; }

    public double getLongitude() { return this.longitude; }

    public double getRaio() { return this.raio; }

    public boolean isLivre() { return this.livre; }

    public boolean isTransportaMed() { return this.transportaMed; }

    public List<Encomendas> getEnc(){
        ArrayList<Encomendas> en =new ArrayList<>();
        for(Encomendas e : this.enc )
            en.add(e.clone());
        return en;
    }
    public int getClassi() {
        return (this.nrClassif == 0)? 5 : (this.classificacoes / this.nrClassif);
    }

    private int getNrC() {
        return this.nrClassif;
    }

    public double getTempoMedio(){return this.tempoMedporKm;}


    public Map<Encomendas, Double> getRegTempo(){
        Map<Encomendas, Double> reg = new HashMap<>();
        for(Map.Entry<Encomendas, Double> entry : this.regTempo.entrySet())
            reg.put(entry.getKey().clone(), entry.getValue());
        return reg; 
       
    }

    //SETS--------------------------------------------------------------------------------------------------------------------------------
    public void setCodVoluntario(String codVoluntario) { this.codVoluntario = codVoluntario; }

    public void setNome(String nome) { this.nome = nome; }

    public void setLatitude(double latitude) { this.latitude = latitude; }

    public void setLongitude(double longitude) { this.longitude = longitude; }

    public void setRaio(double raio) { this.raio = raio; }

    public void setLivre(boolean livre) { this.livre = livre; }

    public void setTransportaMed(boolean transportaMed) { this.transportaMed = transportaMed; }

    public void setTempoMedio(double tempoMedporKm) { this.tempoMedporKm = tempoMedporKm;}

    public void setEnc(List<Encomendas> enc){
        this.enc = new ArrayList<>();
        for(Encomendas e: enc)
            this.enc.add(e);
    }

    public void setRegTempo(Map<Encomendas, Double> reg){
        this.regTempo = new HashMap<>();
        reg.entrySet().forEach(r -> this.regTempo.put(r.getKey().clone(), r.getValue()));
    }
    



    //METODOS------------------------------------------------------------------------------------------------------------------------------------
    void rent() {
        double meteorologia = new Metereologia().getEstacaoAtraso();
        double transito = new Transito().getAtrasoTransito(meteorologia);
        double atraso = (meteorologia % 0.5)+ (transito % 0.5);
        this.tempoMedporKm= this.tempoMedporKm* (1 + atraso);
    }

    public void rate(int Volrating) {
        this.nrClassif++;
        this.classificacoes+= Volrating;
    }

    void swapState() {
        this.livre = !this.livre;
    }

    void swapStateMed(){ this.transportaMed = !this.isTransportaMed();}


   /* the average radius for a
	 * spherical approximation of the figure of the Earth is approximately
	 * 6371.01 kilometers.*/

    public double distanceTo(double lat1, double lon1) {
        double lati1= Math.toRadians(lat1);
        double long1=Math.toRadians(lon1);
        double lati2= Math.toRadians(this.latitude);
        double long2=Math.toRadians(this.longitude);

        double dist = acos(sin(lati1)* sin(lati2) + cos(lati1) * cos(lati2) * cos(long1 - long2)) * 6371;

        //supostamnte estamos a calcular a distancia entre o voluntario e um ponto
        //retorna em kms
        return dist;
    }
    public double estimaTempoVLU(User u, Lojas l){
        rent();
        double tempoVoluntarioL = distanceTo(l.getLat(),l.getLon()) * this.getTempoMedio();
        l.rent();
        double tempoLoja = l.getTpAtend();
        double tempoLU = l.distanceLojaU(u);
        if (l.filaEspera){
            tempoLoja*=l.encA.size();//encomendas a sua frente
            return (tempoVoluntarioL + tempoLoja + tempoLU);
        }
        else return (tempoVoluntarioL + tempoLoja + tempoLU);
    }


    List<Encomendas> getListencV(String clientID) {
        return this.enc
                .stream()
                .filter(e -> e.getCodUtilizador().equals(clientID))
                .collect(Collectors.toList());
    }

    List<Encomendas> getListencV(User c) {
        String clientID = c.getCodUtilizador();
        return this.enc
                .stream()
                .filter(e -> e.getCodUtilizador().equals(clientID))
                .collect(Collectors.toList());
    }

 /*   List<Encomendas> getListencVH(LocalTime init, LocalTime end) {
        return this.enc.stream()
                .filter(e -> e.getTime().isBefore(end)
                        && e.getTime().isAfter(init))
                .collect(Collectors.toList());
    }
*/
    List<Encomendas> getListencVDH(LocalDateTime init, LocalDateTime end) {

        return this.enc.stream()
                .filter(e ->  e.getDataHora().isBefore(end) && e.getDataHora().isAfter(init))
                .collect(Collectors.toList());
    }


    //calcula a distancia de um Voluntario a uma loja + de uma loja a um utilizador
    public double calculaDistanciaEnc( Lojas l, User u, Encomendas e){
        double distVL = distanceTo( l.getLat(), l.getLon());
        double distLU = l.distanceLojaU(u);
        double distTotal = distVL + distLU;
        return distTotal;
    } //permite obter kms precorridos


    public void buscarEncomenda(Lojas l,Encomendas e,User u){ //repara que os voluntarios sao aceites automaticamente
        //swapState();
        //sleep (estimaTempoVLU)

        this.enc.add(e);
        this.latitude=u.getLatitude();
        this.longitude=u.getLongitude();
        this.regTempo.putIfAbsent(e, estimaTempoVLU(u, l));
        swapState();
    }

    //METODOS USUSAIS --------------------------------------------------------------------------------------------------------------------------
    public Voluntarios clone(){return new Voluntarios(this);}

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Voluntarios that = (Voluntarios) o;
        return this.codVoluntario.equals(that.codVoluntario)
                && this.nome.equals(that.nome)
                && this.latitude == that.latitude
                && this.longitude == that.longitude
                && this.raio == that.raio
                && this.livre == that.livre
                && this.transportaMed == that.transportaMed
                && this.enc.equals(that.enc);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Voluntario:").append(this.codVoluntario).append(",")
                                     .append(this.nome).append(",")
                                     .append(this.latitude).append(",")
                                     .append(this.longitude).append(",")
                                     .append(this.raio).append(",")
                                     .append(this.classificacoes).append(",")
                                     .append(this.nrClassif).append(",")
                                     .append(this.tempoMedporKm).append(",")
                                     .append(this.livre).append(",")
                                     .append(this.transportaMed).append(",")
                                     .append(this.enc).append(",")
                                     .append(this.regTempo).append(",");                                   
        return sb.toString();
    }

    @Override
    public int hashCode() {
        return 1;
    }

}
