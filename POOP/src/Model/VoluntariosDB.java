package Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static java.lang.Math.*;
import java.io.Serializable;
import Exceptions.*;

public class VoluntariosDB implements Serializable{
    private static final long serialVersionUID = -8895219750792419080L;
    private Map<String, Voluntarios> voluntarios; //voluntarios, voluntarios

    VoluntariosDB() {
        this.voluntarios = new HashMap<>();
    }

    private VoluntariosDB(VoluntariosDB v) {
        this.voluntarios = v.voluntarios
                .values()
                .stream()
                .collect(Collectors
                        .toMap(Voluntarios::getCodVoluntario, Voluntarios::clone));
    }

    public void addVoluntario(Voluntarios v) throws JaExisteVoluntarioException{
        if(this.voluntarios.putIfAbsent(v.getCodVoluntario(), v) != null)
            throw new JaExisteVoluntarioException();
    }


    public Voluntarios getvoluntario (String cdV) throws NaoExisteVoluntarioException  {
        Voluntarios v = this.voluntarios.get(cdV);
        if(v == null)
            throw new NaoExisteVoluntarioException();
        return v;
    }

    public VoluntariosDB clone() { return new VoluntariosDB(this); }

    public ArrayList<Voluntarios> voluntariosLivres() {
        return this.voluntarios
                .values()
                .stream()
                .filter((e)-> e.isLivre())
                .map(Voluntarios::clone)
                .collect(Collectors
                        .toCollection(ArrayList::new));
    }

    public static double distanceTo(Voluntarios v, double lat1, double lon1) {
        double lati1= Math.toRadians(lat1);
        double long1=Math.toRadians(lon1);
        double lati2= Math.toRadians(v.getLatitude());
        double long2=Math.toRadians(v.getLongitude());

        double dist = acos(sin(lati1)* sin(lati2) + cos(lati1) * cos(lati2) * cos(long1 - long2)) * 6371;

        //supostamnte estamos a calcular a distancia entre o voluntario e um ponto
        //retorna em kms
        return dist;
    }


    /// lista de voluntarios disponiveis perto daquele ponto/cliente ou loja
    //devolve o primeiro voluntario na lista
    //se nao existir temos de criar uma exeçao !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
  
    public Voluntarios volNoRaio(double lac, double loc, double lal, double lol ) {//lac latitude cliente e loc longitude cliente e lal latitude loja e lol longitude loja
    return this.voluntarios
            .values()
            .stream()
            .filter(e -> e.getRaio()>= distanceTo(e,lac,loc) && e.getRaio()>= distanceTo(e, lal, lol) && e.isLivre())
            .collect(Collectors.toList())
            .get(0);
}

    public List<Voluntarios> volNoRaioL(Lojas l , User u ) {//lac latitude cliente e loc longitude cliente e lal latitude loja e lol longitude loja
        return this.voluntarios
                .values()
                .stream()
                .filter(e -> e.getRaio()>= distanceTo(e,l.getLat(),l.getLon()) && e.getRaio()>= distanceTo(e, u.getLatitude(),u.getLongitude()) && e.isLivre())
                .collect(Collectors.toList());
    }
        
    public Voluntarios volNoRaioMed(double lac, double loc, double lal, double lol){
        return this.voluntarios
                .values()
                .stream()
                .filter(e -> e.getRaio()>= distanceTo(e,lac,loc) && e.getRaio()>= distanceTo(e, lal, lol) && e.isLivre() && e.isTransportaMed())
                .collect(Collectors.toList())
                .get(0);
    }

    public List<Voluntarios> volNoRaioMedL(Lojas l , User u){
        return this.voluntarios
                .values()
                .stream()
                .filter(e -> e.getRaio()>= distanceTo(e,l.getLat(),l.getLon()) && e.getRaio()>= distanceTo(e, u.getLatitude(),u.getLongitude()) && e.isLivre() && e.isTransportaMed())
                .collect(Collectors.toList());
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || this.getClass() != o.getClass()) return false;

        VoluntariosDB volun = (VoluntariosDB) o;
        return this.voluntarios.equals(volun.voluntarios);
    }

    @Override
    public int hashCode() {
        return 1;
    }


}
