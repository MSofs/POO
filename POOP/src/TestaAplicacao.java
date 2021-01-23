import java.io.IOException;
import Controller.Controller;
import Model.TrazAqui;
import Exceptions.*;

public class TestaAplicacao{

    public static void main(String[] args) {


        TrazAqui model = new TrazAqui();

        try {
            model = model.read("Guardado.CSV");
        }
        catch (IOException | ClassNotFoundException e) {
            System.out.println("Vou carregar a partir de txt!");
            try{
                model.fazParse("LOG1.txt");
            }
            catch(UserJaExisteException | JaExisteLojaException |JaExisteVoluntarioException | JaExisteEmpresaException | JaExisteEncomendaException | JaExisteEncomendaAceiteException u){
                System.out.println("O user ja existe!");
            }
        }

        new Controller(model).run();
        try {
            model.save("Guardado.CSV");
        }
        catch (IOException eio) {
            System.out.println("Erro a guardar!");
        }
    
        /*   //Isto aqui é para tirar, por enquanto serve para ver se os dados estao a fluir
        //obter uma empresa da DB
        try{
            EmpresasTrans emp = model.empresas.getEmpresa("E1");
            System.out.println(emp);
        }catch(NaoExisteEmpresaException nao){
            System.out.println("A empresa procurada nao existe");
        }
        
        //obter um voluntario da DB
        Voluntarios v = model.voluntarios.getvoluntario("v9");
        System.out.println(v);

        //obter uma loja da DB
        try{
            Lojas l = model.getLoja("l54");
            System.out.println(l);
        }catch(NaoExisteLojaException e){
            System.out.println("User nao existe");
        }

        //obter um user da DB
        try{
            User u = model.users.getUser("u38");
            List<Encomendas> encs = model.getListEncUtilizador(u);
            System.out.println(u);
            System.out.println(encs);
        }catch(UserInexistenteException e){
            System.out.println("User nao existe");
        }

        //obter uma encomenda da DB
        Encomendas e = model.encomendas.getEncomenda("e4031");
        System.out.println(e);
      */


        System.out.println("FIIIMMM");
    }
    
}