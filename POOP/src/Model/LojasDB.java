package Model;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import Exceptions.*;

public class LojasDB implements Serializable{
    private static final long serialVersionUID = 5351270800229918438L;
    private Map<String, Lojas> lojas;

    LojasDB() {
        this.lojas = new HashMap<>();
    }

    private LojasDB(LojasDB l) {
        this.lojas= l.lojas
                .values()
                .stream()
                .collect(Collectors
                        .toMap(Lojas::getCodLoja, Lojas::clone));
    }

    public List<Lojas> getLojas(){
        return this.lojas.values().stream().collect(Collectors.toList());
    }

    public void addLoja(Lojas l) throws JaExisteLojaException{
        if(this.lojas.putIfAbsent(l.getCodLoja(), l) != null)
            throw new JaExisteLojaException();
    }

    public Lojas getLoja(String cdL) throws NaoExisteLojaException {
        Lojas l = this.lojas.get(cdL);
        if(l == null)
            throw new NaoExisteLojaException();
        return l;
    }

    public List<LinhaEncomenda> getProdutos(String codLoja ) throws NaoExisteLojaException{
        Lojas l = this.lojas.get(codLoja);
        return l.getProdutos();
    }

    public void adicionaProduto(String codLoja, LinhaEncomenda l) throws JaExisteProdutoException{
        Lojas lo = this.lojas.get(codLoja);
        lo.adicionaProduto(l);
    }

    public void addEncomenda(Encomendas e){
        String loja = e.getNomeLoja();
        this.lojas.get(loja).adicionaEncomendas(e);
    }

    public LojasDB clone() { return new LojasDB(this); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LojasDB lojasDB = (LojasDB) o;
        return Objects.equals(lojas, lojasDB.lojas);
    }

    @Override
    public int hashCode() {
        return 1;
    }

}


