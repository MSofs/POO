package Model;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import Exceptions.*;

public class TrazAqui implements Serializable{
    private static final long serialVersionUID = 7582768867862556061L;

    private UsersDB users;
    private LojasDB lojas;
    private VoluntariosDB voluntarios;
    private EmpresasTransDB empresas;
    private EncomendasDB encomendas;
    private EncomendasAceitesDB encomendasA;
    private int utilizadorN;
    private int lojaN;
    private int empresaN;
    private int voluntarioN;
    private int encomendaN;

    public TrazAqui(){
        this.users= new UsersDB();
        this.lojas= new LojasDB();
        this.voluntarios= new VoluntariosDB();
        this.empresas= new EmpresasTransDB();
        this.encomendas= new EncomendasDB();
        this.encomendasA = new EncomendasAceitesDB();
        this.utilizadorN = 1;
        this.lojaN = 1;
        this.empresaN = 1;
        this.voluntarioN = 1;
        this.encomendaN = 1000;

    }

    public TrazAqui(UsersDB u,LojasDB l,VoluntariosDB v,EmpresasTransDB e, EncomendasDB enc,EncomendasAceitesDB encA){
        this.users= u;
        this.lojas= l;
        this.voluntarios= v;
        this.empresas= e;
        this.encomendas= enc;
        this.encomendasA = encA;
    }


    public void fazParse (String file) throws UserJaExisteException,JaExisteLojaException,
                                              JaExisteVoluntarioException,JaExisteEmpresaException,
                                              JaExisteEncomendaException,JaExisteEncomendaAceiteException{
        Parse p = new Parse();
        p.parse(file);
        this.users = p.users;
        this.lojas = p.lojas;
        this.voluntarios = p.voluntarios;
        this.empresas = p.empresas;
        this.encomendas = p.encomendas;
        this.encomendasA = p.encomendasA;
    }

    public Lojas getLoja (String lojaID) throws NaoExisteLojaException{  
        return this.lojas.getLoja(lojaID);
    }

    public List<Lojas> getLojas(){
        return this.lojas.getLojas();
    }
    
    public EmpresasTrans getTransportadora (String empID) throws NaoExisteEmpresaException{  
        return this.empresas.getEmpresa(empID);
    }

    public Voluntarios getVoluntario (String VoluntarioID) throws NaoExisteVoluntarioException{  
        return this.voluntarios.getvoluntario(VoluntarioID);
    }

    public User getUser(String userID) throws UserInexistenteException{  
        return this.users.getUser(userID);
    }
    

    public Set<User> getBestClientNrEnc(){
        Set<User> u = new TreeSet<>(new ComparatorNumeroEncomendas());
        Set<User> l = this.users.getUsers();
        for(User user : l ){
            u.add(user);
        }
        return u;
    }


    public Set<EmpresasTrans> getBestEmpresaKm(){
        Set<EmpresasTrans> emp = new TreeSet<>(new ComparatorKmsFeitos());
        Set<EmpresasTrans> l = this.empresas.getEmpresas();
        for(EmpresasTrans e : l){
            emp.add(e);
        }
        return emp;
    }


    public void verificaMail(String email) throws EmailExistenteException{
        this.users.verificaMail(email);
    }

    //ADICAO DE UM USER 
    public void addUser(User a) throws UserJaExisteException{
        this.users.addUtilizador(a); 
    }

    public void addUserCriado(User a){
        try{
        String codigo = a.getCodUtilizador() + this.utilizadorN;
        a.setCodUtilizador(codigo);
        this.utilizadorN++;
        this.users.addUtilizador(a);
        }catch(UserJaExisteException e){
            this.utilizadorN++;
        } 
    }

    
    //ADICAO DE UMA ENCOMENDA
    public void addEncomendas(Encomendas a) throws JaExisteEncomendaException {
        this.encomendas.addEncomenda(a);
    }

    public Encomendas addEncomendaCriada (Encomendas a) throws JaExisteEncomendaException {
        String codigo = a.getCodEncomenda() + this.encomendaN;
        a.setCodEncomenda(codigo);
        this.encomendaN++;
        this.encomendas.addEncomenda(a);
        return a;
    }    
    

    //ADICAO DE UMA EMPRESA
    public void addEmpresa(EmpresasTrans a) throws JaExisteEmpresaException {
        this.empresas.addEmpresa(a);
    }

    public void addEmpresaCriada(EmpresasTrans a) {
        try{
        String codigo = a.getCodEmpresa() + this.empresaN;
        a.setCodEmpresa(codigo);
        this.empresaN++;
        this.empresas.addEmpresa(a);
        }catch(JaExisteEmpresaException e){
            this.empresaN++;
        }
    }


    //ADICAO DE UM VOLUNTARIO
    public void addVoluntario(Voluntarios a) throws JaExisteVoluntarioException {
        this.voluntarios.addVoluntario(a);
    }

    public void addVoluntarioCriado(Voluntarios a){
        try{
        String codigo = a.getCodVoluntario() + this.voluntarioN;
        a.setCodVoluntario(codigo);
        this.voluntarioN++;
        this.voluntarios.addVoluntario(a);
        }catch(JaExisteVoluntarioException e){
            this.voluntarioN++;
        }
    }


    //ADICAO DE UMA LOJA
    public void addLoja(Lojas a) throws JaExisteLojaException {
        this.lojas.addLoja(a);
    }

    public void addLojaCriada(Lojas a) {
        try{
        String codigo = a.getCodLoja() + this.lojaN;
        a.setCodLoja(codigo);
        this.lojaN++;
        this.lojas.addLoja(a);
        }catch(JaExisteLojaException e){
            this.lojaN++;
        }
    }

    //Adicao de um produto
    public void adicionaProduto(String codLoja, LinhaEncomenda l) throws JaExisteProdutoException{
        this.lojas.adicionaProduto(codLoja, l);
    }

/*
    public void addVoluntario(String cdV, String no, double latitude, double longitude, int raio,boolean trMed, double tpM)
            throws JaExisteVoluntarioException {
        Voluntarios a = new Voluntarios(password, cdV,no,latitude,longitude,raio,trMed,tpM);
        this.voluntarios.addVoluntario(a);
    }


    public void addEmpresa(String cdE ,String n,double la,double lo,String nif, int r,double t,boolean isMed,double tmpKm,double pp, double pt)
            throws JaExisteEmpresaException {
        EmpresasTrans a = new EmpresasTrans(password, cdE, n, la, lo, nif, r, t,isMed,tmpKm,pp,pt);
        this.empresas.addEmpresa(a);
    }
*/

// Nao sei se serao necessarias as proximas 3 funcoes
    public void setTempoMedioV(Voluntarios voluntario, int tpMKm) {
       voluntario.setTempoMedio(tpMKm);
    }

    public void setVoluntarioMedico(Voluntarios v, boolean isMed) {
        v.setTransportaMed(isMed);
    }

    public void setTempoAtendLj(Lojas l, int tp) {
        l.setTpAtend(tp);
    }


    //mudar para medico ou vice versa
    public void swapState(Voluntarios v) {
        v.swapState();
    }

    public void swapStateMed(Voluntarios v) {
        v.swapStateMed();
    }

    public void swapState(EmpresasTrans emp) {
        emp.swapState();
    }

    public void swapStateMed(EmpresasTrans emp) {
        emp.swapStateMed();
    }



    //CLASSIFICAR
    public void rate(User cli, Voluntarios v, int a) {
        cli.rate(v,a);
    }

    public void rate(User cli, EmpresasTrans e, int a) {
        cli.rate(e,a);
    }


    //LOGINS
    public User logIn(String username, String passwd) throws UserInexistenteException, PasswordErradaExecption,NaoExisteEmailException {
        //User c = users.getUser(username);
        User c = users.getUserEmail(username);
        if(c==null)throw new UserInexistenteException();
        if(!(c.getPassword().equals(passwd)))
            throw new PasswordErradaExecption();
        return c;
    }

    public Lojas logInL(String username, String passwd) throws NaoExisteLojaException, PasswordErradaExecption {
        Lojas l = lojas.getLoja(username);
        if(l==null)throw new NaoExisteLojaException();
        if(!(l.getPassword().equals(passwd)))
            throw new PasswordErradaExecption();
        return l;
    }

    public Voluntarios logInV(String username, String passwd) throws NaoExisteVoluntarioException, PasswordErradaExecption {
        Voluntarios v = voluntarios.getvoluntario(username);
        if(v==null)throw new NaoExisteVoluntarioException();
        if(!(v.getPassword().equals(passwd)))
            throw new PasswordErradaExecption();
        return v;
    }
    
    public EmpresasTrans logInN(String username, String passwd) throws NaoExisteEmpresaException, PasswordErradaExecption {
        EmpresasTrans e = empresas.getEmpresa(username);
        if(e==null)throw new NaoExisteEmpresaException();
        if(!(e.getPassword().equals(passwd)))
            throw new NaoExisteEmpresaException();
        return e;
    }
    //Utilizadores
    public List<Encomendas> getListEncUtilizaforDH(User client, LocalDateTime init, LocalDateTime end) throws UserInexistenteException{
    return this.users.getUser(client.getCodUtilizador()).getListencUDH(init,end);
    }

    public List<Encomendas> getListEncUtilizador(User client)throws UserInexistenteException {
        return this.users.getUser(client.getCodUtilizador()).getEncFeitas();
    }


    //Voluntarios
    public List<Encomendas> getListEncVoluntariosDH(Voluntarios v,LocalDateTime init, LocalDateTime end)
            throws NaoExisteVoluntarioException {
        return this.voluntarios.getvoluntario(v.getCodVoluntario()).getListencVDH(init,end);
    }

    public List<Encomendas> getListEncVoluntario(Voluntarios v) throws NaoExisteVoluntarioException {
        return this.voluntarios.getvoluntario(v.getCodVoluntario()).getEnc();
    }

    //Empresas
    public List<Encomendas> getListEncEmpresasDH(EmpresasTrans emp,LocalDateTime init, LocalDateTime end)
            throws NaoExisteEmpresaException {
        return this.empresas.getEmpresa(emp.getCodEmpresa()).getListencEDH(init,end);
    }

    public Set<Encomendas> getListEncEmpresas(EmpresasTrans emp) throws NaoExisteEmpresaException {
        return this.empresas.getEmpresa(emp.getCodEmpresa()).getEncomendas(); 
    }

    //Lojas
    public List<Encomendas> getListEncLoja(Lojas loja) throws NaoExisteLojaException {
        return this.lojas.getLoja(loja.getCodLoja()).getEncR();
    }
    public List<Encomendas> getListEncLojaDH(Lojas l,LocalDateTime init, LocalDateTime end) throws NaoExisteLojaException {
        return this.lojas.getLoja(l.getCodLoja()).getListencLDH(init,end);
    }


    public List<LinhaEncomenda> getProdutos(String codLoja) throws NaoExisteLojaException{
        return this.lojas.getProdutos(codLoja);
    }


    public List<Encomendas> getListEncVolxUty(Voluntarios v,User u) throws NaoExisteVoluntarioException {
        return  this.voluntarios.getvoluntario(v.getCodVoluntario()).getListencV(u);
    }

    public double getTotalFaturadoE(EmpresasTrans e, LocalDateTime init, LocalDateTime end) throws UserInexistenteException, NaoExisteLojaException {
        double soma=0;
        Set<Encomendas> encomendas = e.getEncomendas();
        for(Encomendas enc: encomendas){
            User u = this.users.getUser(enc.getCodUtilizador());
            Lojas l = this.lojas.getLoja(enc.getNomeLoja());
            if(enc.getDataHora().isBefore(end)&&enc.getDataHora().isAfter(init))
                soma+=e.calculaPrecoTotal(u,enc,l);
        }
        return soma;
    }

    public double getClassificacaoE(String empresaID) throws NaoExisteEmpresaException {
        return this.empresas.getEmpresa(empresaID).getClassificacoes();
    }

    public double getClassificacaoV(String voluntarioID) throws NaoExisteVoluntarioException {
        return this.voluntarios.getvoluntario(voluntarioID).getClassi();
    }


    //ALGORITMO PARA FAZER UM PEDIDO --------------------------------------------------------------------------------------------------------------

    public String utFazPedidoE(Encomendas e, String utilizador) throws UserInexistenteException, NaoExisteLojaException, NaoExisteEncomendaException, UserForaDeAlcanceException,
            OpcaoInvalidaException {
      int i=0;                  

      String cdU = utilizador;
      User c =this.users.getUser(cdU);
      String cdL =e.getNomeLoja();
      Lojas l = this.lojas.getLoja(cdL);

      boolean isMed = e.isMed;

        List<Voluntarios> vM = new ArrayList<>();
        List<Voluntarios> v = new ArrayList<>();
        List<EmpresasTrans> emp=new ArrayList<>();
        List<EmpresasTrans> empM= new ArrayList<>();
        Voluntarios vol = null;
        Voluntarios volM = null;

      if(isMed){
          vM = voluntarios.volNoRaioMedL(l,c);
          if(vM.size()>0) {
            volM = vM.get(0);
          }
          empM = empresas.empresasNoRaioMed(l.getLat(),l.getLon(),c.getLatitude(),c.getLongitude());
      }
      else {
          v = voluntarios.volNoRaioL(l,c);
          if(v.size()>0) {
              vol = v.get(0);
          }
          emp = empresas.empresasNoRaio(l.getLat(),l.getLon(),c.getLatitude(),c.getLongitude());
          
      }
  

      if(v.isEmpty() && vM.isEmpty() && emp.isEmpty() && empM.isEmpty()){
          throw new UserForaDeAlcanceException();
      } 

      EncomendaAceite ea = new EncomendaAceite(e.getCodEncomenda());
      boolean aceitaEmp=false;

      if (vol!=null){
          l.adicionaEncomendas(ea);
          l.produzEncomenda(this.encomendas);
          vol.buscarEncomenda(l,e,c);
          c.addEncHist(e);
          return vol.getCodVoluntario();
      }
      if(volM!=null) {
          l.adicionaEncomendas(ea);
          l.produzEncomenda(this.encomendas);
          volM.buscarEncomenda(l,e,c);
          c.addEncHist(e);
          return volM.getCodVoluntario();
      }
     if(emp!=null) {

        for (i =0; i < emp.size()  ;i++) {
             EmpresasTrans ep = emp.get(i);
             double custo = ep.calculaPrecoTotal(c, e, l);
             double t = ep.estimaTempo(c, l);
             aceitaEmp = c.aceitarEmpresaTr(custo, t,ep); 
             if(aceitaEmp) {
             l.adicionaEncomendas(ea);
             l.produzEncomenda(this.encomendas);
             double dist = ep.calculaDistanciaEnc(l,c);
             custoTempo ct = new custoTempo();
             ct = ep.buscarenc(c,l,dist,e);
             ep.adicionaEncomenda(e, ct);
             c.addEncHist(e);
             return ep.getCodEmpresa();
             }
     }
    }

     else{
           for(i = 0;i<empM.size() ;i++ ) {
               EmpresasTrans ep = empM.get(i);
               double custo = ep.calculaPrecoTotal(c,e,l);
               double t = ep.estimaTempo(c,l);
               aceitaEmp = c.aceitarEmpresaTr(custo,t,ep);
               if (aceitaEmp) {
                   l.adicionaEncomendas(ea);
                   l.produzEncomenda(this.encomendas);
                   double dist = ep.calculaDistanciaEnc(l,c);
                   custoTempo ct = new custoTempo();
                   ct = ep.buscarenc(c,l,dist,e);
                   ep.adicionaEncomenda(e, ct);
                   c.addEncHist(e);
                   return ep.getCodEmpresa();
               }
      }
    }
     return null;
    
    }


    public void save(String fName) throws IOException {
        FileOutputStream a = new FileOutputStream(fName);
        ObjectOutputStream r = new ObjectOutputStream(a);
        r.writeObject(this);
        r.flush();
        r.close();
    }


    public TrazAqui read(String fName)throws IOException ,ClassNotFoundException {
        FileInputStream r = new FileInputStream(fName);
        ObjectInputStream a = new ObjectInputStream(r);
        TrazAqui u = (TrazAqui) a.readObject();
        a.close();
        return u;
    }


    public void gravaTxt(String filename) throws IOException{
        PrintWriter pw = new PrintWriter(filename);
        pw.print(this);
        pw.flush();
        pw.close();
    }
}