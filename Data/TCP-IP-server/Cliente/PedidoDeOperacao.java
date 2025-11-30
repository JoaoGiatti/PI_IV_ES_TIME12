public class PedidoDeOperacao extends Comunicado
{
    private char   operacao;
    private double valor;
    
    public PedidoDeOperacao (char operacao, double valor)
    {
        this.operacao = operacao;
        this.valor    = valor;
    }
    
    public char getOperacao ()
    {
        return this.operacao;
    }
    
    public double getValor ()
    {
        return this.valor;
    }
    
    public String toString ()
    {
        return (""+this.operacao+this.valor);
    }
}
