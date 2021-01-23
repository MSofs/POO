package Model;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import Exceptions.OpcaoInvalidaException;

public class User implements Serializable{
    private static final long serialVersionUID = -6896735489943952618L;
    private String email;
    private String password;
    private String codUtilizador;
    private String nome; // nome para guardar nos registos das encomendas;
    private List<Encomendas> encFeitas;
    private double latitude;
    private double longitude;

    public User(){
        this.email="";
        this.password="";
        this.codUtilizador="";
        this.nome="";
        this.encFeitas = new ArrayList<Encomendas>();
        this.latitude=0.0;
        this.longitude=0.0;

    }

    public User(String codU , String nome, String email, String passwd,double latitude, double longitude) {
        this.codUtilizador=codU;
        this.nome=nome;
        this.email = email;
        this.password = passwd;
        this.encFeitas = new ArrayList<>();
        this.latitude=latitude;
        this.longitude=longitude;
    }
    public User(User u) {
        this.email = u.getEmail();
        this.password = u.getPassword();
        this.nome=u.getNome();
        this.codUtilizador=u.getCodUtilizador();
        setEncFeitas(u.getEncFeitas());
        this.latitude=u.getLatitude();
        this.longitude=u.getLongitude();
    }



    //GETS------------------------------------------------------------------------------------------------------------------------------------------
    public String getEmail() {
        return this.email;
    }
    public String getPassword(){return this.password;}

    public String getNome() { return nome; }
    
    public String getCodUtilizador() { return codUtilizador; }

    public List<Encomendas> getEncFeitas() {
        ArrayList<Encomendas> e= new ArrayList<>();
        for(Encomendas ec : this.encFeitas)
            e.add(ec.clone());
        return e;
    }

    public double getLatitude() { return latitude; }

    public double getLongitude() { return longitude; }

    //SETS--------------------------------------------------------------------------------------------------------------------------------------

    public void setEmail(String email) { this.email = email; }

    public void setPassword(String password) { this.password = password; }

    public void setNome(String nome){this.nome=nome;}

    public void setCodUtilizador(String codUtilizador) { this.codUtilizador = codUtilizador; }

    public void setEncFeitas(List<Encomendas> encF) {
        this.encFeitas=new ArrayList<>();
        for(Encomendas e : encF)
            this.encFeitas.add(e);
    }

    public void setLatitude(double latitude) { this.latitude = latitude; }
    
    public void setLongitude(double longitude) { this.longitude = longitude; }

    //METODOS -------------------------------------------------------------------------------------------------------------------------------------
 /*   List<Encomendas> getListencUH( LocalTime init, LocalTime end) {
        return this.encFeitas.stream()
                .filter(e -> e.getTime().isBefore(end)
                        && e.getTime().isAfter(init))
                .collect(Collectors.toList());
    }
*/

    List<Encomendas> getListencUDH(LocalDateTime init, LocalDateTime end) {
        return this.encFeitas.stream()
                .filter(e -> e.getDataHora().isBefore(end) && e.getDataHora().isAfter(init))
                .collect(Collectors.toList());
    }

    List<Encomendas> getListencV(Voluntarios v) {
        return v.getListencV(this);
    }

    List<Encomendas> getListencE(EmpresasTrans et) {
        return et.getListClient(this.codUtilizador);
    }

    public void addEncHist(Encomendas enc) {
        this.encFeitas.add(enc);
    }

    public void rate(Voluntarios v,int rating) {
        v.rate(rating);
    }

    public void rate(EmpresasTrans e,int rating){
        e.rate(rating);
    }

    public boolean aceitarEmpresaTr(double c, double t, EmpresasTrans emp) throws OpcaoInvalidaException {
        System.out.println("Nome da Empresa:" + emp.getNome() +"Custo : "+ c + "\nTempo estimado para a entrega : "+ t + "\nAceita (1-sim/ 2-nao) : ");
        Scanner sc = new Scanner (System.in);
        int aceita = sc.nextInt();
        if(aceita == 1) return true;
        else if(aceita ==2)  return false;
             else throw new OpcaoInvalidaException();
    }

    public User getUser(String codU){
        if(codU.equals(this.nome))
            return this;
        else return null;
    }
    //METODOS USUAIS --------------------------------------------------------------------------------------------------------------------
    public User clone(){return new User(this);}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return this.email.equals(user.email) &&
                this.password.equals(user.password) &&
                this.nome.equals(user.nome) &&
                this.codUtilizador.equals(user.codUtilizador) &&
                this.encFeitas.equals(user.encFeitas);

    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Utilizador:").append(this.email).append(",")
                                     .append(this.password).append(",")
                                     .append(this.codUtilizador).append(",")
                                     .append(this.nome).append(",")
                                     .append(this.encFeitas).append(",")
                                     .append(this.latitude).append(",")
                                     .append(this.longitude).append(",");
        return sb.toString();
    }

}
